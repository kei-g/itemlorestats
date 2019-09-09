package com.snowstep115.itemlorestats.item;

import java.util.HashMap;

import net.minecraft.inventory.EquipmentSlotType;

public final class LoreLegs extends LoreArmor<LoreLegs> {
    public static final HashMap<String, LoreLegs> INSTANCES = new HashMap<>();

    public LoreLegs(String name) {
        super(EquipmentSlotType.LEGS, name, INSTANCES);
    }

    @Override
    protected LoreLegs cast() {
        return this;
    }
}
