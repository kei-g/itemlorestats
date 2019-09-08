package com.snowstep115.itemlorestats.eventhandler;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistry;

@EventBusSubscriber(bus = Bus.MOD)
public final class ModEventHandler {
    public static ForgeRegistry<Item> ITEM_REGISTRY;

    @SubscribeEvent
    public static void registerItems(final Register<Item> event) {
        ITEM_REGISTRY = (ForgeRegistry<Item>) event.getRegistry();
    }
}
