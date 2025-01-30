// File: com/bigdad/factionsoffaith/data/FaithManager.java
package com.bigdad.factionsoffaith.data;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FaithManager {
    private static final Map<String, FaithData> FAITHS = new HashMap<>();
    private static final Map<ServerPlayer, String> PLAYER_FAITHS = new HashMap<>();
    private static final Map<Villager, String> VILLAGER_FAITHS = new HashMap<>();
    private static final Map<Villager, Integer> CONVERSION_ATTEMPTS = new HashMap<>();
    private static final int MAX_ATTEMPTS = 3;
    private static final Random RANDOM = new Random();

    public static boolean createFaith(ServerPlayer player, String name, String description) {
        if (FAITHS.containsKey(name)) return false;
        FAITHS.put(name, new FaithData(description, player, 0));
        PLAYER_FAITHS.put(player, name);
        return true;
    }
    public static boolean disbandFaith(ServerPlayer player, String name) {
        if (!FAITHS.containsKey(name)) return false;
        FaithData faith = FAITHS.get(name);
        if (!faith.getCreator().equals(player)) return false;
        FAITHS.remove(name);
        PLAYER_FAITHS.entrySet().removeIf(entry -> entry.getValue().equals(name));
        VILLAGER_FAITHS.entrySet().removeIf(entry -> entry.getValue().equals(name));
        return true;
    }

    public static Integer getVillagerCount(String faith) {
        return (int) VILLAGER_FAITHS.values().stream().filter(f -> f.equals(faith)).count();
    }
    public static Map<String, FaithData> getFaiths() {
        return FAITHS;
    }
    public static String getFaith(ServerPlayer player) {
        return PLAYER_FAITHS.getOrDefault(player, "none");
    }
    public static String getFaith(Villager villager) {
        return VILLAGER_FAITHS.getOrDefault(villager, "none");
    }

    public static boolean joinFaith(ServerPlayer player, String faith) {
        if (!FAITHS.containsKey(faith)) return false;
        if (PLAYER_FAITHS.containsKey(player)) return false;
        PLAYER_FAITHS.put(player, faith);
        return true;
    }

    public static boolean convertVillager(Villager villager, String faith, ServerPlayer player) {
        if (!FAITHS.containsKey(faith)) {
            player.sendSystemMessage(Component.literal("That faith does not exist!").withStyle(ChatFormatting.DARK_RED));
            return false;
        } else if (getFaith(villager).equals("none")) {
            player.sendSystemMessage(Component.literal("The villager has converted to " + faith + "!").withStyle(ChatFormatting.GREEN));
            VILLAGER_FAITHS.put(villager, faith);
            return true;
        }

        String currentFaith = getFaith(villager);
        if (currentFaith.equals(faith)) {
            player.sendSystemMessage(Component.literal("This villager is already part of " + faith + "!").withStyle(ChatFormatting.DARK_RED));
            return false;
        }

        int attempts = CONVERSION_ATTEMPTS.getOrDefault(villager, 0);
        if (attempts >= MAX_ATTEMPTS) {
            player.sendSystemMessage(Component.literal("This villager is too devoted to be converted and should be eliminated").withStyle(ChatFormatting.RED));
            return false;
        }
        boolean success = RANDOM.nextBoolean();
        if (success) {
            VILLAGER_FAITHS.put(villager, faith);
            player.sendSystemMessage(Component.literal("The villager has converted to " + faith + "!").withStyle(ChatFormatting.GREEN));
            CONVERSION_ATTEMPTS.remove(villager); // Reset attempts on success
            return true;
        } else {
            attempts++;
            CONVERSION_ATTEMPTS.put(villager, attempts);
            player.sendSystemMessage(Component.literal("The villager resisted conversion! (" + attempts + "/3)").withStyle(ChatFormatting.YELLOW));
            return false;
        }
    }
}