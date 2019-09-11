package com.snowstep115.itemlorestats.command;

import java.util.function.Consumer;

import com.snowstep115.itemlorestats.IlsMod;
import com.snowstep115.itemlorestats.lore.ArmourLore;
import com.snowstep115.itemlorestats.lore.BlockLore;
import com.snowstep115.itemlorestats.lore.DamageLore;
import com.snowstep115.itemlorestats.lore.DodgeLore;
import com.snowstep115.itemlorestats.lore.HealthLore;
import com.snowstep115.itemlorestats.lore.HealthRegenLore;
import com.snowstep115.itemlorestats.lore.Lore;
import com.snowstep115.itemlorestats.lore.Stats;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public final class IlsCommand extends CommandBase {
    @Override
    public String getName() {
        return "ils";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "ils";
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
        String subcommand = args[0];
        switch (subcommand) {
        case "createlore":
            addLoreItem(mp, Items.IRON_SWORD, new DamageLore());
            addLoreItem(mp, Items.LEATHER_HELMET, new ArmourLore(), new BlockLore(), new DodgeLore(), new HealthLore(),
                    new HealthRegenLore());
            addLoreItem(mp, Items.LEATHER_CHESTPLATE, new ArmourLore(), new BlockLore(), new DodgeLore(),
                    new HealthLore(), new HealthRegenLore());
            addLoreItem(mp, Items.LEATHER_LEGGINGS, new ArmourLore(), new BlockLore(), new DodgeLore(),
                    new HealthLore(), new HealthRegenLore());
            addLoreItem(mp, Items.LEATHER_BOOTS, new ArmourLore(), new BlockLore(), new DodgeLore(), new HealthLore(),
                    new HealthRegenLore());
            break;
        case "stats":
            Stats stats = new Stats();
            for (ItemStack equip : mp.getEquipmentAndArmor())
                Lore.deserialize(equip, lore -> lore.applyTo(stats));
            IlsMod.info(mp, "%s: %.2f-%.2f", new DamageLore().getStatsName(), stats.damageMin, stats.damageMax);
            IlsMod.info(mp, "%s: %.2f%%", new ArmourLore().getStatsName(), stats.reduction);
            IlsMod.info(mp, "%s: %.2f%%", new DodgeLore().getStatsName(), stats.dodge);
            IlsMod.info(mp, "%s: %.2f%%", new BlockLore().getStatsName(), stats.block);
            IlsMod.info(mp, "%s: %.2f/%.2f", new HealthLore().getStatsName(), mp.getHealth() * stats.health / 20,
                    stats.health);
            IlsMod.info(mp, "%s: %.2f%%", new HealthRegenLore().getStatsName(), stats.regeneration);
            break;
        }
    }
}
