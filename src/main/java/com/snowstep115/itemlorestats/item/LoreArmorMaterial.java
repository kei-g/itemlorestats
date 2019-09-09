package com.snowstep115.itemlorestats.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public final class LoreArmorMaterial implements IArmorMaterial {
    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        return 32767;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return 10;
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return SoundEvents.BLOCK_END_PORTAL_SPAWN;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(Items.DIAMOND);
    }

    @Override
    public String getName() {
        return "lore";
    }

    @Override
    public float getToughness() {
        return 10.0f;
    }
}
