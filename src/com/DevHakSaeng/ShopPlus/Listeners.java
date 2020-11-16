package com.DevHakSaeng.ShopPlus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.DevHakSaeng.ShopPlus.Event.ShopBuyEvent;
import com.DevHakSaeng.ShopPlus.Event.ShopSellEvent;

public class Listeners implements Listener {
	
	//아이템 설정창 이벤트
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!e.getInventory().getTitle().contains("§6§lH§9§lK§f§lSHOP §f(§r아이템설정§f) §7(§f")) {
			return;
		}
		String inventoryName = e.getInventory().getTitle();
		String shopName = inventoryName.replace("§6§lH§9§lK§f§lSHOP §f(§r아이템설정§f) §7(§f", "").replace("§7)", "");
		Integer size = e.getInventory().getSize();
		for (int i=0; i < size; i++) {
			ShopManager shop = new ShopManager(shopName);
			if (e.getInventory().getItem(i) != null) {
				shop.setItem(i, e.getInventory().getItem(i));
			} else {
				shop.delItem(i);
			}
		}
		e.getPlayer().sendMessage(Main.cf.getColouredString("ShopItemSetDone").replace("{SHOPNAME}", shopName));	
	}
	
	//가격 설정창 이벤트
	@EventHandler
	public void onItemClick(InventoryClickEvent e) {
		if (!e.getInventory().getTitle().contains("§6§lH§9§lK§f§lSHOP §f(§r가격설정§f) §7(§f")) {
			return;
		}
		e.setCancelled(true);
		String inventoryName = e.getInventory().getTitle();
		String shopName = inventoryName.replace("§6§lH§9§lK§f§lSHOP §f(§r가격설정§f) §7(§f", "").replace("§7)", "");
		Player p = (Player) e.getWhoClicked();
		PlayerManager trade = new PlayerManager(p);
		trade.setBuyEdit(shopName, e.getRawSlot());
		p.closeInventory();
		p.sendMessage(Main.cf.getColouredString("ShopPriceSetStart"));
	}
	
	//상점 거래 이벤트
	@EventHandler
	public void onTrade(InventoryClickEvent e) {
		if (!(e.getInventory().getTitle().contains("§6§lH§9§lK§f§lSHOP §r"))) {
			return;
		}
		e.setCancelled(true);
		boolean isShift = e.isShiftClick();
		//1~64 , 64개 통합구매
		if (e.isLeftClick()) {
			if (e.getCurrentItem() == null) {
				return;
			}
			if (e.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			String shopName = e.getClickedInventory().getTitle().replace("§6§lH§9§lK§f§lSHOP §r", "");
			ShopManager shop = new ShopManager(shopName);
			Player p = (Player) e.getWhoClicked();
			ItemStack buyItem = shop.getItem(e.getRawSlot());
			PlayerManager trade = new PlayerManager(p);
			if (!(shop.getBuyPrice(e.getRawSlot()) > 0)) {
				p.sendMessage(Main.cf.getColouredString("BannedBuy"));
				return;
			}
			double hasMoney = trade.getMoney();
			double buyPrice = shop.getBuyPrice(e.getRawSlot());
			int amount = isShift ? (hasMoney >= buyPrice*64) ? 64 : (int) Math.floor(hasMoney/buyPrice) : (hasMoney >= buyPrice) ? 1 : 0;
			if (!(amount > 0)) {
				p.sendMessage(Main.cf.getColouredString("LackMoney").replace("{LEFTMONEY}", String.valueOf(trade.getMoney())));
				return;
			}
			ShopBuyEvent event = new ShopBuyEvent(e.getRawSlot(), shop, p, amount);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return;
			}
			buyItem.setAmount(amount);
			trade.removeMoney(shop.getBuyPrice(e.getRawSlot())*amount);
			p.getInventory().addItem(buyItem);
			p.sendMessage(Main.cf.getColouredString("BuyItems").replace("{NUM}", String.valueOf(amount)).replace("{LEFTMONEY}", String.valueOf(trade.getMoney())));
		//1~64 , 64개 통합판매
		} else if (e.isRightClick()) {
			if (e.getCurrentItem() == null) {
				return;
			}
			if (e.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			String shopName = e.getClickedInventory().getTitle().replace("§6§lH§9§lK§f§lSHOP §r", "");
			ShopManager shop = new ShopManager(shopName);
			Player p = (Player) e.getWhoClicked();
			ItemStack sellItem = shop.getItem(e.getRawSlot());
			PlayerManager trade = new PlayerManager(p);
			if (!(shop.getSellPrice(e.getRawSlot()) > 0)) {
				p.sendMessage(Main.cf.getColouredString("BannedSell"));
				return;
			}
			int hasAmount = PlayerManager.getItemAmount(p.getInventory(), sellItem);
			int amount = isShift ? (hasAmount >= 64) ? 64 : hasAmount : (hasAmount >= 1) ? 1 : 0;
			if (!(amount > 0)) {
				p.sendMessage(Main.cf.getColouredString("LackItem"));
				return;
			}
			ShopSellEvent event = new ShopSellEvent(e.getRawSlot(), shop, p, amount);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				return;
			}
			sellItem.setAmount(amount);
			if (p.getInventory().getItemInOffHand().isSimilar(sellItem)) {
				int offHandAmount= p.getInventory().getItemInOffHand().getAmount();
				ItemStack offHandItem = sellItem;
				offHandItem.setAmount(offHandAmount-amount);
				p.getInventory().setItemInOffHand(offHandItem);
				if (offHandAmount < amount) {
					offHandItem.setAmount(amount-offHandAmount);
					p.getInventory().removeItem(offHandItem);
				}
			} else {
				p.getInventory().removeItem(sellItem);
			}						
			trade.addMoney(shop.getSellPrice(e.getRawSlot())*amount);
			p.sendMessage(Main.cf.getColouredString("SellItems").replace("{NUM}", String.valueOf(amount)).replace("{LEFTMONEY}", String.valueOf(trade.getMoney())));
		}
	}
	
	
	//NPC상점 이벤트
	@EventHandler
	public void onClickNPC(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		Entity NPC = e.getRightClicked();
		String Name = NPC.getName();
		if (!NPC.hasMetadata("NPC")) {
			return;
		}
		for (String shop : ShopManager.getShops()) {
			ShopManager check = new ShopManager(shop);
			if (check.getNPC() == null) {
				continue;
			}
			if (!check.getNPC().equals(Name)) {
				continue;
			}
			if (e.getHand() != EquipmentSlot.HAND) {
				return;
			}		
			e.setCancelled(true);
			p.openInventory(check.getShopGUI());
			p.sendMessage(Main.cf.getColouredString("OpenShop").replace("{SHOPNAME}", shop));
			return;
		}
	}
	
	//상점 NPC설정 이벤트
	@EventHandler
	public void onSetNPC(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		Entity NPC = e.getRightClicked();
		String Name = NPC.getName();
		if (!NPC.hasMetadata("NPC")) {
			return;
		}
		PlayerManager set = new PlayerManager(p);
		if (set.getNPCEditShop() == null) {
			return;
		}
		if (e.getHand() != EquipmentSlot.HAND) {
			return;
		}
		e.setCancelled(true);
		String shopName = set.getNPCEditShop();
		ShopManager shop = new ShopManager(shopName);
		shop.setNPC(Name);
		set.removeNPCEdit();
		p.sendMessage(Main.cf.getColouredString("ShopNPCSetDone").replace("{SHOPNAME}", shopName).replace("{SHOPNPC}", Name));
	}
	
	//채팅으로 가격설정
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		PlayerManager trade = new PlayerManager(e.getPlayer());
		String shopName = trade.getEditingShop();
		Integer slot = trade.getEditingSlot();
		String state = trade.getEditingState();
		if((shopName == null) || (slot == null)) {
			return;
		}	
		if (state.equals("구매")) {
			e.setCancelled(true);
			ShopManager shop = new ShopManager(shopName);
			Double price = Double.parseDouble(e.getMessage());
			if (price instanceof Double) {
				shop.setBuyPrice(slot, price);
				trade.setSellEdit(shopName, slot);
				e.getPlayer().sendMessage(Main.cf.getColouredString("ShopPriceSetting"));
			} else {
				e.getPlayer().sendMessage(Main.cf.getColouredString("EnterInteger"));
			}
		}
		else if (state.equals("판매")) {
			e.setCancelled(true);
			ShopManager shop = new ShopManager(shopName);
			Double price = Double.parseDouble(e.getMessage());
			if (price instanceof Double) {
				shop.setSellPrice(slot, price);
				trade.removeEdit();
				e.getPlayer().sendMessage(Main.cf.getColouredString("ShopPriceSetDone").replace("{SHOPNAME}", shopName).replace("{SHOPBUY}", String.valueOf(shop.getBuyPrice(slot))).replace("{SHOPSELL}", String.valueOf(price)));
			} else {
				e.getPlayer().sendMessage(Main.cf.getColouredString("EnterInteger"));
			}			
		}
	}
}
