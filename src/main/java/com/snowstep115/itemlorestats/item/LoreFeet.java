package com.snowstep115.itemlorestats.item;

import java.util.HashMap;

import net.minecraft.inventory.EquipmentSlotType;

public final class LoreFeet extends LoreArmor<LoreFeet> {
    public static final HashMap<String, LoreFeet> INSTANCES = new HashMap<>();

    public LoreFeet(String name) {
        super(EquipmentSlotType.FEET, name, INSTANCES);
    }

    @Override
    protected LoreFeet cast() {
        return this;
    }
}
