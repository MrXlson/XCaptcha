package me.xverse.captcha;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new MiningListener(), this);

        getServer().getPluginManager().registerEvents(new CaptchaListener(), this);

        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);

        getServer().getPluginManager().registerEvents(new CommandListener(), this);

        getCommand("xcaptcha").setExecutor(new ReloadCommand());

        getLogger().info("XCaptcha enabled!");
    }

    public static Main getInstance() {
        return instance;
    }
}
