// File: com/bigdad/factionsoffaith/commands/DisbandFaithCommand.java
package com.bigdad.factionsoffaith.commands.custom;

import com.bigdad.factionsoffaith.data.FaithManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DisbandFaithCommand {
    public DisbandFaithCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("disbandfaith")
                .then(Commands.argument("faithname", StringArgumentType.string())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            String name = StringArgumentType.getString(context, "faithname");

                            if (FaithManager.disbandFaith(player, name)) {
                                player.sendSystemMessage(Component.literal("Faith " + name + " has been lost to time!"));
                            } else {
                                player.sendSystemMessage(Component.literal("You do not have permission to disband this faith!").withStyle(ChatFormatting.DARK_RED));
                            }
                            return Command.SINGLE_SUCCESS;
                        })));
    }
}