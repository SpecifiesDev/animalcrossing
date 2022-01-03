package me.specifies.AnimalCrossing.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.md_5.bungee.api.ChatColor;

public class PreventInventoryMovement implements Listener {

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
        	
            // if the item is the close button, close the page, else just cancel.
            if (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                e.setCancelled(true);

                // get the player object then close the page.
                Player p = (Player) e.getWhoClicked();
                p.closeInventory();

            } else e.setCancelled(true);

        }

    }

}