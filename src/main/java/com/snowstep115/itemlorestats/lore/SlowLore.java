package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class SlowLore extends Lore {
    private final double possibility;

    public SlowLore() {
        super("text.slowlore.name");
        this.possibility = IlsMod.SEED.nextDouble() * 5;
    }

    public SlowLore(String value) {
        super("text.slowlore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.possibility = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.slowness.increase(this.possibility);
    }

    @Override
    public String getFormattedString() {
        return String.format(this.possibility <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.possibility);
    }
}
