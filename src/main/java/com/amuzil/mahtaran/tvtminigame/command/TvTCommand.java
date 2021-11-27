package com.amuzil.mahtaran.tvtminigame.command;

import com.amuzil.mahtaran.minigame.Game;
import com.amuzil.mahtaran.minigame.MahtGames;
import com.amuzil.mahtaran.minigame.Team;
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
				if (MahtGames.exists(GAME_NAME)) {
					sendMessage(player, "&cThe event has already been created!");
					return true;
				} else {
					Game game = MahtGames.create(GAME_NAME);
					if (game == null) {
						sendMessage(player, "&cAn unknown error occurred!");
						return true;
					}
					game.setLobby(lobby);

					Team blue = game.createTeam("blue");
					blue.setMinPlayers(1);
					blue.setMaxPlayers(30);
					blue.setSpawn(blueSpawn);

					Team red = game.createTeam("red");
					red.setMinPlayers(1);
					red.setMaxPlayers(30);
					red.setSpawn(redSpawn);

					sendMessage(player, "&6The event has been created!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("start")) {
				if (MahtGames.exists(GAME_NAME)) {
					MahtGames.start(GAME_NAME);
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6Team vs Team event started!"));
					return true;
				} else {
					sendMessage(player, "&cThe game hasn't been created yet!");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("stop")) {
				if (MahtGames.exists(GAME_NAME)) {
					MahtGames.stop(GAME_NAME);
					MahtGames.delete(GAME_NAME);
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
						sendMessage(player, String.format("&9Blue&a spawn position set to &b(%.2f, %.2f, %.2f)", blueSpawn.getX(), blueSpawn.getY(), blueSpawn.getZ()));
						return true;
					} else if (args[2].equalsIgnoreCase("red")) {
						redSpawn = player.getLocation();
						sendMessage(player, String.format("&cRed&a spawn position set to &b(%.2f, %.2f, %.2f)", redSpawn.getX(), redSpawn.getY(), redSpawn.getZ()));
						return true;
					}
				}
			} else if (args[0].equalsIgnoreCase("join")) {
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("blue")) {
						MahtGames.join(GAME_NAME, player, "blue");
						sendMessage(player, "&aJoined the &9blue&a team!");
						return true;
					} else if (args[1].equalsIgnoreCase("red")) {
						MahtGames.join(GAME_NAME, player, "red");
						sendMessage(player, "&aJoined the &cred&a team!");
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
