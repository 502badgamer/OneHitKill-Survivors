package main.java.onehitkill.commands;

import main.java.onehitkill.Inventories;
import main.java.onehitkill.Main;
import main.java.onehitkill.User;
import main.java.onehitkill.listeners.PlayerListener;
import main.java.onehitkill.utils.UserUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOHK implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command must be executed by a player!");
			return true;
		}

		User user = UserUtils.getUser((Player)sender);

		if (!user.player.isOp()) {
			user.player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
			return true;
		}
		
		if (args.length != 0) {
			switch (args[0].toLowerCase()) {
			case "help":
				user.player.sendMessage(ChatColor.BLUE + "/ohk setspawn:" + ChatColor.AQUA + " Set an OHK spawn (when people join they go to a random one)");
				user.player.sendMessage(ChatColor.BLUE + "/ohk kitcustomizer:" + ChatColor.AQUA + " Customize kits");
				return true;
			case "setspawn":
				Main.setSpawn(user.player.getLocation(), (Main.getMaxSpawn()));
				user.player.sendMessage(ChatColor.DARK_RED + "[One Hit Kill] " + ChatColor.BLUE + "Spawn point placed at current location.");
				return true;
			case "kitcustomizer":
				user.player.openInventory(Inventories.kitCustomizer());
				return true;
			}
		}
		
		user.player.sendMessage(ChatColor.RED + "Invalid arguments. Try /ohk help for a list of commands");
		return true;
	}

}
