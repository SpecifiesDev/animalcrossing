package me.specifies.AnimalCrossing.Commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Utils.SQLManager;
import net.md_5.bungee.api.ChatColor;

public class SetMiles implements CommandExecutor {
	
	private Main plugin;
	private SQLManager manager;
	
	public SetMiles() {
		this.plugin = Main.getInstance();
		this.manager = plugin.getManager();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			sender.sendMessage(plugin.color("&cYou must be a superadmin to use this commnad."));
		} else {
			
			if(!(args.length == 2)) {
				sender.sendMessage(plugin.color("&cInvalid arguments."));
			} else {
				
				Player p = Bukkit.getPlayer(args[0]);
				
				if(!(p == null)) {
					
					String view = ChatColor.stripColor(p.getOpenInventory().getTitle());
					
					if(view.equalsIgnoreCase("nookterminal")) {
						
						try {
							
							updateMiles(p, args[1], sender);
							
							ItemFactory factory = new ItemFactory(Material.MAP, 1);
							
							factory.setDisplayName(plugin.color("&3Miles&7:"));
							factory.setLore("&b" + args[1]);
							
							p.getOpenInventory().setItem(22, factory.getItem());
							
						} catch(Exception err) {
							err.printStackTrace();
						}
						
					} else try { updateMiles(p, args[1], sender); } catch(Exception err) { err.printStackTrace(); }
				
					
				} else {
					
					OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
					try {
						if(manager.checkIfPlayerExists(player.getUniqueId().toString())) {
							manager.setPlayerMiles(player.getUniqueId().toString(), args[1]);
							sender.sendMessage(plugin.color("&aUpdated miles for the player &3" + player.getName() + "&8."));
						} else {
							sender.sendMessage("Invalid player.");
						}
					} catch(Exception err) {
						err.printStackTrace();
					}
					
				}
				
				
			}
			
		}
		
		return true;
	}
	
	private void updateMiles(Player p, String miles, CommandSender sender) throws SQLException {
		
		if(manager.checkIfPlayerExists(p.getUniqueId().toString())) {
			manager.setPlayerMiles(p.getUniqueId().toString(), miles);
		} else sender.sendMessage(plugin.color("&cIt would appear that you speciifed player does not exist in the database."));
		
	}

}
