package main.java.onehitkill.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.java.onehitkill.Inventories;
import main.java.onehitkill.Main;
import main.java.onehitkill.User;
import main.java.onehitkill.utils.UserUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Guns implements Listener {
	public Guns(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static int task = 0;
	public static Map<Integer, Integer> taskID = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> taskDelay = new HashMap<Integer, Integer>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		User user = UserUtils.getUser(e.getPlayer());
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (user.inGame) {
				ItemStack gun = e.getItem();

				if (gun == null) {
					return;
				}

				if (gun.getItemMeta().getDisplayName() == null) {
					return;
				}

				if ((e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Health Pack"))) {
					e.setCancelled(true);
					user.player.sendMessage(ChatColor.BLUE + "You will now regen twice as much for 3 minutes!");
					user.healAmount = 3;
					InventoryListener.inventoryDelete(e.getPlayer(), e.getItem().getItemMeta().getDisplayName());
					return;
				}

				if ((e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Doctor Kit"))) {
					e.setCancelled(true);
					user.player.sendMessage(ChatColor.BLUE + "You will now regen twice as much for 3 minutes!");
					user.healAmount = 3;

					ItemMeta meta = e.getItem().getItemMeta();
					List<String> lore = new ArrayList<String>();
					if (meta.getLore() == null || meta.getLore().get(0) == null) {
						lore.add("Uses: 1");
						meta.setLore(lore);
					} else {
						lore = meta.getLore();
						int uses = Integer.parseInt(lore.get(0).replace("Uses: ", ""));
						uses++;
						lore.set(0, "Uses: " + uses);
						meta.setLore(lore);

						if (uses >= 3) {
							InventoryListener.inventoryDelete(e.getPlayer(), "Doctor Kit");
						}

					}

					e.getItem().setItemMeta(meta);

				}

				if (e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Taser")) {
					if (e.getItem().getDurability() > 0) {
						return;
					}
					Arrow arrow = user.player.getWorld().spawnArrow(user.player.getEyeLocation(), e.getPlayer().getLocation().getDirection(), 0.6f, 12);
					arrow.setCustomName("taser");
					arrow.setShooter(user.player);
					PlayerListener.weaponRegen(user.player, e.getItem(), e.getPlayer().getInventory().getHeldItemSlot(), 30);
				}

				String name = gun.getItemMeta().getDisplayName().toLowerCase();

				if (name.contains("clip")) {
					if (name.contains("pistol")) {
						user.pistolclip = true;
					} else if (name.contains("revolver")) {
						user.revolverclip = true;
					} else if (name.contains("shotgun")) {
						user.shotgunclip = true;
					} else if (name.contains("rifle")) {
						user.rifleclip = true;
					} else if (name.contains("smg")) {
						user.smgclip = true;
					}
					user.player.getInventory().setItem(user.player.getInventory().getHeldItemSlot(), null);
					return;
				} else if (!(name.contains("shell")) && !(name.contains("ammo"))) {
					if (gun.getDurability() > 0) {
						return;
					}

					ItemMeta meta = gun.getItemMeta();

					if (name.contains("pistol") && user.pistolclip && user.pistol > 0) {
						shoot(e.getPlayer(), "Pistol", 1, 80);
						user.pistol--;
						meta.setDisplayName("Pistol >> " + user.pistol);
					} else if (name.contains("revolver") && user.revolverclip && user.revolver > 0 && !name.equals("revolver shell")) {
						shoot(e.getPlayer(), "Revolver", 1, 90);
						user.revolver--;
						meta.setDisplayName("Revolver >> " + user.revolver);
					} else if (name.contains("shotgun") && user.shotgunclip && user.shotgun > 0) {
						shoot(e.getPlayer(), "Shotgun", 10, 50);
						user.shotgun--;
						meta.setDisplayName("Shotgun >> " + user.shotgun);
					} else if (name.contains("smg") && user.smgclip && user.smg > 0) {
						shoot(e.getPlayer(), "SMG", 1, 25);
						user.smg--;
						meta.setDisplayName("SMG >> " + user.smg);
					} else if (name.contains("rifle") && user.rifleclip && user.rifle > 0) {
						shoot(e.getPlayer(), "Rifle", 1, 100);
						user.rifle--;
						meta.setDisplayName("Rifle >> " + user.rifle);
					}

					gun.setItemMeta(meta);

					Material m = e.getItem().getType();
					if (Inventories.randInt(0, 100) > 90 && (m.equals(Material.WOOD_HOE) || m.equals(Material.IRON_HOE) || m.equals(Material.GOLD_HOE) || m.equals(Material.DIAMOND_HOE))) {
						PlayerListener.weaponRegen(e.getPlayer(), gun, e.getPlayer().getInventory().getHeldItemSlot(), 15);
					}

				}

			}
		}

		if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			if (user.inGame) {
				if (e.getItem() == null) {
					return;
				}

				if (e.getItem().getItemMeta().getDisplayName() == null) {
					return;
				}

				if (e.getItem().getItemMeta().getDisplayName().toLowerCase().contains("clip")) {
					String name = e.getItem().getItemMeta().getDisplayName().toLowerCase();

					if (name.contains("pistol")) {
						user.player.openInventory(Inventories.gunReload(1));
						user.guntype = "pistol";
					} else if (name.contains("shotgun")) {
						user.player.openInventory(Inventories.gunReload(2));
						user.guntype = "shotgun";
					} else if (name.contains("rifle")) {
						user.player.openInventory(Inventories.gunReload(3));
						user.guntype = "rifle";
					} else if (name.contains("smg")) {
						user.player.openInventory(Inventories.smgReload());
						user.guntype = "smg";
					}

					return;
				} else {
					String name = e.getItem().getItemMeta().getDisplayName().toLowerCase();
					ItemStack clip = new ItemStack(Material.IRON_INGOT);
					ItemMeta meta = clip.getItemMeta();
					ItemMeta gmeta = e.getItem().getItemMeta();

					if (name.contains("pistol") && user.pistolclip == true) {
						meta.setDisplayName("Pistol Clip");
						user.pistolclip = false;
						gmeta.setDisplayName("Pistol >> " + ChatColor.RED + "X");
						e.getItem().setItemMeta(gmeta);
						clip.setItemMeta(meta);
						if (inventoryContains(user.player, null)) {
							user.player.getInventory().addItem(clip);
						} else {
							user.player.getWorld().dropItem(user.player.getLocation(), clip);
						}
					} else if (name.contains("shotgun") && user.shotgunclip == true) {
						meta.setDisplayName("Shotgun Clip");
						user.shotgunclip = false;
						gmeta.setDisplayName("Shotgun >> " + ChatColor.RED + "X");
						e.getItem().setItemMeta(gmeta);
						clip.setItemMeta(meta);
						if (inventoryContains(user.player, Material.AIR)) {
							user.player.getInventory().addItem(clip);
						} else {
							user.player.getWorld().dropItem(user.player.getLocation(), clip);
						}
					} else if (name.contains("rifle") && user.rifleclip == true) {
						meta.setDisplayName("Rifle Clip");
						user.rifleclip = false;
						gmeta.setDisplayName("Rifle >> " + ChatColor.RED + "X");
						e.getItem().setItemMeta(gmeta);
						clip.setItemMeta(meta);
						if (inventoryContains(user.player, Material.AIR)) {
							user.player.getInventory().addItem(clip);
						} else {
							user.player.getWorld().dropItem(user.player.getLocation(), clip);
						}
					} else if (name.contains("smg") && user.smgclip == true) {
						meta.setDisplayName("SMG Clip");
						user.smgclip = false;
						gmeta.setDisplayName("SMG >> " + ChatColor.RED + "X");
						e.getItem().setItemMeta(gmeta);
						clip.setItemMeta(meta);
						if (inventoryContains(user.player, Material.AIR)) {
							user.player.getInventory().addItem(clip);
						} else {
							user.player.getWorld().dropItem(user.player.getLocation(), clip);
						}
					}

					if (name.equals("revolver shell")) {
						InventoryListener.inventoryDelete(e.getPlayer(), "Revolver Shell");
						user.revolver++;
					}

				}

			}
		}
	}

	@EventHandler
	public void onLaunch(ProjectileLaunchEvent e) {
		if (e.getEntity().getShooter() instanceof Player && UserUtils.getUser((Player) e.getEntity().getShooter()).inGame && e.getEntity() instanceof Egg) {
			Player player = (Player) e.getEntity().getShooter();
			if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("smoke")) {
				e.getEntity().setCustomName("smoke");
				InventoryListener.inventoryDelete(player, "Smoke Grenade");
			} else if (player.getInventory().getItemInHand().getItemMeta().getDisplayName().toLowerCase().contains("explosive")) {
				e.getEntity().setCustomName("explosion");
				InventoryListener.inventoryDelete(player, "Explosive Grenade");
			}

		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Egg) {
			Egg egg = (Egg) e.getEntity();

			if (egg.getCustomName() == null) {
				return;
			}
			
			if (egg.getCustomName().equalsIgnoreCase("smoke")) {
				smoke(e.getEntity());
				e.getEntity().remove();
			}

			if (egg.getCustomName().equalsIgnoreCase("explosion")) {
				explode(e.getEntity());
				e.getEntity().remove();
			}
		}
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		try {
			if (e.getCreatureType().equals(CreatureType.CHICKEN) && e.getSpawnReason().equals(SpawnReason.EGG)) {
				e.setCancelled(true);
			}
		} catch (NullPointerException ex) {}
	}

	public void smoke(Entity e) {
		int t = task;
		task++;
		taskDelay.put(t, 200);
		taskID.put(t, Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
			public void run() {
				if (taskDelay.get(t) > 0) {
					e.getWorld().createExplosion(
							e.getLocation().clone().add((double) randDouble(-3.0, 3.0), (double) randDouble(-1.0, 6.0), (double) randDouble(-3.0, 3.0)), 0);
					e.getWorld().createExplosion(
							e.getLocation().clone().add((double) randDouble(-3.0, 3.0), (double) randDouble(-1.0, 6.0), (double) randDouble(-3.0, 3.0)), 0);
					e.getWorld().createExplosion(
							e.getLocation().clone().add((double) randDouble(-3.0, 3.0), (double) randDouble(-1.0, 6.0), (double) randDouble(-3.0, 3.0)), 0);
					e.getWorld().createExplosion(
							e.getLocation().clone().add((double) randDouble(-3.0, 3.0), (double) randDouble(-1.0, 6.0), (double) randDouble(-3.0, 3.0)), 0);
					e.getWorld().createExplosion(
							e.getLocation().clone().add((double) randDouble(-3.0, 3.0), (double) randDouble(-1.0, 6.0), (double) randDouble(-3.0, 3.0)), 0);
					e.getWorld().createExplosion(
							e.getLocation().clone().add((double) randDouble(-3.0, 3.0), (double) randDouble(-1.0, 6.0), (double) randDouble(-3.0, 3.0)), 0);
					taskDelay.put(t, taskDelay.get(t) - 1);
				} else {
					Bukkit.getScheduler().cancelTask(taskID.get(t));
				}
			}
		}, 1L, 1L));
	}

	public void explode(Entity e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
			public void run() {
				e.getWorld().createExplosion(e.getLocation(), 5);
			}
		}, 60L);

	}

	public void shoot(Player p, String name, int amount, int accuracy) {
		for (int i = 0; i < amount; i++) {
			Snowball snowball = p.getWorld().spawn(p.getEyeLocation(), Snowball.class);
			snowball.setShooter(p);
			snowball.setCustomName(name);

			double acc = ((1000 - (((double) accuracy) * 10)) / 25) / 100;

			double xm = randDouble(-acc, acc);
			double ym = randDouble(-acc, acc);
			double zm = randDouble(-acc, acc);

			Vector dir = p.getLocation().getDirection().multiply(2);
			Vector velocity = new Vector((double) dir.getX() + xm, (double) dir.getY() + ym, (double) dir.getZ() + zm);

			snowball.setVelocity(velocity);
		}
	}

	public static double randDouble(double min, double max) {
		Random rand = new Random();

		double randomNum = min + (rand.nextDouble() * (max - min));

		return randomNum;
	}

	public static boolean inventoryContains(Player p, Material m) {
		boolean contains = false;
		for (int i = 0; i < p.getInventory().getSize(); i++) {
			if (p.getInventory().getItem(i) != null && p.getInventory().getItem(i).getType().equals(m)) {
				contains = true;
			}
		}
		return contains;
	}
}
