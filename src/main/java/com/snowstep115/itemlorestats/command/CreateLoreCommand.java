package com.snowstep115.itemlorestats.command;

import com.snowstep115.itemlorestats.lore.ArmourLore;
import com.snowstep115.itemlorestats.lore.DamageLore;

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
            mp.addItemStackToInventory(sword);
            ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
            new ArmourLore().applyTo(helmet);
            mp.addItemStackToInventory(helmet);
            ItemStack chestplate = new ItemStack(Items.LEATHER_CHESTPLATE);
            new ArmourLore().applyTo(chestplate);
            mp.addItemStackToInventory(chestplate);
            ItemStack leggings = new ItemStack(Items.LEATHER_LEGGINGS);
            new ArmourLore().applyTo(leggings);
            mp.addItemStackToInventory(leggings);
            ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);
            new ArmourLore().applyTo(boots);
            mp.addItemStackToInventory(boots);
            break;
        }
    }
}
