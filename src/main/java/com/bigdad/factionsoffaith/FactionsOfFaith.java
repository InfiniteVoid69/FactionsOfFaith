package com.bigdad.factionsoffaith;

import com.bigdad.factionsoffaith.data.FaithData;
import com.bigdad.factionsoffaith.data.FaithDataHandler;
import com.bigdad.factionsoffaith.item.ItemDataHandler;
import com.bigdad.factionsoffaith.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.ConfigCommand;
import org.slf4j.Logger;

import java.util.Map;

import static com.bigdad.factionsoffaith.data.FaithDataHandler.getData;
import static com.bigdad.factionsoffaith.item.ItemDataHandler.deleteDataIfItemGone;


@Mod(FactionsOfFaith.MOD_ID)
public class FactionsOfFaith {
    public static final String MOD_ID = "factionsoffaith";
    private static final Logger LOGGER = LogUtils.getLogger();


    public FactionsOfFaith(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }


    @Mod.EventBusSubscriber
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    @Mod.EventBusSubscriber
    public class ServerEvents {

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            MinecraftServer server = event.getServer();
            ServerLevel world = server.getLevel(ServerLevel.OVERWORLD);
            if (world != null) {
                FaithDataHandler dataHandler = FaithDataHandler.getData(world);
                LOGGER.info("Faith Data Loaded on Server Start. {} faith(s) found.", FaithDataHandler.getFaiths().size());
            }
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                ServerLevel world = event.getServer().overworld();
                for (String name : FaithDataHandler.getFaiths().keySet()) {
                    deleteDataIfItemGone(world, name);
                }
            }
        }
    }
}
