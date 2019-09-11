package com.snowstep115.itemlorestats.lore;

import java.util.function.Consumer;

import com.snowstep115.itemlorestats.util.I18n;
import com.snowstep115.itemlorestats.util.ResourceUtil;
import com.snowstep115.itemlorestats.util.StringUtil;

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

    public static void deserialize(NBTTagList list, Consumer<Lore> consumer) {
        for (int i = 0; i < list.tagCount(); i++) {
            String description = list.getStringTagAt(i);
            Lore lore = Lore.parse(description);
            if (lore == null)
                continue;
            consumer.accept(lore);
        }
    }

    public static void deserialize(NBTTagCompound tag, Consumer<Lore> consumer) {
        if (tag == null)
            return;
        if (!tag.hasKey("display"))
            return;
        NBTTagCompound display = tag.getCompoundTag("display");
        if (!display.hasKey("Lore"))
            return;
        NBTTagList lore = display.getTagList("Lore", NBT.TAG_STRING);
        Lore.deserialize(lore, consumer);
    }

    public static void deserialize(ItemStack itemstack, Consumer<Lore> consumer) {
        NBTTagCompound tag = itemstack.getTagCompound();
        Lore.deserialize(tag, consumer);
    }

    public static Lore parse(String description) {
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
                String key = line.substring(0, index);
                String value = line.substring(index + 1);
                if (value.equals(comps[0])) {
                    if ("text.armourlore.name".equals(key)) {
                        return new ArmourLore(comps[1]);
                    }
                    if ("text.damagelore.name".equals(key)) {
                        return new DamageLore(comps[1]);
                    }
                    if ("text.dodgelore.name".equals(key)) {
                        return new DodgeLore(comps[1]);
                    }
                    if ("text.healthlore.name".equals(key)) {
                        return new HealthLore(comps[1]);
                    }
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
