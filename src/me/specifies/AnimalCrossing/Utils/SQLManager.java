package me.specifies.AnimalCrossing.Utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.specifies.AnimalCrossing.Main;

public class SQLManager {
	
	private final String fileName;
	private final File dataFolder;
	
	private Connection connection;
	
	
	public SQLManager(String fileName, File dataFolder) {
		this.fileName = fileName;
		this.dataFolder = dataFolder;
	}
	
	public void setup() throws SQLException, ClassNotFoundException, IOException {
		
		if(connection != null && !connection.isClosed()) return;
		
		File sqlite;
		
		// This bit of code is designed to mitigate user error. If the user doesn't include .db to end the file in the config, we append it for them.
		if(!this.fileName.substring(this.fileName.length() - 3).equalsIgnoreCase(".db")) sqlite = new File(this.dataFolder, this.fileName + ".db");
		else sqlite = new File(this.dataFolder, this.fileName);
		
		// create the db if it doesn't exist.
		if(!(sqlite.exists())) sqlite.createNewFile();
		
		// make sure the system is running the sqlite driver
		Class.forName("org.sqlite.JDBC");
		
		// establish a connection
		this.connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder + File.separator + this.fileName);
		
		// create our base tables, if they don't exist.
		Statement stmt = this.connection.createStatement();
		stmt.execute("CREATE TABLE IF NOT EXISTS animalcrossing(uuid TEXT, lastLogout TEXT, bells TEXT, nookmiles TEXT)");
	}
	
	public void reset() {
		try {
			this.connection.close();
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder + File.separator + this.fileName);
		} catch(SQLException err) {
			err.printStackTrace();
			Bukkit.getLogger().log(Level.SEVERE, "[AnimalCrossing] Could not refresh the database connection. Shutting down.");
			Bukkit.getPluginManager().disablePlugin(Main.getInstance());
		}
	}
	
	public boolean checkIfPlayerExists(String uuid) {
		
		String query = "SELECT uuid from animalcrossing WHERE uuid = ?";
		
		int count = 0;
		
		try {
			
			PreparedStatement stmt = this.format(query, new String[] {uuid});
			
			ResultSet set = stmt.executeQuery();
			
			while(set.next()) count++;
			
		} catch(SQLException err) {
			err.printStackTrace();
		}
		
		if(count > 0) return true;
		else return false;
	}
	
	public String getPlayerBells(String uuid) throws SQLException {
		String query = "SELECT bells, uuid from animalcrossing WHERE uuid = ?";
		
		PreparedStatement stmt = this.format(query, new String[] {uuid});
		
		ResultSet results = stmt.executeQuery();
		
		String result = "";
		
		while(results.next()) {
			if(results.getString("uuid").equalsIgnoreCase(uuid)) {
				result = results.getString("bells");
			}		
		}
		
		return result;
		
	}
	
	public String getPlayerMiles(String uuid) throws SQLException {
		String query = "SELECT nookmiles, uuid from animalcrossing WHERE uuid = ?";
		
		PreparedStatement stmt = this.format(query,  new String[] {uuid});
		
		ResultSet results = stmt.executeQuery();
		
		String result = "";
		
		while(results.next()) {
			if(results.getString("uuid").equalsIgnoreCase(uuid)) {
				result = results.getString("nookmiles");
			}
		}
		
		return result;
	}
	
	public void setPlayerMiles(String uuid, String miles) {
		
		String query = "UPDATE animalcrossing SET nookmiles = ? WHERE uuid = ?";
		
		try {
			PreparedStatement stmt = this.format(query, new String[] {miles, uuid});
			
			stmt.executeUpdate();
			
		} catch (SQLException err) {
			err.printStackTrace();
		}
	}
	
	public void setPlayerBells(String uuid, String bells) {
		
		String query = "UPDATE animalcrossing SET bells = ? WHERE uuid = ?";
		
		try {
			PreparedStatement stmt = this.format(query, new String[] {bells, uuid});
			
			stmt.executeUpdate();
		} catch(SQLException err) {
			err.printStackTrace();
		}
		
	}
	
	public void createPlayer(String uuid) throws SQLException {
		
		String query = "INSERT into animalcrossing(uuid, lastLogout, bells, nookmiles) VALUES (?, ?, ?, ?)";
		
		PreparedStatement stmt = this.format(query, new String[] {uuid, "0", "500", "0"});
		
		stmt.executeUpdate();
	}
	
	/**
	 * Function designed to ease the code required to format strings with data. Draws heavily from my experience with nodejs' SQLite / MySQL libraries
	 * @param query The query string that needs to be format.
	 * @param format The array of formatted. The first array corresponds with the first ? in the query strings, and so forth.
	 * @return PreparedStatement The formatted statement that can then further be manipulated.
	 * @throws SQLException There was an error in formatting. 9/10 it's going to be an invalid SQL syntax.
	 */
	private PreparedStatement format(String query, String[] format) throws SQLException {
		
		PreparedStatement stmt = this.connection.prepareStatement(query);
		
		for(int i = 1; i <= format.length; i++) stmt.setString(i, format[i-1]);
			
		return stmt;
		
	}

}
