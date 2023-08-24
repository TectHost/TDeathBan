package minealex.tdeathban.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.ChatColor;

import minealex.tdeathban.TDeathBan;

@SuppressWarnings("unused")
public class Commands implements CommandExecutor {

    private TDeathBan plugin;

    public Commands(TDeathBan plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("tdb.reload")) {
                reloadConfigurations();
                String reloadMessage = plugin.getMessages().getString("messages.reload_success", "&5TDeathBan &e> &aTDeathBan configurations reloaded.");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
            } else {
                String noPermissionMessage = plugin.getMessages().getString("messages.no_permission", "&5TDeathBan &e> &cYou don't have permission to use this command.");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage));
            }
            return true;
        } else if (args.length == 1 && args[0].equalsIgnoreCase("version")) {
            if (sender.hasPermission("tdb.version")) {
                String versionMessage = plugin.getMessages().getString("messages.version", "&5TDeathBan &e> &aTDeathBan version: &e{version}");
                versionMessage = versionMessage.replace("{version}", plugin.getDescription().getVersion());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', versionMessage));
            } else {
                String noPermissionMessage = plugin.getMessages().getString("messages.no_permission", "&5TDeathBan &e> &cYou don't have permission to use this command.");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage));
            }
            return true;
        }
        return false;
    }

    private void reloadConfigurations() {
        plugin.reloadConfig();
        plugin.reloadMessages();
        plugin.reloadPlayers();
    }
}
