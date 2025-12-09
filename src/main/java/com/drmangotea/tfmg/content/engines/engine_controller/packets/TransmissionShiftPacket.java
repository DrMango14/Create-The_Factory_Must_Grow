package com.drmangotea.tfmg.content.engines.engine_controller.packets;

import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;

public class TransmissionShiftPacket extends EngineControllerPacketBase {

	private Collection<Integer> activatedButtons;


	public TransmissionShiftPacket(Collection<Integer> activatedButtons) {
		this(activatedButtons, null);
	}

	public TransmissionShiftPacket(Collection<Integer> activatedButtons, BlockPos controllerPos) {
		super(controllerPos);
		this.activatedButtons = activatedButtons;

	}

	public TransmissionShiftPacket(FriendlyByteBuf buffer) {
		super(buffer);
		activatedButtons = new ArrayList<>();
		int size = buffer.readVarInt();
		for (int i = 0; i < size; i++)
			activatedButtons.add(buffer.readVarInt());
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeVarInt(activatedButtons.size());
		activatedButtons.forEach(buffer::writeVarInt);
	}

	@Override
	protected void handleController(ServerPlayer player, EngineControllerBlockEntity controller) {
		if(activatedButtons.contains(6)) {
			controller.shiftBack();
		}else {

			controller.shiftForward();

		}


	}

}
