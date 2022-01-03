package me.specifies.AnimalCrossing.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Inventories.ScrollingInventory;

public class Recipes implements CommandExecutor {
	
	private Main plugin;
	private HashMap<String, String[]> recipes;
	
	public Recipes() {
		this.plugin = Main.getInstance();
		this.recipes = plugin.getRecipes();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for(Map.Entry<String, String[]> entry : recipes.entrySet()) {
				String[] formatted = entry.getValue();
				
				String name = formatted[0];
				String permission = formatted[1];
				String itemType = formatted[2];
				
				if(p.hasPermission(permission)) {
					ItemFactory factory = new ItemFactory(Material.valueOf(itemType), 1);
					
					factory.setDisplayName(plugin.color(name));
					items.add(factory.getItem());
			}
			
			new ScrollingInventory(items, "&cRecipes", p);
		}
			
		} else sender.sendMessage(plugin.color("&cYou must be a player to use this command."));
		
		return true;
	}

}
