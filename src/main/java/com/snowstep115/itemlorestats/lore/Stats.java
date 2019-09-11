package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.config.IlsConfig;
import com.snowstep115.itemlorestats.util.Lazy;

import net.minecraft.entity.EntityLivingBase;

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
    public double reduction;
    public double regeneration;
    public double speed;

    public Stats(EntityLivingBase living) {
        this.health = living.getMaxHealth();
    }

    public final Lazy<Boolean> critical = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.criticalChance);
    public final Lazy<Boolean> blocked = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.block);
    public final Lazy<Boolean> dodged = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.dodge);
    public final Lazy<Boolean> ignited = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.ignition);
    public final Lazy<Boolean> lifeStolen = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.lifeSteal);
}
