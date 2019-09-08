package com.snowstep115.itemlorestats.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.snowstep115.itemlorestats.eventhandler.ModEventHandler;
import com.snowstep115.itemlorestats.item.LoreSword;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public final class CreateLoreCommand extends ItemLoreStatsCommand {
    public static final CreateLoreCommand INSTANCE = new CreateLoreCommand();

    @Override
    public LiteralArgumentBuilder<CommandSource> getArgumentBuilder() {
        return super.getArgumentBuilder().then(Commands.literal("createlore")
                .then(Commands.argument("name", StringArgumentType.word()).executes(this)));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");
        LoreSword sword = new LoreSword(name);
        ModEventHandler.ITEM_REGISTRY.unfreeze();
        ModEventHandler.ITEM_REGISTRY.register(sword);
        ModEventHandler.ITEM_REGISTRY.freeze();
        return 0;
    }
}
