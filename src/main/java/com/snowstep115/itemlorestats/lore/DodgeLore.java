package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.event.WrappedLivingDamageEvent;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class DodgeLore extends Lore {
    public double possibility;

    public DodgeLore() {
        this.possibility = 10d + IlsMod.SEED.nextGaussian() * 10d;
    }

    public DodgeLore(String value) {
        int index = value.lastIndexOf('%');
        this.possibility = Double.parseDouble(value.substring(0, index));
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
        return String.format("§6%s§r %.2f%c", I18n.format("text.dodgelore.name"), this.possibility, '%');
    }
}
