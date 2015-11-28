package main.java.onehitkill.listeners;

import java.util.HashMap;
import java.util.Map;

import main.java.onehitkill.Inventories;
import main.java.onehitkill.Main;
import main.java.onehitkill.User;
import main.java.onehitkill.utils.UserUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener extends UserUtils implements Listener {
	public static Map<Integer, Integer> taskID = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> taskDelay = new HashMap<Integer, Integer>();

	public static int task = 0;

	public PlayerListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		UserUtils.getUser(e.getPlayer()).loadData();
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		UserUtils.getUser(e.getPlayer()).saveUserData();
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		User user = UserUtils.getUser(e.getPlayer());
		if (user.inGame) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		User user = UserUtils.getUser(e.getPlayer());
		if (user.inGame) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		User user = UserUtils.getUser(e.getPlayer());

		if (user.inGame) {
			if (e.getItemDrop().getItemStack().getDurability() > 0) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onSignInteract(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Material m = e.getClickedBlock().getType();
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) e.getClickedBlock().getState();

				if (sign.getLine(1).toLowerCase().contains("one") && sign.getLine(1).toLowerCase().contains("hit")) {
					if (sign.getLine(0).toLowerCase().contains("join")) {
						e.getPlayer().openInventory(Inventories.kitSelection(getUser(e.getPlayer())));
					} else if (sign.getLine(0).toLowerCase().contains("stats")) {
						e.getPlayer().openInventory(Inventories.stats(e.getPlayer()));
					} else if (sign.getLine(0).toLowerCase().contains("cosmetic")) {
						e.getPlayer().openInventory(Inventories.cosmeticShop(getUser(e.getPlayer())));
					} else if (sign.getLine(0).toLowerCase().contains("leaderboard")) {
						e.getPlayer().openInventory(Inventories.leaderboard());
					}
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		User user = UserUtils.getUser(e.getPlayer());

		if (user.inGame) {

			if (!(e.getItem() == null)) {
				ItemStack weapon = e.getItem();
				short dura = weapon.getDurability();
				if (weapon.getDurability() > 0) {
					e.setCancelled(true);
					weapon.setDurability(dura);
					return;
				}

				if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					if (e.getItem().getType().equals(Material.WOOD_HOE) || e.getItem().getType().equals(Material.STONE_HOE)
							|| e.getItem().getType().equals(Material.IRON_HOE) || e.getItem().getType().equals(Material.GOLD_HOE)
							|| e.getItem().getType().equals(Material.DIAMOND_HOE)) {
						e.setCancelled(true);
					}

					if (e.getItem().getType().equals(Material.FLINT_AND_STEEL)) {
						weaponRegen(e.getPlayer(), weapon, e.getPlayer().getInventory().getHeldItemSlot(), 9);
					}
				}

				if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
						public void run() {
							int delay = 0;
							switch (weapon.getType().name().toLowerCase()) {
							case "wood_sword":
								delay = 3;
								break;
							case "wood_axe":
								delay = 5;
								break;
							case "wood_spade":
								delay = 3;
								break;
							case "wood_pickaxe":
								delay = 4;
								break;
							}

							if (delay != 0) {
								weaponRegen(e.getPlayer(), weapon, e.getPlayer().getInventory().getHeldItemSlot(), delay);
							}
						}
					}, 1L);

				}

			}

		}
	}

	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Snowball) {
			Snowball snowball = (Snowball) e.getDamager();
			User user = getUser((Player) snowball.getShooter());

			if (user.inGame) {
				if (e.getEntity() instanceof Player) {
					if (!(getUser((Player) e.getEntity()).inGame)) {
						e.setCancelled(true);
						user.player.sendMessage(ChatColor.RED + "You can't hit that player!");
						return;
					}
				}

				switch (snowball.getCustomName().toLowerCase()) {
				case "pistol":
					e.setDamage(3);
					break;
				case "shotgun":
					e.setDamage(4);
					break;
				case "smg":
					e.setDamage(1);
					break;
				case "revolver":
					e.setDamage(6);
					break;
				}

				if (e.getEntity() instanceof Player) {
					double y = snowball.getLocation().getY();
					boolean headshot = y > ((Player) e.getEntity()).getEyeLocation().getY();

					if (headshot) {
						e.setDamage(6);
						((Player) snowball.getShooter()).sendMessage(ChatColor.DARK_RED + "HEADSHOT!");
					}
				}
			} else {
				if (e.getEntity() instanceof Player) {
					if (getUser((Player) e.getEntity()).inGame) {
						user.player.sendMessage(ChatColor.RED + "You can't hit that player!");
					}
				}
			}

		}

		if (e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getDamager();

			if (arrow.getShooter() instanceof Player && getUser((Player) arrow.getShooter()).inGame) {
				if (arrow.getCustomName() != null && arrow.getCustomName().equalsIgnoreCase("taser")) {
					taser(e.getEntity());
					return;
				}
				e.setDamage(3);

				if (e.getEntity() instanceof Player) {
					double y = arrow.getLocation().getY();
					boolean headshot = y > ((Player) e.getEntity()).getEyeLocation().getY();

					if (headshot) {
						e.setDamage(6);
						((Player) arrow.getShooter()).sendMessage(ChatColor.DARK_RED + "HEADSHOT!");
					}
				}

			}

		}

		if (e.getDamager() instanceof Player) {
			User user = getUser((Player) e.getDamager());
			ItemStack weapon = user.player.getItemInHand();

			if (user.inGame) {
				if (e.getEntity() instanceof Player) {
					if (!(getUser((Player) e.getEntity()).inGame)) {
						e.setCancelled(true);
						user.player.sendMessage(ChatColor.RED + "You can't hit that player!");
						return;
					}
				}

				if (weapon.getDurability() > 0) {
					e.setCancelled(true);
					return;
				}

				e.setDamage(0);
				int delay = 0;
				double damage = 0;

				switch (weapon.getType().name().toLowerCase()) {
				case "wood_sword":
					delay = 3;
					damage = 6;
					break;
				case "wood_axe":
					delay = 5;
					damage = 6;
					break;
				case "wood_spade":
					delay = 3;
					damage = 6;
					break;
				case "wood_pickaxe":
					delay = 4;
					damage = 2;
					break;
				}

				if (delay != 0) {
					e.setDamage(damage);
					weaponRegen(((Player) e.getDamager()), weapon, ((Player) e.getEntity()).getInventory().getHeldItemSlot(), delay);
				}

			} else {
				if (e.getEntity() instanceof Player) {
					if (getUser((Player) e.getEntity()).inGame) {
						user.player.sendMessage(ChatColor.RED + "You can't hit that player!");
						e.setCancelled(true);
						return;
					}
				}
			}
		}

		if (e.getEntity() instanceof Player && getUser((Player) e.getEntity()).inGame) {
			if ((((double) ((Player) e.getEntity()).getHealth()) - e.getDamage()) < 0.5) {
				e.setCancelled(true);
				String cause = "the voices";

				switch (e.getCause()) {
				case PROJECTILE:
					cause = ((Player) ((Projectile) e.getDamager()).getShooter()).getName();
					if (!cause.equalsIgnoreCase(((Player) ((Projectile) e.getDamager()).getShooter()).getName())) {
						killStreakWatch((Player) ((Projectile) e.getDamager()).getShooter());
					}
					break;
				case ENTITY_ATTACK:
					if (e.getDamager() instanceof Player) {
						cause = ((Player) e.getDamager()).getName();
						killStreakWatch((Player) e.getDamager());
					} else {
						cause = e.getEntityType().getName();
					}
					break;
				}

				if (cause.equalsIgnoreCase(e.getEntity().getName())) {
					broadcast(ChatColor.RED + ((Player) e.getEntity()).getName() + " committed suicide!");
				} else {
					broadcast(ChatColor.RED + e.getEntity().getName() + " was killed by " + cause);
				}

				User user = getUser((Player) e.getEntity());

				if (user.killstreak > user.highestkillstreak) {
					user.highestkillstreak = user.killstreak;
					user.player.sendMessage(ChatColor.BLUE + "Your new highest kill streak is " + user.highestkillstreak);
				}

				for (int i = 0; i < ((Player) e.getEntity()).getInventory().getSize(); i++) {
					if (((Player) e.getEntity()).getInventory().getItem(i) != null
							&& !((Player) e.getEntity()).getInventory().getItem(i).getType().equals(Material.STAINED_GLASS_PANE)
							&& !((Player) e.getEntity()).getInventory().getItem(i).getType().equals(Material.GOLD_NUGGET)
							&& !(((Player) e.getEntity()).getInventory().getItem(i).getDurability() > 0)) {
						e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), ((Player) e.getEntity()).getInventory().getItem(i));
					}
				}

				getUser((Player) e.getEntity()).leave();
				leaderboardUpdate(getUser((Player) e.getEntity()));
			}
		}

	}

	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void onTakeDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player && getUser((Player) e.getEntity()).inGame && !e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
			if ((((double) ((Player) e.getEntity()).getHealth()) - e.getDamage()) < 1) {
				e.setCancelled(true);
				String cause = "the voices";
				switch (e.getCause()) {
				case FALL:
					cause = "fall damage";
					break;
				case VOID:
					cause = "void damage";
					break;
				case DROWNING:
					cause = "drowning";
					break;
				case SUFFOCATION:
					cause = "suffocation";
					break;
				case FIRE:
				case FIRE_TICK:
					cause = "fire";
					break;
				case PROJECTILE:
					cause = "an arrow";
					break;
				}
				broadcast(ChatColor.RED + e.getEntity().getName() + " was killed by " + cause);

				User user = getUser((Player) e.getEntity());

				if (user.killstreak > user.highestkillstreak) {
					user.highestkillstreak = user.killstreak;
					user.player.sendMessage(ChatColor.BLUE + "Your new highest kill streak is " + user.highestkillstreak);
				}

				for (int i = 0; i < ((Player) e.getEntity()).getInventory().getSize(); i++) {
					if (((Player) e.getEntity()).getInventory().getItem(i) != null
							&& !((Player) e.getEntity()).getInventory().getItem(i).getType().equals(Material.STAINED_GLASS_PANE)
							&& !((Player) e.getEntity()).getInventory().getItem(i).getType().equals(Material.GOLD_NUGGET)
							&& !(((Player) e.getEntity()).getInventory().getItem(i).getDurability() > 0)) {
						e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), ((Player) e.getEntity()).getInventory().getItem(i));
					}
				}

				getUser((Player) e.getEntity()).leave();
			}
		}
	}

	@EventHandler
	public void onRegen(EntityRegainHealthEvent e) {
		if (e.getEntity() instanceof Player && getUser((Player) e.getEntity()).inGame) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onChat(PlayerChatEvent e) {
		User user = UserUtils.getUser(e.getPlayer());
		if (user.chatLock) {
			Main.configSet("kits." + user.kitID + ".name", e.getMessage());
			e.setCancelled(true);
			user.chatLock = false;
		}
	}

	public static void leaderboardUpdate(User user) {
		if (!Main.config.contains("leaderboard.kills.amt")) {
			Main.configSet("leaderboard.kills.amt", 0);
		}

		if (!Main.config.contains("leaderboard.killstreak.amt")) {
			Main.configSet("leaderboard.killstreak.amt", 0);
		}

		if (!Main.config.contains("leaderboard.timesurvived.amt")) {
			Main.configSet("leaderboard.timesurvived.amt", 0);
		}

		if (user.highestkillstreak > Main.config.getInt("leaderboard.kills.amt")) {
			Main.configSet("leaderboard.kills.amt", user.highestkillstreak);
			Main.configSet("leaderboard.kills.username", user.player.getName());
		}

		if (user.highesttimedkillstreak > Main.config.getInt("leaderboard.killstreak.amt")) {
			Main.configSet("leaderboard.killstreak.amt", user.highesttimedkillstreak);
			Main.configSet("leaderboard.killstreak.username", user.player.getName());
		}

		if (user.mosttimesurvived > Main.config.getInt("leaderboard.timesurvived.amt")) {
			Main.configSet("leaderboard.timesurvived.amt", user.mosttimesurvived);
			Main.configSet("leaderboard.timesurvived.username", user.player.getName());
		}
	}

	public static void broadcast(String message) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (getUser(p).inGame) {
				p.sendMessage(message);
			}
		}
	}

	public static void weaponRegen(Player p, ItemStack w, int s, int d) {
		int t = task;
		getUser(p).tasks.add(t);
		ItemStack weapon = w;
		int delay = d;
		taskDelay.put(t, delay);
		int slot = s;

		weapon.setDurability((short) (weapon.getType().getMaxDurability() - 2));
		Player player = p;
		task++;

		taskID.put(t, Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
			public void run() {
				if (!getUser(p).inGame) {
					Bukkit.getScheduler().cancelTask(taskID.get(t));
				}

				if (!(taskDelay.get(t) == 1)) {
					taskDelay.put(t, taskDelay.get(t) - 1);
					weapon.setDurability((short) (weapon.getDurability() - ((weapon.getType().getMaxDurability() / delay))));
					if (getUser(player).inGame) {
						p.getInventory().setItem(slot, weapon);
					}
				} else {
					weapon.setDurability((short) (0 - weapon.getType().getMaxDurability()));
					if (getUser(player).inGame) {
						p.getInventory().setItem(slot, weapon);
					}
					Bukkit.getScheduler().cancelTask(taskID.get(t));
				}

			}
		}, 20, 20));
	}

	public static void killStreakWatch(Player p) {
		User user = getUser(p);
		int g = 5;
		user.killstreak++;
		user.timedkillstreak++;

		if (user.killstreak > 2) {
			g += 2;
		}

		if (user.killstreak > 5) {
			g += 5;
		}

		if (user.killstreak > 9) {
			g += 10;
		}

		if (user.killstreak == 2) {
			g += 5;
		} else if (user.killstreak == 3) {
			g += 10;
		} else if (user.killstreak > 3) {
			g += 15;
		}

		user.player.sendMessage(ChatColor.BLUE + "You earned " + ChatColor.GOLD + g + ChatColor.BLUE + " gold!");
		user.gold += g;

		if (Bukkit.getScheduler().isQueued(user.killstreaktask)) {
			Bukkit.getScheduler().cancelTask(user.killstreaktask);
		}
		user.killstreaktask = Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
			public void run() {
				if (user.timedkillstreak > user.highesttimedkillstreak) {
					user.highesttimedkillstreak = user.timedkillstreak;
					user.player.sendMessage(ChatColor.BLUE + "Your new highest timed killstreak is " + user.highesttimedkillstreak + "!");
				}
				user.timedkillstreak = 0;
				ItemStack gold = new ItemStack(Material.GOLD_NUGGET);
				ItemMeta meta = gold.getItemMeta();
				meta.setDisplayName("Gold: " + user.gold);
				gold.setItemMeta(meta);
				user.player.getInventory().setItem(3, gold);
				leaderboardUpdate(user);
			}
		}, 120L);
	}

	public static void taser(Entity e) {
		int t = task;
		task++;
		Entity entity = e;
		taskDelay.put(t, 30);
		Location loc = entity.getLocation();

		taskID.put(t, Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("OneHitKill"), new Runnable() {
			public void run() {
				if (!(taskDelay.get(t) <= 1)) {
					if (!(entity instanceof Player && !getUser((Player) entity).inGame)) {
						entity.teleport(loc);
						taskDelay.put(t, taskDelay.get(t) - 1);
					}
				} else {
					Bukkit.getScheduler().cancelTask(taskID.get(t));
				}
			}
		}, 2, 2));

	}

}
