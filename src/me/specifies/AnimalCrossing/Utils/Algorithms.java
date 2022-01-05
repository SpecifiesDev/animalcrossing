package me.specifies.AnimalCrossing.Utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Algorithms {
	
	/**
	 * Algorithm that takes a location and searches in a specified radius and then proceeds to destroy a tree.
	 * @param location location of the broken block
	 * @param leaftype type of the trees leaf
	 * @param logtype type of the trees log
	 * @param radius radius to pass to the iteration algorithm. Ideal radius is about 4.
	 */
	public void fellTree(Location location, Material leaftype, Material logtype, int radius) {
		
		
		ArrayList<Block> nearby = getNearbyTree(location, radius, leaftype, logtype);
		
		for(Block b : nearby) {
			b.setType(Material.AIR);
		}
		
		
	}
	
	/**
	 * Function to retrieve a list of a tree's blocks in a specified radius
	 * @param loc
	 * @param radius
	 * @return
	 */
	private ArrayList<Block> getNearbyTree(Location loc, int radius, Material leaftype, Material logtype) {
		
		ArrayList<Block> container = new ArrayList<Block>();
		
		// first we're going to start off on our x and loop through the specified radius
		for(int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
			
			// then we'll move to our y and do the exact thing, extending in both directions
			for(int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
				
				// finally we'll do our z axis and this will loop in a cube like radius. 
				// if you need a visulatization of the cube, replace the block with a different block
				for(int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
					
					// get the block of the location
					Block block = loc.getWorld().getBlockAt(x, y, z);
					
					// if the block is part of the tree, add it.
					if(block.getType() == leaftype || block.getType() == logtype) {
						container.add(block);
					}
					
					
				}
				
			}
			
			
		}
		
		// return the generated list of blocks
		return container;
		
	}

}
