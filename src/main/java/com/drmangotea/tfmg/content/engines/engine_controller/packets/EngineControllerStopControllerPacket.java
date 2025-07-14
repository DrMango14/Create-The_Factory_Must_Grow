package com.drmangotea.tfmg.content.engines.engine_controller.packets;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerPacketBase;
import com.simibubi.create.content.redstone.link.controller.LinkedControllerStopLecternPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class EngineControllerStopControllerPacket extends EngineControllerPacketBase {
	public static final StreamCodec<ByteBuf, EngineControllerStopControllerPacket> STREAM_CODEC = BlockPos.STREAM_CODEC.map(
			EngineControllerStopControllerPacket::new, EngineControllerStopControllerPacket::getControllerPos
	);


	public EngineControllerStopControllerPacket(BlockPos lecternPos) {
		super(lecternPos);
	}

	@Override
	protected void handleItem(ServerPlayer player, ItemStack heldItem) {

	}

	@Override
	protected void handleLectern(ServerPlayer player, EngineControllerBlockEntity lectern) {
		lectern.tryStopUsing(player);
	}

	@Override
	public PacketTypeProvider getTypeProvider() {
		return TFMGPackets.ENGINE_CONTROLLER_STOP_CONTROL;
	}
}
