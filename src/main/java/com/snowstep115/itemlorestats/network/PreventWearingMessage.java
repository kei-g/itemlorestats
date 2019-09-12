package com.snowstep115.itemlorestats.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PreventWearingMessage
        implements IMessage, IMessageHandler<PreventWearingMessage, PreventWearingMessage> {
    private EnumHand hand;
    private ItemStack itemstack;
    private Integer slotIndex;
    private EntityEquipmentSlot.Type slotType;

    public PreventWearingMessage() {
    }

    public PreventWearingMessage(EnumHand hand, ItemStack itemstack, EntityEquipmentSlot slot) {
        this.hand = hand;
        this.itemstack = itemstack;
        this.slotIndex = slot.getIndex();
        this.slotType = slot.getSlotType();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.hand = EnumHand.values()[buf.readInt()];
        this.itemstack = ByteBufUtils.readItemStack(buf);
        this.slotIndex = buf.readInt();
        this.slotType = EntityEquipmentSlot.Type.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.hand.ordinal());
        ByteBufUtils.writeItemStack(buf, this.itemstack);
        buf.writeInt(this.slotIndex);
        buf.writeInt(this.slotType.ordinal());
    }

    private void onMessage(Minecraft mc) {
        EntityPlayerSP player = mc.player;
        switch (this.slotType) {
        case HAND:
            switch (this.hand) {
            case MAIN_HAND:
                player.inventory.currentItem = (player.inventory.getSlotFor(this.itemstack) + 1) % 9;
                break;
            case OFF_HAND:
                player.inventory.offHandInventory.set(this.slotIndex, ItemStack.EMPTY);
                player.inventory.setItemStack(this.itemstack);
                break;
            }
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
