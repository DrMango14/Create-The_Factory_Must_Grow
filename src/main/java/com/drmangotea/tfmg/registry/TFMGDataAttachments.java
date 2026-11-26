package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.world.resevoir.FluidReservoir;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class TFMGDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TFMG.MOD_ID);

    public static final Supplier<AttachmentType<FluidReservoir>> FLUID_RESERVOIR = ATTACHMENT_TYPES.register(
		"fluid_reservoir", () -> AttachmentType
			.builder(() -> new FluidReservoir())
			.serialize(FluidReservoir.CODEC)
			.sync(FluidReservoir.STREAM_CODEC)
			.build()
	);

	public static final Supplier<AttachmentType<Boolean>> LOCKED_PIPE = ATTACHMENT_TYPES.register("locked_pipe", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).build());

    public static void register(IEventBus modEventBus){
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
