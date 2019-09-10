package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class ArmourLore extends Lore {
    public final double value;

    public ArmourLore() {
        this.value = IlsMod.SEED.nextGaussian() * 50d;
    }

    public ArmourLore(NBTTagCompound tag) {
        this.value = tag.getDouble("value");
    }

    @Override
    public void applyTo(LivingDamageEvent event) {
        double damage = event.getAmount();
        damage -= damage * this.value / 100;
        if (damage < 0)
            damage = 0;
        event.setAmount((float) damage);
    }

    @Override
    public boolean canApply(ItemStack itemstack) {
        Item item = itemstack.getItem();
        return item instanceof ItemArmor;
    }

    @Override
    public String getFormattedString() {
        if (0 < this.value)
            return String.format("§6Armour§r +%.2f%c", this.value, '%');
        return String.format("§6Armour§r %.2f%c", this.value, '%');
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = super.serializeNBT();
        tag.setDouble("value", this.value);
        return tag;
    }
}
