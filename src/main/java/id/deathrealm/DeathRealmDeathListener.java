package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
        
        plugin.getTimerManager().startTimer(player);
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
@EventHandler
public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    if (!plugin.getTimerManager().hasTimer(player.getUniqueId())) {
        return;
    }

    if (plugin.getTimerManager().getRemainingTime(player.getUniqueId()) <= 0) {
        plugin.getTimerManager().removeTimer(player.getUniqueId());
        player.teleport(org.bukkit.Bukkit.getWorlds().get(0).getSpawnLocation());
        return;
    }

    player.teleport(plugin.getDeathWorld().getSpawnLocation());
    plugin.getTimerManager().resumeTimer(player);
}
}
