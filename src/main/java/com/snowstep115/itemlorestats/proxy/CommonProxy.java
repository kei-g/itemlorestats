package com.snowstep115.itemlorestats.proxy;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.lore.Lore;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public abstract class CommonProxy {
    public void preInit(final FMLPreInitializationEvent event) {
    }

    public void init(final FMLInitializationEvent event) {
    }

    public void postInit(final FMLPostInitializationEvent event) {
    }

    @SubscribeEvent
    public static void livingDamageEvent(final LivingDamageEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote)
            return;
        Entity source = event.getSource().getImmediateSource();
        IlsMod.info("damageevent: %s -> %s %f", source == null ? "null" : source.getName(),
                living == null ? "null" : living.getName(), event.getAmount());
        if (source instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source;
            ItemStack sword = player.getHeldItemMainhand();
            Lore.deserialize(sword, lore -> lore.applyTo(event));
            IlsMod.info(source, "§dYou hit a §f%s §dfor §6%.2f §ddamage.§r", living.getName(), event.getAmount());
        } else if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            for (ItemStack itemstack : player.getEquipmentAndArmor()) {
                Item item = itemstack.getItem();
                if (item instanceof ItemArmor)
                    Lore.deserialize(itemstack, lore -> lore.applyTo(event));
            }
            if (source != null)
                IlsMod.info(living, "%s §dhit you for §6%.2f §ddamage.§r", source.getName(), event.getAmount());
        }
    }

    @SubscribeEvent
    public static void livingDropsEvent(final LivingDropsEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote)
            return;
        IlsMod.info("dropsevent: %s, looting level: %d", living.getName(), event.getLootingLevel());
        for (EntityItem entity : event.getDrops())
            IlsMod.info("  -> %s", entity.getItem());
    }

    @SubscribeEvent
    public static void livingHurtEvent(final LivingHurtEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote)
            return;
        IlsMod.info("hurtevent: %s %f", living == null ? "null" : living.getName(), event.getAmount());
    }
}
