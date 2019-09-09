package com.snowstep115.itemlorestats.network;

import com.snowstep115.itemlorestats.IlsMod;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class PacketHandler {
    private static final SimpleChannel HANDLER;
    private static final ResourceLocation NAME = new ResourceLocation(IlsMod.MODID, "main");
    private static final String PROTOCOL_VERSION = Integer.toString(1);

    static {
        HANDLER = NetworkRegistry.ChannelBuilder.named(NAME).clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION)
                .simpleChannel();
        int index = 0;
        HANDLER.messageBuilder(CreateLoreMessage.class, index++).consumer(CreateLoreMessage::handle)
                .decoder(CreateLoreMessage::decode).encoder(CreateLoreMessage::encode).add();
    }

    public static void init() {
    }

    public static <T> void sendAll(T message) {
        HANDLER.send(PacketDistributor.ALL.noArg(), message);
    }

    public static <T> void sendTo(ServerPlayerEntity player, T message) {
        HANDLER.sendTo(message, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <T> void sendToServer(T message) {
        HANDLER.sendToServer(message);
    }
}
