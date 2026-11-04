package id.deathrealm;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DeathTimerManager {

    private final DeathRealm plugin;
    private final File file;
    private final YamlConfiguration data;

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

    public void removeTimer(UUID uuid) {
        data.set(uuid.toString(), null);
        save();
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
