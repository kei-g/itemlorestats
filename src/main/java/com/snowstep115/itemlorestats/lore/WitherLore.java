package com.snowstep115.itemlorestats.lore;

public final class WitherLore extends AbsractPossibilityLore {
    public WitherLore() {
        super("text.witherlore.name");
    }

    public WitherLore(String value) {
        super("text.witherlore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.wither.increase(this.possibility);
    }
}
