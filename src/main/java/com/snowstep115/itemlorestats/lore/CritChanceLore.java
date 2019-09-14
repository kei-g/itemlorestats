package com.snowstep115.itemlorestats.lore;

public final class CritChanceLore extends AbsractPossibilityLore {
    public CritChanceLore() {
        super("text.critchancelore.name");
    }

    public CritChanceLore(String value) {
        super("text.critchancelore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.critical.increase(this.possibility);
    }
}
