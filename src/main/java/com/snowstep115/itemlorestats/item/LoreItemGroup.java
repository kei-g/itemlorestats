package com.snowstep115.itemlorestats.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class LoreItemGroup extends CreativeTabs {
    public static final LoreItemGroup INSTANCE = new LoreItemGroup();

    LoreItemGroup() {
        super("itemlorestats");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.DIAMOND);
    }
}
