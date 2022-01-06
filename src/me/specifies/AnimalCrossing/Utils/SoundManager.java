package me.specifies.AnimalCrossing.Utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundManager {
	
	// soundmanager file to play a sound to a player
	public SoundManager(Sound sound, int p, int v, Player player) {
		
		// play the sound at the location
		player.playSound(player.getLocation(), sound, p, v);
		
	}

}
