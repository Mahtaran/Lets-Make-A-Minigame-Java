package com.amuzil.mahtaran.minigame;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private final String name;
	private final List<Team> teams;
	private Location lobby;

	public Game(String name) {
		this.name = name;
		teams = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void addTeam(Team team) {
		teams.add(team);
	}

	public Team createTeam(String teamName) {
		Team team = new Team(this, teamName);
		addTeam(team);
		return team;
	}

	public Team getTeam(String teamName) {
		return teams.stream().filter(team -> team.getName().equals(teamName)).findFirst().orElse(null);
	}

	public void join(Player player, Team team) {
		team.addPlayer(player);
		player.teleport(lobby);
	}

	public void join(Player player, String teamName) {
		join(player, getTeam(teamName));
	}

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location location) {
		this.lobby = location;
	}

	public void start() {
		teams.forEach(team -> team.getPlayers().forEach(player -> player.teleport(team.getSpawn())));
	}

	public void stop() {
		teams.forEach(team -> team.getPlayers().forEach(player -> player.teleport(lobby)));
	}
}
