package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.data_storage.CylinderFuels;
import com.drmangotea.tfmg.content.engines.upgrades.base.EngineUpgrade;
import com.drmangotea.tfmg.content.items.weapons.fire_extinguisher.FireExtinguisherFuel;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuel;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode.Electrode;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.mode.MixerMode;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus.Internal;

public class TFMGDataComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TFMG.MOD_ID);

	public static final DataComponentType<Integer> SPOOL_AMOUNT = register("spool_amount", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
	public static final DataComponentType<Integer> COIL_TURNS = register("coil_turns", ExtraCodecs.NON_NEGATIVE_INT, ByteBufCodecs.VAR_INT);
	public static final DataComponentType<Integer> CONFIGURATION_WRENCH_NUMBER = register("number", Codec.INT, ByteBufCodecs.VAR_INT);
	public static final DataComponentType<Integer> LITHIUM_BLADE_TIMER = register("timer", Codec.INT, ByteBufCodecs.VAR_INT);

	public static final DataComponentType<FlamethrowerFuel> FLAMETHROWER = register("flamethrower", FlamethrowerFuel.CODEC, FlamethrowerFuel.STREAM_CODEC);
	public static final DataComponentType<FireExtinguisherFuel> FIRE_EXTINGUISHER = register("fire_extinguisher", FireExtinguisherFuel.CODEC, FireExtinguisherFuel.STREAM_CODEC);

	public static final DataComponentType<Integer> ACCUMULATOR_STORAGE = register("storage", Codec.INT, ByteBufCodecs.VAR_INT);

	public static final DataComponentType<Integer> RESISTANCE = register("resistance", Codec.INT, ByteBufCodecs.VAR_INT);
	public static final DataComponentType<Integer> AMOUNT = register("amount", Codec.INT, ByteBufCodecs.VAR_INT);
	public static final DataComponentType<BlockPos> POSITION = register("position", BlockPos.CODEC, BlockPos.STREAM_CODEC);
	
	public static final DataComponentType<Integer> CONNECTOR_ID = register("connector_id", Codec.INT, ByteBufCodecs.VAR_INT);

	public static final DataComponentType<CylinderFuels> ENGINE_CYLINDER = register("engine_cylinder", CylinderFuels.CODEC, CylinderFuels.STREAM_CODEC);

	public static final DataComponentType<MixerMode.Stored> MIXER_MODE = register("mixer_mode", MixerMode.Stored.CODEC, MixerMode.Stored.STREAM_CODEC);
	public static final DataComponentType<Electrode.Stored> ELECTRODE = register("electrode", Electrode.Stored.CODEC, Electrode.Stored.STREAM_CODEC);
	public static final DataComponentType<EngineUpgrade.Stored> ENGINE_UPGRADE = register("engine_upgrade", EngineUpgrade.Stored.CODEC, EngineUpgrade.Stored.STREAM_CODEC);

	/**
	 * @deprecated Use {@link com.drmangotea.tfmg.registry.TFMGDataComponents#FLAMETHROWER}.
	 */
	@Deprecated(since = "1.2.4", forRemoval = true)
	public static final DataComponentType<String> FLAMETHROWER_FUEL = register("flamethrower_fuel", Codec.STRING, ByteBufCodecs.STRING_UTF8);
	/**
	 * @deprecated Use {@link com.drmangotea.tfmg.registry.TFMGDataComponents#ENGINE_CYLINDER}.
	 */
	@Deprecated(since = "1.2.4", forRemoval = true)
	public static final DataComponentType<CompoundTag> FUELS = register("fuels", CompoundTag.CODEC, ByteBufCodecs.COMPOUND_TAG);

	private static <T> DataComponentType<T> register(String name, Codec<T> persistent, StreamCodec<? super RegistryFriendlyByteBuf, T> network) {
		DataComponentType<T> type = new DataComponentType.Builder<T>().persistent(persistent).networkSynchronized(network).build();
		DATA_COMPONENTS.register(name, () -> type);
		return type;
	}

	@Internal
	public static void register(IEventBus modEventBus) {
		DATA_COMPONENTS.register(modEventBus);
	}
}
