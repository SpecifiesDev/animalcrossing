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
import me.specifies.AnimalCrossing.Constants.ConsoleAlerts;
import me.specifies.AnimalCrossing.Constants.PlayerAlerts;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Utils.SQLManager;


// for the time being going to leave the class name to bells. the in game command is
// /info, but I don't want to do the refractoring process as I'm not using ghd

public class Bells implements CommandExecutor {
	
	// create an instance for objects we need from the main class
	private Main plugin;
	private SQLManager manager;
	
	// intialize our objects in the constructor
	public Bells() {
		this.plugin = Main.getInstance();
		this.manager = plugin.getManager();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Ensure the sender is a player, if not, don't permit command execution
		if(sender instanceof Player) {
			
			// Cast Player type to the sender
			Player p = (Player) sender;
			
			// Surround it in a try catch clause for error management
			try {
				// Query our database for the info we need to construct the page.
				String bells = manager.getPlayerBells(p.getUniqueId().toString());
				String miles = manager.getPlayerMiles(p.getUniqueId().toString());
				
				Inventory page = Bukkit.createInventory(null, 45, plugin.color("&aNook&8Terminal"));
				
				// Create placeholder item and populate the inventory. Exclude the spaces where our other objects go.
				ItemFactory placeholders = new ItemFactory(Material.BLACK_STAINED_GLASS_PANE, 1);
				placeholders.setDisplayName(plugin.color("&0"));
				for(int i = 0; i <= 44; i++) if(!(i == 19 || i == 22 || i == 25)) page.setItem(i, placeholders.getItem());
				
				// We could reuse the above factory but won't for the sake of not wanting to cause unforseen issues.
				ItemFactory bellsItem = new ItemFactory(Material.GOLD_NUGGET, 1);
				bellsItem.setDisplayName(plugin.color("&eBalance&7:"));
				bellsItem.setLore("&6" + bells);
				page.setItem(19, bellsItem.getItem());
				
				ItemFactory milesItem = new ItemFactory(Material.MAP, 1);
				milesItem.setDisplayName(plugin.color("&3Miles&7:"));
				milesItem.setLore("&b" + miles);
				page.setItem(22, milesItem.getItem());
				
				// Not entirely sure what this will be. 
				ItemFactory comingsoon = new ItemFactory(Material.RED_STAINED_GLASS_PANE, 1);
				comingsoon.setDisplayName(plugin.color("&4COMING SOON"));
				page.setItem(25, comingsoon.getItem());
				
				// Open the generated inventory.
				p.openInventory(page);
				
				
			} catch(SQLException err) {
				err.printStackTrace();
				p.sendMessage(plugin.color(PlayerAlerts.ERROR_OBTAINING_INFO));
			}
			
		} else {
			sender.sendMessage(plugin.color(ConsoleAlerts.PLAYER_REQUIREMENT));
		}
		
		return true;
	}

}
