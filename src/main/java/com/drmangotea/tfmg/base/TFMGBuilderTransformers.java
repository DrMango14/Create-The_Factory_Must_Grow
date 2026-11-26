package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.blocks.TFMGVanillaBlockStates;
import com.drmangotea.tfmg.base.dyes.DyedMaterialSetList;
import com.drmangotea.tfmg.config.server.TFMGStress;
import com.drmangotea.tfmg.content.decoration.FrameBlock;
import com.drmangotea.tfmg.content.decoration.TrussBlock;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorBlock;
import com.drmangotea.tfmg.content.decoration.kinetics.cogs.TFMGCogWheelBlock;
import com.drmangotea.tfmg.content.decoration.kinetics.cogs.TFMGCogwheelBlockItem;
import com.drmangotea.tfmg.content.decoration.kinetics.encased.TFMGEncasedCogwheelBlock;
import com.drmangotea.tfmg.content.decoration.kinetics.encased.TFMGEncasedShaftBlock;
import com.drmangotea.tfmg.content.decoration.kinetics.flywheels.TFMGFlywheelBlock;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.copycat_cable.CopycatCableBlock;
import com.drmangotea.tfmg.content.electricity.content.lights.neon_tube.NeonTubeBlock;
import com.drmangotea.tfmg.recipes.condition.TagFilledCondition;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.ArrayList;
import java.util.function.Supplier;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.drmangotea.tfmg.datagen.recipes.TFMGRecipeProvider.I.shaft;
import static com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour.interactionBehaviour;
import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;
import static com.simibubi.create.foundation.data.BlockStateGen.simpleCubeAll;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.*;

@SuppressWarnings("removal")
public class TFMGBuilderTransformers {

    public static <B extends TFMGSlidingDoorBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> slidingDoor(String type) {
        return b -> b.initialProperties(() -> Blocks.IRON_DOOR)
                .properties(p -> p.requiresCorrectToolForDrops()
                        .strength(3.0F, 6.0F))
                .blockstate((c, p) -> {
                    ModelFile bottom = AssetLookup.partialBaseModel(c, p, "bottom");
                    ModelFile top = AssetLookup.partialBaseModel(c, p, "top");
                    p.doorBlock(c.get(), bottom, bottom, bottom, bottom, top, top, top, top);
                })
                .addLayer(() -> RenderType::cutoutMipped)
                .transform(pickaxeOnly())
                .onRegister(interactionBehaviour(new DoorMovingInteraction()))
                .onRegister(movementBehaviour(new SlidingDoorMovementBehaviour()))
                .tag(BlockTags.DOORS)
                .tag(BlockTags.WOODEN_DOORS) // for villager AI
                .tag(AllTags.AllBlockTags.NON_DOUBLE_DOOR.tag)
                .loot((lr, block) -> lr.add(block, lr.createDoorTable(block)))
                .item()
                .tag(ItemTags.DOORS)
                .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                .model((c, p) -> p.blockSprite(c, p.modLoc("item/" + type + "_door")))
                .build();
    }

    public static void generateNeonTubeBlockState(DataGenContext<Block, NeonTubeBlock> c, RegistrateBlockstateProvider p) {
        MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());


        builder.part()
                .modelFile(AssetLookup.partialBaseModel(c, p, "center"))
                .addModel()
                .end();

        builder.part().modelFile(AssetLookup.partialBaseModel(c, p, "north"))
                .addModel()
                .condition(PipeBlock.NORTH, true)
                .end();
        builder.part().modelFile(AssetLookup.partialBaseModel(c, p, "south"))
                .addModel()
                .condition(PipeBlock.SOUTH, true)
                .end();
        builder.part().modelFile(AssetLookup.partialBaseModel(c, p, "west"))
                .addModel()
                .condition(PipeBlock.WEST, true)
                .end();
        builder.part().modelFile(AssetLookup.partialBaseModel(c, p, "east"))
                .addModel()
                .condition(PipeBlock.EAST, true)
                .end();
        builder.part().modelFile(AssetLookup.partialBaseModel(c, p, "top"))
                .addModel()
                .condition(PipeBlock.UP, true)
                .end();
        builder.part().modelFile(AssetLookup.partialBaseModel(c, p, "bottom"))
                .addModel()
                .condition(PipeBlock.DOWN, true)
                .end();

    }


    public static <B extends TFMGEncasedShaftBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedShaft(String casing,
                                                                                                             Supplier<CTSpriteShiftEntry> casingShift) {
        return builder -> encasedBase(builder, AllBlocks.SHAFT::get)
                .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCTBehaviour(casingShift.get())))
                .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, casingShift.get(),
                        (s, f) -> f.getAxis() != s.getValue(TFMGEncasedShaftBlock.AXIS))))
                .blockstate((c, p) -> axisBlock(c, p, blockState -> p.models()
                        .getExistingFile(p.modLoc("block/encased_shaft/block_" + casing)), true))
                .item()
                .model(AssetLookup.customBlockItemModel("encased_shaft", "item_" + casing))
                .build();
    }

    public static <B extends TFMGEncasedCogwheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedCogwheel(String casing, String cogMat, Supplier<CTSpriteShiftEntry> casingShift, Supplier<ItemLike> drop) {
        return b -> encasedCogwheelBase(b, casing, cogMat, casingShift, drop, false);
    }

    public static <B extends TFMGEncasedCogwheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedLargeCogwheel(String casing, String cogMat, Supplier<CTSpriteShiftEntry> casingShift, Supplier<ItemLike> drop) {
        return b -> encasedCogwheelBase(b, casing, cogMat, casingShift, drop, true).onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(casingShift.get())));
    }

    private static <B extends TFMGEncasedCogwheelBlock, P> BlockBuilder<B, P> encasedCogwheelBase(BlockBuilder<B, P> b, String casing, String cogMat, Supplier<CTSpriteShiftEntry> casingShift, Supplier<ItemLike> drop, boolean large) {
        String encasedSuffix = "_encased_cogwheel_side" + (large ? "_large" : "");
        String blockFolder = large ? "encased_large_cogwheel" : "encased_cogwheel";
        String wood = casing.equals("industrial") ? "industrial_aluminum_casing" : casing.equals("heavy_casing") ? "heavy_machinery_casing" : casing + "_casing";
        String gearbox = casing.equals("heavy_casing") ? "heavy_gearbox" : casing + "_gearbox";
        String itemSubType = cogMat.equals("wood") ? "" : "_" + cogMat;

        String casing1 = casing.equals("heavy_casing") ? "heavy_machinery" : casing;
        return encasedBase(b, drop).addLayer(() -> RenderType::cutoutMipped)
                .onRegister(CreateRegistrate.casingConnectivity((block, cc) -> cc.make(block, casingShift.get(),
                        (s, f) -> f.getAxis() == s.getValue(TFMGEncasedCogwheelBlock.AXIS)
                                && !s.getValue(f.getAxisDirection() == Direction.AxisDirection.POSITIVE ? TFMGEncasedCogwheelBlock.TOP_SHAFT
                                : TFMGEncasedCogwheelBlock.BOTTOM_SHAFT))))
                .blockstate((c, p) -> axisBlock(c, p, blockState -> {
                    String suffix = (blockState.getValue(TFMGEncasedCogwheelBlock.TOP_SHAFT) ? "_top" : "")
                            + (blockState.getValue(TFMGEncasedCogwheelBlock.BOTTOM_SHAFT) ? "_bottom" : "");
                    String modelName = c.getName() + suffix;
                    return p.models()
                            .withExistingParent(modelName, p.modLoc("block/" + blockFolder + "/block" + suffix))
                            .texture("casing", TFMG.asResource("block/" + wood))
                            .texture("particle", TFMG.asResource("block/" + wood))
                            .texture("4", TFMG.asResource("block/" + gearbox))
                            .texture("1", TFMG.asResource("block/" + wood))
                            .texture("side", TFMG.asResource("block/" + casing1 + encasedSuffix));
                }, false))
                .item()
                .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/" + blockFolder + "/item" + itemSubType))
                        .texture("casing", TFMG.asResource("block/" + wood))
                        .texture("particle", TFMG.asResource("block/" + wood))
                        .texture("1", TFMG.asResource("block/" + wood))
                        .texture("side", TFMG.asResource("block/" + casing1 + encasedSuffix)))
                .build();
    }

    private static <B extends RotatedPillarKineticBlock, P> BlockBuilder<B, P> encasedBase(BlockBuilder<B, P> b, Supplier<ItemLike> drop) {
        return b.initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(TFMGStress.setNoImpact())
                .loot((p, lb) -> p.dropOther(lb, drop.get()));
    }

    public static <B extends CopycatCableBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycatCable() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .addLayer(() -> RenderType::solid)
                .addLayer(() -> RenderType::cutout)
                .addLayer(() -> RenderType::cutoutMipped)
                // .addLayer(() -> RenderType::translucent)
                .color(() -> CopycatCableBlock::wrappedColor)
                .transform(TagGen.axeOrPickaxe());
    }

    /// ////////////
	
	public static void numberedVariantBlockTextures(DataGenContext<Block, Block> ctx, RegistrateBlockstateProvider prov, String name, int n) {
		VariantBlockStateBuilder vb = prov.getVariantBuilder(ctx.get());
		VariantBlockStateBuilder.PartialBlockstate part = vb.partialState();
		
		ArrayList<ConfiguredModel> models = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			models.add(part.modelForState().modelFile(prov.models().cubeAll(
				i == 0 ? name : name + i,
				TFMG.asResource("block/"+name+i)
			)).buildLast());
		}
		
		vb.addModels(vb.partialState(), models.toArray(ConfiguredModel[]::new));
	}
	
	public static BlockBuilder<TFMGCogWheelBlock, CreateRegistrate> cogwheel(
		String name, boolean large, TagKey<Item> ingot
	) {
		return REGISTRATE.block(name, large? TFMGCogWheelBlock::large : TFMGCogWheelBlock::small)
			.initialProperties(SharedProperties::stone)
			.transform(TFMGStress.setNoImpact())
			.transform(axeOrPickaxe())
			.blockstate(BlockStateGen.axisBlockProvider(false))
			.onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
			.item(TFMGCogwheelBlockItem::new).build()
			.recipe((ctx, prov) -> {
				var builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ctx.get(), large ? 2 : 4)
					.unlockedBy("has_ingot", RegistrateRecipeProvider.has(ingot))
					.requires(AllBlocks.SHAFT)
					.requires(ingot);
				if (large) builder.requires(ingot);
				builder.save(prov);
			});
	}
	
    public static BlockEntry<TFMGFlywheelBlock> flywheel(String name, TagKey<Item> ingot, NonNullFunction<BlockBehaviour.Properties, TFMGFlywheelBlock> block) {
        return REGISTRATE.block(name + "_flywheel", block)
                .initialProperties(SharedProperties::softMetal)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(axeOrPickaxe())
                .transform(TFMGStress.setNoImpact())
                .blockstate(BlockStateGen.axisBlockProvider(true))
                .item().transform(customItemModel())
				.recipe((ctx, prov) -> ShapedRecipeBuilder
					.shaped(RecipeCategory.DECORATIONS, ctx.get(), 1)
					.define('C', ingot)
					.define('S', shaft())
					.pattern("CCC")
					.pattern("CSC")
					.pattern("CCC")
					.unlockedBy("has_ingot", RegistrateRecipeProvider.has(ingot))
					.save(prov, TFMG.asResource(name + "_flywheel"))
				)
                .register();
    }

    public static BlockEntry<TrussBlock> truss(String name) {
        return REGISTRATE.block(name + "_truss", TrussBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                .transform(pickaxeOnly())
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(BlockStateGen.axisBlockProvider(false))
                .item()
                .build()
                .register();
    }

    public static BlockEntry<FrameBlock> frame(String name) {
        return REGISTRATE.block(name + "_frame", FrameBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                .properties(p -> p.strength(3))
                .transform(pickaxeOnly())
                .addLayer(() -> RenderType::cutoutMipped)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.getEntry(), AssetLookup.partialBaseModel(ctx, prov)))
                .simpleItem()
                .register();
    }

    public static MaterialSet generateConcrete(boolean rebar) {
        String name = rebar ? "rebar_concrete" : "concrete";
        MaterialSet concrete = new MaterialSet();

        concrete.wall = REGISTRATE.block(name + "_wall", WallBlock::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .properties(p -> p.strength(rebar ? 5f : 3.5f, rebar ? 17f : 3.5f))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.WALLS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(concrete.block.get()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "concrete"))
                .build()
                .register();

        concrete.block = REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.strength(rebar ? 5f : 3.5f, rebar ? 17f : 3.5f))
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .transform(pickaxeOnly())
                .blockstate(simpleCubeAll("concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .transform(tagBlockAndItem(Tags.Blocks.CONCRETES, Tags.Items.CONCRETES))
                .build()
                .register();

        concrete.stairs = REGISTRATE.block(name + "_stairs", p -> new StairBlock(concrete.block.get().defaultBlockState(), p))
                .initialProperties(() -> Blocks.STONE)
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .properties(p -> p.strength(rebar ? 5f : 3.5f, rebar ? 17f : 3.5f))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, name))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.STAIRS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(concrete.block.get()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                .item()
                .transform(customItemModel(name + "_stairs"))
                .register();

        concrete.slab = REGISTRATE.block(name + "_slab", SlabBlock::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.strength(rebar ? 5f : 3.5f, rebar ? 17f : 3.5f))
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, "concrete"))
                .loot((lt, block) -> lt.add(block, lt.createSlabItemTable(block)))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.SLABS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(concrete.block.get()), RecipeCategory.BUILDING_BLOCKS, c, 2))
                .item()
                .transform(customItemModel("concrete_bottom"))
                .register();

        return concrete;
    }

    public static DyedMaterialSetList generateColoredConcrete(boolean rebar) {
        String name = rebar ? "_rebar_concrete" : "_concrete";
        ItemLike undyed = rebar ? TFMGBlocks.REBAR_CONCRETE.block : TFMGBlocks.CONCRETE.block;
        return new DyedMaterialSetList(colour -> {
            String colourName = colour.getSerializedName();
            MaterialSet set = new MaterialSet();
            set.block = REGISTRATE.block(colourName + name, Block::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .blockstate(simpleCubeAll("concrete/" + colourName))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .recipe((c, p) -> {
                        boolean optional = colour.getId() > 15;
						ICondition[] conditions = optional ? new ICondition[]{new TagFilledCondition(colour.getTag())} : new ICondition[]{};
                        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, c.get(), 8)
                                        .pattern("###")
                                        .pattern("#D#")
                                        .pattern("###")
                                        .define('D', colour.getTag())
                                        .define('#', undyed)
                                        .unlockedBy("has_concrete", RegistrateRecipeProvider.has(undyed))
										.save(p.withConditions(conditions), TFMG.asResource("crafting/materials/" + c.getName() + "_dyeing"));
                    })
					.transform(tagBlockAndItem(Tags.Blocks.CONCRETES, Tags.Items.CONCRETES)).build()
					.register();

            set.wall=REGISTRATE.block(colourName + name + "_wall", WallBlock::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, colourName + "_concrete", "concrete/" + colourName))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.WALLS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(set.block.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .item()
                    .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "concrete/" + colourName))
                    .build()
                    .register();

            set.stairs=REGISTRATE.block(colourName + name + "_stairs", p -> new StairBlock(TFMGBlocks.CONCRETE.block.get().defaultBlockState(), p))
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, colourName + "_concrete", "concrete/" + colourName))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.STAIRS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(set.block.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                    .item()
                    .transform(customItemModel(colourName + "_concrete_stairs"))
                    .register();

            set.slab=REGISTRATE.block(colourName + name + "_slab", SlabBlock::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                    .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, colourName + "_concrete", "concrete/" + colourName))
                    .loot((lt, block) -> lt.add(block, lt.createSlabItemTable(block)))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.SLABS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(set.block.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 2))
                    .item()
                    .transform(customItemModel(colourName + "_concrete_bottom"))
                    .register();

            return set;
        });
    }

    public static MaterialSet makeVariants(BlockEntry<?> blockEntry) {
        return makeVariants(blockEntry, false);
    }

    public static MaterialSet makeVariants(BlockEntry<?> blockEntry, boolean recipe) {
        MaterialSet materialSet = new MaterialSet();

        materialSet.block = blockEntry;

        String name = blockEntry.getId().toString().replace("tfmg:", "");


        REGISTRATE.block(name + "_wall", WallBlock::new)
                .initialProperties(blockEntry::get)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, name))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.WALLS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(blockEntry.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                .item()
                .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, name))
                .build()
                .register();

        REGISTRATE.block(name + "_slab", SlabBlock::new)
                .initialProperties(blockEntry::get)
                .properties(BlockBehaviour.Properties::requiresCorrectToolForDrops)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, name))
                .loot((lt, block) -> lt.add(block, lt.createSlabItemTable(block)))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.SLABS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(blockEntry.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 2))
                .item()
                .transform(customItemModel(name + "_bottom"))
                .register();
        REGISTRATE.block(name + "_stairs", p -> new StairBlock((TFMGBlocks.CONCRETE.block.get().defaultBlockState()), p))
                .initialProperties(() -> Blocks.STONE)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, name))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.STAIRS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(blockEntry.asItem()), RecipeCategory.BUILDING_BLOCKS, c, 1))
                .item()
                .transform(customItemModel(name + "_stairs"))
                .register();

        return materialSet;
    }
}
