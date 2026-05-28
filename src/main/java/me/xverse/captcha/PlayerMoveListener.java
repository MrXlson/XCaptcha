package me.xverse.captcha;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if(!CaptchaListener.captchaPlayers.contains(p.getUniqueId())) return;

        FileConfiguration config = Main.getInstance().getConfig();

        if(!config.getBoolean("captcha.freeze-player")) return;

        if(e.getFrom().distance(e.getTo()) > 0) {

            e.setTo(e.getFrom());
        }
    }
}
