package me.specifies.AnimalCrossing.Inventories;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.specifies.AnimalCrossing.Main;

/*
 * Factory class pulled from PlayerStatistics project
 * @ https://github.com/SpecifiesDev/PlayerStatistics/blob/master/src/me/specifies/core/Inventories/ItemFactory.java
 */
public class ItemFactory {
	
	private ItemStack stack;
	private Main plugin;
	
	public ItemFactory(Material m, int amount) {
		this.stack = new ItemStack(m, amount);
		this.plugin = Main.getInstance();
	}
	
	/**
	 * @return this.stack The itemstack with all of its modified data.
	 */
	public ItemStack getItem() {
		return this.stack;
	}
	
	/**
	 * Function to set the type of the item.
	 * @param mat Material to set the type to
	 */
	public void setType(Material mat) {
		this.stack.setType(mat);
	}
	
	/**
	 * Function to set the display name of the item.
	 * @param displayName A string that the item will be named. Takes color codes.
	 */
	public void setDisplayName(String displayName) {
		
		ItemMeta im = this.getMeta();
		
		im.setDisplayName(this.plugin.color(displayName));
		
		this.stack.setItemMeta(im);
		
	}
	
	/**
	 * Function to add to the existing lore of an item.
	 * @param additionString The string to add, follows the set format of line~line1~line2
	 */
	public void addToLore(String additionString) {
		
		ItemMeta im = this.getMeta();
		
		ArrayList<String> construct = new ArrayList<>(im.getLore());
		
		for(String s: additionString.split("~")) {
			construct.add(this.plugin.color(s));
		}
		
		im.setLore(construct);
		
	}
		
	
	/**
	 * Function to set the lore of the item.
	 * @param loreString A string following the format of adding lore.
	 * Example loreString: "&cAn item~&bmeant for the over lords~&cof qarth" Would do An item\nmeant for the over lords\nof qarth.
	 */
	public void setLore(String loreString) {
		
		ArrayList<String> array = new ArrayList<>();
		
		String[] lines = loreString.split("~");
		
		for(String s: lines) {
			array.add(this.plugin.color(s));
		}
		
		ItemMeta im = this.getMeta();
		
		im.setLore(array);
		
		this.stack.setItemMeta(im);
		
	}
	
	/**
	 * Function to add item flag to an item.
	 * @param flag The flag to add.
	 */
	public void addItemFlag(ItemFlag flag) {
		
		ItemMeta im = this.getMeta();
		
		im.addItemFlags(flag);
		
		this.stack.setItemMeta(im);
		
	}
	
	/**
	 * Function to flush the item, if it has undesired meta that could interfere with certain material types
	 * @param mat Material of new itemstack
	 * @param amount Amount of new itemstack
	 */
	public void flush(Material mat, int amount) {
		this.stack = new ItemStack(mat, amount);
	}
	
	
	private ItemMeta getMeta() {
		return this.stack.getItemMeta();
	}

}
