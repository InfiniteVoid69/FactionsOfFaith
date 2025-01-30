// File: com/bigdad/factionsoffaith/events/FaithInteractionHandler.java
package com.bigdad.factionsoffaith.events;

import com.bigdad.factionsoffaith.data.FaithManager;
import com.bigdad.factionsoffaith.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class FaithInteractionHandler {
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        ItemStack heldItem = player.getMainHandItem();

        if (heldItem.getItem() == ModItems.WrittenBookOfFaith.get()) {
            String faithName = heldItem.getHoverName().getString();

            if (target instanceof ServerPlayer targetPlayer) {
                if (!FaithManager.joinFaith(targetPlayer, faithName)) {
                    player.sendSystemMessage(Component.literal("They are already part of a faith!"));
                } else {
                    player.sendSystemMessage(Component.literal(targetPlayer.getName().getString() + " has joined " + faithName + "!"));
                }
            } else if (target instanceof Villager villager) {
                int faithDedication = villager.getPersistentData().getInt("faithdedication");

                if (faithDedication >= 5) {
                    player.sendSystemMessage(Component.literal("This villager is too dedicated to their faith and should be eliminated"));
                } else {
                    int roll = RANDOM.nextInt(6);
                    if (roll > faithDedication) {
                        villager.getPersistentData().putString("faith", faithName);
                        player.sendSystemMessage(Component.literal("The villager has converted to " + faithName + "!"));
                    } else {
                        player.sendSystemMessage(Component.literal("The villager resisted conversion!"));
                    }
                }
            }
        }
    }
}