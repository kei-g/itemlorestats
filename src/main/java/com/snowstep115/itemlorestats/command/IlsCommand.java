package com.snowstep115.itemlorestats.command;

import java.util.function.Consumer;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.config.IlsConfig;
import com.snowstep115.itemlorestats.lore.ArmourLore;
import com.snowstep115.itemlorestats.lore.BlindLore;
import com.snowstep115.itemlorestats.lore.BlockLore;
import com.snowstep115.itemlorestats.lore.CritChanceLore;
import com.snowstep115.itemlorestats.lore.CritDamageLore;
import com.snowstep115.itemlorestats.lore.DamageLore;
import com.snowstep115.itemlorestats.lore.DodgeLore;
import com.snowstep115.itemlorestats.lore.HarmingLore;
import com.snowstep115.itemlorestats.lore.HealthLore;
import com.snowstep115.itemlorestats.lore.HealthRegenLore;
import com.snowstep115.itemlorestats.lore.IgnitionLore;
import com.snowstep115.itemlorestats.lore.LifeStealLore;
import com.snowstep115.itemlorestats.lore.Lore;
import com.snowstep115.itemlorestats.lore.PoisonLore;
import com.snowstep115.itemlorestats.lore.PvEDamageLore;
import com.snowstep115.itemlorestats.lore.PvPDamageLore;
import com.snowstep115.itemlorestats.lore.ReflectLore;
import com.snowstep115.itemlorestats.lore.SlowLore;
import com.snowstep115.itemlorestats.lore.SpeedLore;
import com.snowstep115.itemlorestats.lore.Stats;
import com.snowstep115.itemlorestats.lore.WitherLore;
import com.snowstep115.itemlorestats.lore.XpBonusLore;
import com.snowstep115.itemlorestats.util.CombatLog;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Type;

public final class IlsCommand extends CommandBase {
    @Override
    public String getName() {
        return "ils";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/ils Shows help.";
    }

    private static void addLoreItem(EntityPlayerMP entity, Item item, Lore... lores) {
        ItemStack itemstack = new ItemStack(item);
        Consumer<Lore> add = Lore.addDisplayLore(itemstack);
        for (Lore lore : lores)
            add.accept(lore);
        entity.addItemStackToInventory(itemstack);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP mp = (EntityPlayerMP) sender;
        if (args.length < 1) {
            IlsMod.info(mp, "/ils combatlog <actionbar|chat|none>");
            IlsMod.info(mp, "    Changes display position of combat logs.");
            IlsMod.info(mp, "/ils createlore");
            IlsMod.info(mp, "    Creates a set of gear with lore to test the plugin.");
            IlsMod.info(mp, "/ils lore <player_name> <lore> <value>");
            IlsMod.info(mp, "    Adds or sets the lore line to the item currently in the players main hand. <player_name> must be online.");
            IlsMod.info(mp, "/ils name <name>");
            IlsMod.info(mp, "    Sets the name of the item currently in your main hand. Supports all text formatting.");
            IlsMod.info(mp, "/ils stats");
            IlsMod.info(mp, "    Will display the total stats of your armour and item in your hand.");
            return;
        }
        String subcommand = args[0];
        switch (subcommand) {
        case "combatlog":
            if (args.length < 2)
                IlsMod.info(mp, "<actionbar|char|none> is not specified.");
            else {
                IlsConfig.displayPositionOfCombatLog = CombatLog.valueOf(args[1]);
                ConfigManager.sync(IlsMod.MODID, Type.INSTANCE);
            }
            break;
        case "createlore":
            addLoreItem(mp, Items.IRON_SWORD, new DamageLore(), new CritChanceLore(), new CritDamageLore(),
                    new LifeStealLore(), new IgnitionLore(), new SlowLore(), new PoisonLore(), new WitherLore(),
                    new HarmingLore(), new BlindLore(), new XpBonusLore(), new PvEDamageLore(), new PvPDamageLore());
            addLoreItem(mp, Items.LEATHER_HELMET, new ArmourLore(), new BlockLore(), new DodgeLore(), new HealthLore(),
                    new HealthRegenLore(), new XpBonusLore());
            addLoreItem(mp, Items.LEATHER_CHESTPLATE, new ArmourLore(), new BlockLore(), new DodgeLore(),
                    new HealthLore(), new HealthRegenLore(), new ReflectLore(), new XpBonusLore());
            addLoreItem(mp, Items.LEATHER_LEGGINGS, new ArmourLore(), new BlockLore(), new DodgeLore(),
                    new HealthLore(), new HealthRegenLore(), new XpBonusLore());
            addLoreItem(mp, Items.LEATHER_BOOTS, new ArmourLore(), new BlockLore(), new DodgeLore(), new HealthLore(),
                    new HealthRegenLore(), new SpeedLore(), new XpBonusLore());
            break;
        case "lore":
            if (args.length < 2)
                IlsMod.info(mp, "<player_name> is not specified.");
            else if (args.length < 3)
                IlsMod.info(mp, "<lore> is not specified.");
            else if (args.length < 4)
                IlsMod.info(mp, "<value> is not specified.");
            else {
                EntityPlayerMP mp2 = IlsMod.INSTANCE.getPlayerByUsername(args[1]);
                if (mp2 == null) {
                    IlsMod.info(mp, "%s does not exist", args[1]);
                    return;
                }
                ItemStack equip = mp2.getHeldItemMainhand();
                if (equip.isEmpty()) {
                    IlsMod.info(mp, "%s does not have any item on mainhand", args[1]);
                    return;
                }
                String[] comps = new String[args.length - 2];
                for (int i = 0; i < args.length - 2; i++)
                    comps[i] = args[2 + i];
                String value = String.join(" ", comps);
                do {
                    int index = value.indexOf('"');
                    if (index < 0)
                        break;
                    value = value.substring(0, index) + value.substring(index + 1);
                } while (true);
                Lore lore = Lore.parse(mp2, equip, value);
                if (lore == null) {
                    IlsMod.info(mp, "No lore is found for %s", value);
                    return;
                }
                Consumer<Lore> add = Lore.addDisplayLore(equip);
                add.accept(lore);
                IlsMod.info(mp2, "You are given %s lore by %s.", value, mp.getName());
            }
            break;
        case "name":
            if (args.length < 2)
                IlsMod.info(mp, "<name> is not specified");
            else {
                ItemStack itemstack = mp.getHeldItemMainhand();
                if (itemstack.isEmpty())
                    return;
                String[] comps = new String[args.length - 1];
                for (int i = 0; i < args.length - 1; i++)
                    comps[i] = args[1 + i];
                String name = String.join(" ", comps);
                itemstack.setStackDisplayName(name);
            }
            break;
        case "stats":
            Stats stats = Stats.strictStats(mp);
            IlsMod.info(mp, "%s: %.2f-%.2f", new DamageLore().getStatsName(), stats.damageMin, stats.damageMax);
            IlsMod.info(mp, "%s: %s", new ArmourLore().getStatsName(), stats.reduction);
            IlsMod.info(mp, "%s: %s", new DodgeLore().getStatsName(), stats.dodge);
            IlsMod.info(mp, "%s: %s", new BlockLore().getStatsName(), stats.block);
            IlsMod.info(mp, "%s: %s", new CritChanceLore().getStatsName(), stats.critical);
            IlsMod.info(mp, "%s: %.2f%%", new CritDamageLore().getStatsName(),
                    stats.criticalDamage - IlsConfig.baseCriticalDamage);
            IlsMod.info(mp, "%s: %.2f/%.2f", new HealthLore().getStatsName(),
                    mp.getHealth() * stats.health / mp.getMaxHealth(), stats.health);
            IlsMod.info(mp, "%s: %.2f%%", new HealthRegenLore().getStatsName(), stats.regeneration);
            IlsMod.info(mp, "%s: %s", new LifeStealLore().getStatsName(), stats.lifeSteal);
            IlsMod.info(mp, "%s: %s", new IgnitionLore().getStatsName(), stats.ignition);
            IlsMod.info(mp, "%s: %s", new ReflectLore().getStatsName(), stats.reflection);
            IlsMod.info(mp, "%s: %s", new SlowLore().getStatsName(), stats.slowness);
            IlsMod.info(mp, "%s: %s", new PoisonLore().getStatsName(), stats.poison);
            IlsMod.info(mp, "%s: %s", new WitherLore().getStatsName(), stats.wither);
            IlsMod.info(mp, "%s: %s", new HarmingLore().getStatsName(), stats.harming);
            IlsMod.info(mp, "%s: %s", new BlindLore().getStatsName(), stats.blind);
            IlsMod.info(mp, "%s: %.2f%%", new SpeedLore().getStatsName(), stats.speed);
            IlsMod.info(mp, "%s: %.2f%%", new XpBonusLore().getStatsName(), stats.xpBonus);
            IlsMod.info(mp, "%s: %.2f", new PvEDamageLore().getStatsName(), stats.damagePvE);
            IlsMod.info(mp, "%s: %.2f", new PvPDamageLore().getStatsName(), stats.damagePvP);
            break;
        default:
            IlsMod.info(mp, "Unknown subcommand, %s", subcommand);
            break;
        }
    }
}
