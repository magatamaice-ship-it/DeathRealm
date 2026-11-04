
package id.deathrealm;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class DeathRealmAmbient extends BukkitRunnable {

    private final DeathRealm plugin;

    public DeathRealmAmbient(DeathRealm plugin) {
        this.plugin = plugin;
    }

    public void start() {
        runTaskTimer(plugin, 20L, 20L);
    }

    @Override
    public void run() {
        for (Player player : plugin.getDeathWorld().getPlayers()) {

            if (!plugin.getTimerManager().hasTimer(player.getUniqueId())) {
                continue;
            }

            Location location = player.getLocation();

            double x = ThreadLocalRandom.current().nextDouble(-3.0, 3.0);
            double y = ThreadLocalRandom.current().nextDouble(-1.0, 3.0);
            double z = ThreadLocalRandom.current().nextDouble(-3.0, 3.0);

            Location particleLocation = location.clone().add(x, y, z);

            player.getWorld().spawnParticle(
                    Particle.SOUL,
                    particleLocation,
                    2,
                    0.2, 0.3, 0.2,
                    0.01
            );

            player.getWorld().spawnParticle(
                    Particle.REVERSE_PORTAL,
                    location.clone().add(0, 1, 0),
                    3,
                    0.5, 0.7, 0.5,
                    0.02
            );
        }
    }
}
