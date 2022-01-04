package me.specifies.AnimalCrossing.Commands;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Constants.ConsoleAlerts;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Inventories.ScrollingInventory;

public class RecipeShop implements CommandExecutor {
	
	// objects that we need
	private Main plugin;

	// intialize our objects
	public RecipeShop() {
		this.plugin = Main.getInstance();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		
		// ensure sender is a player, if not deny functionality
		if(sender instanceof Player) {
			
			// cast player type to the sender
			Player p = (Player) sender;
			
			// create an arraylist to store our information
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			
			// loop through the hashmap of recipes and retrieve the necessary info
			for(Map.Entry<String, String[]> entry : plugin.getRecipes().entrySet()) {
				
				// grab the array
				String[] formatted = entry.getValue();
				
				// grab our necessary values to construct an inventory
				String name = formatted[0];
				String permission = formatted[1];
				String itemType = formatted[2];
				String cost = formatted[4];
				
				// if the player hasn't bought the item, add it to the list of items to render
				if(!(p.hasPermission(permission))) {
					
					ItemFactory factory = new ItemFactory(Material.valueOf(itemType), 1);
					
					factory.setDisplayName(plugin.color(name));
					factory.setLore("&eCost&8:~&6" + cost);
					items.add(factory.getItem());
				}
				
				// Create a new scrolling inventory object
				new ScrollingInventory(items, "&cRecipe Shop", p);
				
				
			}
			
		} else sender.sendMessage(plugin.color(ConsoleAlerts.PLAYER_REQUIREMENT));
		
		return true;
	}
	
}
