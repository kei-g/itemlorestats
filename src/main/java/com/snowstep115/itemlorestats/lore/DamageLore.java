package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

public final class DamageLore extends Lore {
    public final int minimumValue;
    public final int maximumValue;

    public DamageLore() {
        super("text.damagelore.name");
        int d1, d2;
        do {
            d1 = (int) Math.round(IlsMod.SEED.nextGaussian() * 20d);
            d2 = (int) Math.round(IlsMod.SEED.nextGaussian() * 20d);
        } while (d1 < 0 || d2 < 0);
        this.minimumValue = Math.min(d1, d2);
        this.maximumValue = Math.max(d1, d2);
    }

    public DamageLore(String value) {
        super("text.damagelore.name");
        String[] comps = value.split("-");
        this.minimumValue = Integer.parseInt(comps[0]);
        this.maximumValue = comps.length < 2 ? this.minimumValue : Integer.parseInt(comps[1]);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.damage += this.minimumValue + IlsMod.SEED.nextDouble() * (this.maximumValue - this.minimumValue);
        stats.damageMax += this.maximumValue;
        stats.damageMin += this.minimumValue;
    }

    @Override
    public String getFormattedString() {
        return String.format("%s +%d-%d", getLocalizedName(), this.minimumValue, this.maximumValue);
    }
}
