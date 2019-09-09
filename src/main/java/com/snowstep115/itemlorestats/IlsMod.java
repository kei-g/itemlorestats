package com.snowstep115.itemlorestats;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Supplier;

import com.mojang.brigadier.CommandDispatcher;
import com.snowstep115.itemlorestats.command.CreateLoreCommand;
import com.snowstep115.itemlorestats.command.IlsHelpCommand;
import com.snowstep115.itemlorestats.item.LoreArmor;
import com.snowstep115.itemlorestats.item.LoreArmorMaterial;
import com.snowstep115.itemlorestats.item.LoreSword;
import com.snowstep115.itemlorestats.item.LoreSwordTier;
import com.snowstep115.itemlorestats.lang.ThrowableRunnable;
import com.snowstep115.itemlorestats.lang.ThrowableSupplier;
import com.snowstep115.itemlorestats.network.PacketHandler;

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
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(IlsMod.MODID)
public final class IlsMod {
    public static IlsMod INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger(IlsMod.MODID);
    public static final String MODID = "itemlorestats";

    public static <T> T execute(ThrowableSupplier<T> task, Supplier<T> alternate) {
        try {
            return task.get();
        } catch (Throwable exception) {
            printStackTrace(exception);
            return alternate.get();
        }
    }

    public static void execute(ThrowableRunnable task) {
        try {
            task.run();
        } catch (Throwable exception) {
            printStackTrace(exception);
        }
    }

    public static void info(String format, Object... args) {
        IlsMod.LOGGER.info(String.format(format, args));
    }

    public static void printStackTrace(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        IlsMod.info(sw.toString());
    }

    private MinecraftServer server;

    public boolean isServerAbsent() {
        return this.server == null;
    }

    public IlsMod() {
        INSTANCE = this;
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::clientSetup);
        bus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopping);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    private void serverStarting(final FMLServerStartingEvent event) {
        this.server = event.getServer();
        CommandDispatcher<CommandSource> disp = event.getCommandDispatcher();
        CreateLoreCommand.INSTANCE.register(disp);
        IlsHelpCommand.INSTANCE.register(disp);
    }

    private void serverStopping(final FMLServerStoppingEvent event) {
        LoreArmor.save();
        LoreArmorMaterial.save();
        LoreSword.save();
        LoreSwordTier.save();
    }
}
