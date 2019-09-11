package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.util.I18n;

public final class HealthLore extends Lore {
    private final double value;

    public HealthLore() {
        super("text.healthlore.name");
        this.value = IlsMod.SEED.nextDouble() * 10d;
    }

    public HealthLore(String value) {
        super("text.healthlore.name");
        this.value = Double.parseDouble(value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.health += this.value;
    }

    @Override
    public String getFormattedString() {
        return String.format("%s +%.2f", getLocalizedName(), this.value);
    }

    @Override
    public String getStatsName() {
        return I18n.format("§6text.health.name§r");
    }
}
