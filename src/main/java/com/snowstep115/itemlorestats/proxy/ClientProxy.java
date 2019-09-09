package com.snowstep115.itemlorestats.proxy;

import com.snowstep115.itemlorestats.item.LoreArmor;
import com.snowstep115.itemlorestats.item.LoreSword;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
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
    public static void registerModelsEvent(final ModelRegistryEvent event) {
        LoreArmor.getAll(armor -> ModelLoader.setCustomModelResourceLocation(armor, 0,
                new ModelResourceLocation(armor.getRegistryName(), null)));
        LoreSword.getAll(sword -> ModelLoader.setCustomModelResourceLocation(sword, 0,
                new ModelResourceLocation(sword.getRegistryName(), null)));
    }
}
