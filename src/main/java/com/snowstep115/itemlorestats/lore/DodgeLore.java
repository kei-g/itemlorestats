package com.snowstep115.itemlorestats.lore;

public final class DodgeLore extends AbsractPossibilityLore {
    public DodgeLore() {
        super("text.dodgelore.name");
    }

    public DodgeLore(String value) {
        super("text.dodgelore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.dodge.increase(this.possibility);
    }
}
