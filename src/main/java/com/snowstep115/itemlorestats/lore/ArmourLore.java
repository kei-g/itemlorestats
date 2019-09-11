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
        this.value = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.reduction += this.value;
    }

    @Override
    public String getFormattedString() {
        if (0 < this.value)
            return String.format("%s +%.2f%%", getLocalizedName(), this.value);
        return String.format("%s %.2f%%", getLocalizedName(), this.value);
    }
}
