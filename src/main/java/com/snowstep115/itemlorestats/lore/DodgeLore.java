package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class DodgeLore extends Lore {
    public double possibility;

    public DodgeLore() {
        super("text.dodgelore.name");
        this.possibility = 10d + IlsMod.SEED.nextGaussian() * 10d;
    }

    public DodgeLore(String value) {
        super("text.dodgelore.name");
        int index = value.lastIndexOf('%');
        this.possibility = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.dodge += this.possibility;
    }

    @Override
    public String getFormattedString() {
        return String.format("%s %.2f%%", getLocalizedName(), this.possibility);
    }
}
