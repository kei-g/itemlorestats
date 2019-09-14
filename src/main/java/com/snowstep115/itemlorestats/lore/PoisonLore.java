package com.snowstep115.itemlorestats.lore;

public final class PoisonLore extends AbsractPossibilityLore {
    public PoisonLore() {
        super("text.poisonlore.name");
    }

    public PoisonLore(String value) {
        super("text.poisonlore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.poison.increase(this.possibility);
    }
}
