package me.specifies.AnimalCrossing.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.specifies.AnimalCrossing.Inventories.ScrollingInventory;
import net.md_5.bungee.api.ChatColor;

public class RemovePlayerInstances implements Listener {
	
	
	@EventHandler
	public void leave(PlayerQuitEvent e) {
		removePlayer(e.getPlayer());
	}
	
	@EventHandler
	public void close(InventoryCloseEvent e) {
		removePlayer((Player) e.getPlayer());
	}
	
	private void removePlayer(Player p) {
		if(ScrollingInventory.users.containsKey(p.getUniqueId()) && !(ChatColor.stripColor(p.getOpenInventory().getTitle()).contains("Recipes"))) {
			ScrollingInventory.users.remove(p.getUniqueId());
		}
	}

}
