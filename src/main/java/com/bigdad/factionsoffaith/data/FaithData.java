// File: com/bigdad/factionsoffaith/data/FaithData.java
package com.bigdad.factionsoffaith.data;

import net.minecraft.server.level.ServerPlayer;

public class FaithData {
    private final String description;
    private final ServerPlayer creator;
    private final Integer level;

    public FaithData(String description, ServerPlayer creator, Integer level) {
        this.description = description;
        this.creator = creator;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public ServerPlayer getCreator() {
        return creator;
    }

    public Integer getLevel() {
        return level;
    }
}