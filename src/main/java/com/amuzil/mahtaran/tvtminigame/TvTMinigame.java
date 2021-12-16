package com.amuzil.mahtaran.tvtminigame;

import com.amuzil.mahtaran.tvtminigame.command.TvTCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getPluginManager;

public final class TvTMinigame extends JavaPlugin {
	public static final String GAME_NAME = "teamvsteam";
	public static final int MAX_POINTS = 3;
	private static TvTMinigame instance;

	public static TvTMinigame getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		// Plugin startup logic
		PluginCommand tvt = getCommand("teamvsteam");
		if (tvt != null) {
			TvTCommand executor = new TvTCommand();
			tvt.setExecutor(executor);
			tvt.setTabCompleter(executor);
		}
		getPluginManager().registerEvents(new TvTEventListener(), this);
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
