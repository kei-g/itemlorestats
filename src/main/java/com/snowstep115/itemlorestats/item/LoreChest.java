package com.snowstep115.itemlorestats.item;

import java.util.HashMap;

import net.minecraft.inventory.EquipmentSlotType;

public final class LoreChest extends LoreArmor<LoreChest> {
    public static final HashMap<String, LoreChest> INSTANCES = new HashMap<>();

    public LoreChest(String name) {
        super(EquipmentSlotType.CHEST, name, INSTANCES);
    }

    @Override
    protected LoreChest cast() {
        return this;
    }
}
