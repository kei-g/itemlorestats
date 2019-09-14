package com.snowstep115.itemlorestats.lore;

public final class BlindLore extends AbsractPossibilityLore {
    public BlindLore() {
        super("text.blindlore.name");
    }

    public BlindLore(String value) {
        super("text.blindlore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.block.increase(this.possibility);
    }
}
