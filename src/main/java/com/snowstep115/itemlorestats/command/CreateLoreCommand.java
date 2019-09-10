package com.snowstep115.itemlorestats.command;

import com.snowstep115.itemlorestats.lore.ArmourLore;
import com.snowstep115.itemlorestats.lore.DamageLore;
import com.snowstep115.itemlorestats.lore.DodgeLore;
import com.snowstep115.itemlorestats.lore.Lore;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public final class CreateLoreCommand extends CommandBase {
    @Override
    public String getName() {
        return "ils";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "ils createlore";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP mp = (EntityPlayerMP) sender;
        String subcommand = args[0];
        switch (subcommand) {
        case "createlore":
            ItemStack sword = new ItemStack(Items.IRON_SWORD);
            new DamageLore().applyTo(sword);
            Lore.deserialize(sword, Lore.addDisplayLore(sword));
            mp.addItemStackToInventory(sword);
            ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
            new ArmourLore().applyTo(helmet);
            new DodgeLore().applyTo(helmet);
            Lore.deserialize(helmet, Lore.addDisplayLore(helmet));
            mp.addItemStackToInventory(helmet);
            ItemStack chestplate = new ItemStack(Items.LEATHER_CHESTPLATE);
            new ArmourLore().applyTo(chestplate);
            new DodgeLore().applyTo(chestplate);
            Lore.deserialize(chestplate, Lore.addDisplayLore(chestplate));
            mp.addItemStackToInventory(chestplate);
            ItemStack leggings = new ItemStack(Items.LEATHER_LEGGINGS);
            new ArmourLore().applyTo(leggings);
            new DodgeLore().applyTo(leggings);
            Lore.deserialize(leggings, Lore.addDisplayLore(leggings));
            mp.addItemStackToInventory(leggings);
            ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);
            new ArmourLore().applyTo(boots);
            new DodgeLore().applyTo(boots);
            Lore.deserialize(boots, Lore.addDisplayLore(boots));
            mp.addItemStackToInventory(boots);
            break;
        }
    }
}
