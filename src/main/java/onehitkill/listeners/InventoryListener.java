package main.java.onehitkill.listeners;

import main.java.onehitkill.Inventories;
import main.java.onehitkill.Main;
import main.java.onehitkill.User;
import main.java.onehitkill.utils.UserUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener {
	public InventoryListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory() instanceof AnvilInventory) {
			return;
		}

		User user = UserUtils.getUser((Player) e.getWhoClicked());
		if (e.getCurrentItem() == null) {
			return;
		}

		ItemStack i = e.getCurrentItem();

		if (user.inGame
				&& ((i.getType().equals(Material.STAINED_GLASS_PANE) || (i.getType().equals(Material.GOLD_NUGGET) || ((i.getDurability() > 0) && !(i.getType()
						.equals(Material.WOOL) || i.getType().equals(Material.STAINED_CLAY))))))) {
			e.setCancelled(true);
			user.player.closeInventory();
			return;
		}

		if (i.getItemMeta() != null && i.getItemMeta().getDisplayName() != null) {
			String name = i.getItemMeta().getDisplayName().toLowerCase();
			if (name.equals("stats")) {
				e.setCancelled(true);
				user.player.closeInventory();
				user.player.openInventory(Inventories.stats(user.player));
			}

			if (name.contains("cosmetic")) {
				e.setCancelled(true);
				user.player.closeInventory();
				user.player.openInventory(Inventories.cosmeticShop(user));
			}
		}
		switch (e.getInventory().getTitle()) {
		case Inventories.kitselection:

			if (i == null || i.getItemMeta() == null || i.getItemMeta().getDisplayName() == null) {
				return;
			}

			e.setCancelled(true);

			user.lastlocation = e.getWhoClicked().getLocation();
			user.lastinventory.setContents(e.getWhoClicked().getInventory().getContents());

			int id = Integer.parseInt(e.getCurrentItem().getItemMeta().getLore().get(0).replace("ID: ", ""));

			if (user.getConfig().contains("kitdelays." + id)) {
				if (user.getConfig().getInt("kitdelays." + id) > 0) {
					user.player.sendMessage(ChatColor.RED + "You have to wait " + user.getConfig().getInt("kitdelays." + id) + " minutes for that class!");
					return;
				}
			} else {
				user.getConfig().set("kitdelays." + id, 0);
			}
			
			user.getConfig().set("kitdelays." + id, 5);

			e.getWhoClicked().getInventory().clear();
			ItemStack[] items = Inventories.kitItems(id);

			for (int j = 0; j < e.getWhoClicked().getInventory().getSize(); j++) {
				e.getWhoClicked().getInventory().setItem(j, new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData()));
				if (j == 3) {
					ItemStack gold = new ItemStack(Material.GOLD_NUGGET);
					ItemMeta meta = gold.getItemMeta();
					meta.setDisplayName("Gold: " + user.gold);
					gold.setItemMeta(meta);
					e.getWhoClicked().getInventory().setItem(j, gold);
				}
			}

			for (int j = 0; j < 7; j++) {
				if (j < 2) {
					e.getWhoClicked().getInventory().setItem(j, items[j]);
				} else {
					if (items[j] != null) {
						e.getWhoClicked().getInventory().setItem(j + 18, items[j]);
					} else {
						e.getWhoClicked().getInventory().setItem(j + 18, null);
					}
				}

			}

			e.getWhoClicked().closeInventory();
			UserUtils.getUser((Player) e.getWhoClicked()).join();
			e.getWhoClicked().sendMessage(ChatColor.BLUE + "Welcome to " + ChatColor.DARK_RED + "One Hit Kill!");
			break;
		case Inventories.kitcustomizer:
			if (i == null || i.getItemMeta() == null || i.getItemMeta().getDisplayName() == null) {
				return;
			}

			if (i.getItemMeta().getDisplayName().equals("Save Kit")) {
				e.setCancelled(true);

				for (int j = 0; j < 7; j++) {
					if (e.getInventory().getItem(j) != null) {
						Main.configSet("kits." + user.kitID + ".items." + j, e.getInventory().getItem(j));
					} else {
						Main.configSet("kits." + user.kitID + ".items." + j, new ItemStack(Material.AIR, 1));
					}
				}

				e.getWhoClicked().closeInventory();
				user.player.sendMessage(ChatColor.BLUE + "Kit saved!");
			} else if (i.getItemMeta().getDisplayName().equals("Cancel")) {
				e.setCancelled(true);
				e.getWhoClicked().closeInventory();
			}

			break;
		case Inventories.kitcustomization:
			e.setCancelled(true);

			if (i == null || i.getItemMeta() == null || i.getItemMeta().getDisplayName() == null) {
				return;
			}

			if (i.getItemMeta().getDisplayName().equals("Create a Kit")) {
				e.getWhoClicked().closeInventory();
				e.getWhoClicked().openInventory(Inventories.kitCreator());
				return;
			}

			int ID = Integer.parseInt(i.getItemMeta().getLore().get(0).replace("ID: ", ""));
			user.kitID = ID;

			e.getWhoClicked().closeInventory();
			e.getWhoClicked().openInventory(Inventories.kitCustomization(ID));

			break;
		case Inventories.kitcreator:

			if (i == null || i.getItemMeta() == null || i.getItemMeta().getDisplayName() == null) {
				return;
			}

			if (i.getItemMeta().getDisplayName().equals("Save Kit")) {
				e.setCancelled(true);
				int NewKit = 0;
				for (NewKit = 0; NewKit < 7; NewKit++) {
					if (Inventories.kitList()[NewKit] == null) {
						break;
					}
				}

				for (int j = 0; j < 6; j++) {
					if (e.getInventory().getItem(j) != null) {
						Main.configSet("kits." + NewKit + ".items." + j, e.getInventory().getItem(j));
					}
				}

				user.promptForName(NewKit);
			} else if (i.getItemMeta().getDisplayName().equals("Cancel")) {
				e.getWhoClicked().closeInventory();
			} else {
				return;
			}

			e.getWhoClicked().closeInventory();
			break;
		case Inventories.reload:
			e.setCancelled(true);
			if (e.getCurrentItem().getType().equals(Material.WOOL)) {
				if (!user.bullet) {
					user.bullet = true;
					switch (user.guntype) {
					case "pistol":

					}

					e.getInventory().setItem(e.getSlot(), new ItemStack(Material.COAL_BLOCK));
				}
			} else if (e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
				if (user.bullet) {
					user.bullet = false;

					switch (user.guntype.toLowerCase()) {
					case "pistol":
						if (inventoryContains(((Player) e.getWhoClicked()), "9mm Ammo")) {
							user.pistol += 1;
							inventoryDelete(((Player) e.getWhoClicked()), "9mm Ammo");
						} else {
							e.getWhoClicked().closeInventory();
						}
						break;
					case "smg":
						if (inventoryContains(((Player) e.getWhoClicked()), "9mm Ammo")) {
							user.smg += 1;
							inventoryDelete(((Player) e.getWhoClicked()), "9mm Ammo");
						} else {
							e.getWhoClicked().closeInventory();
						}
						break;
					case "shotgun":
						if (inventoryContains(((Player) e.getWhoClicked()), "Shotgun Shell")) {
							user.shotgun += 1;
							inventoryDelete(((Player) e.getWhoClicked()), "Shotgun Shell");
						} else {
							e.getWhoClicked().closeInventory();
						}
						break;
					case "rifle":
						if (inventoryContains(((Player) e.getWhoClicked()), "Rifle Shell")) {
							user.rifle += 1;
							inventoryDelete(((Player) e.getWhoClicked()), "Rifle Shell");
						} else {
							e.getWhoClicked().closeInventory();
						}
						break;
					}

					e.getInventory().setItem(e.getSlot(), new ItemStack(Material.IRON_BLOCK));
				}
			}

			break;
		case Inventories.unlock:
			e.setCancelled(true);
			if (e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
				if (e.getCurrentItem().getItemMeta().hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)) {
					e.getCurrentItem().addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);

					int success = 0;
					for (int j = 0; j < e.getInventory().getSize(); j++) {
						if (e.getInventory().getItem(j).getItemMeta().hasEnchant(Enchantment.DAMAGE_ALL)) {
							success++;
						}
					}

					if (success > 2) {
						if (inventoryContains(((Player) e.getWhoClicked()), "Safecracker")) {
							inventoryDelete(((Player) e.getWhoClicked()), "Safecracker");
						}
						user.player.sendMessage(ChatColor.BLUE + "You unlocked the safe!");
						e.getWhoClicked().closeInventory();
						e.getWhoClicked().openInventory(Inventories.lootSafe());
					}

				} else {
					e.getWhoClicked().closeInventory();
				}
			}
			break;
		case Inventories.lootsafe:
			if (e.getCurrentItem() == null) {
				return;
			}
			boolean hasAir = false;
			for (int j = 0; j < e.getWhoClicked().getInventory().getSize(); j++) {
				if (e.getWhoClicked().getInventory().getItem(j) == null) {
					hasAir = true;
				}
			}

			if (hasAir) {
				e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
			} else {
				e.getWhoClicked().getWorld().dropItem(e.getWhoClicked().getLocation(), e.getCurrentItem());
			}
			e.getInventory().remove(e.getCurrentItem());
			break;
		case Inventories.cosmeticshop:
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			ItemMeta m = e.getCurrentItem().getItemMeta();

			boolean owns = Boolean.parseBoolean(m.getLore().get(1).replace("Owned: ", ""));

			if (owns) {
				user.activate(e.getCurrentItem().getType());
			} else {
				int price = Integer.parseInt(m.getLore().get(0).replace("Price: ", ""));
				if (user.gold >= price) {
					user.gold -= price;
					user.activate(e.getCurrentItem().getType());
				} else {
					user.player.closeInventory();
					user.player.sendMessage(ChatColor.RED + "You can't afford that!");
				}
			}

			break;
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Chest) {
			Chest chest = (Chest) e.getClickedBlock().getState();
			if (chest.getInventory().getTitle().equalsIgnoreCase(Inventories.lootchest)) {
				chest.getInventory().setContents(Inventories.lootChest().getContents());
			}
		}

		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Dispenser) {
			Dispenser dispenser = (Dispenser) e.getClickedBlock().getState();
			if (dispenser.getInventory().getTitle().equalsIgnoreCase("Safe")) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
					public void run() {
						if (e.getItem() != null && e.getItem().getItemMeta().getDisplayName() != null
								&& e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Safecracker")) {
							e.getPlayer().closeInventory();
							e.getPlayer().openInventory(Inventories.safeUnlock(3));
							UserUtils.getUser(e.getPlayer()).safe = e.getClickedBlock();
						} else {
							e.getPlayer().closeInventory();
							e.getPlayer().openInventory(Inventories.safeUnlock(2));
							UserUtils.getUser(e.getPlayer()).safe = e.getClickedBlock();
						}
					}
				}, 8L);
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getPlayer() instanceof Player && UserUtils.getUser((Player) e.getPlayer()).inGame
				&& e.getInventory().getTitle().equalsIgnoreCase(Inventories.lootchest)) {
			if (e.getInventory().getHolder() instanceof Chest) {
				Chest chest = (Chest) e.getInventory().getHolder();
				chest.getBlock().setType(Material.AIR);
			}
		}

		if (e.getPlayer() instanceof Player && UserUtils.getUser((Player) e.getPlayer()).inGame
				&& e.getInventory().getTitle().equalsIgnoreCase(Inventories.lootsafe)) {
			UserUtils.getUser((Player) e.getPlayer()).safe.setType(Material.AIR);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		User user = UserUtils.getUser(e.getPlayer());

		if (user.inGame) {
			if (e.getItemDrop().getItemStack().getType().equals(Material.STAINED_GLASS_PANE)
					|| e.getItemDrop().getItemStack().getType().equals(Material.GOLD_NUGGET)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onSwitch(PlayerItemHeldEvent e) {
		User user = UserUtils.getUser(e.getPlayer());

		if (e.getPlayer().getInventory().getItem(e.getNewSlot()) == null) {
			return;
		}

		if (user.inGame) {
			if (e.getPlayer().getInventory().getItem(e.getNewSlot()).getType().equals(Material.STAINED_GLASS_PANE)
					|| e.getPlayer().getInventory().getItem(e.getNewSlot()).getType().equals(Material.GOLD_NUGGET)) {
				e.getPlayer().getInventory().setHeldItemSlot(e.getPreviousSlot());
			}
		}
	}

	public static void inventoryDelete(Player p, String name) {
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (p.getInventory().getItem(i) != null && p.getInventory().getItem(i).getItemMeta().getDisplayName() != null
					&& p.getInventory().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
				if (p.getInventory().getItem(i).getAmount() > 1) {
					p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - 1);
				} else {
					p.getInventory().remove(p.getInventory().getItem(i));
				}
				p.updateInventory();
				return;
			}
		}
	}

	public static boolean inventoryContains(Player p, String name) {
		boolean contains = false;
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (p.getInventory().getItem(i) != null && p.getInventory().getItem(i).getItemMeta().getDisplayName() != null
					&& p.getInventory().getItem(i).getItemMeta().getDisplayName().equalsIgnoreCase(name)) {
				contains = true;
			}
		}

		return contains;
	}

}
