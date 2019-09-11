package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class PoisonLore extends Lore {
    private final double possibility;

    public PoisonLore() {
        super("text.poinsonlore.name");
        this.possibility = IlsMod.SEED.nextDouble() * 5;
    }

    public PoisonLore(String value) {
        super("text.poinsonlore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.possibility = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.poison += this.possibility;
    }

    @Override
    public String getFormattedString() {
        if (0 < this.possibility)
            return String.format("%s +%.2f%%", getLocalizedName(), this.possibility);
        return String.format("%s %.2f%%", getLocalizedName(), this.possibility);
    }
}
