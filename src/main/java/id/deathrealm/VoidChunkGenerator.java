
package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import org.bukkit.plugin.java.JavaPlugin;

public class DeathRealm extends JavaPlugin {

    private World deathWorld;

    @Override
    public void onEnable() {
        getLogger().info("DeathRealm starting...");

        createDeathWorld();

        getLogger().info("DeathRealm enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DeathRealm disabled.");
    }

    private void createDeathWorld() {
        World existing = Bukkit.getWorld("DeathRealm");

        if (existing != null) {
            deathWorld = existing;
            return;
        }

        WorldCreator creator = new WorldCreator("DeathRealm");
        creator.generator(new VoidChunkGenerator());

        deathWorld = Bukkit.createWorld(creator);

        if (deathWorld != null) {
            deathWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            deathWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            deathWorld.setTime(18000);
            deathWorld.setSpawnLocation(0, 100, 0);

            getLogger().info("Void world successfully created!");
        } else {
            getLogger().severe("Failed to create Void world!");
        }
    }

    public World getDeathWorld() {
        return deathWorld;
    }
}
