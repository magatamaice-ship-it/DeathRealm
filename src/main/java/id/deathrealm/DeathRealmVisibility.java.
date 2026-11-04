
package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DeathRealmVisibility implements Listener {

    private final DeathRealm plugin;

    public DeathRealmVisibility(DeathRealm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(
                plugin, this::updateVisibility, 2L);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Bukkit.getScheduler().runTask(
                plugin, this::updateVisibility);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTask(
                plugin, this::updateVisibility);
    }

    public void updateVisibility() {
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            for (Player target : Bukkit.getOnlinePlayers()) {

                if (viewer.equals(target)) {
                    continue;
                }

                boolean viewerDead = isDead(viewer);
                boolean targetDead = isDead(target);

                if (viewerDead != targetDead) {
                    viewer.hidePlayer(plugin, target);
                } else {
                    viewer.showPlayer(plugin, target);
                }
            }
        }
    }

    private boolean isDead(Player player) {
        return plugin.getTimerManager().hasTimer(player.getUniqueId());
    }
}
