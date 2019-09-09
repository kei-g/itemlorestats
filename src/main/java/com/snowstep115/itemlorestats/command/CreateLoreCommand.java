package com.snowstep115.itemlorestats.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.snowstep115.itemlorestats.eventhandler.ModEventHandler;
import com.snowstep115.itemlorestats.item.LoreArmor;
import com.snowstep115.itemlorestats.item.LoreSword;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.inventory.EquipmentSlotType;

public final class CreateLoreCommand extends IlsCommand {
    public static final CreateLoreCommand INSTANCE = new CreateLoreCommand();

    @Override
    protected LiteralArgumentBuilder<CommandSource> getArgumentBuilder() {
        return super.getArgumentBuilder().then(Commands.literal("createlore")
                .then(Commands.argument("name", StringArgumentType.word()).executes(this)));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");
        LoreSword sword = new LoreSword(name + " Sword");
        LoreArmor chestplate = new LoreArmor(name + " Chestplate", EquipmentSlotType.CHEST);
        LoreArmor boots = new LoreArmor(name + " Boots", EquipmentSlotType.FEET);
        LoreArmor helmet = new LoreArmor(name + " Helmet", EquipmentSlotType.HEAD);
        LoreArmor leggings = new LoreArmor(name + " Leggings", EquipmentSlotType.LEGS);
        ModEventHandler.ITEM_REGISTRY.unfreeze();
        ModEventHandler.ITEM_REGISTRY.register(sword);
        ModEventHandler.ITEM_REGISTRY.register(chestplate);
        ModEventHandler.ITEM_REGISTRY.register(boots);
        ModEventHandler.ITEM_REGISTRY.register(helmet);
        ModEventHandler.ITEM_REGISTRY.register(leggings);
        ModEventHandler.ITEM_REGISTRY.freeze();
        return 0;
    }
}
