package com.snowstep115.itemlorestats.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public final class IlsHelpCommand extends IlsCommand {
    public static final IlsHelpCommand INSTANCE = new IlsHelpCommand();

    @Override
    protected LiteralArgumentBuilder<CommandSource> getArgumentBuilder() {
        return super.getArgumentBuilder().then(Commands.literal("help").executes(this));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        PlayerEntity entity = context.getSource().asPlayer();
        entity.sendStatusMessage(new StringTextComponent("/ils createlore <name>"), false);
        entity.sendStatusMessage(new StringTextComponent("  Creates a set of gear with lore to test the plugin."), false);
        return 0;
    }
}
