package id.deathrealm;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DeathRealmFlight implements Listener {

    private final DeathRealm plugin;

    public DeathRealmFlight(DeathRealm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        updateFlight(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        updateFlight(event.getPlayer());
    }

    private void updateFlight(Player player) {
        boolean isDeadRealm =
                player.getWorld().equals(plugin.getDeathWorld())
                && plugin.getTimerManager().hasTimer(player.getUniqueId());

        if (player.getGameMode() == GameMode.CREATIVE
                || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (isDeadRealm) {
            player.setAllowFlight(true);
            player.setFlying(true);
        } else {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }
}
