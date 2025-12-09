package com.drmangotea.tfmg.content.engines.engine_controller.packets;

import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerServerHandler;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class EngineControllerInputPacket extends EngineControllerPacketBase {

    private Collection<Integer> activatedButtons;
    private boolean press;

    public EngineControllerInputPacket(Collection<Integer> activatedButtons, boolean press) {
        this(activatedButtons, press, null);
    }

    public EngineControllerInputPacket(Collection<Integer> activatedButtons, boolean press, BlockPos lecternPos) {
        super(lecternPos);
        this.activatedButtons = activatedButtons;
        this.press = press;
    }

    public EngineControllerInputPacket(FriendlyByteBuf buffer) {
        super(buffer);
        activatedButtons = new ArrayList<>();
        press = buffer.readBoolean();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++)
            activatedButtons.add(buffer.readVarInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
        buffer.writeBoolean(press);
        buffer.writeVarInt(activatedButtons.size());
        activatedButtons.forEach(buffer::writeVarInt);
    }

    @Override
    protected void handleController(ServerPlayer player, EngineControllerBlockEntity controller) {
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


}
