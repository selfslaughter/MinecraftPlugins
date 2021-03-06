package com.ullarah.utab;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

class TabFunctions {

    private final Plugin plugin;
    int headerMessageTotal = 0, headerMessageCurrent = 0, footerMessageTotal = 0, footerMessageCurrent = 0;
    private BukkitTask tabTask;
    private List headerMessages, footerMessages;
    private int tabTimer;

    TabFunctions(Plugin instance) {
        plugin = instance;
    }

    private Plugin getPlugin() {
        return plugin;
    }

    BukkitTask getTabTask() {
        return tabTask;
    }

    void setTabTask(BukkitTask task) {
        this.tabTask = task;
    }

    private List getHeaderMessages() {
        return headerMessages;
    }

    private void setHeaderMessages(List messages) {
        headerMessages = messages;
    }

    private List getFooterMessages() {
        return footerMessages;
    }

    private void setFooterMessages(List messages) {
        footerMessages = messages;
    }

    int getTabTimer() {
        return tabTimer;
    }

    private void setTabTimer(int time) {
        tabTimer = time * 20;
    }

    void sendHeaderFooter(Player player, String header, String footer) {
        header = header.replaceAll("\\{player\\}", player.getPlayerListName());
        footer = footer.replaceAll("\\{player\\}", player.getPlayerListName());

        player.setPlayerListHeaderFooter(header, footer);
    }

    String getCurrentHeader() {
        try {
            return ChatColor.translateAlternateColorCodes('&',
                    (String) getHeaderMessages().get(headerMessageCurrent));
        } catch (Exception e) {
            return "";
        }
    }

    String getCurrentFooter() {
        try {
            return ChatColor.translateAlternateColorCodes('&',
                    (String) getFooterMessages().get(footerMessageCurrent));
        } catch (Exception e) {
            return "";
        }
    }

    boolean reloadTabConfig() {
        try {

            for (Player player : Bukkit.getOnlinePlayers()) sendHeaderFooter(player, "", "");

            headerMessageCurrent = 0;
            footerMessageCurrent = 0;

            getPlugin().reloadConfig();

            FileConfiguration configuration = getPlugin().getConfig();

            setTabTimer(configuration.getInt("timer"));

            setHeaderMessages(configuration.getStringList("header.messages"));
            setFooterMessages(configuration.getStringList("footer.messages"));

            headerMessageTotal = getHeaderMessages().size() - 1;
            footerMessageTotal = getFooterMessages().size() - 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
