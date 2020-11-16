package com.DevHakSaeng.ShopPlus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Config {
	
	private String Path;
	private String configName;
	private File configFile;
	private YamlConfiguration cf;
	
	public Config(String Name) {
		this("plugins/" + Main.instance.getName() + "/", Name);
	}
	
	public Config(String Path, String Name) {
		this.Path = Path;
		this.configName = Name;
		this.configFile = new File(this.Path + this.configName + ".yml");
		this.cf = YamlConfiguration.loadConfiguration(this.configFile);
	}
	
	public void delete() {
		if (this.configFile.exists()) {
			this.configFile.delete();
		}
	}
	
	public String getColouredString(String path) {
		return ChatColor.translateAlternateColorCodes('&', this.cf.getString(path));
	}
	
	public List<String> getColouredStringList(String path) {
		ArrayList<String> msgs = new ArrayList<String>();
		for (String msg : this.cf.getStringList(path)) {
			String coloured = ChatColor.translateAlternateColorCodes('&', msg);
			msgs.add(coloured);
		}
		return msgs;
	}
	
	public boolean exists() {
		return this.configFile.exists();
	}
	
	public boolean contains(String path) {
		return this.cf.contains(path);
	}
	
	public void save() {
		try {this.cf.save(this.configFile);} catch (IOException e) {}
	}
	
	public void load() {
		try {this.cf.load(this.configFile);} catch (IOException | InvalidConfigurationException e) {}
	}
	
	public void set(String Path, Object value) {
		this.cf.set(Path, value);
	}
	
	public ItemStack getItemStack(String Path) {
		return this.cf.getItemStack(Path);
	}
	
	public ItemStack getItemStack(String Path, ItemStack def) {
		return this.cf.getItemStack(Path, def);
	}
	
	public String getString(String Path) {
		return this.cf.getString(Path);
	}
	
	public String getString(String Path, String def) {
		return this.cf.getString(Path, def);
	}
	
	public List<String> getStringList(String Path) {
		return this.cf.getStringList(Path);
	}
	
	public int getInt(String Path) {
		return this.cf.getInt(Path);
	}
	
	public double getDouble(String Path) {
		return this.cf.getDouble(Path);
	}
	
	public Map<String, Object> getValues(Boolean deep) {
		return this.cf.getValues(deep);
	}
}
