package com.snowstep115.itemlorestats.proxy;

import com.snowstep115.itemlorestats.item.LoreArmor;
import com.snowstep115.itemlorestats.item.LoreSword;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public abstract class CommonProxy {
    public void preInit(final FMLPreInitializationEvent event) {
    }

    public void init(final FMLInitializationEvent event) {
    }

    public void postInit(final FMLPostInitializationEvent event) {
    }

    @SubscribeEvent
    public static void registerItemsEvent(final RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        LoreArmor.getAll(registry::register);
        LoreSword.getAll(registry::register);
    }
}
