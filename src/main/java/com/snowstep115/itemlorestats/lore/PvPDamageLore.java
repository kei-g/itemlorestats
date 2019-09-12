package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class PvPDamageLore extends Lore {
    public final double value;

    public PvPDamageLore() {
        super("text.pvpdamagelore.name");
        this.value = IlsMod.SEED.nextDouble() * 20;
    }

    public PvPDamageLore(String value) {
        super("text.pvpdamagelore.name");
        this.value = Double.parseDouble(value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.damagePvP += this.value;
    }

    @Override
    public String getFormattedString() {
        return String.format(this.value <= 0 ? "%s %.2f" : "%s +%.2f", getLocalizedName(), this.value);
    }
}
