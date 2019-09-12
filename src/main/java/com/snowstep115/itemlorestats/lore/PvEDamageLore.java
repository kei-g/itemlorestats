package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class PvEDamageLore extends Lore {
    public final double value;

    public PvEDamageLore() {
        super("text.pvedamagelore.name");
        this.value = IlsMod.SEED.nextDouble() * 20;
    }

    public PvEDamageLore(String value) {
        super("text.pvedamagelore.name");
        this.value = Double.parseDouble(value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.damagePvE += this.value;
    }

    @Override
    public String getFormattedString() {
        return String.format(this.value <= 0 ? "%s %.2f" : "%s +%.2f", getLocalizedName(), this.value);
    }
}
