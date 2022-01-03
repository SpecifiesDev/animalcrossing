package me.specifies.AnimalCrossing.Commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Utils.SQLManager;

public class Bells implements CommandExecutor {
	
	private Main plugin;
	private SQLManager manager;
	
	public Bells() {
		this.plugin = Main.getInstance();
		this.manager = plugin.getManager();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			try {
				String bells = manager.getPlayerBells(p.getUniqueId().toString());
				String miles = manager.getPlayerMiles(p.getUniqueId().toString());
				
				Inventory page = Bukkit.createInventory(null, 45, plugin.color("&aNook&8Terminal"));
				
				ItemFactory placeholders = new ItemFactory(Material.BLACK_STAINED_GLASS_PANE, 1);
				
				placeholders.setDisplayName(plugin.color("&0"));
				
				for(int i = 0; i <= 44; i++) if(!(i == 19 || i == 22 || i == 25)) page.setItem(i, placeholders.getItem());
				
				ItemFactory bellsItem = new ItemFactory(Material.GOLD_NUGGET, 1);
				
				bellsItem.setDisplayName(plugin.color("&eBalance&7:"));
				
				bellsItem.setLore("&6" + bells);
				
				page.setItem(19, bellsItem.getItem());
				
				ItemFactory milesItem = new ItemFactory(Material.MAP, 1);
				
				milesItem.setDisplayName(plugin.color("&3Miles&7:"));
				
				milesItem.setLore("&b" + miles);
				
				page.setItem(22, milesItem.getItem());
				
				ItemFactory comingsoon = new ItemFactory(Material.RED_STAINED_GLASS_PANE, 1);
				
				comingsoon.setDisplayName(plugin.color("&4COMING SOON"));
				
				page.setItem(25, comingsoon.getItem());
				
				
				p.openInventory(page);
				
				
			} catch(SQLException err) {
				err.printStackTrace();
				p.sendMessage(plugin.color("&cWe were unable to retrieve your bells."));
			}
			
		} else {
			sender.sendMessage(plugin.color("&cYou must be a player to use this command."));
		}
		
		return true;
	}

}
