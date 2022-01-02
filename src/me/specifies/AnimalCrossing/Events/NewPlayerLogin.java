package me.specifies.AnimalCrossing.Events;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Utils.SQLManager;

public class NewPlayerLogin implements Listener {
	
	private Main plugin;
	private SQLManager manager;
	
	public NewPlayerLogin() {
		this.plugin = Main.getInstance();
		this.manager = plugin.getManager();
	}
	
	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		
		if(!(manager.checkIfPlayerExists(p.getUniqueId().toString()))) {
			
			try {
				manager.createPlayer(p.getUniqueId().toString());
				p.sendMessage(plugin.color("&8[&3AnimalCrossing&8] &aWelcome to Zimma. We hope you enjoy your stay. As part of our rewards program we've rewarded you with &6500 bells&8."));
			} catch(SQLException err) {
				err.printStackTrace();
				p.sendMessage(plugin.color("&cWe were unable to create your database info. Please contact an administrator to play AnimalCrossing."));
			}
		}
		
	}

}