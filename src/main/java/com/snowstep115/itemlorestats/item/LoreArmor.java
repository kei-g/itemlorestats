package com.snowstep115.itemlorestats.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public final class LoreArmor extends ItemArmor {
    private static final File FILE = new File("config", "lorearmor.json");
    private static final HashMap<EntityEquipmentSlot, Integer> INDEX = new HashMap<>();
    private static final Map<String, LoreArmorGson> INSTANCES;

    static Map<String, LoreArmorGson> load() {
        return IlsMod.execute(() -> {
            FileInputStream fis = new FileInputStream(LoreArmor.FILE);
            try {
                byte[] buf = new byte[fis.available()];
                fis.read(buf);
                String json = new String(buf, "UTF8");
                Gson gson = new Gson();
                Type type = new TypeToken<LinkedHashMap<String, LoreArmorGson>>() {
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

    public static void getAll(Consumer<LoreArmor> consumer) {
        for (String name : INSTANCES.keySet()) {
            LoreArmorGson gson = INSTANCES.get(name);
            consumer.accept(new LoreArmor(name, gson));
        }
    }

    public static LoreArmorGson getOrDefault(String name, EntityEquipmentSlot equipmentSlotType) {
        return LoreArmor.INSTANCES.compute(name,
                ($, instance) -> instance == null ? new LoreArmorGson(equipmentSlotType) : instance);
    }

    public static void save() {
        IlsMod.execute(() -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(INSTANCES);
            FileOutputStream fos = new FileOutputStream(LoreArmor.FILE);
            try {
                fos.write(json.getBytes("UTF8"));
            } finally {
                fos.close();
            }
        });
    }

    private final String name;

    LoreArmor(String name, LoreArmorGson gson) {
        super(LoreArmorMaterial.getOrDefault(name).getEnum(), 0, gson.equipmentSlotType);
        this.name = name;
        setCreativeTab(LoreItemGroup.INSTANCE);
        setMaxDamage(gson.maxDamage);
        String identifier = String.format("lore%s%d", gson.equipmentSlotType.getName(),
                LoreArmor.INDEX.compute(gson.equipmentSlotType, ($, index) -> index == null ? 0 : index + 1));
        setRegistryName(IlsMod.MODID, identifier);
        setUnlocalizedName(IlsMod.MODID + "." + identifier);
    }

    public LoreArmor(String name, EntityEquipmentSlot equipmentSlotType) {
        this(name, LoreArmor.getOrDefault(name, equipmentSlotType));
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        return this.name;
    }

    public static class LoreArmorGson {
        public EntityEquipmentSlot equipmentSlotType = EntityEquipmentSlot.CHEST;
        public int maxDamage = 32767;

        public LoreArmorGson() {
        }

        public LoreArmorGson(EntityEquipmentSlot equipmentSlotType) {
            this.equipmentSlotType = equipmentSlotType;
        }
    }
}
