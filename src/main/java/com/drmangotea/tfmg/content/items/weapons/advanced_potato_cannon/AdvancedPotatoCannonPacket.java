package com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon;

import com.drmangotea.tfmg.TFMGClient;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.content.equipment.zapper.ShootGadgetPacket;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


public class AdvancedPotatoCannonPacket extends ShootGadgetPacket {
		public static final StreamCodec<RegistryFriendlyByteBuf, AdvancedPotatoCannonPacket> STREAM_CODEC = StreamCodec.composite(
				CatnipStreamCodecs.VEC3, packet -> packet.location,
				CatnipStreamCodecs.VEC3, packet -> packet.motion,
				ItemStack.OPTIONAL_STREAM_CODEC, packet -> packet.item,
				CatnipStreamCodecs.HAND, packet -> packet.hand,
				ByteBufCodecs.FLOAT, packet -> packet.pitch,
				ByteBufCodecs.BOOL, packet -> packet.self,
				AdvancedPotatoCannonPacket::new
		);

		private final float pitch;
		private final Vec3 motion;
		private final ItemStack item;

		public AdvancedPotatoCannonPacket(Vec3 location, Vec3 motion, ItemStack item, InteractionHand hand, float pitch, boolean self) {
			super(location, hand, self);
			this.motion = motion;
			this.item = item;
			this.pitch = pitch;
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		protected void handleAdditional() {
			TFMGClient.ADVANCED_POTATO_CANNON_RENDER_HANDLER.beforeShoot(pitch, location, motion, item);
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		protected ShootableGadgetRenderHandler getHandler() {
			return TFMGClient.ADVANCED_POTATO_CANNON_RENDER_HANDLER;
		}

		@Override
		public PacketTypeProvider getTypeProvider() {
			return TFMGPackets.ADVANCED_POTATO_CANNON;
		}
	}
