package me.specifies.AnimalCrossing.Events;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.specifies.AnimalCrossing.Main;
import me.specifies.AnimalCrossing.Constants.PlayerAlerts;
import me.specifies.AnimalCrossing.Utils.SQLManager;

public class NewPlayerLogin implements Listener {

    // create an instance for objects we need from the main class
    private Main plugin;
    private SQLManager manager;

    // initialize the necessary objects
    public NewPlayerLogin() {
        this.plugin = Main.getInstance();
        this.manager = plugin.getManager();
    }

    // create an onlogin event
    @EventHandler
    public void onLogin(PlayerJoinEvent e) {

        // grab the target player
        Player p = e.getPlayer();

        // check if the player exists in the database.
        if (!(manager.checkIfPlayerExists(p.getUniqueId().toString()))) {


            try {
                // attempt to create the player in the database
                manager.createPlayer(p.getUniqueId().toString());

                // on success, send the player our welcome message
                p.sendMessage(plugin.color(PlayerAlerts.WELCOME_MESSAGE));
            } catch (SQLException err) {

                // print the error with creating the player's data in the console
                err.printStackTrace();

                // as pretty much everything is dependent on having a database entry, kick the player and tell them to contact an admin.
                p.kickPlayer(plugin.color(PlayerAlerts.ERROR_UNABLE_TO_CREATE_ENTRY));
            }
        }

    }

}