package com.snowstep115.itemlorestats.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public final class LoreSwordTier implements IItemTier {
    private static final File FILE = new File("config", "loreswordtier.json");
    private static final Map<String, LoreSwordTier> INSTANCES;

    static Map<String, LoreSwordTier> load() {
        return IlsMod.execute(() -> {
            FileInputStream fis = new FileInputStream(LoreSwordTier.FILE);
            try {
                byte[] buf = new byte[fis.available()];
                fis.read(buf);
                String json = new String(buf, "UTF8");
                Gson gson = new Gson();
                Type type = new TypeToken<LinkedHashMap<String, LoreSwordTier>>() {
                }.getType();
                return gson.fromJson(json, type);
            } finally {
                fis.close();
            }
        }, () -> new LinkedHashMap<>());
    }

    static {
        INSTANCES = load();
    }

    public static LoreSwordTier getOrDefault(String name) {
        return LoreSwordTier.INSTANCES.compute(name,
                ($, instance) -> instance == null ? new LoreSwordTier() : instance);
    }

    public static void save() {
        IlsMod.execute(() -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(INSTANCES);
            FileOutputStream fos = new FileOutputStream(LoreSwordTier.FILE);
            try {
                fos.write(json.getBytes("UTF8"));
            } finally {
                fos.close();
            }
        });
    }

    public int maxUses = 32767;
    public float efficiency = 10.0f;
    public float attckDamage = 10.0f;
    public int harvestLevel = 3;
    public int enchantability = 10;
    public String repairMaterial = Items.DIAMOND.getRegistryName().toString();

    @Override
    public int getMaxUses() {
        return this.maxUses;
    }

    @Override
    public float getEfficiency() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamage() {
        return this.attckDamage;
    }

    @Override
    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Ingredient getRepairMaterial() {
        int index = this.repairMaterial.indexOf(':');
        String domain = index < 0 ? "minecraft" : this.repairMaterial.substring(0, index);
        String path = index < 0 ? this.repairMaterial : this.repairMaterial.substring(index + 1);
        ResourceLocation name = new ResourceLocation(domain, path);
        Item item = Registry.ITEM.getOrDefault(name);
        return Ingredient.fromItems(item);
    }
}
