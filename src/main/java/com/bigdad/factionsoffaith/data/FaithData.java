package com.bigdad.factionsoffaith.data;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class FaithData {
    private final String name;
    private String description;
    private int level;
    private final UUID creatorUUID;

    public FaithData(String name, String description, int level, UUID creatorUUID) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.creatorUUID = creatorUUID;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getLevel() { return level; }
    public UUID getCreator() { return creatorUUID; }

    public void setDescription(String description) { this.description = description; }
    public void setLevel(int level) { this.level = level; }


    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putString("description", description);
        tag.putInt("level", level);
        tag.putUUID("creatorUUID", creatorUUID);
        return tag;
    }

    public static FaithData loadFromNBT(CompoundTag tag) {
        return new FaithData(
                tag.getString("name"),
                tag.getString("description"),
                tag.getInt("level"),
                tag.getUUID("creatorUUID")
        );
    }
}
