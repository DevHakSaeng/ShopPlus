package com.DevHakSaeng.ShopPlus;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopManager {
	
	public String shopName;
	public Config cf;
	
	public ShopManager(String name) {
		this.shopName = name;
		this.cf = new Config("shops/" + this.shopName);
	}
	
	public boolean exists() {
		if (this.cf.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	//상점관리 메소드
	
	public void setShop() {
		cf.save();
		cf.load();
	}
	
	public void removeShop() {
		cf.delete();
	}
	
	public void reloadShop() {
		cf.load();
	}
	
	public int getLine() {
		return cf.getInt("줄");
	}
	
	public double getBuyPrice(Integer slot) {
		return cf.getDouble(slot + ".구매가");
	}
	
	public double getSellPrice(Integer slot) {
		return cf.getDouble(slot + ".판매가");
	}
	
	public ItemStack getItem(Integer slot) {
		return cf.getItemStack(slot.toString() + ".아이템");
	}
	
	public Inventory getItemSettingGUI() {
		Inventory inv = Bukkit.createInventory(null, getLine()*9, "§6§lH§9§lK§f§lSHOP §f(§r아이템설정§f) §7(§f" + this.shopName + "§7)");
		for (int i=0; i < getLine()*9; i++) {
			inv.setItem(i, getItem(i));
		}
		return inv;
	}
	
	public Inventory getPriceSettingGUI() {
		Inventory inv = Bukkit.createInventory(null, getLine()*9, "§6§lH§9§lK§f§lSHOP §f(§r가격설정§f) §7(§f" + this.shopName + "§7)");
		for (int i=0; i < getLine()*9; i++) {
			if (getItem(i) != null) {
				ItemStack item = getItem(i);
				List<String> lores;
				ItemMeta meta;
				if (getItem(i).hasItemMeta()) {
					if (getItem(i).getItemMeta().hasLore()) {
						meta = item.getItemMeta();
						lores = meta.getLore();
					} else {
						meta = item.getItemMeta();
						lores = new ArrayList<String>();
					}
				} else {
					meta = Bukkit.getItemFactory().getItemMeta(getItem(i).getType());
					lores = new ArrayList<String>();
				}
				if ((getBuyPrice(i) > 0) && (getSellPrice(i) > 0)) {
					lores.add(" ");
					lores.add("§a구매가 §7: §f" + new DecimalFormat("###,###.##").format(getBuyPrice(i)) + "§6원");
					lores.add("§c판매가 §7: §f" + new DecimalFormat("###,###.##").format(getSellPrice(i)) + "§6원");
					lores.add(" ");
				} else if ((getBuyPrice(i) > 0) && (!(getSellPrice(i) > 0))) {
					lores.add(" ");
					lores.add("§a구매가 §7: §f" + new DecimalFormat("###,###.##").format(getBuyPrice(i)) + "§6원");
					lores.add(" ");
				} else if ((!(getBuyPrice(i) > 0)) && (getSellPrice(i) > 0)) {
					lores.add(" ");
					lores.add("§c판매가 §7: §f" + new DecimalFormat("###,###.##").format(getSellPrice(i)) + "§6원");
					lores.add(" ");
				}
				meta.setLore(lores);
				item.setItemMeta(meta);
				inv.setItem(i, item);
			}
		}
		return inv;
	}
	
	public Inventory getShopGUI() {
		Inventory inv = Bukkit.createInventory(null, getLine()*9, "§6§lH§9§lK§f§lSHOP §r" + this.shopName);
		for (int i=0; i < getLine()*9; i++) {
			if (getItem(i) != null) {
				ItemStack item = getItem(i);
				List<String> lores;
				ItemMeta meta;
				if (getItem(i).hasItemMeta()) {
					if (getItem(i).getItemMeta().hasLore()) {
						meta = item.getItemMeta();
						lores = meta.getLore();
					} else {
						meta = item.getItemMeta();
						lores = new ArrayList<String>();
					}
				} else {
					meta = Bukkit.getItemFactory().getItemMeta(getItem(i).getType());
					lores = new ArrayList<String>();
				}
				if ((getBuyPrice(i) > 0) && (getSellPrice(i) > 0)) {
					lores.add(" ");
					lores.add("§a구매가 §7: §f" + new DecimalFormat("###,###.##").format(getBuyPrice(i)) + "§6원");
					lores.add("§c판매가 §7: §f" + new DecimalFormat("###,###.##").format(getSellPrice(i)) + "§6원");
					lores.add(" ");
				} else if ((getBuyPrice(i) > 0) && (!(getSellPrice(i) > 0))) {
					lores.add(" ");
					lores.add("§a구매가 §7: §f" + new DecimalFormat("###,###.##").format(getBuyPrice(i)) + "§6원");
					lores.add(" ");
				} else if ((!(getBuyPrice(i) > 0)) && (getSellPrice(i) > 0)) {
					lores.add(" ");
					lores.add("§c판매가 §7: §f" + new DecimalFormat("###,###.##").format(getSellPrice(i)) + "§6원");
					lores.add(" ");
				}
				meta.setLore(lores);
				item.setItemMeta(meta);
				inv.setItem(i, item);
			}
		}
		return inv;
	}

	public void setNPC(String entityName) {
		this.cf.set("NPC", entityName);
		this.cf.save();
	}
	
	public String getNPC() {
		return this.cf.getString("NPC");
	}
	
	public static ArrayList<String> getShops(){
		String[] Files = new File("plugins/" + Main.instance.getName() + "/shops/").list();
		ArrayList<String> Shops = new ArrayList<String>();
		for (String fileName : Files) {
			Shops.add(fileName.replace(".yml", ""));
		}
		return Shops;
	}
	
	public void setLine(Integer line) {
		this.cf.set("줄", line);
		this.cf.save();
	}
	
	public void delItem(Integer slot) {
		this.cf.set(slot.toString(), null);
		this.cf.save();
	}
	
	public void setBuyPrice(Integer slot, Double price) {
		this.cf.set(slot + ".구매가", price);
		this.cf.save();
	}
	
	public void setSellPrice(Integer slot, Double price) {
		this.cf.set(slot + ".판매가", price);
		this.cf.save();
	}
	
	public void setItem(Integer slot, ItemStack item) {
		this.cf.set(slot.toString() + ".아이템", item);
		this.cf.save();
	}	
	
}
