// File: com/bigdad/factionsoffaith/data/FaithManager.java
package com.bigdad.factionsoffaith.data;

import com.bigdad.factionsoffaith.FactionsOfFaith;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.bigdad.factionsoffaith.data.FaithDataHandler.getData;

public class FaithManager {
    private static final String FAITH_TAG = FactionsOfFaith.MOD_ID + "_faith";
    private static final String FAITH_DEDICATION_TAG = FactionsOfFaith.MOD_ID + "_dedication";
    private static final Map<Villager, String> VILLAGER_FAITHS = new HashMap<>();
    private static final Map<Villager, Integer> CONVERSION_ATTEMPTS = new HashMap<>();
    private static final int MAX_ATTEMPTS = 3;
    private static final Random RANDOM = new Random();

    public static boolean createFaith(String name, String description, ServerPlayer player, ServerLevel world) {
        FaithDataHandler data = getData(world);
        if (data.getFaith(name) != null) return false;
        data.addFaith(name, description, 1, player.getUUID());
        player.getPersistentData().putString(FAITH_TAG, name);
        return true;
    }

    public static boolean disbandFaith(String name, ServerPlayer player, ServerLevel world) {
        FaithDataHandler data = getData(world);
        if (data.getFaith(name) == null) return false;
        if (!data.getFaith(name).getCreator().equals(player.getUUID())) return false;
        data.removeFaith(name);
        player.getPersistentData().remove(FAITH_TAG);
        for (Entity entity : world.getAllEntities()) {
            if (entity instanceof Villager villager) {
                CompoundTag tag = villager.getPersistentData();
                if (tag.getString(FAITH_TAG).equals(name)) {
                    tag.remove(FAITH_TAG);
                    tag.remove(FAITH_DEDICATION_TAG);
                }
            }
        }
        VILLAGER_FAITHS.entrySet().removeIf(entry -> entry.getValue().equals(name));
        return true;
    }

    public static Integer getVillagerCount(String FAITH, ServerPlayer player) {
        ServerLevel world = player.getLevel();
        int count = 0;
        for (Entity entity : world.getAllEntities()) {
            if (entity instanceof Villager villager) {
                CompoundTag tag = villager.getPersistentData();
                if (tag.contains(FAITH_TAG) && FAITH.equals(tag.getString(FAITH_TAG))) {
                    count++;
                }
            }
        }
        return count;
    }
    public static String getFaith(ServerPlayer player) {
        return player.getPersistentData().getString(FAITH_TAG);
    }
    public static String getFaith(Villager villager) {
        return villager.getPersistentData().getString(FAITH_TAG);
    }

    public static boolean joinFaith(ServerPlayer player, String faith) {
        // Check if faith exists
        if (player.getPersistentData().contains("none")) return false;
        player.getPersistentData().putString(FAITH_TAG, faith);
        return true;
    }

    public static void convertVillager(Villager villager, String FAITH, ServerPlayer player) {
        CompoundTag tag = villager.getPersistentData();
        ServerLevel world = player.getLevel();
        FaithDataHandler data = getData(world);
        player.sendSystemMessage(Component.nullToEmpty("test " + data.getFaith(FAITH)));
        if (data.getFaith(FAITH) == null) {
            player.sendSystemMessage(Component.literal("That faith does not exist!").withStyle(ChatFormatting.DARK_RED));
            return;
        } else if (!tag.contains(FAITH_TAG)) {
            player.sendSystemMessage(Component.literal("The villager has converted to " + FAITH).withStyle(ChatFormatting.GREEN));
            tag.putString(FAITH_TAG, FAITH);
            tag.putString(FAITH_DEDICATION_TAG, "test");
            return;
        }

        if (getFaith(villager).equals(FAITH)) {
            player.sendSystemMessage(Component.literal("This villager is already part of " + FAITH + "!").withStyle(ChatFormatting.DARK_RED));
            return;
        }

        int dedication = tag.getInt(FAITH_DEDICATION_TAG);
        if (dedication == 5) {
            player.sendSystemMessage(Component.literal("This villager is too devoted to be converted and should be eliminated").withStyle(ChatFormatting.RED));
            return;
        }

        int attempts = CONVERSION_ATTEMPTS.getOrDefault(villager, 0);
        if (attempts >= MAX_ATTEMPTS) {
            player.sendSystemMessage(Component.literal("This villager is too devoted to be converted and should be eliminated").withStyle(ChatFormatting.RED));
            return;
        }

        int randomNum = RANDOM.nextInt(6);
        if (dedication >= randomNum) {
            attempts++;
            CONVERSION_ATTEMPTS.put(villager, attempts);
            player.sendSystemMessage(Component.literal("The villager resisted conversion! (" + attempts + "/" + MAX_ATTEMPTS + ")").withStyle(ChatFormatting.DARK_RED));
            return;
        }

        tag.putString(FAITH_TAG, FAITH);
        player.sendSystemMessage(Component.literal("The villager has converted to " + FAITH).withStyle(ChatFormatting.GREEN));
        CONVERSION_ATTEMPTS.remove(villager);
    }
}