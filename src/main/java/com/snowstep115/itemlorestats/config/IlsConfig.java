package com.snowstep115.itemlorestats.config;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = IlsMod.MODID, type = Config.Type.INSTANCE)
public class IlsConfig {
    @LangKey("config.itemlorestats.base_critical_damage.name")
    @RangeDouble(min = 0, max = 1000)
    public static double baseCriticalDamage = 150;

    @LangKey("config.itemlorestats.block_slow_level.name")
    @RangeInt(min = 1, max = 31)
    public static int blockSlowLevel = 3;

    @LangKey("config.itemlorestats.harming_level.name")
    public static int harmingLevel = 1;

    @LangKey("config.itemlorestats.life_steal.name")
    @RangeDouble(min = 0, max = 100)
    public static double lifeSteal = 15;

    @LangKey("config.itemlorestats.slow_level.name")
    @RangeInt(min = 1, max = 31)
    public static int slowLevel = 6;

    @LangKey("config.itemlorestats.poison_level.name")
    @RangeInt(min = 1, max = 31)
    public static int poisonLevel = 3;

    @LangKey("config.itemlorestats.wither_level.name")
    @RangeInt(min = 1, max = 31)
    public static int witherLevel = 1;

    @EventBusSubscriber
    static class ConfigSubscriber {
        @SubscribeEvent
        public static void onConfigChanged(final OnConfigChangedEvent event) {
            if (event.getModID().equals(IlsMod.MODID))
                ConfigManager.load(IlsMod.MODID, Config.Type.INSTANCE);
        }
    }
}
