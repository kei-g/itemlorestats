package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class ArmourLore extends Lore {
    public final double value;

    public ArmourLore() {
        this.value = IlsMod.SEED.nextGaussian() * 50d;
    }

    public ArmourLore(String value) {
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
            return String.format("§6%s§r +%.2f%c", I18n.format("text.armourlore.name"), this.value, '%');
        return String.format("§6%s§r %.2f%c", I18n.format("text.armourlore.name"), this.value, '%');
    }
}
