package com.DevHakSaeng.ShopPlus;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Commands implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// 상점관리 명령어
		if (!label.equals("상점관리")) {
			return false;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(Main.cf.getColouredString("BukkitNo"));
			return false;
		}
		Player p = (Player) sender;
		if (!p.isOp()) {
			p.sendMessage(Main.cf.getColouredString("NoPerm"));
			return false;
		}
		if (args.length == 0) {
			List<String> msgs = Main.cf.getColouredStringList("ShopManage");
			for (String msg : msgs) {
				p.sendMessage(msg);
			}
		}
		else if (args[0].equals("리로드")) {
			if (args.length == 1) {
				Main.cf.load();
				p.sendMessage(Main.cf.getColouredString("ShopReloadConfig"));
			} else {
				ShopManager shop = new ShopManager(args[1]);
				shop.reloadShop();
				p.sendMessage(Main.cf.getColouredString("ShopReloadShop").replace("{SHOPNAME}", args[1]));
			}
		}
		else if (args[0].equals("열기")) {
			if (args.length != 1) {
				ShopManager shop = new ShopManager(args[1]);
				if (!shop.exists()) {
					p.sendMessage(Main.cf.getColouredString("ShopDoesNotExists").replace("{SHOPNAME}", args[1]));
					return false;
				}
				p.openInventory(shop.getShopGUI());
				p.sendMessage(Main.cf.getColouredString("OpenShop").replace("{SHOPNAME}", args[1]));
			}
		}
		else if (args[0].equals("NPC등록")) {
			if (args.length != 1) {
				PlayerManager set = new PlayerManager(p);
				ShopManager shop = new ShopManager(args[1]);
				if (!shop.exists()) {
					p.sendMessage(Main.cf.getColouredString("ShopDoesNotExists").replace("{SHOPNAME}", args[1]));
					return false;
				}
				set.setNPCEdit(args[1]);
				p.sendMessage(Main.cf.getColouredString("ShopNPCSetPrepare").replace("{SHOPNAME}", args[1]));
			}
		}		
		else if (args[0].equals("줄설정")) {
			if ((args.length != 1) && (args.length != 2)) {
				ShopManager shop = new ShopManager(args[2]);
				if (!shop.exists()) {
					p.sendMessage(Main.cf.getColouredString("ShopDoesNotExists").replace("{SHOPNAME}", args[1]));
					return false;
				}
				Integer Line = Integer.parseInt(args[1]);
				if (!(Line instanceof Integer)) {
					p.sendMessage(Main.cf.getColouredString("EnterInteger"));
					return false;
				}
				shop.setLine(Line);
				p.sendMessage(Main.cf.getColouredString("ShopLineSetDone").replace("{SHOPNAME}", args[2]).replace("{SHOPLINE}", args[1]));
			}
		}	
		else if (args[0].equals("생성")) {
			if (args.length != 1) {
				ShopManager shop = new ShopManager(args[1]);
				if (shop.exists()) {
					p.sendMessage(Main.cf.getColouredString("ShopAlreadyExists").replace("{SHOPNAME}", args[1]));
					return false;
				}
				shop.setShop();
				shop.setLine(6);
				p.sendMessage(Main.cf.getColouredString("ShopCreationDone").replace("{SHOPNAME}", args[1]));
			}
		}
		else if (args[0].equals("삭제")) {
			if (args.length != 1) {
				ShopManager shop = new ShopManager(args[1]);
				if (!shop.exists()) {
					p.sendMessage(Main.cf.getColouredString("ShopDoesNotExists").replace("{SHOPNAME}", args[1]));
					return false;
				}
				shop.removeShop();
				p.sendMessage(Main.cf.getColouredString("ShopDeletionDone").replace("{SHOPNAME}", args[1]));		
			}
		}	
		else if (args[0].equals("아이템설정")) {
			if (args.length != 1) {
				ShopManager shop = new ShopManager(args[1]);
				if (!shop.exists()) {
					p.sendMessage(Main.cf.getColouredString("ShopDoesNotExists").replace("{SHOPNAME}", args[1]));
					return false;
				}
				Inventory inv = shop.getItemSettingGUI();
				p.openInventory(inv);
				p.sendMessage(Main.cf.getColouredString("ShopItemSetOpen").replace("{SHOPNAME}", args[1]));
			}
		}	
		else if (args[0].equals("가격설정")) {
			if (args.length != 1) {
				ShopManager shop = new ShopManager(args[1]);
				if (!shop.exists()) {
					p.sendMessage(Main.cf.getColouredString("ShopDoesNotExists").replace("{SHOPNAME}", args[1]));
					return false;
				}
				Inventory inv = shop.getPriceSettingGUI();
				p.openInventory(inv);
				p.sendMessage(Main.cf.getColouredString("ShopPriceSetOpen").replace("{SHOPNAME}", args[1]));
			}
		}
		else {
			p.sendMessage(Main.cf.getColouredString("UnknownCommand"));
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!label.equals("상점관리")) {
			return null;
		}
		if (!(sender instanceof Player)) {
			return null;
		}
		if (args.length == 1) {
			return Arrays.asList("리로드" , "열기" , "NPC등록" , "가격설정" , "아이템설정" , "줄설정" , "생성" , "삭제");
		} else if ((!args[0].equals("생성")) && (!args[0].equals("줄설정")) && (args.length == 2)) {
			return ShopManager.getShops();
		} else if ((args[0].equals("줄설정")) && (args.length == 3)) {
			return ShopManager.getShops();
		} else {
			return null; 
		}
	}
}
