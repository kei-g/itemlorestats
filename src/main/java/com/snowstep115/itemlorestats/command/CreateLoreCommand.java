package com.snowstep115.itemlorestats.command;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.lore.ArmourLore;
import com.snowstep115.itemlorestats.lore.DamageLore;
import com.snowstep115.itemlorestats.lore.DodgeLore;
import com.snowstep115.itemlorestats.lore.Lore;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public final class CreateLoreCommand extends CommandBase {
    @Override
    public String getName() {
        return "ils";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "ils";
    }

    private static void addLoreItem(EntityPlayerMP entity, Item item, Lore... lores) {
        ItemStack itemstack = new ItemStack(item);
        Consumer<Lore> add = Lore.addDisplayLore(itemstack);
        for (Lore lore : lores)
            add.accept(lore);
        entity.addItemStackToInventory(itemstack);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP mp = (EntityPlayerMP) sender;
        String subcommand = args[0];
        switch (subcommand) {
        case "createlore":
            addLoreItem(mp, Items.IRON_SWORD, new DamageLore());
            addLoreItem(mp, Items.LEATHER_HELMET, new ArmourLore(), new DodgeLore());
            addLoreItem(mp, Items.LEATHER_CHESTPLATE, new ArmourLore(), new DodgeLore());
            addLoreItem(mp, Items.LEATHER_LEGGINGS, new ArmourLore(), new DodgeLore());
            addLoreItem(mp, Items.LEATHER_BOOTS, new ArmourLore(), new DodgeLore());
            break;
        case "stats":
            LinkedHashMap<String, Double> min = new LinkedHashMap<>();
            LinkedHashMap<String, Double> max = new LinkedHashMap<>();
            for (ItemStack equip : mp.getEquipmentAndArmor())
                Lore.deserialize(equip, lore -> lore.makeStatistics(min, max));
            for (String name : max.keySet()) {
                double maxValue = max.get(name);
                if (min.containsKey(name))
                    IlsMod.info(mp, "%s: %.2f-%.2f", name, min.get(name), maxValue);
                else
                    IlsMod.info(mp, "%s: %.2f", name, maxValue);
            }
            break;
        }
    }
}
