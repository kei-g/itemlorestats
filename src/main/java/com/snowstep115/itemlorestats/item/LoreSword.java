package com.snowstep115.itemlorestats.item;

import java.util.HashMap;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.item.SwordItem;

public final class LoreSword extends SwordItem {
    public static final HashMap<String, LoreSword> INSTANCES = new HashMap<>();

    private final String name;

    public LoreSword(String name) {
        super(new LoreSwordTier(), 10, 10.0f, new Properties().maxDamage(32767).group(LoreItemGroup.INSTANCE));
        this.name = name;
        String identifier = String.format("loresword%d", INSTANCES.size());
        setRegistryName(IlsMod.MODID, identifier);
        INSTANCES.put(identifier, this);
    }

    @Override
    protected String getDefaultTranslationKey() {
        return this.name;
    }
}
