package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathRealmDeathListener implements Listener {

    private final DeathRealm plugin;

    public DeathRealmDeathListener(DeathRealm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        // Item dan XP tetap mengikuti sistem Minecraft biasa
        event.setKeepInventory(false);
        event.setKeepLevel(false);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTask(plugin, () -> {
            if (plugin.getDeathWorld() != null) {
                player.teleport(plugin.getDeathWorld().getSpawnLocation());
            }
        });
    }
}
