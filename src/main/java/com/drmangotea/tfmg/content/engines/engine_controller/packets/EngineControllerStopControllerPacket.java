package com.drmangotea.tfmg.content.engines.engine_controller.packets;

import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class EngineControllerStopControllerPacket extends EngineControllerPacketBase {

	public EngineControllerStopControllerPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}

	public EngineControllerStopControllerPacket(BlockPos lecternPos) {
		super(lecternPos);
	}

	@Override
	protected void handleController(ServerPlayer player, EngineControllerBlockEntity lectern) {
		lectern.tryStopUsing(player);
	}



}
