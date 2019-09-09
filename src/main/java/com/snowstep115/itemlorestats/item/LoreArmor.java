package com.snowstep115.itemlorestats.item;

import java.util.Map;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;

public abstract class LoreArmor<T extends LoreArmor<T>> extends ArmorItem {
    protected final String name;

    protected LoreArmor(EquipmentSlotType type, String name, Map<String, T> instances) {
        super(new LoreArmorMaterial(), type, new Properties().maxDamage(32767).group(LoreItemGroup.INSTANCE));
        this.name = name;
        String identifier = String.format("lore%s%d", type.getName(), instances.size());
        setRegistryName(IlsMod.MODID, identifier);
        instances.put(identifier, cast());
    }

    protected abstract T cast();

    @Override
    protected String getDefaultTranslationKey() {
        return this.name;
    }
}
