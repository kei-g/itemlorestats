package com.snowstep115.itemlorestats.lore;

public final class ReflectLore extends AbsractPossibilityLore {
    public ReflectLore() {
        super("text.reflectlore.name");
    }

    public ReflectLore(String value) {
        super("text.reflectlore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.reflection.increase(this.possibility);
    }
}
