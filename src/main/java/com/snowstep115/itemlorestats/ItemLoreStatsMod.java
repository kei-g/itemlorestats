package com.snowstep115.itemlorestats;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.mojang.brigadier.CommandDispatcher;
import com.snowstep115.itemlorestats.command.CreateLoreCommand;
import com.snowstep115.itemlorestats.command.ItemLoreStatsCommand;
import com.snowstep115.itemlorestats.lang.ThrowableRunnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ItemLoreStatsMod.MODID)
public final class ItemLoreStatsMod {
    public static ItemLoreStatsMod INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger(ItemLoreStatsMod.MODID);
    public static final String MODID = "itemlorestats";

    public static void execute(ThrowableRunnable task) {
        try {
            task.run();
        } catch (Throwable exception) {
            printStackTrace(exception);
        }
    }

    public static void info(String format, Object... args) {
        ItemLoreStatsMod.LOGGER.info(String.format(format, args));
    }

    public static void printStackTrace(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        ItemLoreStatsMod.info("%s", sw);
    }

    private MinecraftServer server;

    public boolean isServerAbsent() {
        return this.server == null;
    }

    public ItemLoreStatsMod() {
        INSTANCE = this;
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::clientSetup);
        bus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void serverStarting(final FMLServerStartingEvent event) {
        this.server = event.getServer();
        CommandDispatcher<CommandSource> disp = event.getCommandDispatcher();
        ItemLoreStatsCommand.INSTANCE.register(disp);
        CreateLoreCommand.INSTANCE.register(disp);
    }
}
