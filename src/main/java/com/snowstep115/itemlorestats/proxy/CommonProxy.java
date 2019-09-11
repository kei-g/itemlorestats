package com.snowstep115.itemlorestats.proxy;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.config.IlsConfig;
import com.snowstep115.itemlorestats.lore.Lore;
import com.snowstep115.itemlorestats.lore.Stats;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
        Entity source = event.getSource().getImmediateSource();
        IlsMod.info("damageevent: %s -> %s %f", source == null ? "null" : source.getName(),
                living == null ? "null" : living.getName(), event.getAmount());
        if (source instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source;
            Stats stats = new Stats(player);
            Lore.deserialize(player, lore -> lore.applyTo(stats));
            double damage = stats.damage;
            if (stats.critical.get())
                damage = damage * stats.criticalDamage / 100;
            event.setAmount((float) damage);
            if (stats.critical.get())
                IlsMod.info(source, "§dYou crit hit a §f%s §dfor §6%.2f §ddamage.§r", living.getName(),
                        event.getAmount());
            else
                IlsMod.info(source, "§dYou hit a §f%s §dfor §6%.2f §ddamage.§r", living.getName(), event.getAmount());
            if (stats.lifeStolen.get()) {
                double stolen = damage * IlsConfig.lifeSteal / 100;
                IlsMod.info(source, "§dYou stole §6%.2f §dhealth.§r", stolen);
                double health = stats.health * player.getHealth() / player.getMaxHealth();
                health += stolen;
                if (stats.health <= health)
                    health = stats.health;
                health = health * player.getMaxHealth() / stats.health;
                player.setHealth((float) health);
            }
            if (stats.ignited.get()) {
                living.setFire(3);
            }
        } else if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            Stats stats = new Stats(player);
            Lore.deserialize(player, lore -> lore.applyTo(stats));
            double damage = event.getAmount() - event.getAmount() * stats.reduction / 100;
            if (damage < 0)
                damage = 0;
            if (stats.dodged.get())
                damage = 0;
            if (stats.blocked.get())
                damage = 0;
            if (source != null) {
                if (stats.dodged.get())
                    IlsMod.info(living, "%s §dhit you, but you dodged.§r", source.getName());
                else if (stats.blocked.get()) {
                    Potion potion = Potion.getPotionById(2);
                    player.addPotionEffect(new PotionEffect(potion, 30, 6, true, true));
                    IlsMod.info(living, "%s §dhit you, but you blocked.§r", source.getName());
                } else
                    IlsMod.info(living, "%s §dhit you for §6%.2f §ddamage.§r", source.getName(), damage);
            }
            damage = damage * player.getMaxHealth() / stats.health;
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
            Stats stats = new Stats(player);
            Lore.deserialize(player, lore -> lore.applyTo(stats));
            double heal = event.getAmount() * player.getMaxHealth() / stats.health;
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

    public static final ConcurrentHashMap<UUID, Integer> TICKS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void playerTickEvent(final PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player == null || player.world.isRemote)
            return;
        int tick = TICKS.compute(player.getUniqueID(), ($, t) -> t == null ? 1 : t + 1);
        if (tick % 20 == 0) {
            Stats stats = new Stats(player);
            Lore.deserialize(player, lore -> lore.applyTo(stats));
            double health = player.getHealth() + stats.regeneration * player.getMaxHealth() / 100;
            if (health < 0)
                health = 0;
            if (player.getMaxHealth() < health)
                health = player.getMaxHealth();
            player.setHealth((float) health);
        }
    }
}
