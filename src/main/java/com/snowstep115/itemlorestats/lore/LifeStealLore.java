package com.snowstep115.itemlorestats.lore;

public final class LifeStealLore extends AbsractPossibilityLore {
    public LifeStealLore() {
        super("text.lifesteallore.name");
    }

    public LifeStealLore(String value) {
        super("text.lifesteallore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.lifeSteal.increase(this.possibility);
    }
}
