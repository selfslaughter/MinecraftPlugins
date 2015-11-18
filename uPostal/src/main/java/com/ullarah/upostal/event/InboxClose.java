package com.ullarah.upostal.event;

import com.ullarah.upostal.PostalInit;
import com.ullarah.upostal.command.inbox.Update;
import com.ullarah.upostal.function.PlayerProfile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.UUID;

import static com.ullarah.upostal.PostalInit.getInboxDataPath;
import static com.ullarah.upostal.PostalInit.inboxChanged;
import static org.bukkit.ChatColor.stripColor;

public class InboxClose implements Listener {

    @EventHandler
    public void event(final InventoryCloseEvent event) {

        Inventory chestInventory = event.getInventory();

        if (chestInventory.getName().matches("§4Inbox: §3(.*)")) {

            String inboxOwner = stripColor(event.getInventory().getTitle().replace("Inbox: ", ""));
            UUID inboxUUID = new PlayerProfile().lookup(inboxOwner).getId();

            File inboxConfigFile = new File(getInboxDataPath(), inboxUUID.toString() + ".yml");
            FileConfiguration inboxConfig = YamlConfiguration.loadConfiguration(inboxConfigFile);

            UUID inboxViewerUUID = event.getInventory().getViewers().get(0).getUniqueId();
            UUID inboxOwnerUUID = UUID.fromString(inboxConfig.getString("uuid"));

            Update.run(inboxViewerUUID, inboxOwnerUUID, chestInventory);

            if (PostalInit.inboxViewerBusy.contains(inboxOwnerUUID))
                PostalInit.inboxViewerBusy.remove(inboxOwnerUUID);

            if (PostalInit.inboxOwnerBusy.contains(inboxOwnerUUID)) {
                if (inboxChanged.containsKey(inboxUUID)) inboxChanged.remove(inboxUUID);
                PostalInit.inboxOwnerBusy.remove(inboxOwnerUUID);
            }

        }

    }

}