package com.snowstep115.itemlorestats.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PreventWearingMessage
        implements IMessage, IMessageHandler<PreventWearingMessage, PreventWearingMessage> {
    private EnumHand hand;
    private ItemStack itemstackFrom;
    private ItemStack itemstackTo;
    private Integer slotIndex;
    private EntityEquipmentSlot.Type slotType;

    public PreventWearingMessage() {
    }

    public PreventWearingMessage(LivingEquipmentChangeEvent event) {
        EntityEquipmentSlot slot = event.getSlot();
        this.hand = slot == EntityEquipmentSlot.MAINHAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        this.itemstackFrom = event.getFrom();
        this.itemstackTo = event.getTo();
        this.slotIndex = slot.getIndex();
        this.slotType = slot.getSlotType();
        onMessage((EntityPlayer) event.getEntityLiving());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.hand = EnumHand.values()[buf.readInt()];
        this.itemstackFrom = ByteBufUtils.readItemStack(buf);
        this.itemstackTo = ByteBufUtils.readItemStack(buf);
        this.slotIndex = buf.readInt();
        this.slotType = EntityEquipmentSlot.Type.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.hand.ordinal());
        ByteBufUtils.writeItemStack(buf, this.itemstackFrom);
        ByteBufUtils.writeItemStack(buf, this.itemstackTo);
        buf.writeInt(this.slotIndex);
        buf.writeInt(this.slotType.ordinal());
    }

    private void onMessage(EntityPlayer player) {
        switch (this.slotType) {
        case HAND:
            switch (this.hand) {
            case MAIN_HAND:
                player.inventory.currentItem = (player.inventory.getSlotFor(this.itemstackTo) + 1) % 9;
                break;
            case OFF_HAND:
                prevent(player, player.inventory.offHandInventory);
                break;
            }
            break;
        case ARMOR:
            prevent(player, player.inventory.armorInventory);
            break;
        }
    }

    private void prevent(EntityPlayer player, NonNullList<ItemStack> inventory) {
        ItemStack itemstack = this.itemstackFrom;
        inventory.set(this.slotIndex, ItemStack.EMPTY);
        if (!itemstack.isEmpty())
            player.inventory.addItemStackToInventory(itemstack);
        player.inventory.setItemStack(this.itemstackTo);
    }

    @Override
    public PreventWearingMessage onMessage(PreventWearingMessage message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> message.onMessage(mc.player));
        return null;
    }
}
