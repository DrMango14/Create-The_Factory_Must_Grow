package com.drmangotea.tfmg.registry;

import java.util.function.UnaryOperator;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuel;
import org.jetbrains.annotations.ApiStatus.Internal;

import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentType.Builder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TFMGDataComponents {
	private static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, TFMG.MOD_ID);

	public static final DataComponentType<CompoundTag> FUELS = register(
			"fuels",
			builder -> builder.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
	);
	public static final DataComponentType<CompoundTag> FUEL_TAGS = register(
			"fuel_tags",
			builder -> builder.persistent(CompoundTag.CODEC).networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
	);
	public static final DataComponentType<Integer> SPOOL_AMOUNT = register(
			"spool_amount",
			builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	public static final DataComponentType<Integer> COIL_TURNS = register(
			"coil_turns",
			builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	public static final DataComponentType<Integer> CONFIGURATION_WRENCH_NUMBER = register(
			"number",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	public static final DataComponentType<Integer> LITHIUM_BLADE_TIMER = register(
			"timer",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	public static final DataComponentType<FlamethrowerFuel> FLAMETHROWER = register(
			"flamethrower",
			builder -> builder.persistent(FlamethrowerFuel.CODEC).networkSynchronized(FlamethrowerFuel.STREAM_CODEC)
	);
	public static final DataComponentType<String> FLAMETHROWER_FUEL = register(
			"flamethrower_fuel",
			builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8)
	);
	public static final DataComponentType<Integer> ACCUMULATOR_STORAGE = register(
			"storage",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);

	public static final DataComponentType<Integer> RESISTANCE = register(
			"resistance",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	public static final DataComponentType<Integer> AMOUNT = register(
			"amount",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	public static final DataComponentType<Long> POSITION = register(
			"position",
			builder -> builder.persistent(Codec.LONG).networkSynchronized(ByteBufCodecs.VAR_LONG)
	);

	public static final DataComponentType<Integer> CONNECTOR_ID = register(
			"connector_id",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);

	public static final DataComponentType<Integer> X_POS = register(
			"x_pos",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	public static final DataComponentType<Integer> Y_POS = register(
			"y_pos",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);
	public static final DataComponentType<Integer> Z_POS = register(
			"z_pos",
			builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.VAR_INT)
	);



	private static <T> DataComponentType<T> register(String name, UnaryOperator<Builder<T>> builder) {
		DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
		DATA_COMPONENTS.register(name, () -> type);
		return type;
	}

	@Internal
	public static void register(IEventBus modEventBus) {
		DATA_COMPONENTS.register(modEventBus);
	}
}
