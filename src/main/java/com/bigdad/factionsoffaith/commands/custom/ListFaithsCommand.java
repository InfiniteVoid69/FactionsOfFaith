// File: com/bigdad/factionsoffaith/commands/ListFaithsCommand.java
package com.bigdad.factionsoffaith.commands.custom;

import com.bigdad.factionsoffaith.data.FaithData;
import com.bigdad.factionsoffaith.data.FaithManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class ListFaithsCommand {
    public ListFaithsCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("listfaiths")
                .executes(context -> {
                    CommandSourceStack source = context.getSource();
                    ServerPlayer player = source.getPlayerOrException();
                    Map<String, FaithData> faiths = FaithManager.getFaiths();

                    if (faiths.isEmpty()) {
                        player.sendSystemMessage(Component.literal("No faiths have been created yet.").withStyle(ChatFormatting.RED));
                    } else {
                        player.sendSystemMessage(Component.literal("Existing Faiths:").withStyle(ChatFormatting.GOLD));

                        for (Map.Entry<String, FaithData> entry : faiths.entrySet()) {
                            String faithName = entry.getKey();
                            FaithData data = entry.getValue();
                            String createrName = data.getCreator().getName().getString();
                            String description = data.getDescription();
                            String level = data.getLevel().toString();
                            String followers = FaithManager.getVillagerCount(faithName).toString();
                            player.sendSystemMessage(Component.literal("▪ " + faithName + " | Description: " + description + " | Faith Level: " + level + " | Number Of Followers: " + followers + " | Creater: " + createrName)
                                    .withStyle(ChatFormatting.YELLOW));
                        }
                    }
                    return 1;
                }));
    }
}