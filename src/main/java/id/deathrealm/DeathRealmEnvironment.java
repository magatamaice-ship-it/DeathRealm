
package id.deathrealm;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class DeathRealmEnvironment implements Listener {

    private final DeathRealm plugin;

    public DeathRealmEnvironment(DeathRealm plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.getWorld().equals(plugin.getDeathWorld())
                && plugin.getTimerManager().hasTimer(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.getWorld().equals(plugin.getDeathWorld())
                && plugin.getTimerManager().hasTimer(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
