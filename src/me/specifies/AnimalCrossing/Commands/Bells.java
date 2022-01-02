package me.specifies.AnimalCrossing.Commands;

import java.sql.SQLException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Utils.SQLManager;

public class Bells implements CommandExecutor {
	
	private Main plugin;
	private SQLManager manager;
	
	public Bells() {
		this.plugin = Main.getInstance();
		this.manager = plugin.getManager();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			try {
				String bells = manager.getPlayerBells(p.getUniqueId().toString());
				
				p.sendMessage(plugin.color("&8[&3AnimalCrossing&8] &aYour balance is &6" + bells + " &abells&8."));
			} catch(SQLException err) {
				err.printStackTrace();
				p.sendMessage(plugin.color("&cWe were unable to retrieve your bells."));
			}
			
		} else {
			sender.sendMessage(plugin.color("&cYou must be a player to use this command."));
		}
		
		return true;
	}

}
