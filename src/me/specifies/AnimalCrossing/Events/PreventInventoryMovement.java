package me.specifies.AnimalCrossing.Events;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Constants.PlayerAlerts;
import me.specifies.AnimalCrossing.Utils.SQLManager;
import net.md_5.bungee.api.ChatColor;

public class PreventInventoryMovement implements Listener {
	
	// necessary objects
	private Main plugin;
	private SQLManager manager;
	
	// intialized our objects
	public PreventInventoryMovement() {
		this.plugin = Main.getInstance();
		this.manager = plugin.getManager();
	}

    // create an invent to detect inventory click
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        // get the title of the clicked inventory.
        String title = ChatColor.stripColor(e.getView().getTitle());

        // if the inventory is our terminal, we simply need to just cancel.
        if (title.equalsIgnoreCase("nookterminal")) e.setCancelled(true);

        // if we're crafting, need to do a little more.
        if (title.contains("Crafting -")) {
        	
        	// if the item is null, just cancel and return.
        	if(e.getCurrentItem() == null) {
        		e.setCancelled(true);
        		return;
        	}
        	
        	// if the clicker isn't a player, return
        	if(!(e.getWhoClicked() instanceof Player)) return;
        	
            // if the item is the close button, close the page, else just cancel.
            if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                e.setCancelled(true);

                // get the player object then close the page.
                Player p = (Player) e.getWhoClicked();
                p.closeInventory();

            } else e.setCancelled(true);

        }
        
        // if we're purchasing, need a little bit more.
        if(title.contains("Purchase Item")) {
        	
        	// if the item is null, cancel and return
        	if(e.getCurrentItem() == null) {
        		e.setCancelled(true);
        		return;
        	}
        	
        	// if the clicker isn't a player, return
        	if(!(e.getWhoClicked() instanceof Player)) return;
        	
        	ItemStack item = e.getCurrentItem();
        	Player p = (Player) e.getWhoClicked();
        	
        	// this is the cancel item
        	if(item.getType() == Material.RED_STAINED_GLASS_PANE) {
        		// cancel the event and close the inventory
        		e.setCancelled(true);
        		p.closeInventory();
        	} else if(item.getType() == Material.GREEN_STAINED_GLASS_PANE) {
        		// cancel the event
        		e.setCancelled(true);
        		
        		// retrieve the player's balance
        		try {
        			
        			// grab the bells of the player and convert them
        			String bells = manager.getPlayerBells(p.getUniqueId().toString());
        			int bellsConversion = Integer.valueOf(bells);
        			
        			// grab the target item
        			String targetItem = title.split("-")[1].replaceFirst(" ", "");
        			
        			// grab the recipe
        			String[] recipe = plugin.getRecipes().get(targetItem);
        			
        			// grab the cost of the item and convert it
        			String itemCost = recipe[4];
        			int costConversion = Integer.valueOf(itemCost);
        			
        			// if the players balance wouldn't be < 0, the purchase is sucessful, else, it wouldn't be
        			if(bellsConversion - costConversion >= 0) {
        				
        				// get our left over balance so we can insert it into the database
        				String leftOver = Integer.toString(bellsConversion - costConversion);
        				
        				try {
        					
        					// set the player bells
        					manager.setPlayerBells(p.getUniqueId().toString(), leftOver);
        					
        					// give them the permission for the item, we're using gm as a permission manager for ease by just executing console commands
        					ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        					String command = "manuaddp " + p.getName() + " " + recipe[1];
        					Bukkit.dispatchCommand(console, command);
        					
        					// alert them of the purchase and close the inventory
        					p.sendMessage(plugin.color(String.format(PlayerAlerts.PURCHASE_SUCESSFUL, recipe[0])));
        					p.closeInventory();
        					
        				} catch(Exception err) {
        					// alert the player that there was an issue
        					p.sendMessage(plugin.color(PlayerAlerts.ERROR_UNABLE_TO_CREATE_ENTRY));
        					err.printStackTrace();
        				}
        				
        			} else {
        				// close the inventory and alert them they don't have enough
        				p.closeInventory();
        				p.sendMessage(plugin.color(PlayerAlerts.INSUFFICIENT_BALANCE));
        			}
        			
        			
        		} catch(SQLException err) {
        			// print error and alert the player.
        			err.printStackTrace();
        			p.closeInventory();
        			p.sendMessage(PlayerAlerts.ERROR_OBTAINING_INFO);
        		}
        		
        		
        	} else {
        		e.setCancelled(true);
        	}
        	
        	
        	
        }

    }

}