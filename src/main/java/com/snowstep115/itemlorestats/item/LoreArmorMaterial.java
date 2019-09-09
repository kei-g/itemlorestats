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

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;

public final class LoreArmorMaterial implements IArmorMaterial {
    private static final File FILE = new File("config", "armormaterial.json");
    private static final Map<String, LoreArmorMaterial> INSTANCES;

    static Map<String, LoreArmorMaterial> load() {
        return IlsMod.execute(() -> {
            FileInputStream fis = new FileInputStream(LoreArmorMaterial.FILE);
            try {
                byte[] buf = new byte[fis.available()];
                fis.read(buf);
                String json = new String(buf, "UTF8");
                Gson gson = new Gson();
                Type type = new TypeToken<LinkedHashMap<String, LoreArmorMaterial>>() {
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

    public static LoreArmorMaterial getOrDefault(String name) {
        return LoreArmorMaterial.INSTANCES.compute(name,
                ($, instance) -> instance == null ? new LoreArmorMaterial() : instance);
    }

    public static void save() {
        IlsMod.execute(() -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(INSTANCES);
            FileOutputStream fos = new FileOutputStream(LoreArmorMaterial.FILE);
            try {
                fos.write(json.getBytes("UTF8"));
            } finally {
                fos.close();
            }
        });
    }

    public int durability = 32767;
    public int damageReductionAmount = 10;
    public int enchantability = 10;
    public String repairMaterial = Items.DIAMOND.getRegistryName().toString();
    public float toughness = 10.0f;

    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        return this.durability;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return this.damageReductionAmount;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return SoundEvents.BLOCK_END_PORTAL_SPAWN;
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

    @Override
    public String getName() {
        return "lore";
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }
}
