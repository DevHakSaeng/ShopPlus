package com.DevHakSaeng.ShopPlus.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.DevHakSaeng.ShopPlus.ShopManager;

public final class ShopBuyEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private int buyedSlot;
	private int buyedAmount;
	private Player buyedPlayer;
	private ShopManager buyedShop;
	
	public ShopBuyEvent(int slot, ShopManager shop, Player player, int amount){
		buyedSlot = slot;
		buyedShop = shop;
		buyedPlayer = player;
		buyedAmount = amount;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public ShopManager getShop() {
    	return buyedShop;
    }
	
    public int getSlot() {
    	return buyedSlot;
    }
    
    public Player getPlayer() {
    	return buyedPlayer;
    }
    
    public int getAmount() {
    	return buyedAmount;
    }
    
    public ItemStack getItem() {
    	ItemStack item = getShop().getItem(buyedSlot);
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
