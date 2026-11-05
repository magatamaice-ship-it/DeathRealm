package id.deathrealm;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeathRealmCommand implements CommandExecutor {

    private final DeathRealm plugin;

    public DeathRealmCommand(DeathRealm plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!sender.hasPermission("deathrealm.admin")) {
            sender.sendMessage("§cKamu tidak memiliki izin!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(
                    "§eGunakan: /lahirkankembali <nama_player>"
            );
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            sender.sendMessage("§cPemain tidak ditemukan atau sedang offline!");
            return true;
        }

        if (!plugin.getTimerManager().hasTimer(target.getUniqueId())) {
            sender.sendMessage("§cPemain tersebut tidak sedang berada di DeathRealm!");
            return true;
        }

        plugin.getTimerManager().resurrect(target);

        sender.sendMessage(
                "§aBerhasil melahirkan kembali " + target.getName() + "!"
        );

        return true;
    }
}
