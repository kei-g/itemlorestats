package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class LifeStealLore extends Lore {
    private final double possibility;

    public LifeStealLore() {
        super("text.lifesteallore.name");
        this.possibility = IlsMod.SEED.nextDouble() * 5;
    }

    public LifeStealLore(String value) {
        super("text.lifesteallore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.possibility = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.lifeSteal.increase(this.possibility);
    }

    @Override
    public String getFormattedString() {
        return String.format(this.possibility <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.possibility);
    }
}
