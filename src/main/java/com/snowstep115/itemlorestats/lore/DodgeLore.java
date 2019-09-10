package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.event.WrappedLivingDamageEvent;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class DodgeLore extends Lore {
    public double possibility;

    public DodgeLore() {
        this.possibility = 10d + IlsMod.SEED.nextGaussian() * 10d;
    }

    public DodgeLore(NBTTagCompound tag) {
        this.possibility = tag.getDouble("possibility");
    }

    @Override
    public void applyTo(LivingDamageEvent event) {
        if (event instanceof WrappedLivingDamageEvent) {
            WrappedLivingDamageEvent wrapped = (WrappedLivingDamageEvent) event;
            if (IlsMod.SEED.nextDouble() * 100 <= this.possibility)
                wrapped.dodged.set(true);
            event.setAmount(0);
        }
    }

    @Override
    public boolean canApply(ItemStack itemstack) {
        Item item = itemstack.getItem();
        return item instanceof ItemArmor;
    }

    @Override
    public String getFormattedString() {
        return String.format("§6Dodge§r %.2f%c", this.possibility, '%');
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = super.serializeNBT();
        tag.setDouble("possibility", this.possibility);
        return tag;
    }
}
