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
import me.specifies.AnimalCrossing.Constants.ConsoleAlerts;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Inventories.ScrollingInventory;

public class Recipes implements CommandExecutor {

    // create an instance for objects we need from the main class
    private Main plugin;
    private HashMap < String, String[] > recipes;

    // intialized our objects in the constructor
    public Recipes() {
        this.plugin = Main.getInstance();
        this.recipes = plugin.getRecipes();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {

        // ensure sender is a player, if not deny functionality
        if (sender instanceof Player) {

            // Cast player type to the sender
            Player p = (Player) sender;

            // Create an item stack to store our processed information
            ArrayList < ItemStack > items = new ArrayList < ItemStack > ();

            // loop through the hashmap and retrieve the necessary information.
            for (Map.Entry < String, String[] > entry: recipes.entrySet()) {
                // Grab the array
                String[] formatted = entry.getValue();

                // Grab our necessary values to construct the inventory
                String name = formatted[0];
                String permission = formatted[1];
                String itemType = formatted[2];

                // If the player has the permission node in accordance to the item, add it to the array to render
                if (p.hasPermission(permission)) {
                    ItemFactory factory = new ItemFactory(Material.valueOf(itemType), 1);

                    factory.setDisplayName(plugin.color(name));
                    items.add(factory.getItem());
                }

            }
            
            // Create a new scrolling inventory object so the the player can actively use our scrolling inventory system.
            new ScrollingInventory(items, "&cRecipes", p);
        } else sender.sendMessage(plugin.color(ConsoleAlerts.PLAYER_REQUIREMENT));

        return true;
    }

}