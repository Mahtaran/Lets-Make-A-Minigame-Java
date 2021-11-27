package com.amuzil.mahtaran.tvtminigame.command;

import com.alexlew.gameapi.types.Game;
import com.alexlew.gameapi.types.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TvTCommand implements CommandExecutor, TabCompleter {
	private static final String GAME_NAME = "teamvsteam";
	private Location lobby;
	private Location blueSpawn;
	private Location redSpawn;

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		if (!(sender instanceof Player)) {
			sendMessage(sender, "&cOnly players can use this command.");
			return true;
		}
		Player player = (Player) sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("create")) {
				if (Game.getGames().containsKey(GAME_NAME)) {
					sendMessage(player, "&cThe event has already been created!");
					return true;
				} else {
					Game game = new Game(GAME_NAME);
					game.setWorld(player.getWorld());
					game.setLobby(lobby);

					Team blue = new Team("blue", game);
					blue.setMinPlayer(1);
					blue.setMaxPlayer(30);
					blue.setSpawn(blueSpawn);
					game.addTeam(blue);

					Team red = new Team("red", game);
					red.setMinPlayer(1);
					red.setMaxPlayer(30);
					red.setSpawn(redSpawn);
					game.addTeam(red);

					sendMessage(player, "&6The event has been created!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("start")) {
				if (Game.getGames().containsKey(GAME_NAME)) {
					Game.getGames().get(GAME_NAME).start();
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6Team vs Team event started!"));
					return true;
				} else {
					sendMessage(player, "&cThe game hasn't been created yet!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (Game.getGames().containsKey(GAME_NAME)) {
					Game.getGames().get(GAME_NAME).stop();
					Game.getGames().get(GAME_NAME).delete();
					sendMessage(player, "&aThe game has been stopped!");
					return true;
				} else {
					sendMessage(player, "&cThe game hasn't been created yet!");
					return true;
				}
			}
		} else if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("set")) {
				if (args.length == 2 && args[1].equalsIgnoreCase("lobby")) {
					lobby = player.getLocation();
					sendMessage(player, String.format("&aLobby position set to &b(%.2f, %.2f, %.2f)", lobby.getX(), lobby.getY(), lobby.getZ()));
					return true;
				} else if (args.length == 3 && args[1].equalsIgnoreCase("spawn")) {
					if (args[2].equalsIgnoreCase("blue")) {
						blueSpawn = player.getLocation();
						sendMessage(player, String.format("&aBlue spawn position set to &b(%.2f, %.2f, %.2f)", blueSpawn.getX(), blueSpawn.getY(), blueSpawn.getZ()));
						return true;
					} else if (args[2].equalsIgnoreCase("red")) {
						redSpawn = player.getLocation();
						sendMessage(player, String.format("&aRed spawn position set to &b(%.2f, %.2f, %.2f)", redSpawn.getX(), redSpawn.getY(), redSpawn.getZ()));
						return true;
					}
				}
			} else if (args[0].equalsIgnoreCase("join")) {
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("blue")) {
						Game.getGames().get(GAME_NAME).addPlayer(player);
						Game.getGames().get(GAME_NAME).getTeam("blue").addPlayer(player);
						return true;
					} else if (args[1].equalsIgnoreCase("red")) {
						Game.getGames().get(GAME_NAME).addPlayer(player);
						Game.getGames().get(GAME_NAME).getTeam("red").addPlayer(player);
						return true;
					}
				}
			}
		}
		sendHelp(sender, alias, args);
		return true;
	}

	private void sendMessage(CommandSender receiver, String message) {
		receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}

	@Override
	@Nullable
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
		List<String> result = new ArrayList<>();
		if (args.length == 1) {
			addCompletions(result, args[0], "create", "start", "stop", "set", "join");
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set")) {
				addCompletions(result, args[1], "lobby", "spawn");
			} else if (args[0].equalsIgnoreCase("join")) {
				addCompletions(result, args[1], "blue", "red");
			}
		} else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set")) {
				if (args[1].equalsIgnoreCase("spawn")) {
					addCompletions(result, args[2], "blue", "red");
				}
			}
		}
		return result;
	}

	public void sendHelp(CommandSender receiver, String alias, String[] args) {
		String helpMessage = "/%s [create|start|stop|set|join]";
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("create")) {
				helpMessage = "/%s create";
			} else if (args[0].equalsIgnoreCase("start")) {
				helpMessage = "/%s start";
			} else if (args[0].equalsIgnoreCase("stop")) {
				helpMessage = "/%s stop";
			} else if (args[0].equalsIgnoreCase("set")) {
				helpMessage = "/%s set [lobby|spawn]";
				if (args.length >= 2) {
					if (args[1].equalsIgnoreCase("lobby")) {
						helpMessage = "/%s set lobby";
					} else if (args[1].equalsIgnoreCase("spawn")) {
						helpMessage = "/%s set spawn [blue|red]";
						if (args.length >= 3) {
							if (args[2].equalsIgnoreCase("blue")) {
								helpMessage = "/%s set spawn blue";
							} else if (args[2].equalsIgnoreCase("red")) {
								helpMessage = "/%s set spawn red";
							}
						}
					}
				}
			} else if (args[0].equalsIgnoreCase("join")) {
				helpMessage = "/%s join [blue|red]";
				if (args.length >= 2) {
					if (args[1].equalsIgnoreCase("blue")) {
						helpMessage = "/%s join blue";
					} else if (args[1].equalsIgnoreCase("red")) {
						helpMessage = "/%s join red";
					}
				}
			}
		}
		sendMessage(receiver, "&cIncorrect usage; try " + String.format(helpMessage, alias));
	}

	public void addCompletions(List<String> completions, String argument, String... possibleCompletions) {
		for (String completion : possibleCompletions) {
			if (completion.toLowerCase().startsWith(argument.toLowerCase())) {
				completions.add(completion);
			}
		}
	}
}
