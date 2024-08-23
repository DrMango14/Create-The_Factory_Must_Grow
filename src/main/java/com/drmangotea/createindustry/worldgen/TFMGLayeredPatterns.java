package com.drmangotea.createindustry.worldgen;


import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGPaletteStoneTypes;
import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.infrastructure.worldgen.LayerPattern;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Blocks;

public class TFMGLayeredPatterns {

	public static final NonNullSupplier<LayerPattern>

			BAUXITE = () -> LayerPattern.builder()
			.layer(l -> l.weight(1)
					.passiveBlock())
			.layer(l -> l.weight(2)
					.block(TFMGPaletteStoneTypes.BAUXITE.getBaseBlock())
					.size(1, 3))
			.layer(l -> l.weight(1)
					.block(Blocks.SMOOTH_BASALT)
					.block(Blocks.GRANITE)
					.size(2, 2))
			.layer(l -> l.weight(1)
					.blocks(Blocks.GRANITE, Blocks.SMOOTH_BASALT))
			.layer(l -> l.weight(1)
					.block(AllPaletteStoneTypes.ANDESITE.getBaseBlock()))
			.build();

		public static final NonNullSupplier<LayerPattern>

				GALENA = () -> LayerPattern.builder()
				.layer(l -> l.weight(1)
						.passiveBlock())
				.layer(l -> l.weight(2)
						.block(TFMGPaletteStoneTypes.GALENA.getBaseBlock())
						.size(1, 3))
				.layer(l -> l.weight(1)
						.block(Blocks.SMOOTH_BASALT)
						.block(Blocks.DRIPSTONE_BLOCK)
						.size(2, 2))
				.layer(l -> l.weight(1)
						.blocks(Blocks.DRIPSTONE_BLOCK, Blocks.SMOOTH_BASALT))
				.layer(l -> l.weight(1)
						.block(AllPaletteStoneTypes.DEEPSLATE.getBaseBlock()))
				.build();

	public static final NonNullSupplier<LayerPattern>

			LIGNITE = () -> LayerPattern.builder()
			.layer(l -> l.weight(1)
					.passiveBlock())
			.layer(l -> l.weight(2)
					.block(TFMGBlocks.LIGNITE.get())
					.size(1, 3))
			.layer(l -> l.weight(1)
					.block(Blocks.TUFF)
					.block(Blocks.DEEPSLATE)
					.size(2, 2))
			.layer(l -> l.weight(1)
					.blocks(Blocks.DEEPSLATE, Blocks.TUFF))
			.layer(l -> l.weight(1)
					.block(AllPaletteStoneTypes.SCORIA.getBaseBlock()))
			.build();


	public static final NonNullSupplier<LayerPattern>

			FIRECLAY = () -> LayerPattern.builder()
			.layer(l -> l.weight(2)
					.passiveBlock())
			.layer(l -> l.weight(2)
					.block(TFMGBlocks.FIRECLAY.get())
					.size(1, 2))
			.layer(l -> l.weight(2)
					.block(Blocks.SAND)
					.block(Blocks.GRAVEL)
					.size(1, 3))
			.layer(l -> l.weight(1)
					.block(AllPaletteStoneTypes.CRIMSITE.getBaseBlock()))
			.build();

	public static final NonNullSupplier<LayerPattern>

			SULFUR = () -> LayerPattern.builder()
			.inNether()
			.layer(l -> l.weight(2)
					.passiveBlock())
			.layer(l -> l.weight(2)
					.block(TFMGBlocks.SULFUR.get())
					.size(1, 2))
			.layer(l -> l.weight(3)
					.block(AllPaletteStoneTypes.SCORCHIA.getBaseBlock())
					.block(Blocks.BLACKSTONE)
					.size(1, 3))
			.layer(l -> l.weight(1)
					.block(Blocks.MAGMA_BLOCK))
			.layer(l -> l.weight(2)
					.block(Blocks.BASALT)
					.block(Blocks.SMOOTH_BASALT))
			.build();

	public static final NonNullSupplier<LayerPattern>

			FIRECLAY_NETHER = () -> LayerPattern.builder()
			.inNether()
			.layer(l -> l.weight(2)
					.passiveBlock())
			.layer(l -> l.weight(2)
					.block(TFMGBlocks.FIRECLAY.get())
					.size(1, 2))
			.layer(l -> l.weight(3)
					.block(AllPaletteStoneTypes.SCORCHIA.getBaseBlock())
					.block(Blocks.GRAVEL)
					.size(1, 3))
			.layer(l -> l.weight(1)
					.block(Blocks.MAGMA_BLOCK))
			.layer(l -> l.weight(2)
					.block(Blocks.SOUL_SOIL)
					.block(Blocks.SOUL_SAND))
			.build();







}
