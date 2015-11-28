package main.java.onehitkill.utils;

import java.util.HashMap;

import main.java.onehitkill.User;

import org.bukkit.entity.Player;

public class UserUtils {
	public static HashMap<Player, User> players = new HashMap<Player, User>();

	public static User getUser(Player user) {
		if (user == null) {
			return null;
		}
		User player = null;
		if (players.containsKey(user)) {
			return players.get(user);
		} else {
			player = new User(user);
			player.loadData();
			players.put(user, player);
		}
		return player;
	}
	
}
