package com.snowstep115.itemlorestats.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class LoreItemGroup extends ItemGroup {
    public static final LoreItemGroup INSTANCE = new LoreItemGroup();

    LoreItemGroup() {
        super("itemlorestats");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.DIAMOND);
    }
}
