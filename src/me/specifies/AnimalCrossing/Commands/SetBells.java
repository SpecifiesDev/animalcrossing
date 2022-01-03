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
import me.specifies.AnimalCrossing.Constants.ConsoleAlerts;
import me.specifies.AnimalCrossing.Constants.PlayerAlerts;
import me.specifies.AnimalCrossing.Inventories.ItemFactory;
import me.specifies.AnimalCrossing.Utils.SQLManager;
import net.md_5.bungee.api.ChatColor;

public class SetBells implements CommandExecutor {

    // create an instance for objects we need from the main class
    private Main plugin;
    private SQLManager manager;

    // initialize the objects in our constructor
    public SetBells() {
        this.plugin = Main.getInstance();
        this.manager = plugin.getManager();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // If the user is a player, we're just going to deny this.
        // As this is designed for an SMP with friends and I really only designed these commands
        // for testing purposes, I see no use of making it permissable outside of the console.
        if (sender instanceof Player) {
            sender.sendMessage(plugin.color(PlayerAlerts.ERROR_SUPERADMIN_REQUIRED));
        } else {
            // The command requires two arguments, so if they
            if (!(args.length == 2)) {
                sender.sendMessage(plugin.color(ConsoleAlerts.INVALID_ARGUMENTS_PASSED));
            } else {

                // Go ahead and parse our arguments.
                String target = args[0];
                String bells = args[1];

                // Try to retrieve a player object from the bukkit stack.
                Player p = Bukkit.getPlayer(target);

                // If the player object isn't null, that means they are online. We then check and see
                // If they are in the info page.
                if (!(p == null)) {

                    // grab the view. 
                    // note: apparently this will never be null. So, I simply just check the string.
                    InventoryView view = p.getOpenInventory();

                    // if they are in fact in the terminal, we're going to actively update the information.
                    if (ChatColor.stripColor(view.getTitle()).equalsIgnoreCase("nookterminal")) {

                        // surround in a try/catch. No need to send a message, just print st to the sender if an error occurs.
                        try {

                            // use function to not have redundant code
                            // if this method fails, the inventory will not be updated as to not confuse the user.
                            updateBells(p, bells, sender);

                            // generate an item factory and update it's data with the new data
                            ItemFactory bellsItem = new ItemFactory(Material.GOLD_NUGGET, 1);
                            bellsItem.setDisplayName(plugin.color("&eBalance&8:"));
                            bellsItem.setLore("&6" + bells);

                            // set the item.
                            view.setItem(19, bellsItem.getItem());

                        } catch (SQLException err) {
                            err.printStackTrace();
                        }

                    } else {

                        // if the player isn't in the terminal, we'll just update their data.
                        try {
                            updateBells(p, bells, sender);
                        } catch (SQLException err) {

                            // just throw the error to the console, as it's a console only command.
                            err.printStackTrace();
                        }

                    }
                } else {

                    // Yes, I know ofp is deprecated, but it's a small project for friends.
                    // I'm controlling the version, and software. Easiest way to do this for this sake.
                    @SuppressWarnings("deprecation")
					OfflinePlayer player = Bukkit.getOfflinePlayer(target);

                    // check if the player is in the database.
                    if (manager.checkIfPlayerExists(player.getUniqueId().toString())) {

                        // surround in a try/catch. If the player is updated, they receive a success message.
                        try {
                            // update the player
                            manager.setPlayerBells(player.getUniqueId().toString(), bells);

                            // send the updated message
                            sender.sendMessage(plugin.color(String.format(ConsoleAlerts.UPDATED_PLAYER_BELLS, player.getName())));
                        } catch (Exception err) {
                            err.printStackTrace();
                        }

                    } else {
                        // if the player doesn't exist, alert the console
                        sender.sendMessage(plugin.color(ConsoleAlerts.NO_DATABASE_ENTRIES));
                    }
                }
            }
        }

        return true;
    }

    /**
     * Update the bells of a target player
     * @param p Player to update, need for uuid.
     * @param bells Bells to set to.
     * @param sender Sender of the command.
     * @throws SQLException
     */
    private void updateBells(Player p, String bells, CommandSender sender) throws SQLException {

        // check if the player exists
        if (manager.checkIfPlayerExists(p.getUniqueId().toString())) {
            // set the player bells
            manager.setPlayerBells(p.getUniqueId().toString(), bells);

            // send the updated message
            sender.sendMessage(plugin.color(String.format(ConsoleAlerts.UPDATED_PLAYER_BELLS, p.getName())));
        } else {
        	
        	// alert the console that the player doesn't exist.
            sender.sendMessage(plugin.color(ConsoleAlerts.NO_DATABASE_ENTRIES));
        }

    }

}