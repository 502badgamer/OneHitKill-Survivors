package main.java.onehitkill.listeners;

import main.java.onehitkill.Main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class LocationSelectionListener implements Listener {
	public LocationSelectionListener(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getPlayer().isOp() && e.getItemInHand().getType().equals(Material.REDSTONE)) {
			if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) {
				return;
			}
			String name = e.getPlayer().getItemInHand().getItemMeta().getDisplayName().toLowerCase();
			switch (name) {
			case "lootchest":
			case "loot chest":
			case "lootchests":
			case "loot chests":
				int New = 0;
				for (int i = 0; i < 99; i++) {
					New = i;
					if (!(Main.config.contains("lootchests." + i))) {
						break;
					}
				}
				
				Main.config.set("lootchests." + New + ".world", e.getBlock().getWorld().getName());
				Main.config.set("lootchests." + New + ".x", (int)e.getBlock().getX());
				Main.config.set("lootchests." + New + ".y", (int)e.getBlock().getY());
				Main.configSet(("lootchests." + New + ".z"), (int)e.getBlock().getZ());
				
				e.getBlock().setType(Material.AIR);
				e.getPlayer().sendMessage(ChatColor.BLUE + "Loot Chest location registered at: " + e.getBlock().getX() + ", " + e.getBlock().getY() + ", " + e.getBlock().getZ());
				break;
			case "lootsafe":
			case "loot safe":
			case "lootsafes":
			case "loot safes":
			case "safe":
				int New1 = 0;
				for (int i = 0; i < 99; i++) {
					New1 = i;
					if (!(Main.config.contains("lootsafes." + i))) {
						break;
					}
				}
				
				Main.config.set("lootsafes." + New1 + ".world", e.getBlock().getWorld().getName());
				Main.config.set("lootsafes." + New1 + ".x", (int)e.getBlock().getX());
				Main.config.set("lootsafes." + New1 + ".y", (int)e.getBlock().getY());
				Main.configSet("lootsafes." + New1 + ".z", (int)e.getBlock().getZ());
				
				e.getBlock().setType(Material.AIR);
				e.getPlayer().sendMessage(ChatColor.BLUE + "Loot Safe location registered at: " + e.getBlock().getX() + ", " + e.getBlock().getY() + ", " + e.getBlock().getZ());
				break;
			}
			
		}
	}

}
