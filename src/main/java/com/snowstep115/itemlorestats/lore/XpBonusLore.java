package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class XpBonusLore extends Lore {
    public final double value;

    public XpBonusLore() {
        super("text.xpbonuslore.name");
        this.value = IlsMod.SEED.nextGaussian() * 5;
    }

    public XpBonusLore(String value) {
        super("text.xpbonuslore.name");
        int index = value.lastIndexOf('%');
        if (index < 0)
            throw new IllegalArgumentException();
        this.value = Double.parseDouble(value.substring(0, index));
    }

    @Override
    public void applyTo(Stats stats) {
        stats.xpBonus += this.value;
    }

    @Override
    public String getFormattedString() {
        return String.format(this.value <= 0 ? "%s %.2f%%" : "%s +%.2f%%", getLocalizedName(), this.value);
    }
}
