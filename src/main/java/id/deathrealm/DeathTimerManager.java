package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathTimerManager {

    private final DeathRealm plugin;
    private final File file;
    private final YamlConfiguration data;

    private final Map<UUID, BukkitTask> tasks = new HashMap<>();

    private static final long DEATH_TIME = 60 * 60 * 1000L;

    public DeathTimerManager(DeathRealm plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(plugin.getDataFolder(), "timers.yml");
        data = YamlConfiguration.loadConfiguration(file);
    }

    public void startTimer(Player player) {
        UUID uuid = player.getUniqueId();

        long endTime = System.currentTimeMillis() + DEATH_TIME;

        data.set(uuid.toString(), endTime);
        save();

        plugin.updateVisibility();
        startCountdown(player);
    }

    public long getRemainingTime(UUID uuid) {
        long endTime = data.getLong(uuid.toString(), 0);

        if (endTime == 0) {
            return 0;
        }

        return Math.max(0, endTime - System.currentTimeMillis());
    }

    public boolean hasTimer(UUID uuid) {
        return data.contains(uuid.toString());
    }

    public void resumeTimer(Player player) {
        if (hasTimer(player.getUniqueId())) {
            startCountdown(player);
        }
    }

    private void startCountdown(Player player) {
        UUID uuid = player.getUniqueId();

        if (tasks.containsKey(uuid)) {
            return;
        }

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {
                    if (!player.isOnline()) {
                        BukkitTask current = tasks.remove(uuid);

                        if (current != null) {
                            current.cancel();
                        }

                        plugin.getBossBar().remove(player);
                        return;
                    }

                    long remaining = getRemainingTime(uuid);

                    if (remaining <= 0) {
                        resurrect(player);
                        return;
                    }

                    plugin.getBossBar().update(player, remaining);
                },
                20L,
                20L
        );

        tasks.put(uuid, task);
    }

    public void resurrect(Player player) {
        UUID uuid = player.getUniqueId();

        if (!hasTimer(uuid)) {
            return;
        }

        Location destination = player.getRespawnLocation();

        if (destination == null
                || destination.getWorld() == null
                || destination.getWorld().equals(plugin.getDeathWorld())) {

            World mainWorld = Bukkit.getWorld("world");

            if (mainWorld == null) {
                plugin.getLogger().severe(
                        "Dunia utama 'world' tidak ditemukan!"
                );
                return;
            }

            destination = mainWorld.getSpawnLocation();
        }

        Location oldLocation = player.getLocation().clone();

        oldLocation.getWorld().spawnParticle(
                Particle.SOUL,
                oldLocation.clone().add(0, 1, 0),
                40,
                0.5, 0.8, 0.5,
                0.05
        );

        oldLocation.getWorld().playSound(
                oldLocation,
                Sound.ENTITY_ALLAY_DEATH,
                1.0f,
                1.2f
        );

        removeTimer(uuid);

        player.teleport(destination);

        Location newLocation = player.getLocation();

        newLocation.getWorld().spawnParticle(
                Particle.TOTEM_OF_UNDYING,
                newLocation.clone().add(0, 1, 0),
                60,
                0.5, 1.0, 0.5,
                0.1
        );

        newLocation.getWorld().playSound(
                newLocation,
                Sound.ITEM_TOTEM_USE,
                1.0f,
                1.0f
        );
    }

    public void removeTimer(UUID uuid) {
        data.set(uuid.toString(), null);
        save();

        BukkitTask task = tasks.remove(uuid);

        if (task != null) {
            task.cancel();
        }

        Player player = Bukkit.getPlayer(uuid);

        if (player != null) {
            plugin.getBossBar().remove(player);
        }

        plugin.updateVisibility();
    }

    private void save() {
        try {
            data.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe("Gagal menyimpan timer!");
            exception.printStackTrace();
        }
    }
}
