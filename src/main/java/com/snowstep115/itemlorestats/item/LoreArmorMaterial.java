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

import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public final class LoreArmorMaterial {
    private static final File FILE = new File("config", "lorearmormaterial.json");
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
                ($, instance) -> instance == null ? new LoreArmorMaterial($) : instance);
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

    public String name;
    public String textureName;
    public int durability = 32767;
    public int[] damageReductionAmounts = new int[] { 10, 10, 10, 10 };
    public int enchantability = 10;
    public String repairMaterial = Items.DIAMOND.getRegistryName().toString();
    public float toughness = 10.0f;

    public LoreArmorMaterial() {
    }

    public LoreArmorMaterial(String name) {
        this.name = name;
        this.textureName = name;
    }

    public ArmorMaterial getEnum() {
        ArmorMaterial material = EnumHelper.addArmorMaterial(this.name, this.textureName, this.durability,
                this.damageReductionAmounts, this.enchantability, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, this.toughness);
        material.setRepairItem(new ItemStack(Item.getByNameOrId(this.repairMaterial), 1));
        return material;
    }
}
