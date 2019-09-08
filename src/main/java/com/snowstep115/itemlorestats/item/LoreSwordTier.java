package com.snowstep115.itemlorestats.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

public final class LoreSwordTier implements IItemTier {
    public static final LoreSwordTier INSTANCE = new LoreSwordTier();

    @Override
    public int getMaxUses() {
        return 32767;
    }

    @Override
    public float getEfficiency() {
        return 10.0f;
    }

    @Override
    public float getAttackDamage() {
        return 10.0f;
    }

    @Override
    public int getHarvestLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(Items.DIAMOND);
    }
}
