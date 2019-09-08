package com.snowstep115.itemlorestats.item;

import com.snowstep115.itemlorestats.ItemLoreStatsMod;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

public final class LoreArmor extends ArmorItem {
    public static final LoreArmor INSTANCE = new LoreArmor();

    LoreArmor() {
        super(ArmorMaterial.DIAMOND, EquipmentSlotType.CHEST, new Properties().maxDamage(32767).group(LoreItemGroup.INSTANCE));
        setRegistryName(ItemLoreStatsMod.MODID, "lorearmor");
    }
}
