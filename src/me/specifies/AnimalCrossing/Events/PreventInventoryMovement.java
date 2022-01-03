package me.specifies.AnimalCrossing.Events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import net.md_5.bungee.api.ChatColor;

public class PreventInventoryMovement implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		String title = ChatColor.stripColor(e.getView().getTitle());
		
		if(title.equalsIgnoreCase("nookterminal")) e.setCancelled(true);
		
		if(title.contains("Crafting -")) {
			
			if(e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
				e.setCancelled(true);
				Player p = (Player) e.getWhoClicked();
				p.closeInventory();
			} else e.setCancelled(true);
			
		}
		
	}

}
