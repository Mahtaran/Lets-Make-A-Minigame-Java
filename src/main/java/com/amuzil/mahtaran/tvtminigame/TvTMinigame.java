package com.amuzil.mahtaran.tvtminigame;

import com.amuzil.mahtaran.tvtminigame.command.TvTCommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TvTMinigame extends JavaPlugin {
	@Override
	public void onEnable() {
		// Plugin startup logic
		PluginCommand tvt = getCommand("teamvsteam");
		if (tvt != null) {
			tvt.setExecutor(new TvTCommand());
			tvt.setTabCompleter(new TvTCommand());
		}
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
