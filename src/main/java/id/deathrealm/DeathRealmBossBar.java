package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathRealmBossBar {

    private final Map<UUID, BossBar> bars = new HashMap<>();

    public void update(Player player, long remainingTime) {
        UUID uuid = player.getUniqueId();

        BossBar bar = bars.computeIfAbsent(uuid, key ->
                Bukkit.createBossBar(
                        "Kelahiran Kembali",
                        BarColor.PURPLE,
                        BarStyle.SOLID
                )
        );

        long seconds = Math.max(0, remainingTime / 1000);

        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        String time = String.format(
                "%02d:%02d:%02d",
                hours, minutes, secs
        );

        double progress = Math.max(
                0.0,
                Math.min(1.0, (double) remainingTime / 3600000.0)
        );

        bar.setTitle("§5Kelahiran Kembali dalam §d" + time);
        bar.setProgress(progress);

        if (!bar.getPlayers().contains(player)) {
            bar.addPlayer(player);
        }
    }

    public void remove(Player player) {
        BossBar bar = bars.remove(player.getUniqueId());

        if (bar != null) {
            bar.removeAll();
        }
    }

    public void removeAll() {
        for (BossBar bar : bars.values()) {
            bar.removeAll();
        }

        bars.clear();
    }
}
