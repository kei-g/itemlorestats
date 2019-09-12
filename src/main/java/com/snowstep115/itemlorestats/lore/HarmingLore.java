package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class HarmingLore extends Lore {
    private final double possibility;

    public HarmingLore() {
        super("text.harminglore.name");
        this.possibility = IlsMod.SEED.nextGaussian();
    }

    public HarmingLore(String value) {
        super("text.harminglore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.possibility = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.harming.increase(this.possibility);
    }

    @Override
    public String getFormattedString() {
        return String.format(this.possibility <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.possibility);
    }
}
