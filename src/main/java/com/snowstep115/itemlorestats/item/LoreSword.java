package com.snowstep115.itemlorestats.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.item.SwordItem;

public final class LoreSword extends SwordItem {
    private static final File FILE = new File("config", "loresword.json");
    private static int INDEX = 0;
    private static final Map<String, LoreSwordGson> INSTANCES;

    static Map<String, LoreSwordGson> load() {
        return IlsMod.execute(() -> {
            FileInputStream fis = new FileInputStream(LoreSword.FILE);
            try {
                byte[] buf = new byte[fis.available()];
                fis.read(buf);
                String json = new String(buf, "UTF8");
                Gson gson = new Gson();
                Type type = new TypeToken<LinkedHashMap<String, LoreSwordGson>>() {
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

    public static void getAll(Consumer<LoreSword> consumer) {
        for (String name : INSTANCES.keySet()) {
            LoreSwordGson gson = INSTANCES.get(name);
            consumer.accept(new LoreSword(name, gson));
        }
    }

    public static LoreSwordGson getOrDefault(String name) {
        return LoreSword.INSTANCES.compute(name, ($, instance) -> instance == null ? new LoreSwordGson() : instance);
    }

    public static void save() {
        IlsMod.execute(() -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(INSTANCES);
            FileOutputStream fos = new FileOutputStream(LoreSword.FILE);
            try {
                fos.write(json.getBytes("UTF8"));
            } finally {
                fos.close();
            }
        });
    }

    private final String name;

    LoreSword(String name, LoreSwordGson gson) {
        super(LoreSwordTier.getOrDefault(name), gson.attackDamage, gson.attackSpeed,
                new Properties().maxDamage(gson.maxDamage).group(LoreItemGroup.INSTANCE));
        this.name = name;
        String identifier = String.format("loresword%d", LoreSword.INDEX++);
        setRegistryName(IlsMod.MODID, identifier);
    }

    public LoreSword(String name) {
        this(name, LoreSword.getOrDefault(name));
    }

    @Override
    protected String getDefaultTranslationKey() {
        return this.name;
    }

    public static class LoreSwordGson {
        public int attackDamage = 10;
        public float attackSpeed = 10.0f;
        public int maxDamage = 32767;
    }
}
