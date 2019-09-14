package com.snowstep115.itemlorestats.lore;

import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

import com.snowstep115.itemlorestats.util.I18n;
import com.snowstep115.itemlorestats.util.ResourceUtil;
import com.snowstep115.itemlorestats.util.StringUtil;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

public abstract class Lore {
    public static Consumer<Lore> addDisplayLore(ItemStack itemstack) {
        if (!itemstack.hasTagCompound())
            itemstack.setTagCompound(new NBTTagCompound());
        NBTTagCompound tag = itemstack.getTagCompound();
        if (!tag.hasKey("display"))
            tag.setTag("display", new NBTTagCompound());
        NBTTagCompound display = tag.getCompoundTag("display");
        if (!display.hasKey("Lore"))
            display.setTag("Lore", new NBTTagList());
        NBTTagList list = display.getTagList("Lore", NBT.TAG_STRING);
        return lore -> list.appendTag(new NBTTagString(lore.getFormattedString()));
    }

    public static void deserialize(EntityLivingBase living, ItemStack itemstack, NBTTagList list,
            Consumer<Lore> consumer) {
        for (int i = 0; i < list.tagCount(); i++) {
            String description = list.getStringTagAt(i);
            Lore lore = Lore.parse(living, itemstack, description);
            if (lore == null)
                continue;
            consumer.accept(lore);
        }
    }

    public static void deserialize(EntityLivingBase living, ItemStack itemstack, NBTTagCompound tag,
            Consumer<Lore> consumer) {
        if (tag == null)
            return;
        if (!tag.hasKey("display"))
            return;
        NBTTagCompound display = tag.getCompoundTag("display");
        if (!display.hasKey("Lore"))
            return;
        NBTTagList lore = display.getTagList("Lore", NBT.TAG_STRING);
        Lore.deserialize(living, itemstack, lore, consumer);
    }

    @SuppressWarnings("unchecked")
    public static void deserialize(EntityLivingBase living, ItemStack itemstack, Consumer<Lore> consumer) {
        NBTTagCompound tag = itemstack.getTagCompound();
        Lore.deserialize(living, itemstack, tag, consumer);
        try {
            Item item = itemstack.getItem();
            Method method = item.getClass().getMethod("getTooltip");
            List<String> tooltip = (List<String>) method.invoke(item);
            for (String description : tooltip) {
                Lore lore = parse(living, itemstack, description);
                if (lore == null)
                    continue;
                consumer.accept(lore);
            }
        } catch (Throwable exception) {
        }
    }

    public static void deserialize(EntityLivingBase living, Consumer<Lore> consumer) {
        for (ItemStack equip : living.getEquipmentAndArmor())
            Lore.deserialize(living, equip, consumer);
    }

    public static Lore parse(EntityLivingBase living, ItemStack itemstack, String description) {
        String[] comps = StringUtil.decompose(description);
        StringUtil.undecorate(comps);
        return ResourceUtil.enumerateResources("assets/itemlorestats/lang/", ".lang", br -> {
            do {
                String line = br.readLine();
                if (line == null)
                    return null;
                if (line.isEmpty())
                    continue;
                int index = line.indexOf('=');
                if (index < 0)
                    continue;
                String value = line.substring(index + 1);
                if (!value.equals(comps[0]))
                    continue;
                String key = line.substring(0, index);
                switch (key) {
                case "text.armourlore.name":
                    return new ArmourLore(comps[1]);
                case "text.blindlore.name":
                    return new BlindLore(comps[1]);
                case "text.blocklore.name":
                    return new BlockLore(comps[1]);
                case "text.critchancelore.name":
                    return new CritChanceLore(comps[1]);
                case "text.critdamagelore.name":
                    return new CritDamageLore(comps[1]);
                case "text.damagelore.name":
                    return new DamageLore(comps[1]);
                case "text.dodgelore.name":
                    return new DodgeLore(comps[1]);
                case "text.harminglore.name":
                    return new HarmingLore(comps[1]);
                case "text.healthlore.name":
                    return new HealthLore(comps[1]);
                case "text.healthregenlore.name":
                    return new HealthRegenLore(comps[1]);
                case "text.ignitionlore.name":
                    return new IgnitionLore(comps[1]);
                case "text.levellore.name":
                    return new LevelLore(itemstack, comps[1]);
                case "text.lifesteallore.name":
                    return new LifeStealLore(comps[1]);
                case "text.poisonlore.name":
                    return new PoisonLore(comps[1]);
                case "text.pvedamagelore.name":
                    return new PvEDamageLore(comps[1]);
                case "text.pvpdamagelore.name":
                    return new PvPDamageLore(comps[1]);
                case "text.reflectlore.name":
                    return new ReflectLore(comps[1]);
                case "text.slowlore.name":
                    return new SlowLore(comps[1]);
                case "text.soulboundlore.name":
                    return new BoundToLore(itemstack, comps[1]);
                case "text.speedlore.name":
                    return new SpeedLore(comps[1]);
                case "text.witherlore.name":
                    return new WitherLore(comps[1]);
                case "text.xpbonuslore.name":
                    return new XpBonusLore(comps[1]);
                }
            } while (true);
        });
    }

    protected final String unlocalizedName;

    protected Lore(String name) {
        this.unlocalizedName = String.format("§6%s§r", name);
    }

    public abstract void applyTo(Stats stats);

    public abstract String getFormattedString();

    public String getLocalizedName() {
        return I18n.format(this.unlocalizedName);
    }

    public String getStatsName() {
        return getLocalizedName();
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }
}
