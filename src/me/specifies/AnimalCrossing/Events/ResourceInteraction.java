package me.specifies.AnimalCrossing.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Utils.Algorithms;
import me.specifies.AnimalCrossing.Utils.SoundManager;

public class ResourceInteraction implements Listener {
	
	// import necessary objects
	private Main plugin;
	
	// initialize objects
	public ResourceInteraction() {
		this.plugin = Main.getInstance();
	}
	
	// materials that are considered resources
	private ArrayList<Material> resources = new ArrayList<>(Arrays.asList(new Material[] {
		Material.OAK_LOG	
	}));
	
	@EventHandler
	public void destroy(BlockBreakEvent e) {
		
		// if the block broken is a resource block, intialize resource generation
		if(resources.contains(e.getBlock().getType())) {
			
			// grab the type from our block
			Material type = e.getBlock().getType();
			
			// grab the location of our block
			Location loc = e.getBlock().getLocation();
			
			// grab our config file
			FileConfiguration config = plugin.getConfig();
			
			// get the resource type
			String resourceType = config.getString("resources." + type.toString() + ".type");
			
			// if the resource isn't configured, return
			if(resourceType == null) return;
			
			// grab our player and cancel the break event
			Player p = e.getPlayer();
			e.setCancelled(true);
			
			// if the resource is a tree
			
			// EVENTUALLY GOING TO COME BACK AND BASE YIELD ON RECIPE ITEMS
			if(resourceType.equals("tree")) {
				
				// grab the rest of our necessary configuration
				int durability = config.getInt("resources." + type.toString() + ".drop.durability");
				Material saplingType = Material.valueOf(config.getString("resources." + type.toString() + ".drop.sapling"));
				String saplingName = config.getString("resources." + type.toString() + ".drop.sapling_name");
				String[] drops = config.getString("resources." + type.toString() + ".drop.item").split(":");
				
				// grab our leaftype and logtype
				Material leafType = Material.valueOf(config.getString("resources." + type.toString() + ".drop.leaf"));
				Material logType = Material.valueOf(config.getString("resources." + type.toString() + ".drop.log"));
				
				if(plugin.getResources().containsKey(loc)) {
					
					// grab how many times said tree has been broken
					int broken = plugin.getResources().get(loc);
					
					
					// if it has reached it's durability, remove the true and put the appropriate item in the player's inventory
					if(broken == durability - 1) {
						
						// grab an instance of our algorithms class
						Algorithms algorithms = new Algorithms();
						
						// delete the tree
						algorithms.fellTree(loc,  leafType, logType, 10);
						
						// flush the tree's location from the resources map
						plugin.getResources().remove(loc);
						
						// first the appropriate sapling
						ItemFactory factory = new ItemFactory(saplingType, 1);
						factory.setDisplayName(plugin.color(saplingName));
						
						// add the sapling to the inventory
						p.getInventory().addItem(factory.getItem());
						
						// now we're going to add the fruit
						factory.flush(Material.valueOf(drops[0]), Integer.parseInt(drops[1]));
						
						// add the fruits to the inventory
						p.getInventory().addItem(factory.getItem());
						
						// retrieve the wood the player is going to receive
						ItemStack wood = getWoodByChance();
						
						// put the wood in the inventory
						p.getInventory().addItem(wood);
						
						// play a ding 
						new SoundManager(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 40, 20, p);
						
						
					} 
					// the tree has been broken before but isn't at the durability range
					else {
						
						// retrieve the wood to give the player
						ItemStack wood = getWoodByChance();
						
						// increment the durability in our hashmap
						int next = broken += 1;
						
						
						// first remove the instance
						plugin.getResources().remove(loc);
						
						// then update it
						plugin.getResources().put(loc, next);
						
						// add the wood to the player's inventory
						p.getInventory().addItem(wood);
						
						// play a sound to the player.
						new SoundManager(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 40, 20, p);
					}
					
				} 
				// tree has never been touched
				else {
					
					// retrieve the wood to give the player
					ItemStack wood = getWoodByChance();
					
					// add an instance to the hashmap
					plugin.getResources().put(loc, 1);
					
					// add the wood to the player's inventory
					p.getInventory().addItem(wood);
					
					// play a sound to the player.
					new SoundManager(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 40, 20, p);
					
					
					
				}
				
			}
			
			
			
		}
		
	}
	
	private ItemStack getWoodByChance() {
		
		// define our return item
		ItemStack item = null;
		
		// import our random class. we could use the
		// post 1.7 method, but I prefer this way for this particular instance
		Random rand = new Random();
		
		// generate our number
		int generation = rand.nextInt((10000 - 1) + 1) + 1;
		
		// least probable wood
		if(generation <= 1000) {
			item = new ItemStack(Material.ACACIA_PLANKS, 1);
		} 
		// second most probable wood
		else if(generation > 1000 && generation <= 3000) {
			item = new ItemStack(Material.DARK_OAK_PLANKS, 1);
		} // most probable wood
		else if(generation > 3000 && generation <= 10000) {
			item = new ItemStack(Material.OAK_PLANKS, 1);
		} // just in case something goes wrong in generation to not throw errors
		else item = new ItemStack(Material.AIR);
		
		return item;
		
	}

}
