package com.snowstep115.itemlorestats.proxy;

import java.util.List;

import com.snowstep115.itemlorestats.lore.Lore;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public final class ClientProxy extends CommonProxy {
    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(final FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(final FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @SubscribeEvent
    public static void itemTooltipEvent(final ItemTooltipEvent event) {
        ItemStack itemstack = event.getItemStack();
        List<String> tooltip = event.getToolTip();
        Lore.deserialize(itemstack, lore -> tooltip.add(lore.getFormattedString()));
    }
}
