package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class ArmourLore extends Lore {
    public final double value;

    public ArmourLore() {
        super("text.armourlore.name");
        this.value = IlsMod.SEED.nextGaussian() * 10d;
    }

    public ArmourLore(String value) {
        super("text.armourlore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.value = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.reduction += this.value;
    }

    @Override
    public String getFormattedString() {
        return String.format(this.value <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.value);
    }
}
