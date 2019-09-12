package com.snowstep115.itemlorestats.lore;

import net.minecraft.item.ItemStack;

public final class LevelLore extends Lore {
    private final ItemStack itemstack;
    private final int value;

    public LevelLore() {
        super("text.levellore.name");
        this.itemstack = ItemStack.EMPTY;
        this.value = 0;
    }

    public LevelLore(ItemStack itemstack, String value) {
        super("text.levellore.name");
        this.itemstack = itemstack;
        this.value = Integer.parseInt(value);
    }

    @Override
    public void applyTo(Stats stats) {
        stats.level.put(this.itemstack, this.value);
    }

    @Override
    public String getFormattedString() {
        return String.format("%s %d", getLocalizedName(), this.value);
    }
}
