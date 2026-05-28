package me.xverse.captcha;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Main.getInstance().reloadConfig();

        sender.sendMessage("§aXCaptcha config reloadnut.");

        return true;
    }
}
