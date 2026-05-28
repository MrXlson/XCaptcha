package me.xverse.captcha;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class CaptchaGUI {

    public static void openCaptcha(Player p) {

        if(CaptchaListener.captchaPlayers.contains(p.getUniqueId())) return;

        FileConfiguration config = Main.getInstance().getConfig();

        int size = config.getInt("captcha.gui.size");

        String title = config.getString("captcha.gui.title")
                .replace("&", "§");

        Inventory inv = Bukkit.createInventory(null, size, title);

        Random random = new Random();

        int slot = random.nextInt(size);

        Material fill = Material.valueOf(
                config.getString("captcha.gui.fill-material")
        );

        Material captcha = Material.valueOf(
                config.getString("captcha.gui.captcha-material")
        );

        for(int i = 0; i < size; i++) {

            if(i == slot) {
                inv.setItem(i, new ItemStack(captcha));
            } else {
                inv.setItem(i, new ItemStack(fill));
            }
        }

        CaptchaListener.captchaPlayers.add(p.getUniqueId());

        p.openInventory(inv);

        int timeout = config.getInt("captcha.timeout-seconds");

        Bukkit.getScheduler().runTaskLater(
                Main.getInstance(),
                () -> {

                    if(CaptchaListener.captchaPlayers.contains(p.getUniqueId())) {

                        p.kickPlayer(
                                config.getString("messages.timeout")
                                        .replace("&", "§")
                        );
                    }

                },
                timeout * 20L
        );

        for(int i = 0; i <= timeout; i++) {

            int timeLeft = timeout - i;

            Bukkit.getScheduler().runTaskLater(
                    Main.getInstance(),
                    () -> {

                        if(CaptchaListener.captchaPlayers.contains(p.getUniqueId())) {

                            p.sendActionBar(
                                    "§cKlikni na BEDROCK! §7" + timeLeft + "s"
                            );
                        }

                    },
                    i * 20L
            );
        }
    }
}
