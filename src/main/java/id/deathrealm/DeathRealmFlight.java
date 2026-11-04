
package id.deathrealm;

import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class DeathRealmFlight implements Listener {

    private final DeathRealm plugin;
    private final NamespacedKey flightKey;

    public DeathRealmFlight(DeathRealm plugin) {
        this.plugin = plugin;
        this.flightKey = new NamespacedKey(plugin, "previous_flight");
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().equals(plugin.getDeathWorld())) {
            enableDeathFlight(player);
        } else if (event.getFrom().equals(plugin.getDeathWorld())) {
            restoreFlight(player);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getWorld().equals(plugin.getDeathWorld())
                && plugin.getTimerManager().hasTimer(player.getUniqueId())) {
            enableDeathFlight(player);
        }
    }

    private void enableDeathFlight(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE
                || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (!player.getPersistentDataContainer().has(
                flightKey, PersistentDataType.BYTE)) {

            byte previousFlight = (byte) (
                    player.getAllowFlight() ? 1 : 0
            );

            player.getPersistentDataContainer().set(
                    flightKey,
                    PersistentDataType.BYTE,
                    previousFlight
            );
        }

        player.setAllowFlight(true);
        player.setFlying(true);
    }

    private void restoreFlight(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE
                || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        byte previousFlight = player.getPersistentDataContainer().getOrDefault(
                flightKey,
                PersistentDataType.BYTE,
                (byte) 0
        );

        player.setFlying(false);
        player.setAllowFlight(previousFlight == 1);

        player.getPersistentDataContainer().remove(flightKey);
    }
}
