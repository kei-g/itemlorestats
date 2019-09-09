package com.snowstep115.itemlorestats.item;

import java.util.HashMap;

import net.minecraft.inventory.EquipmentSlotType;

public final class LoreHelmet extends LoreArmor<LoreHelmet> {
    public static final HashMap<String, LoreHelmet> INSTANCES = new HashMap<>();

    public LoreHelmet(String name) {
        super(EquipmentSlotType.HEAD, name, INSTANCES);
    }

    @Override
    protected LoreHelmet cast() {
        return this;
    }
}
