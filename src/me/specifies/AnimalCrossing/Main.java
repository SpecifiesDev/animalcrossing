package me.specifies.AnimalCrossing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.specifies.AnimalCrossing.Commands.Bells;
import me.specifies.AnimalCrossing.Commands.Recipes;
import me.specifies.AnimalCrossing.Commands.SetBells;
import me.specifies.AnimalCrossing.Commands.SetMiles;
import me.specifies.AnimalCrossing.Events.NewPlayerLogin;
import me.specifies.AnimalCrossing.Events.PageTraversal;
import me.specifies.AnimalCrossing.Events.PreventInventoryMovement;
import me.specifies.AnimalCrossing.Events.RemovePlayerInstances;
import me.specifies.AnimalCrossing.Inventories.ScrollingInventory;
import me.specifies.AnimalCrossing.Utils.SQLManager;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	
	// instance of this class
	private static Main instance;
	
	// instance of our sqlmanager
	private final SQLManager manager = new SQLManager(this.getConfig().getString("sql-lite-file"), this.getDataFolder());
	
	// instance of our parsed recipes
	private HashMap<String, String[]> recipes = new HashMap<>();
	
	public void onEnable() {
		
		// get the main instance so we can refer to it in other classes
		instance = this;
		
		// save default config
		this.saveDefaultConfig();
		
		// log started
		System.out.print("Animal Crossing Started");
		
		// register commands and events
		registerCommands();
		registerEvents();
		
		// do our database checks. if they fail, disable the plugin as it's dependent on connection to the database.
		try {
			this.manager.setup();
			Bukkit.getLogger().info("[AnimalCrossing] A connection has been established to the SQLite database.");
		} catch (IOException err) {
			Bukkit.getLogger().log(Level.WARNING, "Unable to create database file. Disabling AutoMiner. Stack Trace:");
			err.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
		} catch(ClassNotFoundException err) {
			Bukkit.getLogger().log(Level.WARNING, "There was no SQLite driver found. This plugin will not work until the proper drivers are installed.");
			Bukkit.getPluginManager().disablePlugin(this);
		} catch(SQLException err) {
			err.printStackTrace();
		}
		
		// process our configured recipes and register them
		processRecipes();
	}
	
	public void onDisable() {
		
		// remove every player from our inventories tracking, and then close their inventory
		for(Map.Entry<UUID, ScrollingInventory> entry : ScrollingInventory.users.entrySet()) {
			
			UUID u = entry.getKey();
			
			Player p = Bukkit.getPlayer(u);
			
			ScrollingInventory.users.remove(u);
			
			try { p.closeInventory(); } catch(Exception err) { /* No reason to log */}
		}
		
		// nullify our instance
		instance = null;
	}
	
	/*
	 * getters
	 */
	
	public HashMap<String, String[]> getRecipes() {
		return recipes;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public SQLManager getManager() {
		return manager;
	}
	
	/*
	 * public utility
	 */
	
	public String color(String m) {
		return ChatColor.translateAlternateColorCodes('&', m);
	}
	
	public HashMap<Integer, String> processRecipe(String recipe) {
		
		// split the parent string objects. i.e 1:x~2:b = [1:x, 2:b]
		String[] parsed = recipe.split("~");
		
		// create a new hashmap to specify placement
		HashMap<Integer, String> itemPlacement = new HashMap<Integer, String>();
		
		// loop through each parsed string.
		for(String item: parsed) {
			
			// split each string further in order to grab the slot and material
			String[] itemContainer = item.split(":");
			
			// parse the retrieved values and put them in the hashmap.
			int slot = Integer.parseInt(itemContainer[0]);
			String material = itemContainer[1];
			itemPlacement.put(slot, material);
		}
	
		// return the processed recipe
		return itemPlacement;
	}
	
	/*
	 * private utility functions
	 */
	
	private void registerCommands() {
		
		getCommand("info").setExecutor(new Bells());
		getCommand("setbells").setExecutor(new SetBells());
		getCommand("setmiles").setExecutor(new SetMiles());
		getCommand("recipes").setExecutor(new Recipes());
		
	}
	
	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new NewPlayerLogin(), this);
		pm.registerEvents(new PreventInventoryMovement(), this);
		pm.registerEvents(new PageTraversal(), this);
		pm.registerEvents(new RemovePlayerInstances(), this);
		
	}
	
	private void processRecipes() {
		
		// loop through each recipe in our config.
		for(String key : this.getConfig().getConfigurationSection("recipes").getKeys(false)) {
			
			// grab the necessary information
			String name = this.getConfig().getString("recipes." + key + ".name");
			String permission = this.getConfig().getString("recipes." + key + ".permission");
			String itemType = this.getConfig().getString("recipes." + key + ".item");
			List<String> items = this.getConfig().getStringList("recipes." + key + ".recipe");
			
			// loop through our stringlist of items in the grid, and convert them to a string for later manipulation.
			String itemresult = "";
			int count = 0;
			for(String item : items) {
				count++;
				if(count == items.size()) itemresult += item;
				else itemresult += item + "~";
			}
			
			// create a formatted array and put it in our recipse hashmap for later manipulation.
			String[] formatted = {name, permission, itemType, itemresult};
			recipes.put(ChatColor.stripColor(this.color(name)), formatted);
		}
		
	}

}
