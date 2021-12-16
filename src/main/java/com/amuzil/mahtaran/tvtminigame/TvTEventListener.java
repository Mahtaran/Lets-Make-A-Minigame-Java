package com.amuzil.mahtaran.tvtminigame;

import com.amuzil.mahtaran.mahtgames.Game;
import com.amuzil.mahtaran.mahtgames.MahtGames;
import com.amuzil.mahtaran.mahtgames.Points;
import com.amuzil.mahtaran.mahtgames.Team;
import com.amuzil.mahtaran.mahtgames.event.bukkit.TeamWinPointsEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class TvTEventListener implements Listener {
	@EventHandler
	public void disableFriendlyFire(EntityDamageByEntityEvent event) {
		if (MahtGames.exists(TvTMinigame.GAME_NAME)) {
			if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
				Player attacker = (Player) event.getDamager();
				Player victim = (Player) event.getEntity();
				Game game = MahtGames.getGameOfPlayer(attacker);
				if (game != null && game == MahtGames.getGameOfPlayer(victim)) {
					if (game.getTeamOfPlayer(attacker) == game.getTeamOfPlayer(victim)) {
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void respawn(PlayerRespawnEvent event) {
		if (MahtGames.exists(TvTMinigame.GAME_NAME)) {
			Game game = MahtGames.getGameOfPlayer(event.getPlayer());
			if (game != null && game.getInternalName().equals(TvTMinigame.GAME_NAME)) {
				event.setRespawnLocation(game.getTeamOfPlayer(event.getPlayer()).getSpawn());
			}
		}
	}

	@EventHandler
	public void scoringSystem(PlayerDeathEvent event) {
		if (MahtGames.exists(TvTMinigame.GAME_NAME)) {
			Game game = MahtGames.getGameOfPlayer(event.getEntity());
			if (game != null && game.getInternalName().equals(TvTMinigame.GAME_NAME)) {
				Bukkit.getScheduler().runTaskLater(TvTMinigame.getInstance(), () -> {
					event.getEntity().spigot().respawn();
					if (event.getEntity().getKiller() != null) {
						Player killer = event.getEntity().getKiller();
						Team team = game.getTeamOfPlayer(killer);
						team.addPoints(new Points(1));
						Bukkit.broadcastMessage("Team " + team.getDisplayName() + " points: " + team.getPoints().getPoints());
					}
				}, 1);
			}
		}
	}

	@EventHandler
	public void onGameWon(TeamWinPointsEvent event) {
		if (event.getPoints().getGame().getInternalName().equals(TvTMinigame.GAME_NAME)) {
			if (event.getPoints().getPoints() >= TvTMinigame.MAX_POINTS) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&" + event.getPoints().getTeam().getDisplayName() + " &bhas won Team vs Team!"));
				event.getPoints().getTeam().getPlayers().forEach(player -> player.getInventory().addItem(new ItemStack(Material.DIAMOND, 5)));
				Bukkit.getScheduler().runTaskLater(TvTMinigame.getInstance(), () -> {
					MahtGames.stop(TvTMinigame.GAME_NAME);
					MahtGames.delete(TvTMinigame.GAME_NAME);
				}, 1);
			}
		}
	}
}
