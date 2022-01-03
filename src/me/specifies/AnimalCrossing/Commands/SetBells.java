package me.specifies.AnimalCrossing.Commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Utils.SQLManager;
import net.md_5.bungee.api.ChatColor;

public class SetBells implements CommandExecutor {
		
	private Main plugin;
	private SQLManager manager;
	
	public SetBells() {
		this.plugin = Main.getInstance();
		this.manager = plugin.getManager();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			sender.sendMessage(plugin.color("&cYou must be a superadmin to use this command."));
		} else {
			if(!(args.length == 2)) {
				sender.sendMessage(plugin.color("&cInvalid arguments."));
			} else {
				String target = args[0];
				String bells = args[1];
				
				Player p = Bukkit.getPlayer(target);
				
				if(!(p == null)) {
					InventoryView view = p.getOpenInventory();
						
					if(ChatColor.stripColor(view.getTitle()).equalsIgnoreCase("nookterminal")) {
						try {
							updateBells(p, bells, sender);
								
							ItemFactory bellsItem = new ItemFactory(Material.GOLD_NUGGET, 1);
							bellsItem.setDisplayName(plugin.color("&eBalance&8:"));
							bellsItem.setLore("&6" + bells);
							
							view.setItem(19, bellsItem.getItem());
								
						} catch(SQLException err) {
							err.printStackTrace();
						}
					} else {
						try {
							updateBells(p, bells, sender);
						} catch(SQLException err) {
							err.printStackTrace();
						}
					}
				} else {
					OfflinePlayer player = Bukkit.getOfflinePlayer(target);
					
					if(manager.checkIfPlayerExists(player.getUniqueId().toString())) {
						manager.setPlayerBells(player.getUniqueId().toString(), bells);
						sender.sendMessage(plugin.color("&aUpdated the bells for the player &3" + player.getName() + "&8."));
					} else {
						sender.sendMessage(plugin.color("&cIt would appear that the player you specified has no database entries."));
					}
				}
			}
		}
		
		return true;
	}
	
	private void updateBells(Player p, String bells, CommandSender sender) throws SQLException {
		
		if(manager.checkIfPlayerExists(p.getUniqueId().toString())) {
			manager.setPlayerBells(p.getUniqueId().toString(), bells);
			sender.sendMessage(plugin.color("&aUpdated the bells for the player &3" + p.getName() + "&8."));
		} else {
			sender.sendMessage(plugin.color("&cIt would appear that the player you specified has no database entries."));
		}
		
	}

}
