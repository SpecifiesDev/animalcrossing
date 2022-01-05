package me.specifies.AnimalCrossing.Events;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Utils.Algorithms;

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
			if(resourceType.equals("tree")) {
				
				// grab the rest of our necessary configuration
				int durability = config.getInt("resources." + type.toString() + ".drop.durability");
				Material saplingType = Material.valueOf(config.getString("resources." + type.toString() + ".drop.sapling"));
				String saplingName = config.getString("resources." + type.toString() + ".drop.sapling_name");
				
				// grab our leaftype and logtype
				Material leafType = Material.valueOf(config.getString("resources." + type.toString() + ".drop.leaf"));
				Material logType = Material.valueOf(config.getString("resources." + type.toString() + ".drop.log"));
				
				// grab an instance of our algorithms class
				Algorithms algorithms = new Algorithms();
				
				algorithms.fellTree(loc, leafType, logType, 5);
				
				if(plugin.getResources().containsKey(loc)) {
					
					int broken = plugin.getResources().get(loc);
					
					if(broken == durability) {
						
					} else {
						
					}
					
				} else {
					
				}
				
			}
			
			
			
		}
		
	}

}
