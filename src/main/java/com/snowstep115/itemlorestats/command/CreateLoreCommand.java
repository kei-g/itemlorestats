package com.snowstep115.itemlorestats.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.snowstep115.itemlorestats.network.CreateLoreMessage;
import com.snowstep115.itemlorestats.network.PacketHandler;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

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
        CreateLoreMessage message = new CreateLoreMessage(name);
        PacketHandler.sendAll(message);
        return 0;
    }
}
