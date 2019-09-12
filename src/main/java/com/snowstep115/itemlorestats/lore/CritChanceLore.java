package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class CritChanceLore extends Lore {
    private final double possibility;

    public CritChanceLore() {
        super("text.critchancelore.name");
        this.possibility = IlsMod.SEED.nextGaussian() * 5d;
    }

    public CritChanceLore(String value) {
        super("text.critchancelore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.possibility = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.critical.increase(this.possibility);
    }

    @Override
    public String getFormattedString() {
        return String.format(this.possibility <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.possibility);
    }
}
