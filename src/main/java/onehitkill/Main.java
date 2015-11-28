package main.java.onehitkill;

import java.lang.reflect.Field;

import main.java.onehitkill.commands.CommandOHK;
import main.java.onehitkill.listeners.Guns;
import main.java.onehitkill.listeners.InventoryListener;
import main.java.onehitkill.listeners.LocationSelectionListener;
import main.java.onehitkill.listeners.MainListener;
import main.java.onehitkill.listeners.PlayerListener;
import main.java.onehitkill.utils.UserUtils;
import net.minecraft.server.v1_8_R2.TileEntityChest;
import net.minecraft.server.v1_8_R2.TileEntityDispenser;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R2.block.CraftChest;
import org.bukkit.craftbukkit.v1_8_R2.block.CraftDispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

	public static FileConfiguration config;

	public void onEnable() {
		System.out.println("[One Hit Kill: Survivors] Initializing...");
		initConfig();
		init();
		commands();
		regenWave();

		for (Player p : Bukkit.getOnlinePlayers()) {
			UserUtils.getUser(p).loadData();
		}

		System.out.println("[One Hit Kill: Survivors] Initialized!");
	}

	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			UserUtils.getUser(p).saveUserData();
		}
		saveConfig();
	}

	public void initConfig() {
		config = getConfig();
		saveConfig();
	}

	public void init() {
		new MainListener(this);
		new PlayerListener(this);
		new InventoryListener(this);
		new LocationSelectionListener(this);
		new Guns(this);
	}

	public void commands() {
		getCommand("ohk").setExecutor(new CommandOHK());
	}

	public void leave(Player p) {

	}

	public static void setSpawn(Location loc, int i) {
		config.set("data.spawn" + i + ".world", loc.getWorld().getName());
		config.set("data.spawn" + i + ".x", loc.getX());
		config.set("data.spawn" + i + ".y", loc.getY());
		config.set("data.spawn" + i + ".z", loc.getZ());
		Bukkit.getPluginManager().getPlugin("OneHitKill").saveConfig();
	}

	public static void configSet(String path, Object newValue) {
		config.set(path, newValue);
		Bukkit.getPluginManager().getPlugin("OneHitKill").saveConfig();
	}

	public static void regenWave() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					User user = UserUtils.getUser(p);
					PlayerListener.leaderboardUpdate(user);
					for (int i = 0; i < 8; i++) {
						if (user.getConfig().contains("kitdelays." + i) && user.getConfig().getInt("kitdelays." + i) > 0) {
							user.getConfig().set("kitdelays." + i, user.getConfig().getInt("kitdelays." + i) - 1);
						}
					}
					
					if (user.inGame && p.getHealth() < p.getMaxHealth()) {
						p.setHealth(p.getHealth() + 1);
						user.timesurvived++;

						inventoryUpdate(user);

						if (user.timesurvived > user.mosttimesurvived) {
							user.mosttimesurvived = user.timesurvived;
						}

						if (UserUtils.getUser(p).healAmount > 0) {
							p.setHealth(p.getHealth() + 1);
							UserUtils.getUser(p).healAmount--;
						}

					}
				}
			}
		}, 1200, 1200);
	}

	public static void inventoryUpdate(User user) {
		user.player.getInventory().remove(Material.STAINED_GLASS_PANE);
		user.player.getInventory().remove(Material.GOLD_NUGGET);
		for (int i = 0; i < user.player.getInventory().getSize(); i++) {
			if (i == 3) {
				ItemStack gold = new ItemStack(Material.GOLD_NUGGET);
				ItemMeta meta = gold.getItemMeta();
				meta.setDisplayName("Gold: " + user.gold);
				gold.setItemMeta(meta);
				user.player.getInventory().setItem(i, gold);
			} else if ((i > 19 && i < 25) || (i < 2)) {
				if ((!(user.player.getInventory().getItem(i) == null))
						&& (user.player.getInventory().getItem(i).getType().equals(Material.STAINED_GLASS_PANE) || user.player.getInventory().getItem(i)
								.getType().equals(Material.GOLD_NUGGET))) {
					user.player.getInventory().remove(i);
				}
			} else {
				user.player.getInventory().setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData()));
			}
		}
	}

	public static int getMaxSpawn() {
		int Max = 0;

		for (int i = 0; i < 200; i++) {
			if (!config.contains("data.spawn" + Max + ".world")) {
				break;
			} else {
				Max++;
			}
		}

		return Max;
	}

	public static void lootChestWave() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
			public void run() {
				placeLootChests();
				placeLootSafes();
			}
		}, 12000, 12000);
	}

	public static void placeLootChests() {
		for (int i = 0; i < 200; i++) {
			if (config.contains("lootchests." + i)) {
				Location loc = new Location(Bukkit.getWorld(config.getString("lootchests." + i + ".world")), config.getInt("lootchests." + i + ".x"),
						config.getInt("lootchests." + i + ".y"), config.getInt("lootchests." + i + ".z"));

				loc.getBlock().setType(Material.AIR);
				if (Inventories.randInt(0, 100) > 50) {

					loc.getBlock().setType(Material.CHEST);
					CraftChest chest = new CraftChest(loc.getBlock());

					try {
						Field inventoryField = chest.getClass().getDeclaredField("chest");
						inventoryField.setAccessible(true);
						TileEntityChest teChest = ((TileEntityChest) inventoryField.get(chest));
						teChest.a(Inventories.lootchest);
					} catch (Exception e) {}
				}
			} else {
				break;
			}
		}
	}

	public static void placeLootSafes() {
		for (int i = 0; i < 200; i++) {
			if (config.contains("lootsafes." + i)) {
				Location loc = new Location(Bukkit.getWorld(config.getString("lootsafes." + i + ".world")), config.getInt("lootsafes." + i + ".x"),
						config.getInt("lootsafes." + i + ".y"), config.getInt("lootsafes." + i + ".z"));

				loc.getBlock().setType(Material.AIR);

				loc.getBlock().setType(Material.DISPENSER);
				CraftDispenser dispenser = new CraftDispenser(loc.getBlock());

				try {
					Field inventoryField = dispenser.getClass().getDeclaredField("dispenser");
					inventoryField.setAccessible(true);
					TileEntityDispenser teDispenser = ((TileEntityDispenser) inventoryField.get(dispenser));
					teDispenser.a("Safe");
				} catch (Exception e) {}
			}
		}
	}

}
