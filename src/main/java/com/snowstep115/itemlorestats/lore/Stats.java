package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.util.Lazy;

public final class Stats {
    public double damage;
    public double damageMax;
    public double damageMin;
    public double dodge;
    public double health = 20;
    public double reduction;
    public double regeneration;

    public final Lazy<Boolean> dodged = new Lazy<>(() -> IlsMod.SEED.nextDouble() * 100 <= this.dodge);
}
