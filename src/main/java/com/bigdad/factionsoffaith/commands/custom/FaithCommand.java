// File: com/bigdad/factionsoffaith/commands/CreateFaithCommand.java
package com.bigdad.factionsoffaith.commands.custom;

import com.bigdad.factionsoffaith.data.FaithData;
import com.bigdad.factionsoffaith.data.FaithDataHandler;
import com.bigdad.factionsoffaith.data.FaithManager;
import com.bigdad.factionsoffaith.events.ServerMessageHelper;
import com.bigdad.factionsoffaith.item.ModItems;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class FaithCommand {
    public FaithCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("faith")
                .then(Commands.literal("create")
                .then(Commands.argument("faithname", StringArgumentType.string())
                        .then(Commands.argument("description", StringArgumentType.string())
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayerOrException();
                                    ServerLevel world = player.getLevel();
                                    String name = StringArgumentType.getString(context, "faithname");
                                    String description = StringArgumentType.getString(context, "description");

                                    if (!FaithManager.createFaith(name, description, player, world)) {
                                        player.sendSystemMessage(Component.literal("That faith already exists!").withStyle(ChatFormatting.DARK_RED));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    ItemStack book = new ItemStack(ModItems.WrittenBookOfFaith.get());
                                    book.setHoverName(Component.literal(name));
                                    book.getOrCreateTag().putString("faithDescription", description);

                                    player.addItem(book);
                                    player.sendSystemMessage(Component.literal("Faith " + name + " created!").withStyle(ChatFormatting.GREEN));
                                    return Command.SINGLE_SUCCESS;
                                }))))
                .then(Commands.literal("delete")
                    .then(Commands.argument("faithname", StringArgumentType.string())
                            .executes(context -> {
                                ServerPlayer player = context.getSource().getPlayerOrException();
                                ServerLevel world = player.getLevel();
                                String name = StringArgumentType.getString(context, "faithname");

                                if (FaithManager.disbandFaith(name, player, world )) {
                                    player.sendSystemMessage(Component.literal("Faith " + name + " has been lost to time!"));
                                } else {
                                    player.sendSystemMessage(Component.literal("You do not have permission to disband this faith!").withStyle(ChatFormatting.DARK_RED));
                                }
                                return Command.SINGLE_SUCCESS;
                            })))
                .then(Commands.literal("list")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            ServerPlayer player = source.getPlayerOrException();
                            MinecraftServer server = source.getLevel().getServer();
                            Map<String, FaithData> FAITHS = FaithDataHandler.getFaiths();

                            if (FAITHS.isEmpty()) {
                                player.sendSystemMessage(Component.literal("No faiths have been created yet.").withStyle(ChatFormatting.RED));
                            } else {
                                player.sendSystemMessage(Component.literal("Existing Faiths:").withStyle(ChatFormatting.GOLD));

                                for (FaithData FAITH : FAITHS.values()) {
                                    int followers = FaithManager.getVillagerCount(FAITH.getName(), player);
                                    player.sendSystemMessage(Component.literal("\n" + "▪ " + FAITH.getName() + "\n" + " | Description: " + FAITH.getDescription() + "\n" + " | Faith Level: " + FAITH.getLevel() + "\n" + " | Number Of Followers: " + followers + "\n" + " | Creater: " + server.getPlayerList().getPlayer(FAITH.getCreator()).getName().getString())
                                            .withStyle(ChatFormatting.YELLOW));
                                }
                            }
                            return 1;
                        }))
                .then(Commands.literal("test")
                    .executes( context -> {
                        CommandSourceStack source = context.getSource();
                        ServerPlayer player = source.getPlayerOrException();
                        int followers = FaithManager.getVillagerCount("test1", player);
                        System.out.println("test" + followers);
                        return Command.SINGLE_SUCCESS;
                    }))
                .then(Commands.literal("announce")
                    .then(Commands.argument("message", net.minecraft.commands.arguments.MessageArgument.message())
                            .executes(context -> {
                                MinecraftServer server = context.getSource().getServer();
                                String message = net.minecraft.commands.arguments.MessageArgument.getMessage(context, "message").getString();
                                ServerMessageHelper.announceToServer(server, "§b[Announcement]§r " + message);
                                return Command.SINGLE_SUCCESS;
                            })))
        );
    }
}