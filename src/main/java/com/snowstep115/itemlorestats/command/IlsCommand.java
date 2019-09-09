package com.snowstep115.itemlorestats.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public abstract class IlsCommand implements Command<CommandSource> {
    protected LiteralArgumentBuilder<CommandSource> getArgumentBuilder() {
        return Commands.literal("ils");
    }

    public void register(CommandDispatcher<CommandSource> disp) {
        disp.register(getArgumentBuilder());
    }
}
