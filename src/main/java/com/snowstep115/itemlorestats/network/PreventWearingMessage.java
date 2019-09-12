package com.snowstep115.itemlorestats.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PreventWearingMessage
        implements IMessage, IMessageHandler<PreventWearingMessage, PreventWearingMessage> {
    private ItemStack itemstack;
    private Integer slotIndex;
    private EntityEquipmentSlot.Type slotType;

    public PreventWearingMessage() {
    }

    public PreventWearingMessage(ItemStack itemstack, EntityEquipmentSlot slot) {
        this.itemstack = itemstack;
        this.slotIndex = slot.getIndex();
        this.slotType = slot.getSlotType();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.itemstack = ByteBufUtils.readItemStack(buf);
        this.slotIndex = buf.readInt();
        this.slotType = EntityEquipmentSlot.Type.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.itemstack);
        buf.writeInt(this.slotIndex);
        buf.writeInt(this.slotType.ordinal());
    }

    private void onMessage(Minecraft mc) {
        EntityPlayerSP player = mc.player;
        switch (this.slotType) {
        case HAND:
            break;
        case ARMOR:
            player.inventory.armorInventory.set(this.slotIndex, ItemStack.EMPTY);
            player.inventory.setItemStack(this.itemstack);
            break;
        }
    }

    @Override
    public PreventWearingMessage onMessage(PreventWearingMessage message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> message.onMessage(mc));
        return null;
    }
}
