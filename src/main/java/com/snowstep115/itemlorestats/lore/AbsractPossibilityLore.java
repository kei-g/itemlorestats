package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public abstract class AbsractPossibilityLore extends Lore {
    protected final double possibility;

    protected AbsractPossibilityLore(String name) {
        super(name);
        double possibility;
        do {
            possibility = IlsMod.SEED.nextGaussian() * 5;
        } while (possibility < 0);
        this.possibility = possibility;
    }

    protected AbsractPossibilityLore(String name, String value) {
        super(name);
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.possibility = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public String getFormattedString() {
        return String.format(this.possibility <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.possibility);
    }
}
