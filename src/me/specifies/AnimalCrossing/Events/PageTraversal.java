package me.specifies.AnimalCrossing.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Inventories.ScrollingInventory;

public class PageTraversal implements Listener {

    // Grab an instance of our plugin, and initialize it in the constructor
    Main plugin;
    public PageTraversal() {
        this.plugin = Main.getInstance();
    }

    // create a click event to process page traversing
    @EventHandler(ignoreCancelled = true)
    public void click(InventoryClickEvent e) {

        // if the entity that interacted isn't a player, don't do anything else.
        if (!(e.getWhoClicked() instanceof Player)) return;

        // cast player in order to grab necessary data
        Player p = (Player) e.getWhoClicked();

        // if the player isn't in our hashmap of users in the inventory, we're going to just return.
        if (!ScrollingInventory.users.containsKey(p.getUniqueId())) return;

        // great, the player has a scrollable inventory open.
        // we're going to get the state of that inventory so we know where they're at.
        ScrollingInventory state = ScrollingInventory.users.get(p.getUniqueId());

        // if the item is null (nothing/air), we're going to add sound effects and return.
        if (e.getCurrentItem() == null) {
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 2, 10);
            return;
        }

        // we're going to grab the item so we can see what they clicked.
        ItemStack item = e.getCurrentItem();

        // if the item is our next page arrow, we're going to traverse to the next page
        if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("next page")) {

            // cancel the event so they can't take the item.
            e.setCancelled(true);

            // if the player is at the end of possible pages, don't traverse.
            if (state.currentPage >= state.pages.size() - 1) return;

            // edit the state of the player's stored inventory.
            state.currentPage++;

            // play a sound effect, and reload the player's state.
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 29);
            p.openInventory(state.pages.get(state.currentPage));

        }
        // if the item is our last page arrow, we're going to traverse backwards.
        else if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("previous page")) {

            // cancel the event so they can't take the item.
            e.setCancelled(true);

            // if the player isn't on the first page, traverse backwards.
            if (state.currentPage > 0) {

                // change the page the player is on.
                state.currentPage--;

                // play a sound effect and reload the player's state.
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 29);
                p.openInventory(state.pages.get(state.currentPage));
            }
        }
        // if the item is just a glass pane, cancel event and play sound effects
        else if (item.getType() == Material.GREEN_STAINED_GLASS_PANE) {
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 2, 10);
        }
        // if the item is anything else, we're going to assume it's a rendered recipe.
        else {

            // cancel the event so they can't take the item.
            e.setCancelled(true);

            // get the raw name of the item clicked so we can query our hashmap for the recipe data.
            String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            // create an inventory object so we can construct the recipe inventory.
            Inventory recipe = Bukkit.createInventory(null, 45, plugin.color("&cCrafting &7- &b" + clicked));

            // create an item factory, starting with our filler object. add necessary data
            ItemFactory factory = new ItemFactory(Material.BLACK_STAINED_GLASS_PANE, 1);
            factory.setDisplayName(plugin.color("&9"));

            // create a whitelist of slots to not fill. will also use these for placing the objects on the crafting grid.
            ArrayList < Integer > whitelisted = new ArrayList < > (Arrays.asList(new Integer[] {
                12,
                13,
                14,
                21,
                22,
                23,
                30,
                31,
                32
            }));

            // loop through the inventory size. if the slot isn't whitelisted, put our placeholder item.
            for (int i = 0; i <= 44; i++) {
                if (!whitelisted.contains(i)) recipe.setItem(i, factory.getItem()); 
            }

            // grab our recipe string for the clicked item
            String recipeParse = plugin.getRecipes().get(clicked)[3];

            // use our process recipe function to receive a hashmap in which to loop and populate our grid.
            HashMap < Integer, String > parsedRecipes = plugin.processRecipe(recipeParse);

            // loop through the hashmaps entries
            for (Entry < Integer, String > entry: parsedRecipes.entrySet()) {

                // we're going to retrieve the slot. this is in accordance to our whitelisted variable.
                // so if the slot is 3, the actual index in the inventory will be 14.
                int slot = entry.getKey();

                // retrieve the material string
                String material = entry.getValue();

                // create an itemstack to populate.
                ItemStack targetItem = new ItemStack(Material.valueOf(material), 1);

                // populate the specified slot with the item.
                recipe.setItem(whitelisted.get(slot), targetItem);

            }

            // add a close page button for quick exit
            factory.setType(Material.RED_STAINED_GLASS_PANE);
            factory.setDisplayName(plugin.color("&cCLOSE PAGE"));
            recipe.setItem(40, factory.getItem());

            // because this is navigating the user outside of the scrollable page, we want to remove them so they can open another one later.
            ScrollingInventory.users.remove(p.getUniqueId());

            // close their inventory, and open the generated grid.
            p.closeInventory();
            p.openInventory(recipe);


        }

    }

}