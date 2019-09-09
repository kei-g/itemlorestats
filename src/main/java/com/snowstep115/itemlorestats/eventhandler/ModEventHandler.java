package com.snowstep115.itemlorestats.eventhandler;

import com.snowstep115.itemlorestats.item.LoreArmor;
import com.snowstep115.itemlorestats.item.LoreSword;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(bus = Bus.MOD)
public final class ModEventHandler {
    @SubscribeEvent
    public static void registerItems(final Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        LoreArmor.getAll(registry::register);
        LoreSword.getAll(registry::register);
    }
}
