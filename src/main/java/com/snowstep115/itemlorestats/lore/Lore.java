package com.snowstep115.itemlorestats.lore;

import java.lang.reflect.Constructor;
import java.util.function.Consumer;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public abstract class Lore {
    private static final String KEY = "com.snowstep115.itemlorestats";
    private static final String NAMESPACE = KEY + ".lore";

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
        return lore -> {
            NBTTagString str = new NBTTagString(lore.getFormattedString());
            list.appendTag(str);
        };
    }

    @SuppressWarnings("unchecked")
    public static Lore deserializeFrom(NBTTagCompound tag) {
        return IlsMod.execute(() -> {
            String loreClass = NAMESPACE + "." + tag.getString("LoreClass");
            Class<? extends Lore> clazz = (Class<? extends Lore>) Lore.class.getClassLoader().loadClass(loreClass);
            Constructor<? extends Lore> ctor = clazz.getDeclaredConstructor(NBTTagCompound.class);
            ctor.setAccessible(true);
            return ctor.newInstance(tag);
        }, () -> null);
    }

    public static void deserialize(NBTTagList list, Consumer<Lore> consumer) {
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound loreTag = list.getCompoundTagAt(i);
            Lore lore = Lore.deserializeFrom(loreTag);
            if (lore == null)
                continue;
            consumer.accept(lore);
        }
    }

    public static void deserialize(NBTTagCompound tag, Consumer<Lore> consumer) {
        if (tag == null)
            return;
        NBTTagList list = tag.getTagList(Lore.KEY, NBT.TAG_COMPOUND);
        deserialize(list, consumer);
    }

    public static void deserialize(ItemStack itemstack, Consumer<Lore> consumer) {
        NBTTagCompound tag = itemstack.getTagCompound();
        deserialize(tag, consumer);
    }

    public void applyTo(ItemStack itemstack) {
        if (!itemstack.hasTagCompound())
            itemstack.setTagCompound(new NBTTagCompound());
        NBTTagCompound tag = itemstack.getTagCompound();
        if (!tag.hasKey(Lore.KEY))
            tag.setTag(Lore.KEY, new NBTTagList());
        NBTTagList list = tag.getTagList(Lore.KEY, NBT.TAG_COMPOUND);
        NBTTagCompound nbt = serializeNBT();
        list.appendTag(nbt);
    }

    public abstract void applyTo(LivingDamageEvent event);

    public abstract boolean canApply(ItemStack itemstack);

    public abstract String getFormattedString();

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("LoreClass", getClass().getSimpleName());
        return tag;
    }
}
