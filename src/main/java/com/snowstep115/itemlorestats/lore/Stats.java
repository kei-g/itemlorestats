package com.snowstep115.itemlorestats.lore;

import java.util.HashMap;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.config.IlsConfig;
import com.snowstep115.itemlorestats.util.Probabilistic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class Stats {
    public static Stats strictStats(EntityLivingBase living) {
        return new Stats(living, new Stats(living));
    }

    public final Probabilistic blind = new Probabilistic();
    public final Probabilistic block = new Probabilistic();
    public final Probabilistic critical = new Probabilistic();
    public double criticalDamage = IlsConfig.baseCriticalDamage;
    public double damage;
    public double damageMax;
    public double damageMin;
    public double damagePvE;
    public double damagePvP;
    public final Probabilistic dodge = new Probabilistic();
    public final Probabilistic harming = new Probabilistic();
    public double health;
    public final Probabilistic ignition = new Probabilistic();
    public final HashMap<ItemStack, Integer> level = new HashMap<>();
    public final Probabilistic lifeSteal = new Probabilistic();
    public final Probabilistic poison = new Probabilistic();
    public double reduction;
    public final Probabilistic reflection = new Probabilistic();
    public double regeneration;
    public final Probabilistic slowness = new Probabilistic();
    public final HashMap<ItemStack, String> soulbound = new HashMap<>();
    public double speed;
    public final Probabilistic wither = new Probabilistic();
    public double xpBonus;

    public Stats(EntityLivingBase living) {
        this.health = living.getMaxHealth();
        Lore.deserialize(living, lore -> lore.applyTo(this));
    }

    public Stats(EntityLivingBase living, Stats stats) {
        this.health = living.getMaxHealth();
        if (living instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) living;
            for (ItemStack equip : living.getEquipmentAndArmor())
                if (stats.level.containsKey(equip) && player.experienceLevel < stats.level.get(equip))
                    IlsMod.info(player, "§d%s requires %d level§r", equip.getDisplayName(), stats.level.get(equip));
                else if (stats.soulbound.containsKey(equip) && !player.getName().equals(stats.soulbound.get(equip)))
                    IlsMod.info(player, "§d%s is bound to §r%s", equip.getDisplayName(), stats.soulbound.get(equip));
                else
                    Lore.deserialize(living, equip, lore -> lore.applyTo(this));
        } else
            for (ItemStack equip : living.getEquipmentAndArmor())
                if (stats.soulbound.containsKey(equip) && !living.getName().equals(stats.soulbound.get(equip)))
                    ;
                else
                    Lore.deserialize(living, equip, lore -> lore.applyTo(this));
    }

    public void apply(Entity source, EntityLivingBase living, LivingDamageEvent event) {
        double damage = event.getAmount() - event.getAmount() * this.reduction / 100;
        if (damage < 0)
            damage = 0;
        event.setAmount((float) damage);
    }

    public void apply(Entity source, EntityPlayer living, LivingDamageEvent event) {
        double damage = event.getAmount() - event.getAmount() * this.reduction / 100;
        if (damage < 0)
            damage = 0;
        event.setAmount((float) damage);
    }

    public void apply(EntityLivingBase source, EntityLivingBase living, LivingDamageEvent event) {
    }

    public void apply(EntityPlayer source, EntityLivingBase living, LivingDamageEvent event) {
        Stats stats = new Stats(living);
        double damage = event.getAmount() + this.damage + this.damagePvE;
        if (this.critical.occurred())
            damage *= this.criticalDamage / 100;
        damage -= damage * stats.reduction / 100;
        if (damage < 0)
            damage = 0;
        if (stats.reflection.occurred()) {
            if (this.critical.occurred())
                IlsMod.combatlog(source, "message.itemlorestats.pve.crit_hit_reflected", living.getName());
            else
                IlsMod.combatlog(source, "message.itemlorestats.pve.hit_reflected", living.getName());
            double health = source.getHealth();
            health -= damage;
            if (health < 0)
                health = 0;
            source.setHealth((float) health);
            damage = 0;
        } else if (stats.dodge.occurred()) {
            if (this.critical.occurred())
                IlsMod.combatlog(source, "message.itemlorestats.pve.crit_hit_dodged", living.getName());
            else
                IlsMod.combatlog(source, "message.itemlorestats.pve.hit_dodged", living.getName());
            damage = 0;
        } else if (stats.block.occurred()) {
            if (this.critical.occurred())
                IlsMod.combatlog(source, "message.itemlorestats.pve.crit_hit_blocked", living.getName());
            else
                IlsMod.combatlog(source, "message.itemlorestats.pve.hit_blocked", living.getName());
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, IlsConfig.blockSlowLevel, true, true));
            damage = 0;
        } else {
            if (this.critical.occurred())
                IlsMod.combatlog(source, "message.itemlorestats.pve.crit_hit", living.getName(), damage);
            else
                IlsMod.combatlog(source, "message.itemlorestats.pve.hit", living.getName(), damage);
            if (this.lifeSteal.occurred()) {
                double stolen = damage * IlsConfig.lifeSteal / 100;
                IlsMod.combatlog(source, "message.itemlorestats.pve.lifesteal", stolen);
                double health = source.getHealth();
                health += stolen;
                if (source.getMaxHealth() < health)
                    health = source.getMaxHealth();
                source.setHealth((float) health);
            }
        }
        event.setAmount((float) damage);
        if (this.blind.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 60, IlsConfig.blindLevel, true, true));
        if (this.harming.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(7), 60, IlsConfig.harmingLevel, true, true));
        if (this.ignition.occurred())
            living.setFire(3);
        if (this.slowness.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, IlsConfig.slowLevel, true, true));
        if (this.poison.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(19), 60, IlsConfig.poisonLevel, true, true));
        if (this.wither.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(20), 60, IlsConfig.witherLevel, true, true));
    }

    public void apply(EntityLivingBase source, EntityPlayer living, LivingDamageEvent event) {
        Stats stats = new Stats(living);
        double damage = event.getAmount() + this.damage + this.damagePvP;
        if (this.critical.occurred())
            damage *= this.criticalDamage / 100;
        damage -= damage * stats.reduction / 100;
        if (damage < 0)
            damage = 0;
        if (stats.reflection.occurred()) {
            if (this.critical.occurred())
                IlsMod.combatlog(living, "message.itemlorestats.evp.crit_hit_reflected", source.getName());
            else
                IlsMod.combatlog(living, "message.itemlorestats.evp.hit_reflected", source.getName());
            double health = source.getHealth();
            health -= damage;
            if (health < 0)
                health = 0;
            source.setHealth((float) health);
            damage = 0;
        } else if (stats.dodge.occurred()) {
            if (this.critical.occurred())
                IlsMod.combatlog(living, "message.itemlorestats.evp.crit_hit_dodged", source.getName());
            else
                IlsMod.combatlog(living, "message.itemlorestats.evp.hit_dodged", source.getName());
            damage = 0;
        } else if (stats.block.occurred()) {
            if (this.critical.occurred())
                IlsMod.combatlog(living, "message.itemlorestats.evp.crit_hit_blocked", source.getName());
            else
                IlsMod.combatlog(living, "message.itemlorestats.evp.hit_blocked", source.getName());
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 30, IlsConfig.blockSlowLevel, true, true));
            damage = 0;
        } else {
            if (this.critical.occurred())
                IlsMod.combatlog(living, "message.itemlorestats.evp.crit_hit", source.getName(), damage);
            else
                IlsMod.combatlog(living, "message.itemlorestats.evp.hit", source.getName(), damage);
            if (this.lifeSteal.occurred()) {
                double stolen = damage * IlsConfig.lifeSteal / 100;
                IlsMod.combatlog(source, "message.itemlorestats.evp.lifesteal", source.getName(), stolen);
                double health = source.getHealth();
                health += stolen;
                if (source.getMaxHealth() < health)
                    health = source.getMaxHealth();
                source.setHealth((float) health);
            }
        }
        event.setAmount((float) damage);
        if (this.blind.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 60, IlsConfig.blindLevel, true, true));
        if (this.harming.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(7), 60, IlsConfig.harmingLevel, true, true));
        if (this.ignition.occurred())
            living.setFire(3);
        if (this.slowness.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, IlsConfig.slowLevel, true, true));
        if (this.poison.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(19), 60, IlsConfig.poisonLevel, true, true));
        if (this.wither.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(20), 60, IlsConfig.witherLevel, true, true));
    }

    public void apply(EntityPlayer source, EntityPlayer living, LivingDamageEvent event) {
        // XXX: NOTE - Dodge and LifeSteal have no effect at PvP.
        Stats stats = new Stats(living);
        double damage = event.getAmount() + this.damage + this.damagePvP;
        if (this.critical.occurred())
            damage *= this.criticalDamage / 100;
        damage -= damage * stats.reduction / 100;
        if (damage < 0)
            damage = 0;
        if (stats.reflection.occurred()) {
            if (this.critical.occurred())
                IlsMod.combatlog(source, "message.itemlorestats.pvp.crit_hit_reflected", living.getName());
            else
                IlsMod.combatlog(source, "message.itemlorestats.pvp.hit_reflected", living.getName());
            double health = source.getHealth();
            health -= damage;
            if (health < 0)
                health = 0;
            source.setHealth((float) health);
            damage = 0;
        } else if (stats.block.occurred()) {
            if (this.critical.occurred())
                IlsMod.combatlog(source, "message.itemlorestats.pvp.crit_hit_blocked", living.getName());
            else
                IlsMod.combatlog(source, "message.itemlorestats.pvp.hit_blocked", living.getName());
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, IlsConfig.blockSlowLevel, true, true));
            damage = 0;
        } else {
            if (this.critical.occurred())
                IlsMod.combatlog(source, "message.itemlorestats.pvp.crit_hit", living.getName(), damage);
            else
                IlsMod.combatlog(source, "message.itemlorestats.pvp.hit", living.getName(), damage);
        }
        event.setAmount((float) damage);
        if (this.blind.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 60, IlsConfig.blindLevel, true, true));
        if (this.harming.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(7), 60, IlsConfig.harmingLevel, true, true));
        if (this.ignition.occurred())
            living.setFire(3);
        if (this.slowness.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, IlsConfig.slowLevel, true, true));
        if (this.poison.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(19), 60, IlsConfig.poisonLevel, true, true));
        if (this.wither.occurred())
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(20), 60, IlsConfig.witherLevel, true, true));
    }
}
