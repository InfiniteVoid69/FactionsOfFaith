package com.bigdad.factionsoffaith.events;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

public class ServerMessageHelper {

    public static void announceToServer(MinecraftServer server, String message) {
        if (server != null) {
            server.getPlayerList().broadcastSystemMessage(Component.literal(message), false);
        }
    }
}
