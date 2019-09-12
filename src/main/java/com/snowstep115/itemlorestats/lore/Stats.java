package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.config.IlsConfig;
import com.snowstep115.itemlorestats.util.Lazy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class Stats {
    public double block;
    public double criticalChance;
    public double criticalDamage = IlsConfig.baseCriticalDamage;
    public double damage;
    public double damageMax;
    public double damageMin;
    public double dodge;
    public double health;
    public double ignition;
    public double lifeSteal;
    public double poison;
    public double reduction;
    public double reflection;
    public double regeneration;
    public double slowness;
    public double speed;

    public Stats(EntityLivingBase living) {
        this.health = living.getMaxHealth();
        Lore.deserialize(living, lore -> lore.applyTo(this));
    }

    public final Lazy<Boolean> critical = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.criticalChance);
    public final Lazy<Boolean> blocked = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.block);
    public final Lazy<Boolean> dodged = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.dodge);
    public final Lazy<Boolean> ignited = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.ignition);
    public final Lazy<Boolean> lifeStolen = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.lifeSteal);
    public final Lazy<Boolean> poisoned = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.poison);
    public final Lazy<Boolean> reflected = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.reflection);
    public final Lazy<Boolean> slown = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.slowness);

    public void apply(Entity source, EntityLivingBase living, LivingDamageEvent event) {
    }

    public void apply(Entity source, EntityPlayer living, LivingDamageEvent event) {
        Stats stats = new Stats(living);
        double damage = event.getAmount() - event.getAmount() * stats.reduction / 100;
        if (damage < 0)
            damage = 0;
        damage = damage * living.getMaxHealth() / stats.health;
        event.setAmount((float) damage);
    }

    public void apply(EntityLivingBase source, EntityLivingBase living, LivingDamageEvent event) {
    }

    public void apply(EntityPlayer source, EntityLivingBase living, LivingDamageEvent event) {
        double damage = this.damage;
        if (this.critical.get())
            damage = damage * this.criticalDamage / 100;
        damage -= living.getTotalArmorValue();
        if (damage < 0)
            damage = 0;
        event.setAmount((float) damage);
        if (this.critical.get())
            IlsMod.info(source, "§dYou crit hit a §f%s §dfor §6%.2f §ddamage.§r", living.getName(), damage);
        else
            IlsMod.info(source, "§dYou hit a §f%s §dfor §6%.2f §ddamage.§r", living.getName(), damage);
        if (this.lifeStolen.get()) {
            double stolen = damage * IlsConfig.lifeSteal / 100;
            IlsMod.info(source, "§dYou stole §6%.2f §dhealth.§r", stolen);
            double health = this.health * source.getHealth() / source.getMaxHealth();
            health += stolen;
            if (this.health <= health)
                health = this.health;
            health = health * source.getMaxHealth() / this.health;
            source.setHealth((float) health);
        }
        if (this.ignited.get()) {
            living.setFire(3);
        }
        if (this.slown.get()) {
            Potion slow = Potion.getPotionById(2);
            living.addPotionEffect(new PotionEffect(slow, 60, IlsConfig.slowLevel, true, true));
        }
        if (this.poisoned.get()) {
            Potion poison = Potion.getPotionById(19);
            living.addPotionEffect(new PotionEffect(poison, 60, IlsConfig.poisonLevel, true, true));
        }
    }

    public void apply(EntityLivingBase source, EntityPlayer living, LivingDamageEvent event) {
        Stats stats = new Stats(living);
        double damage = event.getAmount() - living.getTotalArmorValue();
        if (damage < 0)
            damage = 0;
        damage -= damage * stats.reduction / 100;
        if (damage < 0)
            damage = 0;
        if (stats.dodged.get())
            damage = 0;
        if (stats.blocked.get())
            damage = 0;
        if (stats.reflected.get()) {
            IlsMod.info(living, "%s §dhit you, but you reflected.§r", source.getName());
        } else if (stats.dodged.get()) {
            IlsMod.info(living, "%s §dhit you, but you dodged.§r", source.getName());
        } else if (stats.blocked.get()) {
            Potion potion = Potion.getPotionById(2);
            living.addPotionEffect(new PotionEffect(potion, 30, IlsConfig.blockSlowLevel, true, true));
            IlsMod.info(living, "%s §dhit you, but you blocked.§r", source.getName());
        } else
            IlsMod.info(living, "%s §dhit you for §6%.2f §ddamage.§r", source.getName(), damage);
        if (stats.reflected.get()) {
            double health = source.getHealth();
            health -= damage;
            if (health < 0)
                health = 0;
            source.setHealth((float) health);
            damage = 0;
        }
        damage = damage * living.getMaxHealth() / stats.health;
        event.setAmount((float) damage);
        if (stats.slown.get())
            source.addPotionEffect(new PotionEffect(Potion.getPotionById(2), 60, IlsConfig.slowLevel, true, true));
        if (stats.poisoned.get())
            source.addPotionEffect(new PotionEffect(Potion.getPotionById(19), 60, IlsConfig.poisonLevel, true, true));
    }

    public void apply(EntityPlayer source, EntityPlayer living, LivingDamageEvent event) {
        // XXX: NOTE - Dodge and LifeSteal have no effect at PvP.
    }
}
