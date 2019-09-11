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
        stats.criticalChance += this.possibility;
    }

    @Override
    public String getFormattedString() {
        if (0 < this.possibility)
            return String.format("%s +%.2f%%", getLocalizedName(), this.possibility);
        return String.format("%s %.2f%%", getLocalizedName(), this.possibility);
    }
}
