package com.snowstep115.itemlorestats.network;

import java.util.function.Supplier;

import com.snowstep115.itemlorestats.item.LoreArmor;
import com.snowstep115.itemlorestats.item.LoreSword;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public final class CreateLoreMessage {
    public static CreateLoreMessage decode(PacketBuffer buf) {
        String name = buf.readString();
        return new CreateLoreMessage(name);
    }

    public static void encode(CreateLoreMessage message, PacketBuffer buf) {
        buf.writeString(message.name);
    }

    public static void handle(CreateLoreMessage message, Supplier<Context> context) {
        final Context ctx = context.get();
        ctx.enqueueWork(message::handle);
        ctx.setPacketHandled(true);
    }

    private final String name;

    public CreateLoreMessage(String name) {
        this.name = name;
    }

    private void handle() {
        Minecraft.getInstance().deferTask(() -> {
            LoreSword sword = new LoreSword(this.name + " Sword");
            LoreArmor chestplate = new LoreArmor(this.name + " Chestplate", EquipmentSlotType.CHEST);
            LoreArmor boots = new LoreArmor(this.name + " Boots", EquipmentSlotType.FEET);
            LoreArmor helmet = new LoreArmor(this.name + " Helmet", EquipmentSlotType.HEAD);
            LoreArmor leggings = new LoreArmor(this.name + " Leggings", EquipmentSlotType.LEGS);
            ResourceLocation key = ForgeRegistries.ITEMS.getRegistryName();
            ForgeRegistry<Item> registry = RegistryManager.ACTIVE.getRegistry(key);
            registry.unfreeze();
            registry.register(sword);
            registry.register(chestplate);
            registry.register(boots);
            registry.register(helmet);
            registry.register(leggings);
            registry.freeze();
        });
    }
}
