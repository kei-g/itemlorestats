package com.snowstep115.itemlorestats.lore;

import java.util.Map;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class ArmourLore extends Lore {
    public final double value;

    public ArmourLore() {
        super("text.armourlore.name");
        this.value = IlsMod.SEED.nextGaussian() * 50d;
    }

    public ArmourLore(String value) {
        super("text.armourlore.name");
        int index = value.lastIndexOf('%');
        this.value = Double.parseDouble(value.substring(0, index));
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
            return String.format("%s +%.2f%c", getLocalizedName(), this.value, '%');
        return String.format("%s %.2f%c", getLocalizedName(), this.value, '%');
    }

    @Override
    public void makeStatistics(Map<String, Double> min, Map<String, Double> max) {
        max.compute(getLocalizedName(), ($, value) -> value == null ? this.value : value + this.value);
    }
}
