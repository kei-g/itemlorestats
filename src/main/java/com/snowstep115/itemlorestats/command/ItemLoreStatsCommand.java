package com.snowstep115.itemlorestats.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ItemLoreStatsCommand implements Command<CommandSource> {
    public static final ItemLoreStatsCommand INSTANCE = new ItemLoreStatsCommand();

    protected LiteralArgumentBuilder<CommandSource> getArgumentBuilder() {
        return Commands.literal("ils");
    }

    public void register(CommandDispatcher<CommandSource> disp) {
        disp.register(getArgumentBuilder());
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        return 0;
    }
}
