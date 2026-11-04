package id.deathrealm;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
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
        
        plugin.getTimerManager().startTimer(player);Location location = player.getLocation();

new BukkitRunnable() {
    int count = 0;

    @Override
    public void run() {
        if (count >= 20) {
            cancel();
            return;
        }

        location.getWorld().spawnParticle(
                Particle.SOUL,
                location.clone().add(0, count * 0.15, 0),
                8,
                0.3, 0.3, 0.3,
                0.02
        );

        count++;
    }
}.runTaskTimer(plugin, 0L, 2L);

player.getWorld().playSound(
        location,
        Sound.ENTITY_ALLAY_DEATH,
        1.0f,
        0.7f
);
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
