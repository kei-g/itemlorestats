package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.config.IlsConfig;
import com.snowstep115.itemlorestats.util.Lazy;

public final class Stats {
    public double block;
    public double criticalChance;
    public double criticalDamage = IlsConfig.baseCriticalDamage;
    public double damage;
    public double damageMax;
    public double damageMin;
    public double dodge;
    public double health = 20;
    public double reduction;
    public double regeneration;

    public final Lazy<Boolean> critical = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.criticalChance);
    public final Lazy<Boolean> blocked = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.block);
    public final Lazy<Boolean> dodged = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.dodge);
}
