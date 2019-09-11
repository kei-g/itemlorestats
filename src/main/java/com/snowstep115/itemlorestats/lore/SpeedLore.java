package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class SpeedLore extends Lore {
    public final double value;

    public SpeedLore() {
        super("text.speedlore.name");
        this.value = IlsMod.SEED.nextGaussian() * 5;
    }

    public SpeedLore(String value) {
        super("text.speedlore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.value = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.speed += this.value;
    }

    @Override
    public String getFormattedString() {
        if (0 < this.value)
            return String.format("%s +%.2f%%", getLocalizedName(), this.value);
        return String.format("%s %.2f%%", getLocalizedName(), this.value);
    }
}
