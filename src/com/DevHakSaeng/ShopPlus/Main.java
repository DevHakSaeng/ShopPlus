package com.DevHakSaeng.ShopPlus;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	public static Main instance;
	public static FileConfiguration config;
	public static Config cf;
	private static Economy econ;
	
	@Override
	public void onEnable() {
		instance = this;
		if (!setupEconomy()) {
			Bukkit.getConsoleSender().sendMessage("§cVAULT 플러그인이 없습니다!");
			getServer().getPluginManager().disablePlugin(instance);
			return;
		}
		saveDefaultConfig();
		config = getConfig();
		cf = new Config("config");
		setCommandExecutors();
		registerListeners();
		Bukkit.getConsoleSender().sendMessage("§6§lH§9§lK§f§lSHOP §a활성화!");
	}
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§6§lH§9§lK§f§lSHOP §c비활성화!");
	}
	
	private void setCommandExecutors() {
		String[] cmd = {"상점관리"};
		for (String s : cmd) {
			getCommand(s).setExecutor(new Commands());
			getCommand(s).setTabCompleter(new Commands());
		}
	}

	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
	}
	
	
	//Vault 이코노미 설정
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

    public static Economy getEconomy() {
        return econ;
    }
    
}
