package me.xverse.captcha;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {

        Player p = e.getPlayer();

        if(!CaptchaListener.captchaPlayers.contains(p.getUniqueId())) return;

        FileConfiguration config = Main.getInstance().getConfig();

        if(!config.getBoolean("captcha.block-commands")) return;

        String command = e.getMessage().split(" ")[0]
                .replace("/", "")
                .toLowerCase();

        List<String> allowed = config.getStringList("captcha.allowed-commands");

        if(allowed.contains(command)) return;

        e.setCancelled(true);

        p.sendMessage("§cBěhem CAPTCHA nemůžeš používat commandy!");
    }
}
