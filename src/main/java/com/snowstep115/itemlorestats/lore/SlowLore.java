package com.snowstep115.itemlorestats.lore;

public final class SlowLore extends AbsractPossibilityLore {
    public SlowLore() {
        super("text.slowlore.name");
    }

    public SlowLore(String value) {
        super("text.slowlore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.slowness.increase(this.possibility);
    }
}
