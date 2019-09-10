package com.snowstep115.itemlorestats.lore;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public final class DamageLore extends Lore {
    public final int minimumValue;
    public final int maximumValue;

    public DamageLore() {
        int d1, d2;
        do {
            d1 = (int) Math.round(IlsMod.SEED.nextGaussian() * 127d);
            d2 = (int) Math.round(IlsMod.SEED.nextGaussian() * 127d);
        } while (d1 < 0 || d2 < 0);
        this.minimumValue = Math.min(d1, d2);
        this.maximumValue = Math.max(d1, d2);
    }

    public DamageLore(String value) {
        String[] comps = value.split("-");
        this.minimumValue = Integer.parseInt(comps[0]);
        this.maximumValue = Integer.parseInt(comps[1]);
    }

    @Override
    public void applyTo(LivingDamageEvent event) {
        double damage = event.getAmount();
        damage += this.minimumValue + IlsMod.SEED.nextDouble() * (this.maximumValue - this.minimumValue);
        event.setAmount((float) damage);
    }

    @Override
    public boolean canApply(ItemStack itemstack) {
        Item item = itemstack.getItem();
        return item instanceof ItemBow || item instanceof ItemSword || item instanceof ItemTool;
    }

    @Override
    public String getFormattedString() {
        return String.format("§6%s§r +%d-%d", I18n.format("text.damagelore.name"), this.minimumValue,
                this.maximumValue);
    }
}
