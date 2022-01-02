package me.specifies.AnimalCrossing.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Utils.SQLManager;

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
				String player = args[0];
				String bells = args[1];
				
				OfflinePlayer p = Bukkit.getOfflinePlayer(player);
				
				if(manager.checkIfPlayerExists(p.getUniqueId().toString())) {
					manager.setPlayerBells(p.getUniqueId().toString(), bells);
				} else {
					sender.sendMessage(plugin.color("&cIt would appear that the player you specified has no database entries."));
				}
			}
		}
		
		return true;
	}

}
