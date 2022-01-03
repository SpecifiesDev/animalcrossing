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
	
	public ArrayList<Inventory> pages = new ArrayList<Inventory>();
	
	public UUID id;
	public int currentPage = 0;
	
	private int currentSlot = 0;
	private int pageBuilder = 2;
	
	public static HashMap<UUID, ScrollingInventory> users = new HashMap<UUID, ScrollingInventory>();
	
	
	private Main plugin;
	
	public ScrollingInventory(ArrayList<ItemStack> items, String name, Player p) {
		
		this.id = UUID.randomUUID();
		this.plugin = Main.getInstance();
		
		populate(items, name, p);
		
		
	}
	
	private void populate(ArrayList<ItemStack> items, String name, Player p) {
		
		Inventory page = getBlankPage(name + " &7- &c1");
		
		for(int i = 0; i < items.size(); i++) {
			if(currentSlot == 45) {
				pages.add(page);
				page = getBlankPage(name + " &7- &c" + pageBuilder);
				currentSlot = 0;
				pageBuilder++;
				page.setItem(currentSlot, items.get(i));
			} else page.setItem(currentSlot, items.get(i));
			
			currentSlot++;
		}
		
		pages.add(page);
		
		p.openInventory(pages.get(currentPage));
		users.put(p.getUniqueId(), this);
		
	}
	
	private Inventory getBlankPage(String name) {
		
		Inventory page = Bukkit.createInventory(null, 54, plugin.color(name));
		
		ItemFactory factory = new ItemFactory(Material.ARROW, 1);
		factory.setDisplayName(plugin.color("&aNext Page"));
		
		page.setItem(53, factory.getItem());
		
		factory.setDisplayName(plugin.color("&cPrevious Page"));
		
		page.setItem(45, factory.getItem());
		
		factory.setType(Material.GREEN_STAINED_GLASS_PANE);
		
		factory.setDisplayName(plugin.color("&9"));
		
		for(int i = 46; i < 53; i++) { page.setItem(i, factory.getItem()); }
		
		return page;
	}
		
}


