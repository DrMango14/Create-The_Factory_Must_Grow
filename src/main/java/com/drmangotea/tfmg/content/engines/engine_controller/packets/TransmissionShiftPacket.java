package com.drmangotea.tfmg.content.engines.engine_controller.packets;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import com.drmangotea.tfmg.registry.TFMGPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransmissionShiftPacket extends EngineControllerPacketBase {

	public static final StreamCodec<ByteBuf, TransmissionShiftPacket> STREAM_CODEC = StreamCodec.composite(
			CatnipStreamCodecBuilders.list(ByteBufCodecs.INT), p -> p.activatedButtons,
			CatnipStreamCodecs.NULLABLE_BLOCK_POS, TransmissionShiftPacket::getControllerPos,
			TransmissionShiftPacket::new
	);

	private List<Integer> activatedButtons;


	public TransmissionShiftPacket(List<Integer> activatedButtons) {
		this(activatedButtons, null);
	}

	public TransmissionShiftPacket(List<Integer> activatedButtons, BlockPos controllerPos) {
		super(controllerPos);
		this.activatedButtons = activatedButtons;

	}

	@Override
	protected void handleItem(ServerPlayer player, ItemStack heldItem) {

	}

	@Override
	protected void handleLectern(ServerPlayer player, EngineControllerBlockEntity controller) {
		if(activatedButtons.contains(6)) {
			controller.shiftBack();
		}else {

			controller.shiftForward();

		}
	}

	@Override
	public PacketTypeProvider getTypeProvider() {
		return TFMGPackets.ENGINE_TRANSMISSION_PACKET;
	}
}
