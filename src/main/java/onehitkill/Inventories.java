package main.java.onehitkill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.java.onehitkill.utils.UserUtils;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Inventories {
	public static final String kitselection = "Choose Your Kit";
	public static final String kitcustomization = "Which Kit to Edit?";
	public static final String kitcustomizer = "Kit Customizer";
	public static final String kitcreator = "Kit Creator";
	public static final String lootchest = "Loot Chest";
	public static final String lootsafe = "Loot Safe";
	public static final String reload = "Gun Reload";
	public static final String unlock = "Unlock the Safe";
	public static final String cosmeticedit = "Edit the Cosmetic Items";
	public static final String cosmeticshop = "Cosmetic Shop";
	public static final String newcosmetic = "New Cosmetic Item";

	/**
	 * Inventory for Loot Chests
	 */
	public static Inventory lootChest() {
		Inventory inv = Bukkit.createInventory(null, 27, lootchest);

		inv.setContents(randomLoot(inv.getSize(), 5));

		return inv;
	}

	/**
	 * Inventory for stats
	 */
	public static Inventory stats(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, "Stats");
		User user = UserUtils.getUser(p);

		ItemStack item = new ItemStack(Material.BONE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Most Kills: " + user.highestkillstreak);
		item.setItemMeta(meta);
		inv.setItem(2, item);
		item = new ItemStack(Material.ROTTEN_FLESH);
		meta.setDisplayName("Highest Killstreak: " + user.highesttimedkillstreak);
		item.setItemMeta(meta);
		inv.setItem(4, item);
		item = new ItemStack(Material.WATCH);
		meta.setDisplayName("Longest Time Survived: " + user.mosttimesurvived + " minutes");
		item.setItemMeta(meta);
		inv.setItem(6, item);

		return inv;
	}
	
	/**
	 * Inventory for top 9 players stats
	 */
	public static Inventory leaderboard() {
		Inventory inv = Bukkit.createInventory(null, 9, "Most Kills Leaderboard");
		
		ItemStack s = new ItemStack(397, 1, (short) 3);
		SkullMeta sm = (SkullMeta) s.getItemMeta();
		sm.setOwner(Main.config.getString("leaderboard.kills.username"));
		sm.setDisplayName(Main.config.getString("leaderboard.kills.username"));
		sm.setLore(Arrays.asList("Most Kills: " + Main.config.getInt("leaderboard.kills.amt")));
		s.setItemMeta(sm);
		inv.setItem(1, s);
		
		ItemStack s2 = new ItemStack(397, 1, (short) 3);
		SkullMeta sm2 = (SkullMeta) s2.getItemMeta();
		sm2.setOwner(Main.config.getString("leaderboard.killstreak.username"));
		sm2.setDisplayName(Main.config.getString("leaderboard.killstreak.username"));
		sm2.setLore(Arrays.asList("Highest Killstreak: " + Main.config.getInt("leaderboard.killstreak.amt")));
		s2.setItemMeta(sm2);
		inv.setItem(4, s2);
		
		ItemStack s3 = new ItemStack(397, 1, (short) 3);
		SkullMeta sm3 = (SkullMeta) s3.getItemMeta();
		sm3.setOwner(Main.config.getString("leaderboard.timesurvived.username"));
		sm3.setDisplayName(Main.config.getString("leaderboard.timesurvived.username"));
		sm3.setLore(Arrays.asList("Longest Time Survived: " + Main.config.getInt("leaderboard.timesurvived.amt") + " minutes"));
		s3.setItemMeta(sm3);
		inv.setItem(7, s3);
		
		return inv;
	}
	
	public static Inventory cosmeticShop(User user) {
		Inventory inv = Bukkit.createInventory(null, 36, cosmeticshop);

		boolean owns;
		ItemStack item;
		ItemMeta meta;

		item = new ItemStack(Material.LEATHER_HELMET);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 1000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(0, item);

		item = new ItemStack(Material.CHAINMAIL_HELMET);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 2500", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(1, item);
		
		item = new ItemStack(Material.GOLD_HELMET);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 5000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(2, item);

		item = new ItemStack(Material.DIAMOND_HELMET);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 10000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(3, item);
		
		
		
		item = new ItemStack(Material.LEATHER_CHESTPLATE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 1000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(4, item);

		item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 2500", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(5, item);
		
		item = new ItemStack(Material.GOLD_CHESTPLATE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 5000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(6, item);

		item = new ItemStack(Material.DIAMOND_CHESTPLATE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 10000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(7, item);

		
		
		item = new ItemStack(Material.LEATHER_LEGGINGS);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 1000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(8, item);

		item = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 2500", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(9, item);
		
		item = new ItemStack(Material.GOLD_LEGGINGS);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 5000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(10, item);

		item = new ItemStack(Material.DIAMOND_LEGGINGS);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 10000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(11, item);
		
		
		
		item = new ItemStack(Material.LEATHER_BOOTS);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 1000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(12, item);

		item = new ItemStack(Material.CHAINMAIL_BOOTS);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 2500", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(13, item);
		
		item = new ItemStack(Material.GOLD_BOOTS);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 5000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(14, item);

		item = new ItemStack(Material.DIAMOND_BOOTS);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 10000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(15, item);
		
		
		
		item = new ItemStack(Material.STONE_SWORD);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 1000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(16, item);
		
		item = new ItemStack(Material.IRON_SWORD);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 2500", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(17, item);
		
		item = new ItemStack(Material.GOLD_SWORD);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 5000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(18, item);
		
		item = new ItemStack(Material.DIAMOND_SWORD);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 10000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(19, item);

		
		
		item = new ItemStack(Material.STONE_AXE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 1000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(20, item);
		
		item = new ItemStack(Material.IRON_AXE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 2500", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(21, item);
		
		item = new ItemStack(Material.GOLD_AXE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 5000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(22, item);
		
		item = new ItemStack(Material.DIAMOND_AXE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 10000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(23, item);
		
		
		
		item = new ItemStack(Material.STONE_SPADE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 1000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(24, item);
		
		item = new ItemStack(Material.IRON_SPADE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 2500", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(25, item);
		
		item = new ItemStack(Material.GOLD_SPADE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 5000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(26, item);
		
		item = new ItemStack(Material.DIAMOND_SPADE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 10000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(27, item);
		
		
		
		item = new ItemStack(Material.STONE_PICKAXE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 1000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(28, item);
		
		item = new ItemStack(Material.IRON_PICKAXE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 2500", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(29, item);
		
		item = new ItemStack(Material.GOLD_PICKAXE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 5000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(30, item);
		
		item = new ItemStack(Material.DIAMOND_PICKAXE);
		meta = item.getItemMeta();
		owns = user.cosmetics.contains(item.getType());
		meta.setLore(Arrays.asList("Price: 10000", "Owned: " + owns, "Active: " + user.isActive(item.getType())));
		item.setItemMeta(meta);
		inv.setItem(31, item);
		
		return inv;
	}

	/**
	 * 0 1 2 3 4 5 6 7 8 0 X X X X X X X X X 1 X X X X X X X X X 2 X X X X X X X
	 * X X 3 X X X X X X X X X
	 * 
	 * 
	 * /** Inventory for unlocking a loot safe
	 */
	public static Inventory safeUnlock(int modifier) {
		Inventory inv = Bukkit.createInventory(null, 27, unlock);

		for (int i = 0; i < 27; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData()));

			if ((i >= (0 + modifier) && i <= (8 - modifier)) || (i >= (9 + modifier) && i <= (17 - modifier)) || (i >= (18 + modifier) && i <= (26 - modifier))) {
				inv.setItem(i, new ItemStack(Material.STAINED_CLAY, 1, DyeColor.GRAY.getData()));
				ItemMeta m = inv.getItem(i).getItemMeta();
				inv.getItem(i).setItemMeta(m);
			}

		}

		int rand = randInt(0 + modifier, 8 - modifier);
		int rand2 = randInt(9 + modifier, 17 - modifier);
		int rand3 = randInt(18 + modifier, 26 - modifier);

		ItemMeta meta = (new ItemStack(Material.STAINED_CLAY, 1, DyeColor.GRAY.getData()).getItemMeta());
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		inv.getItem(rand).setItemMeta(meta);
		inv.getItem(rand2).setItemMeta(meta);
		inv.getItem(rand3).setItemMeta(meta);

		return inv;
	}

	/**
	 * Inventory for Loot Safes
	 */
	public static Inventory lootSafe() {
		Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, lootsafe);

		inv.setContents(randomLoot(inv.getSize(), 15));

		return inv;
	}

	/**
	 * Inventory for gun reloading
	 */
	public static Inventory gunReload(int modifier) {
		Inventory inv = Bukkit.createInventory(null, 27, reload);

		for (int i = 0; i < 27; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData()));

			if (i > (0 + modifier) && i < (8 - modifier)) {
				inv.setItem(i, new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData()));
			}

			if (i > (18 + modifier) && i < (26 - modifier)) {
				inv.setItem(i, new ItemStack(Material.STAINED_CLAY, 1, DyeColor.GRAY.getData()));
			}
		}

		return inv;
	}

	public static Inventory smgReload() {
		Inventory inv = Bukkit.createInventory(null, 54, reload);

		for (int i = 0; i < 54; i++) {
			inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData()));

			if ((i > 0 && i < 8) || (i > 9 && i < 17)) {
				inv.setItem(i, new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData()));
			}

			if ((i > 36 && i < 44) || (i > 45 && i < 53)) {
				inv.setItem(i, new ItemStack(Material.STAINED_CLAY, 1, DyeColor.GRAY.getData()));
			}
		}

		return inv;
	}

	/**
	 * Inventory for selecting a kit upon joining
	 */
	public static Inventory kitSelection(User user) {
		Inventory inv = Bukkit.createInventory(null, 9, kitselection);

		try {
			inv.setContents(kitList(user));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return inv;
	}

	/**
	 * Inventory to select which kit to customize
	 */
	public static Inventory kitCustomizer() {
		Inventory inv = Bukkit.createInventory(null, 9, kitcustomization);

		try {
			inv.setContents(kitList());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		inv.setItem(8, getEssentials("create"));

		return inv;
	}

	/**
	 * Inventory to edit a specific kit
	 */
	public static Inventory kitCustomization(int kitID) {
		Inventory inv = Bukkit.createInventory(null, 9, kitcustomizer);

		try {
			inv.setContents(kitItems(kitID));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		inv.setItem(7, getEssentials("save"));
		inv.setItem(8, getEssentials("cancel"));

		return inv;
	}

	public static Inventory kitCreator() {
		Inventory inv = Bukkit.createInventory(null, 9, kitcreator);

		inv.setItem(7, getEssentials("save"));
		inv.setItem(8, getEssentials("cancel"));

		return inv;
	}

	public static ItemStack[] randomLoot(int amt, int chance) {
		ItemStack[] loot = new ItemStack[amt];

		for (int i = 0; i < loot.length; i++) {
			if (randInt(0, 100) > (100 - chance)) {
				loot[i] = lootItems.get(randInt(0, lootItems.size() - 1));
			}
		}
		return loot;
	}

	public static ItemStack[] kitItems(int kitID) {
		ItemStack[] contents = new ItemStack[7];
		if (Main.config.contains("kits." + kitID)) {
			for (int i = 0; i < 7; i++) {
				if (Main.config.contains("kits." + kitID + ".items." + i)) {
					contents[i] = Main.config.getItemStack("kits." + kitID + ".items." + i);
				}
			}
		} else {
			contents = null;
		}
		return contents;
	}

	public static ItemStack[] kitList() {
		ItemStack[] kits = new ItemStack[8];
		for (int i = 0; i < 8; i++) {
			if (Main.config.contains("kits." + i)) {
				List<String> lore = new ArrayList<String>();
				lore.add(0, "ID: " + i);
				for (int j = 0; j < 7; j++) {
					if (Main.config.contains("kits." + i + ".items." + j)) {
						ItemStack item = Main.config.getItemStack("kits." + i + ".items." + j);

						if (item != null && item.getItemMeta() != null) {
							if (item.getItemMeta().getDisplayName() == null) {
								lore.add((item.getType().name() + " x " + item.getAmount()));
							} else {
								lore.add((item.getItemMeta().getDisplayName() + " x " + item.getAmount() + " (" + item.getType().name() + ")"));
							}
						}
					}
				}

				kits[i] = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.CYAN.getData());
				ItemMeta meta = kits[i].getItemMeta();
				meta.setLore(lore);
				meta.setDisplayName(Main.config.getString("kits." + i + ".name"));
				kits[i].setItemMeta(meta);

			}
		}
		return kits;
	}
	
	public static ItemStack[] kitList(User user) {
		ItemStack[] kits = new ItemStack[8];
		for (int i = 0; i < 8; i++) {
			if (Main.config.contains("kits." + i)) {
				List<String> lore = new ArrayList<String>();
				lore.add(0, "ID: " + i);
				for (int j = 0; j < 7; j++) {
					if (Main.config.contains("kits." + i + ".items." + j)) {
						ItemStack item = Main.config.getItemStack("kits." + i + ".items." + j);

						if (item != null && item.getItemMeta() != null) {
							if (item.getItemMeta().getDisplayName() == null) {
								lore.add((item.getType().name() + " x " + item.getAmount()));
							} else {
								lore.add((item.getItemMeta().getDisplayName() + " x " + item.getAmount() + " (" + item.getType().name() + ")"));
							}
						}
					}
				}
				lore.add("Delay: " + user.getConfig().getInt("kitdelays." + i));

				kits[i] = new ItemStack(Material.STAINED_CLAY, 1, DyeColor.CYAN.getData());
				ItemMeta meta = kits[i].getItemMeta();
				meta.setLore(lore);
				meta.setDisplayName(Main.config.getString("kits." + i + ".name"));
				kits[i].setItemMeta(meta);

			}
		}
		return kits;
	}

	private static ItemStack getEssentials(String id) {
		ItemStack item = new ItemStack(Material.POTATO);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();

		switch (id) {
		case "save":
			item = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData());
			meta.setDisplayName("Save Kit");
			break;
		case "cancel":
			item = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());
			meta.setDisplayName("Cancel");
			break;
		case "create":
			item = new ItemStack(Material.WOOL, 1, DyeColor.WHITE.getData());
			meta.setDisplayName("Create a Kit");
			break;
		}
		item.setItemMeta(meta);
		return item;
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	private static List<ItemStack> lootItems = Arrays.asList(makeItem(Material.WOOD_HOE, 1, "Pistol", null), makeItem(Material.WOOD_HOE, 1, "Pistol", null),
			makeItem(Material.WOOD_HOE, 1, "Pistol", null), makeItem(Material.WOOD_HOE, 1, "Pistol", null), makeItem(Material.WOOD_HOE, 1, "Pistol", null),
			makeItem(Material.STONE_HOE, 1, "Revolver", null), makeItem(Material.STONE_HOE, 1, "Revolver", null),
			makeItem(Material.STONE_HOE, 1, "Revolver", null), makeItem(Material.IRON_HOE, 1, "Shotgun", null),
			makeItem(Material.IRON_HOE, 1, "Shotgun", null), makeItem(Material.GOLD_HOE, 1, "SMG", null), makeItem(Material.DIAMOND_HOE, 1, "Rifle", null),
			makeItem(Material.EGG, 1, "Smoke Grenade", null), makeItem(Material.EGG, 1, "Smoke Grenade", null),
			makeItem(Material.EGG, 1, "Smoke Grenade", null), makeItem(Material.EGG, 1, "Smoke Grenade", null),
			makeItem(Material.EGG, 1, "Smoke Grenade", null), makeItem(Material.EGG, 1, "Smoke Grenade", null),
			makeItem(Material.EGG, 1, "Smoke Grenade", null), makeItem(Material.EGG, 1, "Smoke Grenade", null),
			makeItem(Material.EGG, 1, "Smoke Grenade", null), makeItem(Material.EGG, 1, "Smoke Grenade", null),
			makeItem(Material.EGG, 1, "Explosive Grenade", null), makeItem(Material.EGG, 1, "Explosive Grenade", null),
			makeItem(Material.EGG, 1, "Explosive Grenade", null), makeItem(Material.EGG, 1, "Explosive Grenade", null),
			makeItem(Material.EGG, 1, "Explosive Grenade", null), makeItem(Material.EGG, 1, "Explosive Grenade", null),
			makeItem(Material.EGG, 1, "Explosive Grenade", null), makeItem(Material.EGG, 1, "Explosive Grenade", null),
			makeItem(Material.EGG, 1, "Explosive Grenade", null), makeItem(Material.EGG, 1, "Explosive Grenade", null),
			makeItem(Material.SPONGE, 1, "Health Pack", null), makeItem(Material.SPONGE, 1, "Health Pack", null),
			makeItem(Material.SPONGE, 1, "Health Pack", null), makeItem(Material.SPONGE, 1, "Health Pack", null),
			makeItem(Material.SPONGE, 1, "Health Pack", null), makeItem(Material.SPONGE, 1, "Health Pack", null),
			makeItem(Material.SPONGE, 1, "Health Pack", null), makeItem(Material.SPONGE, 1, "Health Pack", null),
			makeItem(Material.SPONGE, 1, "Health Pack", null), makeItem(Material.SPONGE, 1, "Health Pack", null),
			makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null),
			makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null),
			makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null),
			makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null),
			makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null),
			makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null),
			makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.GRILLED_PORK, 1, null, null), makeItem(Material.APPLE, 1, null, null),
			makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null),
			makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null),
			makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null),
			makeItem(Material.ARROW, 1, null, null), makeItem(Material.ARROW, 1, null, null), makeItem(Material.ARROW, 1, null, null),
			makeItem(Material.ARROW, 1, null, null), makeItem(Material.ARROW, 1, null, null), makeItem(Material.APPLE, 1, null, null),
			makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null),
			makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null),
			makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null), makeItem(Material.APPLE, 1, null, null),
			makeItem(Material.ARROW, 1, null, null), makeItem(Material.ARROW, 1, null, null), makeItem(Material.ARROW, 1, null, null),
			makeItem(Material.ARROW, 1, null, null), makeItem(Material.ARROW, 1, null, null), makeItem(Material.INK_SACK, 1, "9mm Ammo", null),
			makeItem(Material.INK_SACK, 1, "9mm Ammo", null), makeItem(Material.INK_SACK, 1, "9mm Ammo", null),
			makeItem(Material.INK_SACK, 1, "9mm Ammo", null), makeItem(Material.INK_SACK, 1, "Revolver Shell", null),
			makeItem(Material.INK_SACK, 1, "Revolver Shell", null), makeItem(Material.INK_SACK, 1, "Revolver Shell", null),
			makeItem(Material.INK_SACK, 1, "Shotgun Shell", null), makeItem(Material.INK_SACK, 1, "Shotgun Shell", null),
			makeItem(Material.INK_SACK, 1, "Rifle Shell", null));

	private static ItemStack makeItem(Material material, int amount, String displayname, List<String> lore) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();

		if (displayname != null) {
			meta.setDisplayName(displayname);
		}

		if (lore != null) {
			meta.setLore(lore);
		}

		item.setItemMeta(meta);
		return item;
	}

}
