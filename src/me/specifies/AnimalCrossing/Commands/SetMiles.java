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
import me.specifies.AnimalCrossing.Constants.ConsoleAlerts;
import me.specifies.AnimalCrossing.Constants.PlayerAlerts;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Utils.SQLManager;
import net.md_5.bungee.api.ChatColor;

public class SetMiles implements CommandExecutor {

    // create an instance for objects we need from the main class
    private Main plugin;
    private SQLManager manager;

    // initialize the necessary objects
    public SetMiles() {
        this.plugin = Main.getInstance();
        this.manager = plugin.getManager();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // if the sender is a player, deny execution.
        if (sender instanceof Player) {
            sender.sendMessage(plugin.color(PlayerAlerts.ERROR_SUPERADMIN_REQUIRED));
        } else {

            // This command requires 2 arguments, if the sender doesn't supply them, we alert them of it.
            if (!(args.length == 2)) {
                sender.sendMessage(plugin.color(ConsoleAlerts.INVALID_ARGUMENTS_PASSED));
            } else {

                // Attempt to retrieve a player object from bukkit
                Player p = Bukkit.getPlayer(args[0]);

                // if the player isn't null, they're online.
                if (!(p == null)) {

                    // Retrieve the inventory view of the player.
                    String view = ChatColor.stripColor(p.getOpenInventory().getTitle());

                    // If they are in fact in the terminal, we're going to update their inventory.
                    if (view.equalsIgnoreCase("nookterminal")) {

                        // surround in a try/catch so we can let the console know it didn't execute properly if something happens.
                        try {

                            // attempt to update miles before editing the inventory.
                            updateMiles(p, args[1], sender);

                            // create the necessary itemstack, and update the item.
                            ItemFactory factory = new ItemFactory(Material.MAP, 1);

                            factory.setDisplayName(plugin.color("&3Miles&7:"));
                            factory.setLore("&b" + args[1]);

                            p.getOpenInventory().setItem(22, factory.getItem());

                        } catch (Exception err) {
                            err.printStackTrace();
                        }

                    } else try {
                        updateMiles(p, args[1], sender);
                    } catch (Exception err) {
                        err.printStackTrace();
                    } // just update the miles if they're not in the terminal


                } else {

                    // grab an offline player object so we can process their uuid
                    @SuppressWarnings("deprecation")
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

                    // surround in a try/catch in order to alert the console of any issues.
                    try {

                        // check if the player exists
                        if (manager.checkIfPlayerExists(player.getUniqueId().toString())) {

                            // attempt to set the specified miles
                            manager.setPlayerMiles(player.getUniqueId().toString(), args[1]);

                            // alert the console of success.
                            sender.sendMessage(plugin.color(String.format(ConsoleAlerts.UPDATED_PLAYER_MILES, player.getName())));
                        } else {

                            // alert the console that the player doesn't exist.
                            sender.sendMessage(plugin.color(ConsoleAlerts.NO_DATABASE_ENTRIES));
                        }
                    } catch (Exception err) {
                        err.printStackTrace();
                    }

                }


            }

        }

        return true;
    }

    /**
     * Function to update miles of a specified player.
     * @param p Player to update.
     * @param miles Miles specified to set to
     * @param sender Sender of the command.
     * @throws SQLException
     */
    private void updateMiles(Player p, String miles, CommandSender sender) throws SQLException {

        // check if the player exists
        if (manager.checkIfPlayerExists(p.getUniqueId().toString())) {

            // attempt to update the player's miles
            manager.setPlayerMiles(p.getUniqueId().toString(), miles);

            // alert the console of success
            sender.sendMessage(plugin.color(String.format(ConsoleAlerts.UPDATED_PLAYER_MILES, p.getName())));
        } else sender.sendMessage(plugin.color("&cIt would appear that you speciifed player does not exist in the database."));

    }

}