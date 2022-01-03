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
	
	Main plugin;
	public PageTraversal() { this.plugin = Main.getInstance(); }
	
	@EventHandler(ignoreCancelled = true)
	public void click(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		
		Player p = (Player) e.getWhoClicked();
		
		if(!ScrollingInventory.users.containsKey(p.getUniqueId())) return;
		
		ScrollingInventory state = ScrollingInventory.users.get(p.getUniqueId());
		
		
		if(e.getCurrentItem() == null) {
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 2, 10);
			return;
		}
		
		ItemStack item = e.getCurrentItem();
		
		if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("next page")) {
			
			e.setCancelled(true);
			if(state.currentPage >= state.pages.size() - 1) return;
			
			state.currentPage++;
			
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 29);
			
			p.openInventory(state.pages.get(state.currentPage));
			
		} else if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("previous page")) {
			
			e.setCancelled(true);
			
			if(state.currentPage > 0) {
				state.currentPage--;
				p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 10, 29);
				p.openInventory(state.pages.get(state.currentPage));
			}
		}  else if(item.getType() == Material.GREEN_STAINED_GLASS_PANE) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_BREAK, 2, 10);
		} else {
			// open and display the recipe
			
			e.setCancelled(true);
			
			String clicked = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
			
			Inventory recipe = Bukkit.createInventory(null, 45, plugin.color("&cCrafting &7- &b" + clicked));
			
			ItemFactory factory = new ItemFactory(Material.BLACK_STAINED_GLASS_PANE, 1);
			
			factory.setDisplayName(plugin.color("&9"));
			
			ArrayList<Integer> whitelisted = new ArrayList<>(Arrays.asList(new Integer[] {12, 13, 14, 21, 22, 23, 30, 31 ,32}));
			
			for(int i = 0; i <= 44; i++) {
				if(!whitelisted.contains(i)) {
					recipe.setItem(i, factory.getItem());
				}
			}
			
			String recipeParse = plugin.getRecipes().get(clicked)[3];
			
			HashMap<Integer, String> parsedRecipes = plugin.processRecipe(recipeParse);
			
			for(Entry<Integer, String> entry : parsedRecipes.entrySet()) {
				int slot = entry.getKey();
				String material = entry.getValue();
				ItemStack targetItem = new ItemStack(Material.valueOf(material), 1);
				
				recipe.setItem(whitelisted.get(slot), targetItem);
				
			}
			
			factory.setType(Material.RED_STAINED_GLASS_PANE);
			factory.setDisplayName(plugin.color("&cCLOSE PAGE"));
			
			recipe.setItem(40, factory.getItem());
			
			ScrollingInventory.users.remove(p.getUniqueId());
			
			p.closeInventory();
			
			p.openInventory(recipe);
			
			
		}
		
	}

}
