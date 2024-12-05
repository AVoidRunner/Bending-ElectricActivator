package me.Mastervrunner.DischargeEffect;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.projectkorra.projectkorra.BendingPlayer;

/*
 * Implements Listener so that the server knows this is checking for events.
 */
public class DischargeEffectListener implements Listener {

	/*
	 * The event method.
	 * This specific event is looking for "PlayerAnimationEvent" which is triggered any time
	 * the server sees that the player has left-clicked. This is also triggered by other
	 * things but we are using it for the left-click function.
	 */
	
	@EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        

        if (!player.isSneaking()) {
    		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

    		/*
    		 * If this event has been cancelled or a player that triggered the event does not exist,
    		 * then return.
    		 */
    		if (event.isCancelled() || bPlayer == null) {
    			return;

    		/*
    		 * If the player exists and the event wasn't cancelled, but their bound ability is non-existent,
    		 * then return;
    		 * 
    		 * This basically ensures that only players with bind abilities will be checked by the server.
    		 */
    		}  
    		
    		if (bPlayer.getBoundAbilityName().equalsIgnoreCase("Discharge")) {
    			new DischargeEffect(player);

    		}
        }
	}
}