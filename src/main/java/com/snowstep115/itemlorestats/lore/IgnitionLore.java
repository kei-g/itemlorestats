package com.snowstep115.itemlorestats.lore;

public final class IgnitionLore extends AbsractPossibilityLore {
    public IgnitionLore() {
        super("text.ignitionlore.name");
    }

    public IgnitionLore(String value) {
        super("text.ignitionlore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.ignition.increase(this.possibility);
    }
}
