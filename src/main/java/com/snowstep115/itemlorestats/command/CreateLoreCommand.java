package com.snowstep115.itemlorestats.command;

import com.snowstep115.itemlorestats.item.LoreArmor;
import com.snowstep115.itemlorestats.item.LoreSword;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public final class CreateLoreCommand extends CommandBase {
    @Override
    public String getName() {
        return "ils";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "ils createlore <name>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String subcommand = args[0];
        switch (subcommand) {
        case "createlore":
            String name = args[1];
            LoreSword sword = new LoreSword(name + " Sword");
            LoreArmor chestplate = new LoreArmor(name + " Chestplate", EntityEquipmentSlot.CHEST);
            LoreArmor boots = new LoreArmor(name + " Boots", EntityEquipmentSlot.FEET);
            LoreArmor helmet = new LoreArmor(name + " Helmet", EntityEquipmentSlot.HEAD);
            LoreArmor leggings = new LoreArmor(name + " Leggings", EntityEquipmentSlot.LEGS);
            ForgeRegistry<Item> registry = (ForgeRegistry<Item>) ForgeRegistries.ITEMS;
            registry.unfreeze();
            registry.register(sword);
            registry.register(chestplate);
            registry.register(boots);
            registry.register(helmet);
            registry.register(leggings);
            registry.freeze();
            break;
        }
    }
}
