package com.snowstep115.itemlorestats.item;

import java.util.HashMap;

import com.snowstep115.itemlorestats.ItemLoreStatsMod;

import net.minecraft.item.SwordItem;

public final class LoreSword extends SwordItem {
    public static final HashMap<String, LoreSword> INSTANCES = new HashMap<>();

    public LoreSword(String name) {
        super(LoreSwordTier.INSTANCE, 10, 10.0f, new Properties().maxDamage(32767).group(LoreItemGroup.INSTANCE));
        setRegistryName(ItemLoreStatsMod.MODID, name);
    }
}
