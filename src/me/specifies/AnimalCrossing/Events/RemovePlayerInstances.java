package me.specifies.AnimalCrossing.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.specifies.AnimalCrossing.Inventories.ScrollingInventory;
import net.md_5.bungee.api.ChatColor;

public class RemovePlayerInstances implements Listener {

    // two events to make sure that the player is NEVER left in the map of open inventories.
    @EventHandler
    public void leave(PlayerQuitEvent e) {
        removePlayer(e.getPlayer());
    }

    @EventHandler
    public void close(InventoryCloseEvent e) {
        removePlayer((Player) e.getPlayer());
    }

    // if the player is in scrolling inventories, AND they aren't inside of the recipes inventory, we remove them.
    // the reason we need the && operand here is because without it, the click event fires regardless, even if they're
    // inside the recipes terminal. meaning, they would incorrectly be removed and would no long be subject to the
    // traversal event
    private void removePlayer(Player p) {
        if (ScrollingInventory.users.containsKey(p.getUniqueId()) && !(ChatColor.stripColor(p.getOpenInventory().getTitle()).contains("Recipes"))) {
            ScrollingInventory.users.remove(p.getUniqueId());
        }
    }

}