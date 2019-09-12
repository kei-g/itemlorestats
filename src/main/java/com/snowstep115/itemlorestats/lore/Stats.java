package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.config.IlsConfig;
import com.snowstep115.itemlorestats.util.Probabilistic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class Stats {
    public final Probabilistic blind = new Probabilistic();
    public final Probabilistic block = new Probabilistic();
    public final Probabilistic critical = new Probabilistic();
    public double criticalDamage = IlsConfig.baseCriticalDamage;
    public double damage;
    public double damageMax;
    public double damageMin;
    public final Probabilistic dodge = new Probabilistic();
    public final Probabilistic harming = new Probabilistic();
    public double health;
    public final Probabilistic ignition = new Probabilistic();
    public final Probabilistic lifeSteal = new Probabilistic();
    public final Probabilistic poison = new Probabilistic();
    public double reduction;
    public final Probabilistic reflection = new Probabilistic();
    public double regeneration;
    public final Probabilistic slowness = new Probabilistic();
    public double speed;
    public final Probabilistic wither = new Probabilistic();

    public Stats(EntityLivingBase living) {
        this.health = living.getMaxHealth();
        Lore.deserialize(living, lore -> lore.applyTo(this));
    }

    public void apply(Entity source, EntityLivingBase living, LivingDamageEvent event) {
        double damage = event.getAmount() - event.getAmount() * this.reduction / 100;
        if (damage < 0)
            damage = 0;
        damage = damage * living.getMaxHealth() / this.health;
        event.setAmount((float) damage);
    }

    public void apply(Entity source, EntityPlayer living, LivingDamageEvent event) {
        double damage = event.getAmount() - event.getAmount() * this.reduction / 100;
        if (damage < 0)
            damage = 0;
        damage = damage * living.getMaxHealth() / this.health;
        event.setAmount((float) damage);
    }

    public void apply(EntityLivingBase source, EntityLivingBase living, LivingDamageEvent event) {
    }

    public void apply(EntityPlayer source, EntityLivingBase living, LivingDamageEvent event) {
        Stats stats = new Stats(living);
        double damage = event.getAmount() + this.damage;
        if (this.critical.occurred())
            damage *= this.criticalDamage / 100;
        damage -= damage * stats.reduction / 100;
        if (damage < 0)
            damage = 0;
        if (stats.reflection.occurred()) {
            if (this.critical.occurred())
                IlsMod.info(source, "§dYou crit hit a §f%s§d but reflected.§r", living.getName());
            else
                IlsMod.info(source, "§dYou hit a §f%s§d but reflected.§r", living.getName());
            double health = source.getHealth() * this.health / source.getMaxHealth();
            health -= damage;
            if (health < 0)
                health = 0;
            health = health * source.getMaxHealth() / this.health;
            source.setHealth((float) health);
            damage = 0;
        } else if (stats.dodge.occurred()) {
            if (this.critical.occurred())
                IlsMod.info(source, "§dYou crit hit a §f%s§d but dodged.§r", living.getName());
            else
                IlsMod.info(source, "§dYou hit a §f%s§d but dodged.§r", living.getName());
            damage = 0;
        } else if (stats.block.occurred()) {
            if (this.critical.occurred())
                IlsMod.info(source, "§dYou crit hit a §f%s§d but blocked.§r", living.getName());
            else
                IlsMod.info(source, "§dYou hit a §f%s§d but blocked.§r", living.getName());
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, IlsConfig.blockSlowLevel, true, true));
            damage = 0;
        } else {
            if (this.critical.occurred())
                IlsMod.info(source, "§dYou crit hit a §f%s §dfor §6%.2f §ddamage.§r", living.getName(), damage);
            else
                IlsMod.info(source, "§dYou hit a §f%s §dfor §6%.2f §ddamage.§r", living.getName(), damage);
            if (this.lifeSteal.occurred()) {
                double stolen = damage * IlsConfig.lifeSteal / 100;
                IlsMod.info(source, "§dYou stole §6%.2f §dhealth.§r", stolen);
                double health = this.health * source.getHealth() / source.getMaxHealth();
                health += stolen;
                if (this.health <= health)
                    health = this.health;
                health = health * source.getMaxHealth() / this.health;
                source.setHealth((float) health);
            }
        }
        damage = damage * living.getMaxHealth() / stats.health;
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
        double damage = event.getAmount() + this.damage;
        if (this.critical.occurred())
            damage *= this.criticalDamage / 100;
        damage -= damage * stats.reduction / 100;
        if (damage < 0)
            damage = 0;
        if (stats.reflection.occurred()) {
            if (this.critical.occurred())
                IlsMod.info(living, "%s §dcrit hit you, but you reflected.§r", source.getName());
            else
                IlsMod.info(living, "%s §dhit you, but you reflected.§r", source.getName());
            double health = source.getHealth() * this.health / source.getMaxHealth();
            health -= damage;
            if (health < 0)
                health = 0;
            health = health * source.getMaxHealth() / this.health;
            source.setHealth((float) health);
            damage = 0;
        } else if (stats.dodge.occurred()) {
            if (this.critical.occurred())
                IlsMod.info(living, "%s §dcrit hit you, but you dodged.§r", source.getName());
            else
                IlsMod.info(living, "%s §dhit you, but you dodged.§r", source.getName());
            damage = 0;
        } else if (stats.block.occurred()) {
            if (this.critical.occurred())
                IlsMod.info(living, "%s §dcrit hit you, but you blocked.§r", source.getName());
            else
                IlsMod.info(living, "%s §dhit you, but you blocked.§r", source.getName());
            living.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 30, IlsConfig.blockSlowLevel, true, true));
            damage = 0;
        } else {
            if (this.critical.occurred())
                IlsMod.info(living, "%s §dcrit hit you for §6%.2f §ddamage.§r", source.getName(), damage);
            else
                IlsMod.info(living, "%s §dhit you for §6%.2f §ddamage.§r", source.getName(), damage);
            if (this.lifeSteal.occurred()) {
                double stolen = damage * IlsConfig.lifeSteal / 100;
                IlsMod.info(source, "§d%s stole §6%.2f §dhealth.§r", source.getName(), stolen);
                double health = this.health * source.getHealth() / source.getMaxHealth();
                health += stolen;
                if (this.health <= health)
                    health = this.health;
                health = health * source.getMaxHealth() / this.health;
                source.setHealth((float) health);
            }
        }
        damage = damage * living.getMaxHealth() / stats.health;
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
    }
}
