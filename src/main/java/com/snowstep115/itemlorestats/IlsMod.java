package com.snowstep115.itemlorestats;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.function.Supplier;

import com.snowstep115.itemlorestats.command.IlsCommand;
import com.snowstep115.itemlorestats.lang.ThrowableRunnable;
import com.snowstep115.itemlorestats.lang.ThrowableSupplier;
import com.snowstep115.itemlorestats.proxy.CommonProxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = IlsMod.MODID, name = IlsMod.NAME, version = IlsMod.VERSION)
public final class IlsMod {
    @Instance
    public static IlsMod INSTANCE;

    public static final String CLIENT_PROXY = "com.snowstep115.itemlorestats.proxy.ClientProxy";
    public static final String SERVER_PROXY = "com.snowstep115.itemlorestats.proxy.ServerProxy";

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy PROXY;

    public static final Random SEED = new Random();

    public static final Logger LOGGER = LogManager.getLogger(IlsMod.MODID);
    public static final String MODID = "itemlorestats";
    public static final String NAME = "Item Lore Stats";
    public static final String VERSION = "HEAD";

    public static <T> T execute(ThrowableSupplier<T> task, Supplier<T> alternate) {
        try {
            return task.get();
        } catch (Throwable exception) {
            IlsMod.info("%s", exception.getLocalizedMessage());
            printStackTrace(exception);
            return alternate.get();
        }
    }

    public static void execute(ThrowableRunnable task) {
        try {
            task.run();
        } catch (Throwable exception) {
            IlsMod.info("%s", exception.getLocalizedMessage());
            printStackTrace(exception);
        }
    }

    public static void info(String format, Object... args) {
        IlsMod.LOGGER.info(String.format(format, args));
    }

    public static void info(Entity entity, String format, Object... args) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            String message = String.format(format, args);
            player.sendStatusMessage(new TextComponentString(message), false);
        }
    }

    public static void printStackTrace(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        IlsMod.info("%s", sw.toString());
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        PROXY.preInit(event);
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        PROXY.postInit(event);
    }

    @EventHandler
    public void serverStarting(final FMLServerStartingEvent event) {
        event.registerServerCommand(new IlsCommand());
    }

    @EventHandler
    public void serverStopping(final FMLServerStoppingEvent event) {
    }
}
