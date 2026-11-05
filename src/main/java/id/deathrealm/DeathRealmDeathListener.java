package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathRealmDeathListener implements Listener {

    private final DeathRealm plugin;

    public DeathRealmDeathListener(DeathRealm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        event.setKeepInventory(false);
        event.setKeepLevel(false);

        plugin.getTimerManager().startTimer(player);

        Location location = player.getLocation().clone();

        location.getWorld().playSound(
                location,
                Sound.ENTITY_ALLAY_DEATH,
                1.0f,
                0.7f
        );

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count >= 10) {
                    cancel();
                    return;
                }

                location.getWorld().spawnParticle(
                        Particle.SOUL,
                        location.clone().add(0, count * 0.25, 0),
                        12,
                        0.3, 0.2, 0.3,
                        0.02
                );

                count++;
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getTimerManager().hasTimer(player.getUniqueId())) {
            return;
        }

        event.setRespawnLocation(
                plugin.getDeathWorld().getSpawnLocation()
        );

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            if (plugin.getTimerManager().getRemainingTime(
                    player.getUniqueId()) <= 0) {
                plugin.getTimerManager().resurrect(player);
                return;
            }

            player.teleport(
                    plugin.getDeathWorld().getSpawnLocation()
            );

            plugin.getTimerManager().resumeTimer(player);
        }, 1L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getTimerManager().hasTimer(player.getUniqueId())) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!player.isOnline()) {
                return;
            }

            if (plugin.getTimerManager().getRemainingTime(
                    player.getUniqueId()) <= 0) {
                plugin.getTimerManager().resurrect(player);
                return;
            }

            player.teleport(
                    plugin.getDeathWorld().getSpawnLocation()
            );

            plugin.getTimerManager().resumeTimer(player);
        }, 1L);
    }
}
