// File: com/bigdad/factionsoffaith/commands/CreateFaithCommand.java
package com.bigdad.factionsoffaith.commands.custom;

import com.bigdad.factionsoffaith.data.FaithManager;
import com.bigdad.factionsoffaith.item.ModItems;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class CreateFaithCommand {
    public CreateFaithCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("createfaith")
                .then(Commands.argument("faithname", StringArgumentType.string())
                        .then(Commands.argument("description", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    String name = StringArgumentType.getString(context, "faithname");
                                    String description = StringArgumentType.getString(context, "description");

                                    if (!FaithManager.createFaith(player, name, description)) {
                                        player.sendSystemMessage(Component.literal("That faith already exists!").withStyle(ChatFormatting.DARK_RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    ItemStack book = new ItemStack(ModItems.WrittenBookOfFaith.get());
                                    book.setHoverName(Component.literal(name));
                                    book.getOrCreateTag().putString("faithDescription", description);

                                    player.addItem(book);
                                    player.sendSystemMessage(Component.literal("Faith " + name + " created!").withStyle(ChatFormatting.GREEN));
                                    return Command.SINGLE_SUCCESS;
                                }))));
    }
}