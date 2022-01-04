package me.specifies.AnimalCrossing.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Exceptions.RecipeExceedsLimit;
import net.md_5.bungee.api.ChatColor;

public class RecipeManager {
	
	// declare all of the items we need for the manager
	private HashMap<Integer, String> items;
	private ItemStack endItem;
	private ShapedRecipe recipe;
	private Main plugin;
	
	public RecipeManager(HashMap<Integer, String> itemset, String name, ItemStack endItem) throws RecipeExceedsLimit {
		
		// get an instance of our main plugin
		this.plugin = Main.getInstance();
		
		// check if the recipe is too large, if so throw a new exception.
		// if it throws an error, the item will not be registered.
		if(itemset.size() > 9) throw new RecipeExceedsLimit("[AnimalCrossing] The recipe with the name " + ChatColor.stripColor(plugin.color(name)) + " exceeds the item limit. The item will not be registered.");
		
		// go ahead and add the display name of our end item
		ItemMeta im = endItem.getItemMeta();
		im.setDisplayName(plugin.color(name));
		endItem.setItemMeta(im);
		
		// set our objects
		this.items = itemset;
		this.endItem = endItem;
		this.recipe = new ShapedRecipe(generateKeyspacedName(name), this.endItem);
		
	
	}
	
	public ShapedRecipe getGeneratedRecipe() {
		
		// because it's a shaped recipe, we need to fill each slot and ONLY register it to the correct slot, so go a-b-c so forth.
		this.recipe.shape("ABC", "DEF", "GHI");
		
		// create an arraylist of the exact same values so we can remove them as we add items. the remaining slots will be set to air.
		ArrayList<String> tracking = new ArrayList<>(Arrays.asList(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I"}));
		
		// create an arraylist of everything we've added.
		ArrayList<String> holding = new ArrayList<>();
		
		// loop through our map of generated items, and apply them accordingly
		for(Map.Entry<Integer, String> entry : this.items.entrySet()) {
			
			// parse our slot and material
			int craftingslot = entry.getKey();
			Material itemMaterial = Material.valueOf(entry.getValue());
			
			// set the ingredient to the according character / material
			recipe.setIngredient(tracking.get(craftingslot).charAt(0), itemMaterial);
			
			// add the used character to our holder arraylist
			holding.add(tracking.get(craftingslot));
		}
		
		// remove all of the characters that we used
		for(String e : holding) {
			tracking.remove(e);
		}
		
		// loop through and set the remaining slots to air
		for(String character : tracking) {
			recipe.setIngredient(character.charAt(0), Material.AIR);
		}
		
		// return the recipe
		return this.recipe;
		
	}
	
	
	/**
	 * Utility function to add a keyspacedname from the original name
	 * @param itemName Name of the item
	 * @return parsed ksn
	 */
	private NamespacedKey generateKeyspacedName(String itemName) {
		
		// remove all color codes from the name
		String raw = ChatColor.stripColor(plugin.color(itemName));
		
		// split it with spaces
		String[] split = raw.split(" ");
		
		// this is going to be our return string
		String returnValue = "";
		
		// if there's only one word in the array, we're just going to return it.
		if(split.length == 1) {
			returnValue = split[1];
		} 
		// if there's not, we need to at _ between each word
		else {
			
			// add a count variable so we can track if we're at the last word
			int count = 0;
			
			for(String name : split) {
				
				// if we're on the last word, just add it, else, add it + the _
				if(count == split.length) {
					returnValue += name.toLowerCase();
				} else {
					returnValue += name.toLowerCase() + "_";
				}
			}
		}
		
		// return our generated keyspacedname
		return new NamespacedKey(plugin, returnValue);
		
	}
	
	
	
	

}
