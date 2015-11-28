package main.java.onehitkill;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class User {

	public Player player;
	public String username;
	public boolean inGame, online;
	public PlayerInventory inventory;
	private FileConfiguration config;
	private File file;

	public User(Player player) {
		this.player = player;
		username = player.getName();
		online = player.isOnline();
		inventory = player.getInventory();
	}

	/**
	 * Stats
	 */
	public int gold;

	public int mostkills;
	public int highestkillstreak;
	public int highesttimedkillstreak;
	public int mosttimesurvived;

	public int killstreak;
	public int timesurvived;
	public int timedkillstreak;
	public int killstreaktask;
	
	public String format(String string) {
		string = string.replaceAll("&", "§");
		return string;
	}

	/**
	 * Variables for gun reloading
	 */
	public boolean bullet;
	public String guntype;
	public int pistol;
	public int revolver;
	public int smg;
	public int shotgun;
	public int rifle;

	public boolean pistolclip;
	public boolean revolverclip;
	public boolean smgclip;
	public boolean shotgunclip;
	public boolean rifleclip;

	/**
	 * Other variables
	 */
	public int healAmount;
	public Block safe;

	/**
	 * Cosmetic items
	 */
	public List<Material> cosmetics;
	public List<Material> activecosmetics;

	/**
	 * Returns whether or not they own a cosmetic item
	 */
	public boolean hasCosmetic(Material m) {
		return cosmetics.contains(m);
	}

	/**
	 * Returns whether or not a cosmetic item is active
	 */
	public boolean isActive(Material m) {
		return activecosmetics.contains(m);
	}

	public void activate(Material m) {
		if (cosmetics.contains(m)) {
			switch (m) {
			case LEATHER_CHESTPLATE:
			case GOLD_CHESTPLATE:
			case DIAMOND_CHESTPLATE:
				activecosmetics.remove(Material.LEATHER_CHESTPLATE);
				activecosmetics.remove(Material.GOLD_CHESTPLATE);
				activecosmetics.remove(Material.DIAMOND_CHESTPLATE);
				activecosmetics.add(m);
				break;
			case STONE_AXE:
			case IRON_AXE:
			case GOLD_AXE:
			case DIAMOND_AXE:
				activecosmetics.remove(Material.STONE_AXE);
				activecosmetics.remove(Material.IRON_AXE);
				activecosmetics.remove(Material.GOLD_AXE);
				activecosmetics.remove(Material.DIAMOND_AXE);
				activecosmetics.add(m);
				break;
			case STONE_SWORD:
			case IRON_SWORD:
			case GOLD_SWORD:
			case DIAMOND_SWORD:
				activecosmetics.remove(Material.STONE_SWORD);
				activecosmetics.remove(Material.IRON_SWORD);
				activecosmetics.remove(Material.GOLD_SWORD);
				activecosmetics.remove(Material.DIAMOND_SWORD);
				activecosmetics.add(m);
				break;
			case STONE_SPADE:
			case IRON_SPADE:
			case GOLD_SPADE:
			case DIAMOND_SPADE:
				activecosmetics.remove(Material.STONE_SPADE);
				activecosmetics.remove(Material.IRON_SPADE);
				activecosmetics.remove(Material.GOLD_SPADE);
				activecosmetics.remove(Material.DIAMOND_SPADE);
				activecosmetics.add(m);
				break;
			default:
				break;
			}
		}
	}
	
	public List<Integer> tasks;

	/**
	 * Player's inventory and location so it can be returned when they leave the
	 * game
	 */
	public Inventory lastinventory;
	public Location lastlocation;

	public void join() {
		if (!inGame) {
			killstreak = 0;
			timesurvived = 0;

			inGame = true;
			pistol = 0;
			revolver = 0;
			smg = 0;
			shotgun = 0;
			rifle = 0;
			pistolclip = true;
			revolverclip = true;
			smgclip = true;
			shotgunclip = true;
			rifleclip = true;
			healAmount = 0;

			int spawn = 0;
			for (int i = 0; i < 100; i++) {
				spawn = Inventories.randInt(0, Main.getMaxSpawn());
				if (Main.config.contains("data.spawn" + spawn)) {
					break;
				}
			}
			player.teleport(new Location(Bukkit.getWorld(Main.config.getString("data.spawn" + spawn + ".world")), Main.config.getInt("data.spawn" + spawn
					+ ".x"), Main.config.getInt("data.spawn" + spawn + ".y"), Main.config.getInt("data.spawn" + spawn + ".z")));

			player.setGameMode(GameMode.SURVIVAL);
			player.setMaxHealth(6);
			player.setHealth(6);
			player.setFoodLevel(20);
			player.setSaturation(10);

			player.getInventory().setHeldItemSlot(0);

			saveUserData();
		}
	}

	public void leave() {
		if (inGame) {
			if (lastinventory != null) {
				player.getInventory().setContents(lastinventory.getContents());
			}

			player.teleport(lastlocation);

			if (killstreak > mostkills) {
				mostkills = killstreak;
			}

			inGame = false;
			
			player.setMaxHealth(20);
			player.setHealth(20);
			player.setFoodLevel(20);

			Location water = player.getEyeLocation();

			water.getBlock().setType(Material.WATER);

			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
				public void run() {
					water.getBlock().setType(Material.AIR);
				}
			}, 8L);

		}
	}

	public boolean chatLock;
	public int kitID;

	public void promptForName(int id) {
		chatLock = true;
		kitID = id;
		player.sendMessage(ChatColor.BLUE + "What do you want to call the kit?");
	}

	public void loadData() {
		file = new File("plugins" + File.separator + "OneHitKill" + File.separator + "userdata" + File.separator + player.getUniqueId().toString() + ".yml");
		setConfig(YamlConfiguration.loadConfiguration(file));
		if (!file.exists()) {
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Welcome to the server, " + username);
			inGame = false;

			lastlocation = player.getLocation();
			lastinventory = player.getInventory();

			gold = 0;

			mostkills = 0;
			highestkillstreak = 0;
			highesttimedkillstreak = 0;
			mosttimesurvived = 0;

			pistolclip = true;
			revolverclip = true;
			smgclip = true;
			shotgunclip = true;
			rifleclip = true;

			pistol = 0;
			revolver = 0;
			smg = 0;
			shotgun = 0;
			rifle = 0;

			cosmetics = new ArrayList<Material>();
			cosmetics.add(Material.AIR);
			activecosmetics = new ArrayList<Material>();
			activecosmetics.add(Material.AIR);
		}
		loadUserData();
	}

	public void loadUserData() {
		inGame = getConfig().getBoolean("data.inGame");

		lastinventory = Bukkit.createInventory(player, player.getInventory().getSize());
		for (int i = 0; i < lastinventory.getSize(); i++) {
			if (getConfig().contains("data.lastinventory.items." + i)) {
				lastinventory.setItem(i, getConfig().getItemStack("data.lastinventory.items." + i));
			}

		}

		if (getConfig().contains("data.lastlocation")) {
			lastlocation = new Location(Bukkit.getWorld(getConfig().getString("data.lastlocation.world")), getConfig().getDouble("data.lastlocation.x"),
					getConfig().getDouble("data.lastlocation.y"), getConfig().getDouble("data.lastlocation.z"));
		}

		gold = getConfig().getInt("stats.gold");

		mostkills = getConfig().getInt("stats.mostkills");
		highestkillstreak = getConfig().getInt("stats.highestkillstreak");
		highesttimedkillstreak = getConfig().getInt("stats.highesttimedkillstreak");
		mosttimesurvived = getConfig().getInt("stats.mosttimesurvived");

		pistolclip = getConfig().getBoolean("data.clips.pistol");
		revolverclip = getConfig().getBoolean("data.clips.revolver");
		smgclip = getConfig().getBoolean("data.clips.smg");
		shotgunclip = getConfig().getBoolean("data.clips.shotgun");
		rifleclip = getConfig().getBoolean("data.clips.rifle");

		pistol = getConfig().getInt("data.bullets.pistol");
		revolver = getConfig().getInt("data.bullets.revolver");
		smg = getConfig().getInt("data.bullets.smg");
		shotgun = getConfig().getInt("data.bullets.shotgun");
		rifle = getConfig().getInt("data.bullets.rifle");

		cosmetics = new ArrayList<Material>();
		List<String> cosmeticstring = getConfig().getStringList("data.cosmetics");
		for (int i = 0; i < cosmeticstring.size(); i++) {
			if (cosmeticstring.get(i) != null) {
				cosmetics.add(Material.getMaterial(cosmeticstring.get(i)));
			}
		}
		
		activecosmetics = new ArrayList<Material>();
		List<String> activestring = getConfig().getStringList("data.activecosmetics");
		for (int i = 0; i < activestring.size(); i++) {
			if (activestring.get(i) != null) {
				activecosmetics.add(Material.getMaterial(activestring.get(i)));
			}
		}
		
		tasks = new ArrayList<Integer>();

	}

	public void saveUserData() {
		getConfig().set("data.username", username);
		getConfig().set("data.inGame", inGame);

		if (lastinventory != null) {
			for (int i = 0; i < lastinventory.getSize(); i++) {
				if (lastinventory.getItem(i) != null) {
					getConfig().set("data.lastinventory.items." + i, lastinventory.getItem(i));
				} else {
					getConfig().set("data.lastinventory.items." + i, null);
				}
			}
		}
		getConfig().set("data.lastlocation.world", lastlocation.getWorld().getName());
		getConfig().set("data.lastlocation.x", lastlocation.getX());
		getConfig().set("data.lastlocation.y", lastlocation.getY());
		getConfig().set("data.lastlocation.z", lastlocation.getZ());

		getConfig().set("stats.gold", gold);

		getConfig().set("stats.mostkills", mostkills);
		getConfig().set("stats.highestkillstreak", highestkillstreak);
		getConfig().set("stats.highesttimedkillstreak", highesttimedkillstreak);
		getConfig().set("stats.mosttimesurvived", mosttimesurvived);

		getConfig().set("data.clips.pistol", pistolclip);
		getConfig().set("data.clips.revolver", revolverclip);
		getConfig().set("data.clips.smg", smgclip);
		getConfig().set("data.clips.shotgun", shotgunclip);
		getConfig().set("data.clips.rifle", rifleclip);

		getConfig().set("data.bullets.pistol", pistol);
		getConfig().set("data.bullets.revolver", revolver);
		getConfig().set("data.bullets.smg", smg);
		getConfig().set("data.bullets.shotgun", shotgun);
		getConfig().set("data.bullets.rifle", rifle);

		List<String> cosmeticstring = new ArrayList<String>();
		for (int i = 0; i < cosmetics.size(); i++) {
			if (cosmetics.get(i) != null) {
				cosmeticstring.add(cosmetics.get(i).name());
			}
		}
		getConfig().set("data.cosmetics", cosmeticstring);
		
		List<String> activestring = new ArrayList<String>();
		for (int i = 0; i < activecosmetics.size(); i++) {
			if (activecosmetics.get(i) != null) {
				activestring.add(activecosmetics.get(i).name());
			}
		}

		try {
			getConfig().save(file);
		} catch (IOException ex) {
		}
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void setConfig(FileConfiguration config) {
		this.config = config;
	}

}
