package me.specifies.AnimalCrossing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.specifies.AnimalCrossing.Commands.Bells;
import me.specifies.AnimalCrossing.Commands.SetBells;
import me.specifies.AnimalCrossing.Events.NewPlayerLogin;
import me.specifies.AnimalCrossing.Utils.SQLManager;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	private final SQLManager manager = new SQLManager(this.getConfig().getString("sql-lite-file"), this.getDataFolder());
	
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
	}
	
	public void onDisable() {
		instance = null;
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public SQLManager getManager() {
		return manager;
	}
	
	public String color(String m) {
		return ChatColor.translateAlternateColorCodes('&', m);
	}
	
	/*
	 * private  
	 */
	private void registerCommands() {
		
		getCommand("bells").setExecutor(new Bells());
		getCommand("setbells").setExecutor(new SetBells());
		
	}
	
	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new NewPlayerLogin(), this);
		
	}

}
