package com.bigdad.factionsoffaith.item;

import com.bigdad.factionsoffaith.FactionsOfFaith;
import com.bigdad.factionsoffaith.item.custom.BookOfFaith;
import com.bigdad.factionsoffaith.item.custom.WrittenBookOfFaith;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FactionsOfFaith.MOD_ID);

    public static final RegistryObject<Item> WrittenBookOfFaith = ITEMS.register("written_bookoffaith",
            () -> new WrittenBookOfFaith(new Item.Properties()
                    .tab(ModCreativeModeTab.FACTIONSOFFAITHTAB)
                    .stacksTo(1)
                    .rarity(Rarity.EPIC)
            ));

    public static final RegistryObject<Item> BookOfFaith = ITEMS.register("bookoffaith",
            () -> new BookOfFaith(new Item.Properties()
                    .tab(ModCreativeModeTab.FACTIONSOFFAITHTAB)
                    .stacksTo(1)
            ));

    public static void register(IEventBus eventbus) {
        ITEMS.register(eventbus);
    }
}
