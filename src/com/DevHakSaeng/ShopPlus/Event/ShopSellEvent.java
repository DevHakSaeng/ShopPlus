package com.DevHakSaeng.ShopPlus.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.DevHakSaeng.ShopPlus.ShopManager;

public final class ShopSellEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private int selledSlot;
	private int selledAmount;
	private Player selledPlayer;
	private ShopManager selledShop;
	
	public ShopSellEvent(int slot, ShopManager shop, Player player, int amount){
		selledSlot = slot;
		selledShop = shop;
		selledPlayer = player;
		selledAmount = amount;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public ShopManager getShop() {
    	return selledShop;
    }
	
    public int getSlot() {
    	return selledSlot;
    }
    
    public Player getPlayer() {
    	return selledPlayer;
    }
    
    public int getAmount() {
    	return selledAmount;
    }
    
    public ItemStack getItem() {
    	ItemStack item = getShop().getItem(selledSlot);
    	item.setAmount(getAmount());
    	return item;
    }
    
    public void setCancelled(boolean cancel) {
    	cancelled = cancel;
    }

	public boolean isCancelled() {
		return cancelled;
	}
    
}