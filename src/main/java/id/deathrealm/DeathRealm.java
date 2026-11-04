
package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathRealm extends JavaPlugin {

    private World deathWorld;
    private DeathTimerManager timerManager;
    private DeathRealmAmbient ambient;

    @Override
    public void onEnable() {

        getLogger().info("Starting DeathRealm...");

        // Membuat dunia DeathRealm yang kosong.
        WorldCreator creator = new WorldCreator("DeathRealm");
        creator.generator(new VoidChunkGenerator());

        deathWorld = Bukkit.createWorld(creator);

        if (deathWorld == null) {
            getLogger().severe("Gagal membuat dunia DeathRealm!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Pengaturan dunia.
        deathWorld.setSpawnLocation(new Location(deathWorld, 0, 100, 0));
        deathWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        deathWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        deathWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        deathWorld.setTime(18000);
        deathWorld.setStorm(false);
        deathWorld.setThundering(false);

        // Sistem timer.
        timerManager = new DeathTimerManager(this);

        // Daftarkan listener.
        getServer().getPluginManager().registerEvents(
                new DeathRealmDeathListener(this), this);

        getServer().getPluginManager().registerEvents(
                new DeathRealmFlight(this), this);

        getServer().getPluginManager().registerEvents(
                new DeathRealmProtection(this), this);

        DeathRealmVisibility visibility = new DeathRealmVisibility(this);

        getServer().getPluginManager().registerEvents(
                visibility, this);

        // Efek atmosfer DeathRealm.
        ambient = new DeathRealmAmbient(this);
        ambient.start();

        // Perbarui visibilitas pemain setelah plugin aktif.
        Bukkit.getScheduler().runTask(this, visibility::updateVisibility);

        getLogger().info("DeathRealm successfully enabled!");
    }

    @Override
    public void onDisable() {
        if (ambient != null) {
            ambient.cancel();
        }

        getLogger().info("DeathRealm disabled.");
    }

    public World getDeathWorld() {
        return deathWorld;
    }

    public DeathTimerManager getTimerManager() {
        return timerManager;
    }
}
