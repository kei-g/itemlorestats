package com.snowstep115.itemlorestats.lore;

import java.util.function.Consumer;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

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

    public static Lore deserializeFrom(String description) {
        String[] comps = description.split(" ");
        if (comps.length < 2)
            return null;
        while (true) {
            int index = comps[0].indexOf('§');
            if (index < 0)
                break;
            comps[0] = comps[0].substring(0, index) + comps[0].substring(index + 2);
        }
        if (I18n.format("text.armourlore.name").equals(comps[0])) {
            return new ArmourLore(comps[1]);
        } else if (I18n.format("text.damagelore.name").equals(comps[0])) {
            return new DamageLore(comps[1]);
        } else if (I18n.format("text.dodgelore.name").equals(comps[0])) {
            return new DodgeLore(comps[1]);
        }
        IlsMod.info("%s, not matched", description);
        return null;
    }

    public static void deserialize(NBTTagList list, Consumer<Lore> consumer) {
        for (int i = 0; i < list.tagCount(); i++) {
            String loreTag = list.getStringTagAt(i);
            Lore lore = Lore.deserializeFrom(loreTag);
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

    public abstract void applyTo(LivingDamageEvent event);

    public abstract boolean canApply(ItemStack itemstack);

    public abstract String getFormattedString();
}
