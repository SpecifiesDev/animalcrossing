package me.specifies.AnimalCrossing.Inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.specifies.AnimalCrossing.Main;

public class ScrollingInventory {
	
	// an arraylist of pages in the inventory
	public ArrayList<Inventory> pages = new ArrayList<Inventory>();
	
	// object to assign a uuid in the hashmap incase we ever really need to access it
	public UUID id;
	
	// the current page the owner is on.
	public int currentPage = 0;
	
	// hashmap containing all of the users in a scrolling inventory. backbone of the logic of this code.
	public static HashMap<UUID, ScrollingInventory> users = new HashMap<UUID, ScrollingInventory>();
	
	// values used to construct our pages.
	private int currentSlot = 0;
	private int pageBuilder = 2;
	

	
	// object of the main plugin
	private Main plugin;
	
	/**
	 * @param items Items to populate the scrolling inventory with
	 * @param name Name of the inventory.
	 * @param p Target player to assign the inventory to.
	 */
	public ScrollingInventory(ArrayList<ItemStack> items, String name, Player p) {
		
		// generate a random id, and grab the main plugin instance
		this.id = UUID.randomUUID();
		this.plugin = Main.getInstance();
		
		// populate the inventory with pages.
		populate(items, name, p);
		
		
	}
	
	
	private void populate(ArrayList<ItemStack> items, String name, Player p) {
		
		// grab a new blank page.
		Inventory page = getBlankPage(name + " &7- &c1");
		
		// loop through all of our configured items
		for(int i = 0; i < items.size(); i++) {
			
			// if the page is on it's last slot, go to the next.
			if(currentSlot == 45) {
				
				// add the final item to the page.
				page.setItem(currentSlot, items.get(i));
				
				// add the page to the arraylist
				pages.add(page);
				
				// get a new blank page
				page = getBlankPage(name + " &7- &c" + pageBuilder);
				
				// reset the currentslot
				currentSlot = 0;
				
				// increment the pagebuilder so that the pages in the name increment accordingly
				pageBuilder++;
				
			} else page.setItem(currentSlot, items.get(i)); // add the item to the page
			
			// increment the slot we're on
			currentSlot++;
		}
		
		// add the last page
		pages.add(page);
		
		// open the generated inventory for the player, on the first page.
		p.openInventory(pages.get(currentPage));
		
		// place the user in our hashmap so we can track their access to the inventory
		users.put(p.getUniqueId(), this);
		
	}
	
	/**
	 * Function to get a new page for a scrolling inventory.
	 * @param name Name of the inventory
	 * @return page Blank inventory.
	 */
	private Inventory getBlankPage(String name) {
		
		// Initialize our page.
		Inventory page = Bukkit.createInventory(null, 54, plugin.color(name));
		
		// create a fatory, and begin with the next page arrow.
		ItemFactory factory = new ItemFactory(Material.ARROW, 1);
		factory.setDisplayName(plugin.color("&aNext Page"));
		
		// set the next page arrow.
		page.setItem(53, factory.getItem());
		
		// adjust the name for the prev page arrow.
		factory.setDisplayName(plugin.color("&cPrevious Page"));
		
		// set the prev page arrow.
		page.setItem(45, factory.getItem());
		
		// create a filler object between the next / prev page arrows
		factory.setType(Material.GREEN_STAINED_GLASS_PANE);
		factory.setDisplayName(plugin.color("&9"));
		
		// populate the space between the next / prev page arrows
		for(int i = 46; i < 53; i++) { page.setItem(i, factory.getItem()); }
		
		// return the generated page.
		return page;
	}
		
}


