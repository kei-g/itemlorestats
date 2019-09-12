package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class IgnitionLore extends Lore {
    private final double possibility;

    public IgnitionLore() {
        super("text.ignitionlore.name");
        this.possibility = IlsMod.SEED.nextGaussian();
    }

    public IgnitionLore(String value) {
        super("text.ignitionlore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.possibility = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.ignition.increase(this.possibility);
    }

    @Override
    public String getFormattedString() {
        return String.format(this.possibility <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.possibility);
    }
}
