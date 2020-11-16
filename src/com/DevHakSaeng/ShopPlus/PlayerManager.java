package com.DevHakSaeng.ShopPlus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {
	
	public OfflinePlayer player;
	public static Config cf = new Config("tempData");
	public static Config NPC = new Config("NPCSet");
	
	public PlayerManager(Player p) {
		this.player = (OfflinePlayer) p;
	}

	
	public static int getItemAmount(Inventory inv, ItemStack countedItem) {
		List<ItemStack> Items = new ArrayList<ItemStack>(Arrays.asList(inv.getContents()));
		Integer count = 0;
		for (ItemStack item : Items) {
			if (countedItem.isSimilar(item)) {
				count = count + item.getAmount();
			}
		}
		return count;
	}	
	
	
	public double getMoney() {
		return Main.getEconomy().getBalance(player);
	}
	
	public boolean hasMoney(double money) {
		return getMoney() >= money;
	}
	
	public void addMoney(double money) {
		Main.getEconomy().depositPlayer(player, money);
	}
	
	public void removeMoney(double money) {
		Main.getEconomy().withdrawPlayer(player, money);
	}
	
	public void setMoney(double money) {
		removeMoney(getMoney());
		addMoney(money);
	}
	
	//가격설정상태, NPC설정상태 관리메소드
	public int getEditingSlot() {
		return cf.getInt(player.getName() + ".슬롯");
	}
	
	public String getEditingShop() {
		return cf.getString(player.getName() + ".상점");
	}
	
	public String getEditingState() {
		return cf.getString(player.getName() + ".상태");
	}
	
	public String getNPCEditShop() {
		return NPC.getString(player.getName() + ".상점");
	}
	
	public void setBuyEdit(String shopName, int slot) {
		cf.set(player.getName() + ".슬롯", slot);
		cf.set(player.getName() + ".상태", "구매");
		cf.set(player.getName() + ".상점", shopName);
	}
	
	public void setSellEdit(String shopName, int slot) {
		cf.set(player.getName() + ".슬롯", slot);
		cf.set(player.getName() + ".상태", "판매");
		cf.set(player.getName() + ".상점", shopName);
	}
	
	public void removeEdit() {
		cf.set(player.getName(), null);
	}
	
	public void setNPCEdit(String shopName) {
		NPC.set(player.getName() + ".상점", shopName);
	}
	
	public void removeNPCEdit() {
		NPC.set(player.getName(), null);
	}
	
}
