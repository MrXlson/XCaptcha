package me.xverse.captcha;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class MiningListener implements Listener {

    private final HashMap<UUID, Integer> mined = new HashMap<>();

    private final HashMap<UUID, Integer> nextCaptcha = new HashMap<>();

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    private final Random random = new Random();

    @EventHandler
    public void onMine(BlockBreakEvent e) {

        FileConfiguration config = Main.getInstance().getConfig();

        if(!config.getBoolean("captcha.enabled")) return;

        Player p = e.getPlayer();

        if(p.hasPermission("xcaptcha.bypass")) return;

        if(p.getGameMode() == GameMode.CREATIVE) return;

        if(p.getGameMode() == GameMode.SPECTATOR) return;

        UUID uuid = p.getUniqueId();

        String world = p.getWorld().getName();

        if(config.getStringList("captcha.disabled-worlds").contains(world)) {
            return;
        }

        if(!config.getStringList("captcha.enabled-worlds").contains(world)) {
            return;
        }

        Material block = e.getBlock().getType();

        if(config.getStringList("captcha.ignored-blocks").contains(block.name())) {
            return;
        }

        if(cooldown.containsKey(uuid)) {

            long end = cooldown.get(uuid);

            if(System.currentTimeMillis() < end) {
                return;
            }
        }

        int amount = mined.getOrDefault(uuid, 0) + 1;

        mined.put(uuid, amount);

        if(!nextCaptcha.containsKey(uuid)) {

            int min = config.getInt("captcha.min-blocks");

            int max = config.getInt("captcha.max-blocks");

            nextCaptcha.put(uuid, random.nextInt((max - min) + 1) + min);
        }

        int needed = nextCaptcha.get(uuid);

        if(amount >= needed) {

            mined.put(uuid, 0);

            int min = config.getInt("captcha.min-blocks");

            int max = config.getInt("captcha.max-blocks");

            nextCaptcha.put(uuid, random.nextInt((max - min) + 1) + min);

            CaptchaGUI.openCaptcha(p);

            int cooldownSeconds = config.getInt("captcha.success-cooldown");

            cooldown.put(
                    uuid,
                    System.currentTimeMillis() + (cooldownSeconds * 1000L)
            );
        }
    }
}
