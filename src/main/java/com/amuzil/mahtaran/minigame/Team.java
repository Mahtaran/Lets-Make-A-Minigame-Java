package com.amuzil.mahtaran.minigame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Team {
	private final Game game;
	private final String name;
	private int minPlayers;
	private int maxPlayers;
	private Location spawn;
	private final List<UUID> members;

	public Team(Game game, String name) {
		this(game, name, 0, 0, null);
	}

	public Team(Game game, String name, int minPlayers, int maxPlayers, Location spawn) {
		this.game = game;
		this.name = name;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.spawn = spawn;
		this.members = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public int getMinPlayers() {
		return minPlayers;
	}

	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public List<UUID> getMembers() {
		return members;
	}

	public void addMember(UUID member) {
		members.add(member);
	}

	public void removeMember(UUID member) {
		members.remove(member);
	}

	public boolean hasMember(UUID member) {
		return members.contains(member);
	}

	public List<OfflinePlayer> getOfflinePlayers() {
		return members.stream().map(Bukkit::getOfflinePlayer).collect(Collectors.toList());
	}

	public List<Player> getPlayers() {
		return Bukkit.getOnlinePlayers().stream().filter(this::hasPlayer).collect(Collectors.toList());
	}

	public void addPlayer(Player player) {
		addMember(player.getUniqueId());
	}

	public void removePlayer(Player player) {
		removeMember(player.getUniqueId());
	}

	public boolean hasPlayer(Player player) {
		return hasMember(player.getUniqueId());
	}
}
