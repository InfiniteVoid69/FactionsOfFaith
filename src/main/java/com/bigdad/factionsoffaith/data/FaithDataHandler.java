package com.bigdad.factionsoffaith.data;

import com.bigdad.factionsoffaith.FactionsOfFaith;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FaithDataHandler extends SavedData {
    private static final Map<String, FaithData> FAITHS = new HashMap<>();
    public static final String Faith_Data_Name = FactionsOfFaith.MOD_ID + "_data";

    public static FaithDataHandler getData(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(
                FaithDataHandler::load,
                FaithDataHandler::new,
                Faith_Data_Name
        );
    }

    public void addFaith(String name, String description, int level, UUID creatorUUID) {
        FAITHS.put(name, new FaithData(name, description, level, creatorUUID));
        setDirty();
    }

    public void removeFaith(String name) {
        FAITHS.remove(name);
        setDirty();
    }

    public FaithData getFaith(String name) {
        return FAITHS.get(name);
    }

    public static Map<String, FaithData> getFaiths() {
        return FAITHS;
    }

    private static final Logger LOGGER = LogManager.getLogger();
    public static void logMessage() {
        LOGGER.info("This is an info message.");
        LOGGER.warn("This is a warning message.");
        LOGGER.error("This is an error message!");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();
        for (Map.Entry<String, FaithData> FAITH : FAITHS.entrySet()) {
            CompoundTag tag = FAITH.getValue().saveToNBT();
            tag.putString("name", FAITH.getKey());
            list.add(tag);
        }
        nbt.put("FAITHS", list);
        return nbt;
    }

    public static FaithDataHandler load(CompoundTag nbt) {
        FaithDataHandler data = new FaithDataHandler();
        ListTag list = nbt.getList("FAITHS", 10);
        for (int i = 0; i < list.size(); i++) {
            FaithData faith = FaithData.loadFromNBT(list.getCompound(i));
            FAITHS.put(faith.getName(), faith);
        }
        return data;
    }
}
