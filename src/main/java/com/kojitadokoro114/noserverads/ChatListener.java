package com.kojitadokoro114.noserverads;

import com.kojitadokoro114.noserverads.client.VirtualClient;
import com.kojitadokoro114.noserverads.object.RemoteServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.kojitadokoro114.noserverads.NoServerAdsPlugin.*;

public class ChatListener implements Listener {

    private static final Pattern HOST_PORT_PATTERN = Pattern.compile("(?i)([a-z0-9.-]+|\\d{1,3}(?:\\.\\d{1,3}){3})(?::(\\d{1,5}))?");

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        List<RemoteServer> servers = test(e.getMessage());
        for (RemoteServer server : servers) {
            if (blacklist.contains(server)) {
                doPunish(e.getPlayer());
                e.setCancelled(true);
                return;
            }
            if (debug) {
                plugin.getLogger().info("Start ping " + server.host + ":" + server.port);
            }
            VirtualClient.tryPing(server.host, server.port, () -> doPunish(e.getPlayer()));
        }
    }

    private static void doPunish(Player player) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (String command : punishments) {
                String realCommand = command.replace("$player", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), realCommand);
            }
        });
    }

    private static List<RemoteServer> test(String input) {
        Matcher matcher = HOST_PORT_PATTERN.matcher(input);
        List<RemoteServer> servers = new ArrayList<>();
        while (matcher.find()) {
            String host = matcher.group(1);
            String portStr = matcher.group(2);
            int port = (portStr != null) ? Integer.parseInt(portStr) : 25565;
            servers.add(new RemoteServer(host, port));
        }
        return servers;
    }
}