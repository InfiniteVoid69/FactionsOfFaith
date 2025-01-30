// File: com/bigdad/factionsoffaith/commands/ModCommands.java
package com.bigdad.factionsoffaith.commands;

import com.bigdad.factionsoffaith.FactionsOfFaith;
import com.bigdad.factionsoffaith.commands.custom.CreateFaithCommand;
import com.bigdad.factionsoffaith.commands.custom.DisbandFaithCommand;
import com.bigdad.factionsoffaith.commands.custom.ListFaithsCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = FactionsOfFaith.MOD_ID)
public class ModCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {

        new CreateFaithCommand(event.getDispatcher());
        new DisbandFaithCommand(event.getDispatcher());
        new ListFaithsCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}