package com.snowstep115.itemlorestats.lore;

public final class BlockLore extends AbsractPossibilityLore {
    public BlockLore() {
        super("text.blocklore.name");
    }

    public BlockLore(String value) {
        super("text.blocklore.name", value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.block.increase(this.possibility);
    }
}
