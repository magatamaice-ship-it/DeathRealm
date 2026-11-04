
package id.deathrealm;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DeathRealmProtection implements Listener {

    private final DeathRealm plugin;

    public DeathRealmProtection(DeathRealm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null || event.getTo().getWorld() == null) {
            return;
        }

        Player player = event.getPlayer();

        boolean enteringDeathRealm =
                event.getTo().getWorld().equals(plugin.getDeathWorld());

        boolean leavingDeathRealm =
                event.getFrom().getWorld().equals(plugin.getDeathWorld())
                && !enteringDeathRealm;

        boolean hasTimer =
                plugin.getTimerManager().hasTimer(player.getUniqueId());

        if (enteringDeathRealm && !hasTimer) {
            event.setCancelled(true);
            return;
        }

        if (leavingDeathRealm && hasTimer) {
            event.setCancelled(true);
        }
    }
}
