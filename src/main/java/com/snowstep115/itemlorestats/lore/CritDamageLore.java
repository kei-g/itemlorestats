package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class CritDamageLore extends Lore {
    private final double value;

    public CritDamageLore() {
        super("text.critdamagelore.name");
        this.value = IlsMod.SEED.nextGaussian() * 5;
    }

    public CritDamageLore(String value) {
        super("text.critdamagelore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.value = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.criticalDamage += this.value;
    }

    @Override
    public String getFormattedString() {
        return String.format(this.value <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.value);
    }
}
