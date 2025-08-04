package com.drmangotea.tfmg.registry;

import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;

import com.mojang.serialization.Codec;
import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides.ClipboardType;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItemComponent;
import com.simibubi.create.content.equipment.symmetryWand.mirror.SymmetryMirror;
import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import com.simibubi.create.content.equipment.zapper.terrainzapper.PlacementOptions;
import com.simibubi.create.content.equipment.zapper.terrainzapper.TerrainBrushes;
import com.simibubi.create.content.equipment.zapper.terrainzapper.TerrainTools;
import com.simibubi.create.content.fluids.potion.PotionFluid.BottleType;
import com.simibubi.create.content.logistics.box.PackageItem.PackageOrderData;
import com.simibubi.create.content.logistics.filter.AttributeFilterWhitelistMode;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute.ItemAttributeEntry;
import com.simibubi.create.content.logistics.redstoneRequester.AutoRequestData;
import com.simibubi.create.content.logistics.stockTicker.PackageOrderWithCrafts;
import com.simibubi.create.content.logistics.tableCloth.ShoppingListItem.ShoppingList;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe.SequencedAssembly;
import com.simibubi.create.content.redstone.displayLink.ClickToLinkBlockItem.ClickToLinkData;
import com.simibubi.create.content.schematics.cannon.SchematicannonBlockEntity.SchematicannonOptions;
import com.simibubi.create.content.trains.track.BezierTrackPointLocation;
import com.simibubi.create.content.trains.track.TrackPlacement.ConnectingFrom;

import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentType.Builder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
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
