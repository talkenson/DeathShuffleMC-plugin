package talkenson.tkpl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import talkenson.tkpl.commands.Setup;
import talkenson.tkpl.lib.DeathTypes;

public class PlayerDeathListener implements Listener {
  Setup setupInstance = null;
  DeathTypes dt = null;

  public PlayerDeathListener(Setup setupInstance, DeathTypes dt) {
    this.setupInstance = setupInstance;
    this.dt = dt;
  }

  @EventHandler
  public void onPlayerDead(PlayerDeathEvent e) {
    if (setupInstance.getIsGameActive() && e.getDeathMessage() != null) {
      Player player = e.getEntity();
      var deathTypeForPlayer = setupInstance.getPlayerToDeathType().get(player.getName());
      if (dt.testDeathType(deathTypeForPlayer, e.getDeathMessage())) {
        setupInstance.acceptDeath(player);
      } else {
        Msg.send(e.getEntity(), "Reminder: You need to be killed by " + deathTypeForPlayer);
      }
    }
  }

  @EventHandler
  public void onPlayerDisconnect(PlayerQuitEvent e) {
    if (setupInstance.getIsGameActive()) {
      e.setQuitMessage(e.getPlayer().getDisplayName() + " ran away from the server in fear.");
      setupInstance.acceptDeath(e.getPlayer(), true);
    }
  }
}
