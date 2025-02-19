package com.bigdad.factionsoffaith.item;

import com.bigdad.factionsoffaith.data.FaithDataHandler;
import com.bigdad.factionsoffaith.events.ServerMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import static com.bigdad.factionsoffaith.data.FaithDataHandler.getData;
import static com.bigdad.factionsoffaith.data.FaithManager.disbandFaith;

public class ItemDataHandler {
    public static boolean doesItemExist(ServerLevel world, String itemName) {
        for (Entity entity : world.getAllEntities()) {
            if (entity instanceof ItemEntity itemEntity) {
                Component customName = itemEntity.getItem().getHoverName();
                if (customName.getString().equalsIgnoreCase(itemName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void deleteDataIfItemGone(ServerLevel world, String FAITH) {
        MinecraftServer server = world.getLevel().getServer();
        if (!doesItemExist(world, FAITH)) {
            FaithDataHandler data = getData(world);
            ServerMessageHelper.announceToServer(server, "§b[Announcement]§r " + FAITH + "Has Been Purged of Its Existence");
            disbandFaith(data.getFaith(FAITH).getName(), server.getPlayerList().getPlayer(data.getFaith(FAITH).getCreator()), world);
        }
    }
}
