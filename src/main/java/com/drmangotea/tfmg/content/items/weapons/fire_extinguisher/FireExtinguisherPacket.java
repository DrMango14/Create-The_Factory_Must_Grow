package com.drmangotea.tfmg.content.items.weapons.fire_extinguisher;


import com.drmangotea.tfmg.TFMGClient;
import com.simibubi.create.content.equipment.zapper.ShootGadgetPacket;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FireExtinguisherPacket extends ShootGadgetPacket {



	public FireExtinguisherPacket(Vec3 location, InteractionHand hand, boolean self) {
		super(location, hand, self);
	}

	public FireExtinguisherPacket(FriendlyByteBuf buffer) {
		super(buffer);
	}

	@Override
	protected void readAdditional(FriendlyByteBuf buffer) {
	}

	@Override
	protected void writeAdditional(FriendlyByteBuf buffer) {
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	protected void handleAdditional() {

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected ShootableGadgetRenderHandler getHandler() {
		return TFMGClient.FIRE_EXTINGUISHER_RENDER_HANDLER;
	}

}
