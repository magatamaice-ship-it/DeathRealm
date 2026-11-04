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
        Player player = event.getPlayer();

        boolean enteringDeathRealm =
                event.getTo().getWorld().equals(plugin.getDeathWorld());

        boolean leavingDeathRealm =
                event.getFrom().getWorld().equals(plugin.getDeathWorld())
                && !enteringDeathRealm;

        // Pemain hidup tidak boleh masuk DeathRealm
        if (enteringDeathRealm
                && !plugin.getTimerManager().hasTimer(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

        // Pemain yang masih memiliki waktu hukuman tidak boleh keluar
        if (leavingDeathRealm
                && plugin.getTimerManager().getRemainingTime(player.getUniqueId()) > 0) {
            event.setCancelled(true);
        }
    }
}
