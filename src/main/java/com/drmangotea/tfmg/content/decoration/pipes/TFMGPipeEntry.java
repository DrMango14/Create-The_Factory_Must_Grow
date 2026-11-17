package com.drmangotea.tfmg.content.decoration.pipes;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.TFMGRegistrate;
import com.drmangotea.tfmg.config.server.TFMGStress;
import com.drmangotea.tfmg.content.decoration.pipes.block.*;
import com.drmangotea.tfmg.content.decoration.pipes.rendering.TFMGPipeAttachmentModel;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pipes.SmartFluidPipeGenerator;
import com.simibubi.create.content.fluids.pipes.valve.FluidValveBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.recipe.CommonMetal;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("removal")
public class TFMGPipeEntry {
    private final TFMGPipes.PipeMaterial material;
    private final TFMGRegistrate registrate;

    private final BlockEntry<? extends TFMGPipeBlock> pipe;
    private final BlockEntry<? extends TFMGEncasedPipeBlock> encased;
    private final BlockEntry<? extends TFMGGlassPipeBlock> glass;
    private final BlockEntry<? extends TFMGPumpBlock> pump;
    private final BlockEntry<? extends TFMGSmartFluidPipeBlock> smart;
    private final BlockEntry<? extends TFMGFluidValveBlock> valve;

    public TFMGPipeEntry(TFMGPipes.PipeMaterial material, TFMGRegistrate registrate) {
        this.material = material;
        this.registrate = registrate;
        this.pipe = registerPipe();
        this.encased = registerEncasedPipe();
        this.glass = registerGlassPipe();
        this.pump = registerPump();
        this.smart = registerSmartPipe();
        this.valve = registerValve();
    }
	
	private NonNullFunction<TFMGPipes.PipeMaterial, NonNullFunction<BakedModel, ? extends BakedModel>> getMaterialAttachmentModel() {
		return TFMGPipeAttachmentModel::getForMaterial;
	}
	
	public NonNullFunction<BakedModel, ? extends BakedModel> getAttachmentModel() {
		return getMaterialAttachmentModel().apply(material);
	}

    public BlockEntry<? extends TFMGPipeBlock> getPipe() {
        return pipe;
    }

    public BlockEntry<? extends TFMGEncasedPipeBlock> getEncased() {
        return encased;
    }

    public BlockEntry<? extends TFMGGlassPipeBlock> getGlass() {
        return glass;
    }

    public BlockEntry<? extends TFMGPumpBlock> getPump() {
        return pump;
    }

    public BlockEntry<? extends TFMGSmartFluidPipeBlock> getSmart() {
        return smart;
    }

    public BlockEntry<? extends TFMGFluidValveBlock> getValve() {
        return valve;
    }

    protected BlockEntry<? extends TFMGPipeBlock> registerPipe() {
        return this.registrate.block(this.material.name + "_pipe", p -> new TFMGPipeBlock(p, this.material))
                .initialProperties(SharedProperties::copperMetal)
                .transform(pickaxeOnly())
                .blockstate(BlockStateGen.pipe())
                .onRegister(CreateRegistrate.blockModel(this::getAttachmentModel))
                .item().transform(customItemModel())
				.recipe((ctx, prov) -> {
					Criterion<InventoryChangeTrigger.TriggerInstance> has = RegistrateRecipeProvider.has(material.ingot);
					ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 4)
						.unlockedBy("has_material", has)
						.define('I', material.ingot)
						.define('P', material.sheet)
						.pattern("   ")
						.pattern("PIP")
						.pattern("   ")
						.save(prov, TFMG.asResource(this.material.name + "_pipe"));
					ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 4)
						.unlockedBy("has_material", has)
						.define('I', material.ingot)
						.define('P', material.sheet)
						.pattern(" P ")
						.pattern(" I ")
						.pattern(" P ")
						.save(prov, TFMG.asResource(this.material.name + "_pipe_vertical"));
				})
                .register();
    }

    protected BlockEntry<? extends TFMGEncasedPipeBlock> registerEncasedPipe() {
        return this.registrate.block("encased_" + this.material.name + "_pipe", p -> new TFMGEncasedPipeBlock(p, AllBlocks.COPPER_CASING::get, this.material))
                .initialProperties(SharedProperties::copperMetal)
                .properties(p -> p.noOcclusion().mapColor(MapColor.TERRACOTTA_LIGHT_GRAY))
                .transform(axeOrPickaxe())
                .blockstate(BlockStateGen.encasedPipe())
                .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(material.encasedSpriteShift)))
                .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(
					block, material.encasedSpriteShift,
					(s, f) -> !s.getValue(TFMGEncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(f))
				)))
                .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withoutAO))
                .loot((p, b) -> p.dropOther(b, this.pipe.get()))
                .transform(EncasingRegistry.addVariantTo(this.pipe))
                .register();
    }

    protected BlockEntry<? extends TFMGGlassPipeBlock> registerGlassPipe() {
        return this.registrate.block("glass_" + this.material.name + "_pipe", p -> new TFMGGlassPipeBlock(p, this.material))
                .initialProperties(SharedProperties::copperMetal)
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/" + this.material.name + "_pipe/window")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED))
                .onRegister(CreateRegistrate.blockModel(this::getAttachmentModel))
                .loot((p, b) -> p.dropOther(b, this.pipe.get()))
                .register();
    }

    protected BlockEntry<? extends TFMGPumpBlock> registerPump() {
        return this.registrate.block(this.material.name + "_mechanical_pump", (p) -> new TFMGPumpBlock(p, this.material))
                .initialProperties(SharedProperties::copperMetal)
                .transform(pickaxeOnly())
                .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
                .onRegister(CreateRegistrate.blockModel(this::getAttachmentModel))
                .transform(TFMGStress.setImpact(4.0))
                .item().transform(customItemModel())
				.recipe((ctx, prov) -> ShapelessRecipeBuilder
					.shapeless(RecipeCategory.DECORATIONS, ctx.get())
					.unlockedBy("has_material", RegistrateRecipeProvider.has(material.ingot))
					.requires(this.pipe) //a lil risky, but pipe is registered before the pump so it works
					.requires(AllBlocks.COGWHEEL)
					.save(prov, TFMG.asResource(this.material.name + "_mechanical_pump"))
				)
                .register();
    }

    protected BlockEntry<? extends TFMGSmartFluidPipeBlock> registerSmartPipe() {
        return this.registrate.block(this.material.name + "_smart_fluid_pipe", TFMGSmartFluidPipeBlock::new)
                .initialProperties(SharedProperties::copperMetal)
                .transform(pickaxeOnly())
                .blockstate(new SmartFluidPipeGenerator()::generate)
                .onRegister(CreateRegistrate.blockModel(this::getAttachmentModel))
                .item().transform(customItemModel())
                .register();
    }

    protected BlockEntry<? extends TFMGFluidValveBlock> registerValve() {
        return this.registrate.block(this.material.name + "_fluid_valve", TFMGFluidValveBlock::new)
                .initialProperties(SharedProperties::copperMetal)
                .transform(pickaxeOnly())
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate((c, p) -> BlockStateGen.directionalAxisBlock(c, p,
                        (state, vertical) -> AssetLookup.partialBaseModel(c, p, vertical ? "vertical" : "horizontal",
                                state.getValue(FluidValveBlock.ENABLED) ? "open" : "closed")))
                .onRegister(CreateRegistrate.blockModel(this::getAttachmentModel))
                .item().transform(customItemModel())
				.recipe((ctx, prov) -> ShapelessRecipeBuilder
					.shapeless(RecipeCategory.DECORATIONS, ctx.get())
					.unlockedBy("has_material", RegistrateRecipeProvider.has(material.ingot))
					.requires(CommonMetal.IRON.plates)
					.requires(this.pipe) //a lil risky, but pipe is registered before the valve so it works
					.save(prov, TFMG.asResource(this.material.name + "_fluid_valve"))
				)
                .register();
    }
}
