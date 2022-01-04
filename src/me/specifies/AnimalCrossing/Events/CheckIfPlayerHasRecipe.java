package me.specifies.AnimalCrossing.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Constants.PlayerAlerts;
import net.md_5.bungee.api.ChatColor;

public class CheckIfPlayerHasRecipe implements Listener {
	
	// create an object for our plugin class
	private Main plugin;
	
	// initialize the object
	public CheckIfPlayerHasRecipe() {
		this.plugin = Main.getInstance();
	}
	
	@EventHandler
	public void craft(PrepareItemCraftEvent e) {
		
		// if the crafter is a player, continue
		if(e.getView().getPlayer() instanceof Player) {
			
			// grab our player object
			Player p = (Player) e.getView().getPlayer();
			
			// as the event is called everytime an item is placed, this will be null until the item appears.
			// so, if it's null, simply return.
			if(e.getRecipe() == null) return;
			
			// grab our recipe target item
			ItemStack end = e.getRecipe().getResult();
			
			// we're then going to grab our rawname, removing color and spacing so we can target the config item.
			String rawName = ChatColor.stripColor(end.getItemMeta().getDisplayName()).replaceAll(" ", "").toLowerCase();
			
			// grab the permission for the item
			String permission = plugin.getConfig().getString("recipes." + rawName + ".permission");
			
			// if the permission is null, it's not a custom item
			if(permission == null) return;
			
			// if the player hasn't bought the recipe, close the inventory and alert them that they haven't unlocked it.
			if(!(p.hasPermission(permission))) {
				p.closeInventory();
				p.sendMessage(plugin.color(String.format(PlayerAlerts.INVALID_PERMISSION_CRAFTING, end.getItemMeta().getDisplayName())));
			}
			
			
		}
		
		
	}

}
