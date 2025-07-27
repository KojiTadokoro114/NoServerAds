package com.kojitadokoro114.noserverads;

import com.kojitadokoro114.noserverads.client.VirtualClient;
import com.kojitadokoro114.noserverads.object.RemoteServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NoServerAdsPlugin extends JavaPlugin {

    public static NoServerAdsPlugin plugin;

    public static List<String> punishments;
    public static List<String> whitelist;
    public static Set<RemoteServer> blacklist;
    public static boolean debug;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        plugin = this;
        reload();
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        VirtualClient.shutdown();
    }

    public void reload() {
        reloadConfig();
        punishments = getConfig().getStringList("punishments");
        whitelist = getConfig().getStringList("whitelist");
        debug = getConfig().getBoolean("debug-mode", false);
        blacklist = getConfig().getStringList("blacklist").stream()
            .map(RemoteServer::parse).collect(Collectors.toSet());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1 || !args[0].equals("reload")) {
            return true;
        }
        reload();
        sender.sendMessage(ChatColor.AQUA + "[NoServerAds] Reload complete!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length <= 1) {
            return Collections.singletonList("reload");
        }
        return Collections.emptyList();
    }
}
