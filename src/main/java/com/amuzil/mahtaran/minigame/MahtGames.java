package com.amuzil.mahtaran.minigame;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MahtGames {
	private static final Map<String, Game> games = new HashMap<>();

	/*@
	ensures exists(game.getName()) == true <==> \result == false;
	@*/
	public static boolean register(Game game) {
		if (exists(game.getName())) {
			return false;
		} else {
			games.put(game.getName(), game);
			return true;
		}
	}

	/*@
	ensures exists(gameName) == true <==> \result == null;
	@*/
	public static Game create(String gameName) {
		Game game = new Game(gameName);
		if (register(game)) {
			return game;
		} else {
			return null;
		}
	}

	public static boolean exists(String gameName) {
		return games.containsKey(gameName);
	}

	public static Game get(String gameName) {
		return games.get(gameName);
	}

	/*@
	ensures exists(gameName) == true <==> \result == true;
	@*/
	public static boolean join(String gameName, Player player, String teamName) {
		if (exists(gameName)) {
			get(gameName).join(player, teamName);
			return true;
		} else {
			return false;
		}
	}

	/*@
	ensures exists(gameName) == true <==> \result == true;
	@*/
	public static boolean start(String gameName) {
		if (exists(gameName)) {
			get(gameName).start();
			return true;
		} else {
			return false;
		}
	}

	/*@
	ensures exists(gameName) == true <==> \result == true;
	@*/
	public static boolean stop(String gameName) {
		if (exists(gameName)) {
			get(gameName).stop();
			return true;
		} else {
			return false;
		}
	}

	/*@
	ensures exists(gameName) == true <==> \result == true;
	@*/
	public static boolean delete(String gameName) {
		if (exists(gameName)) {
			games.remove(gameName);
			return true;
		} else {
			return false;
		}
	}
}
