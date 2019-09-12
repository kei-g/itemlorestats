package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class HealthRegenLore extends Lore {
    private final double value;

    public HealthRegenLore() {
        super("text.healthregenlore.name");
        this.value = IlsMod.SEED.nextGaussian() * 5d;
    }

    public HealthRegenLore(String value) {
        super("text.healthregenlore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.value = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.regeneration += this.value;
    }

    @Override
    public String getFormattedString() {
        return String.format(this.value <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.value);
    }
}
