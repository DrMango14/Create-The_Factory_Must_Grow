package com.drmangotea.tfmg.content.engines.engine_controller.packets;

import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.content.redstone.link.controller.*;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.*;

public class EngineControllerInputPacket extends EngineControllerPacketBase {

    public static final StreamCodec<ByteBuf, EngineControllerInputPacket> STREAM_CODEC = StreamCodec.composite(
            CatnipStreamCodecBuilders.list(ByteBufCodecs.INT), p -> p.activatedButtons,
            ByteBufCodecs.BOOL, p -> p.press,
            CatnipStreamCodecs.NULLABLE_BLOCK_POS, EngineControllerInputPacket::getControllerPos,
            EngineControllerInputPacket::new
    );


    private List<Integer> activatedButtons;
    private boolean press;

    public EngineControllerInputPacket(List<Integer> activatedButtons, boolean press) {
        this(activatedButtons, press, null);
    }

    public EngineControllerInputPacket(List<Integer> activatedButtons, boolean press, BlockPos lecternPos) {
        super(lecternPos);
        this.activatedButtons = activatedButtons;
        this.press = press;
    }


    @Override
    protected void handleItem(ServerPlayer player, ItemStack heldItem) {

    }

    @Override
    protected void handleLectern(ServerPlayer player, EngineControllerBlockEntity controller) {
        UUID uniqueID = player.getUUID();
        Level level = controller.getLevel();
        BlockPos pos = controller.getBlockPos();

        List<Couple<RedstoneLinkNetworkHandler.Frequency>> list = new ArrayList<>();
        if (activatedButtons.contains(2))
            list.add(EngineControllerBlockEntity.toFrequency(controller, 0));

        if (activatedButtons.contains(3))
            list.add(EngineControllerBlockEntity.toFrequency(controller, 1));

        if (activatedButtons.contains(7))
            list.add(EngineControllerBlockEntity.toFrequency(controller, 2));


        LinkedControllerServerHandler.receivePressed(level, pos, uniqueID, list, press);


        //LinkedControllerServerHandler.receivePressed(controller.getLevel(), controller.getBlockPos(), uniqueID, activatedButtons.stream()
        //        .map(i -> EngineControllerBlockEntity.toFrequency(controller, i))
        //        .collect(Collectors.toList()), press);
        controller.handleInput(activatedButtons, press);
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.ENGINE_CONTROLLER_INPUT;
    }
}
