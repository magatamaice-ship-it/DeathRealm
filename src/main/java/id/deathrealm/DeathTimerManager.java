
package id.deathrealm;

import org.bukkit.Bukkit;
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
        long endTime = System.currentTimeMillis() + DEATH_TIME;

        data.set(player.getUniqueId().toString(), endTime);
        save();

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

        final BukkitTask[] taskRef = new BukkitTask[1];

        taskRef[0] = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            if (!player.isOnline()) {
                taskRef[0].cancel();
                tasks.remove(uuid);
                return;
            }

            if (getRemainingTime(uuid) <= 0) {
                resurrect(player);
                taskRef[0].cancel();
            }

        }, 20L, 20L);

        tasks.put(uuid, taskRef[0]);
    }

    public void resurrect(Player player) {
        removeTimer(player.getUniqueId());

        World mainWorld = Bukkit.getWorlds().get(0);
        player.teleport(mainWorld.getSpawnLocation());

        player.getWorld().spawnParticle(
                org.bukkit.Particle.TOTEM_OF_UNDYING,
                player.getLocation().add(0, 1, 0),
                60,
                0.5, 1.0, 0.5,
                0.1
        );

        player.getWorld().playSound(
                player.getLocation(),
                org.bukkit.Sound.ITEM_TOTEM_USE,
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
