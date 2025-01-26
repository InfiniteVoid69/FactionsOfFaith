package com.bigdad.factionsoffaith.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BookOfFaith extends Item {
    public BookOfFaith(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.literal(" "));
            pTooltipComponents.add(Component.literal("What are you looking For?").withStyle(ChatFormatting.GOLD));
        } else {
            pTooltipComponents.add(Component.literal("Used To Create A Faith").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        
        return super.useOn(pContext);
    }
}
