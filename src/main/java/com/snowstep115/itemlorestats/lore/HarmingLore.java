package com.snowstep115.itemlorestats.lore;

public final class HarmingLore extends AbsractPossibilityLore {
    public HarmingLore() {
        super("text.harminglore.name");
    }

    public HarmingLore(String value) {
        super("text.harminglore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.harming.increase(this.possibility);
    }
}
