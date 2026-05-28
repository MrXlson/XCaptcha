package me.xverse.captcha;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CaptchaListener implements Listener {

    public static Set<UUID> captchaPlayers = new HashSet<>();

    public static HashMap<UUID, Long> cooldown = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;

        FileConfiguration config = Main.getInstance().getConfig();

        String title = config.getString("captcha.gui.title")
                .replace("&", "§");

        if (!e.getView().getTitle().equals(title)) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;

        Material captcha = Material.valueOf(
                config.getString("captcha.gui.captcha-material")
        );

        if (e.getCurrentItem().getType() == captcha) {

            captchaPlayers.remove(p.getUniqueId());

            int cooldownSeconds = config.getInt("captcha.success-cooldown");

            cooldown.put(
                    p.getUniqueId(),
                    System.currentTimeMillis() + (cooldownSeconds * 1000L)
            );

            p.sendMessage(
                    config.getString("messages.success")
                            .replace("&", "§")
            );

            Bukkit.getScheduler().runTaskLater(
                    Main.getInstance(),
                    () -> p.closeInventory(),
                    1L
            );

        } else {

            captchaPlayers.remove(p.getUniqueId());

            Bukkit.getScheduler().runTaskLater(
                    Main.getInstance(),
                    () -> p.kickPlayer(
                            config.getString("messages.fail")
                                    .replace("&", "§")
                    ),
                    1L
            );
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {

        Player p = (Player) e.getPlayer();

        if (!captchaPlayers.contains(p.getUniqueId())) return;

        Bukkit.getScheduler().runTaskLater(
                Main.getInstance(),
                () -> {

                    if (captchaPlayers.contains(p.getUniqueId())) {

                        p.openInventory(e.getInventory());
                    }

                },
                2L
        );
    }
}
