package com.snowstep115.itemlorestats.lore;

import java.util.Map;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.event.WrappedLivingDamageEvent;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class DodgeLore extends Lore {
    public double possibility;

    public DodgeLore() {
        super("text.dodgelore.name");
        this.possibility = 10d + IlsMod.SEED.nextGaussian() * 10d;
    }

    public DodgeLore(String value) {
        super("text.dodgelore.name");
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
        return String.format("%s %.2f%c", getLocalizedName(), this.possibility, '%');
    }

    @Override
    public void makeStatistics(Map<String, Double> min, Map<String, Double> max) {
        max.compute(getLocalizedName(), ($, value) -> value == null ? this.possibility : value + this.possibility);
    }
}
