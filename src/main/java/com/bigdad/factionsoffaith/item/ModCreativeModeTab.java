package com.bigdad.factionsoffaith.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab FACTIONSOFFAITHTAB = new CreativeModeTab("factionsoffaithtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.WrittenBookOfFaith.get());
        }
    };
}
