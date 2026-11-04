
package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathRealm extends JavaPlugin {

    private World deathWorld;

    @Override
    public void onEnable() {
        getLogger().info("DeathRealm is starting...");

        createDeathWorld();
        
        getServer().getPluginManager().registerEvents(
    new DeathRealmFlight(this), this
);
        getServer().getPluginManager().registerEvents(
    new DeathRealmDeathListener(this), this
);
        getLogger().info("DeathRealm successfully enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DeathRealm has been disabled.");
    }

    private void createDeathWorld() {
        World existing = Bukkit.getWorld("DeathRealm");

        if (existing != null) {
            deathWorld = existing;
            getLogger().info("DeathRealm world already exists.");
            return;
        }

        WorldCreator creator = new WorldCreator("DeathRealm");
        creator.type(WorldType.FLAT);
        creator.generateStructures(false);

        deathWorld = Bukkit.createWorld(creator);

        if (deathWorld != null) {
            deathWorld.setGameRule(
                org.bukkit.GameRule.DO_DAYLIGHT_CYCLE,
                false
            );

            deathWorld.setTime(18000);
            deathWorld.setStorm(false);

            getLogger().info("DeathRealm world created.");
        } else {
            getLogger().severe("Failed to create DeathRealm world!");
        }
    }

    public World getDeathWorld() {
        return deathWorld;
    }
}
