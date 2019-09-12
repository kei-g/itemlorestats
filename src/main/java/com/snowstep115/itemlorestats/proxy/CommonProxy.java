package com.snowstep115.itemlorestats.proxy;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.lore.Stats;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

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
        Entity source = event.getSource().getTrueSource();
        IlsMod.info("damageevent: %s -> %f", source == null ? "null" : source.getName(), event.getAmount());
        if (source instanceof EntityLivingBase) {
            EntityLivingBase source1 = (EntityLivingBase) source;
            Stats stats = new Stats(source1);
            if (source instanceof EntityPlayer) {
                EntityPlayer source2 = (EntityPlayer) source;
                if (living instanceof EntityPlayer) {
                    EntityPlayer living2 = (EntityPlayer) living;
                    stats.apply(source2, living2, event);
                } else
                    stats.apply(source2, living, event);
            } else if (living instanceof EntityPlayer) {
                EntityPlayer living1 = (EntityPlayer) living;
                stats.apply(source1, living1, event);
            } else
                stats.apply(source1, living, event);
        } else {
            Stats stats = new Stats(living);
            if (living instanceof EntityPlayer) {
                EntityPlayer living1 = (EntityPlayer) living;
                stats.apply(source, living1, event);
            } else
                stats.apply(source, living, event);
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
    public static void livingEquipmentChangeEvent(final LivingEquipmentChangeEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote)
            return;
        IlsMod.info("equipmentchangeevent: %s %s -> %s", living.getName(), event.getFrom(), event.getTo());
        Stats stats = new Stats(living);
        AbstractAttributeMap attr = living.getAttributeMap();
        Multimap<String, AttributeModifier> modifiers = ArrayListMultimap.create();
        modifiers.put("generic.movementSpeed",
                new AttributeModifier(UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635"), "generic.movementSpeed",
                        stats.speed / 100, 2));
        attr.applyAttributeModifiers(modifiers);
    }

    @SubscribeEvent
    public static void livingHealEvent(final LivingHealEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote)
            return;
        Stats stats = new Stats(living);
        double heal = event.getAmount() * living.getMaxHealth() / stats.health;
        event.setAmount((float) heal);
    }

    @SubscribeEvent
    public static void livingHurtEvent(final LivingHurtEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living.world.isRemote)
            return;
        IlsMod.info("hurtevent: %s %f", living.getName(), event.getAmount());
        Stats stats = new Stats(living);
        double damage = event.getAmount() * living.getMaxHealth() / stats.health;
        event.setAmount((float) damage);
    }

    @SubscribeEvent
    public static void playerPickupXpEvent(final PlayerPickupXpEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player == null || player.world.isRemote)
            return;
        EntityXPOrb orb = event.getOrb();
        if (orb == null)
            return;
        Stats stats = new Stats(player);
        double xp = orb.xpValue;
        xp += xp * stats.xpBonus / 100;
        orb.xpValue = (int) xp;
    }

    public static final ConcurrentHashMap<UUID, Integer> TICKS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void playerTickEvent(final PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player == null || player.world.isRemote)
            return;
        int tick = TICKS.compute(player.getUniqueID(), ($, t) -> t == null ? 1 : t + 1);
        if (tick % 20 == 0) {
            Stats stats = new Stats(player);
            double amount = stats.regeneration * player.getMaxHealth() / 100;
            player.heal((float) amount);
        }
    }
}
