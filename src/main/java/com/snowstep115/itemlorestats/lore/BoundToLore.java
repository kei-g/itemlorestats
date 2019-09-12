package com.snowstep115.itemlorestats.lore;

import net.minecraft.item.ItemStack;

public final class BoundToLore extends Lore {
    private final ItemStack itemstack;
    private final String username;

    public BoundToLore() {
        super("text.soulboundlore.name");
        this.itemstack = ItemStack.EMPTY;
        this.username = null;
    }

    public BoundToLore(ItemStack itemstack, String value) {
        super("text.soulboundlore.name");
        this.itemstack = itemstack;
        this.username = value;
    }

    @Override
    public void applyTo(Stats stats) {
        stats.soulbound.put(this.itemstack, this.username);
    }

    @Override
    public String getFormattedString() {
        return String.format("%s %s", getLocalizedName(), this.username);
    }
}
