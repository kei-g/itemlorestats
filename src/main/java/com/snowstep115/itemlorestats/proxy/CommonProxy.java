package com.snowstep115.itemlorestats.proxy;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.lore.Lore;
import com.snowstep115.itemlorestats.lore.Stats;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
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
            Stats stats = new Stats();
            Lore.deserialize(sword, lore -> lore.applyTo(stats));
            event.setAmount((float) stats.damage);
            IlsMod.info(source, "§dYou hit a §f%s §dfor §6%.2f §ddamage.§r", living.getName(), event.getAmount());
        } else if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            Stats stats = new Stats();
            for (ItemStack itemstack : player.getEquipmentAndArmor())
                Lore.deserialize(itemstack, lore -> lore.applyTo(stats));
            double damage = event.getAmount() - event.getAmount() * stats.reduction / 100;
            if (damage < 0)
                damage = 0;
            if (stats.dodged.get())
                damage = 0;
            if (source != null) {
                if (stats.dodged.get())
                    IlsMod.info(living, "%s §dhit you, but you dodged.§r", source.getName());
                else
                    IlsMod.info(living, "%s §dhit you for §6%.2f §ddamage.§r", source.getName(), damage);
            }
            damage = damage * 20 / stats.health;
            event.setAmount((float) damage);
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
    public static void livingHealEvent(final LivingHealEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote)
            return;
        IlsMod.info("healevent: %s %f", living == null ? "null" : living.getName(), event.getAmount());
        if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            Stats stats = new Stats();
            for (ItemStack equip : player.getEquipmentAndArmor())
                Lore.deserialize(equip, lore -> lore.applyTo(stats));
            double heal = event.getAmount() * 20 / stats.health;
            event.setAmount((float) heal);
        }
    }

    @SubscribeEvent
    public static void livingHurtEvent(final LivingHurtEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote)
            return;
        IlsMod.info("hurtevent: %s %f", living == null ? "null" : living.getName(), event.getAmount());
    }
}
