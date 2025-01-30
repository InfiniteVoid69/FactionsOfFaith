// File: com/bigdad/factionsoffaith/event/FaithBookEventHandler.java
package com.bigdad.factionsoffaith.events;

import com.bigdad.factionsoffaith.data.FaithManager;
import com.bigdad.factionsoffaith.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FaithBookEventHandler {
    @SubscribeEvent
    public static void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (!player.level.isClientSide) {
            return;
        }
        InteractionHand hand = event.getHand();
        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.getItem() != ModItems.WrittenBookOfFaith.get()) return;

        player.sendSystemMessage(Component.literal("The book glows with divine energy!").withStyle(ChatFormatting.AQUA));
    }

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        Entity target = event.getTarget();
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() != ModItems.WrittenBookOfFaith.get()) return;
        String faith = heldItem.getHoverName().getString();

        if (FaithManager.getFaith(serverPlayer).equals("none")) {
            player.sendSystemMessage(Component.literal(player.getName().getString() + " you must be part of a faith to convert others!").withStyle(ChatFormatting.RED));
            return;
        }

        if (target instanceof ServerPlayer targetPlayer) {
                player.sendSystemMessage(Component.literal("Debug: Player selected").withStyle(ChatFormatting.YELLOW));
            handlePlayerInteraction(faith, serverPlayer, serverPlayer);
        } else if (target instanceof Villager villager) {
                player.sendSystemMessage(Component.literal("Debug: " + serverPlayer.getName().getString() + " | Villager selected " + villager).withStyle(ChatFormatting.YELLOW));
            FaithManager.convertVillager(villager, faith, serverPlayer);
        }
    }

    private static void handlePlayerInteraction(String faith, ServerPlayer player, ServerPlayer targetPlayer) {
        if (!FaithManager.joinFaith(targetPlayer, faith)) {
            player.sendSystemMessage(Component.literal(targetPlayer.getName().getString() + " is already in a faith!").withStyle(ChatFormatting.DARK_RED));
        } else {
            player.sendSystemMessage(Component.literal(targetPlayer.getName().getString() + " has joined " + faith + "!").withStyle(ChatFormatting.GREEN));
        }
    }
}