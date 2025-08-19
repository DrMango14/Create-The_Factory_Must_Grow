package com.drmangotea.tfmg.base;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.blocks.TFMGVanillaBlockStates;
import com.drmangotea.tfmg.config.TFMGStress;
import com.drmangotea.tfmg.content.decoration.FrameBlock;
import com.drmangotea.tfmg.content.decoration.TrussBlock;
import com.drmangotea.tfmg.content.decoration.doors.TFMGSlidingDoorBlock;
import com.drmangotea.tfmg.content.decoration.encased.TFMGEncasedCogwheelBlock;
import com.drmangotea.tfmg.content.decoration.encased.TFMGEncasedShaftBlock;
import com.drmangotea.tfmg.content.decoration.flywheels.TFMGFlywheelBlock;
import com.drmangotea.tfmg.content.electricity.connection.copycat_cable.CopycatCableBlock;
import com.drmangotea.tfmg.content.electricity.lights.neon_tube.NeonTubeBlock;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

import java.util.*;
import java.util.function.Supplier;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
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

    public static <B extends TFMGEncasedCogwheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedCogwheel(
            String casing, Supplier<CTSpriteShiftEntry> casingShift) {
        return b -> encasedCogwheelBase(b, casing, casingShift, AllBlocks.COGWHEEL::get, false);
    }

    public static <B extends TFMGEncasedCogwheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedLargeCogwheel(
            String casing, Supplier<CTSpriteShiftEntry> casingShift) {
        return b -> encasedCogwheelBase(b, casing, casingShift, AllBlocks.LARGE_COGWHEEL::get, true)
                .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(casingShift.get())));
    }

    private static <B extends TFMGEncasedCogwheelBlock, P> BlockBuilder<B, P> encasedCogwheelBase(BlockBuilder<B, P> b,
                                                                                                  String casing, Supplier<CTSpriteShiftEntry> casingShift, Supplier<ItemLike> drop, boolean large) {
        String encasedSuffix;
        if (!large) {
            encasedSuffix = "_encased_cogwheel_side" + (large ? "_connected" : "");
        } else encasedSuffix = "_encased_cogwheel_side_large";
        String blockFolder = large ? "encased_large_cogwheel" : "encased_cogwheel";
        String wood = casing.equals("steel") ? "steel_casing" : "heavy_machinery_casing";
        String gearbox = casing.equals("steel") ? "steel_gearbox" : "heavy_gearbox";

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
                            .texture("casing", TFMG.asResource("block/" + casing1 + "_casing"))
                            .texture("particle", TFMG.asResource("block/" + casing1 + "_casing"))
                            .texture("4", TFMG.asResource("block/" + gearbox))
                            .texture("1", TFMG.asResource("block/" + wood))
                            .texture("side", TFMG.asResource("block/" + casing1 + encasedSuffix));
                }, false))
                .item()
                .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/" + blockFolder + "/item"))
                        .texture("casing", TFMG.asResource("block/" + casing1 + "_casing"))
                        .texture("particle", TFMG.asResource("block/" + casing1 + "_casing"))
                        .texture("1", TFMG.asResource("block/" + wood))
                        .texture("side", TFMG.asResource("block/" + casing1 + encasedSuffix)))
                .build();
    }

    private static <B extends RotatedPillarKineticBlock, P> BlockBuilder<B, P> encasedBase(BlockBuilder<B, P> b,
                                                                                           Supplier<ItemLike> drop) {
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
    public static BlockEntry<TFMGFlywheelBlock> flywheel(String name, NonNullFunction<BlockBehaviour.Properties, TFMGFlywheelBlock> block) {
        return REGISTRATE.block(name + "_flywheel", block)
                .initialProperties(SharedProperties::softMetal)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(axeOrPickaxe())
                .transform(TFMGStress.setNoImpact())
                .blockstate(BlockStateGen.axisBlockProvider(true))
                .item()
                .transform(customItemModel())
                .register();
    }

    public static BlockEntry<TrussBlock> truss(String name) {
        return REGISTRATE.block(name + "_truss", TrussBlock::new)
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(p -> p.noOcclusion())
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

    public static final String[] COLORS = {"white", "blue", "light_blue", "red", "green", "lime", "pink", "magenta", "yellow", "gray", "light_gray", "brown", "cyan", "purple", "orange","black"};

    public static void generateCautionBlocks() {


        for (String color : COLORS) {

            if(Objects.equals(color, "black"))
                continue;

            String firstLetter = color.substring(0, 1).toUpperCase();
            String colorWithoutC = color.substring(1);

            String upperCaseColor = firstLetter + colorWithoutC;
            String light = "Light";
            if (upperCaseColor.contains(light)) {
                String nameWithoutLight = upperCaseColor.substring(6);

                String firstLetter2 = nameWithoutLight.substring(0, 1).toUpperCase();
                String colorWithoutC2 = nameWithoutLight.substring(1);

                upperCaseColor = light + " " + firstLetter2 + colorWithoutC2;


            }

            REGISTRATE.block(color + "_caution_block", TFMGHorizontalDirectionalBlock::new)
                    .initialProperties(() -> Blocks.COPPER_BLOCK)

                    .properties(p -> p.requiresCorrectToolForDrops())
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
                            .withExistingParent(c.getName(), p.modLoc("block/caution_block"))
                            .texture("0", p.modLoc("block/caution_block/" + color))
                            .texture("particle", p.modLoc("block/caution_block/" + color))
                    ))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.tag(AllTags.forgeItemTag("ingots/aluminum")), RecipeCategory.BUILDING_BLOCKS, c::get, 2))
                    .item()
                    .build()
                    .lang(upperCaseColor + " Caution Block")
                    .register();
        }
    }

    public static MaterialSet generateConcrete(boolean rebar) {

        String name = rebar ? "rebar_concrete" : "concrete";

        MaterialSet concrete = new MaterialSet();



        concrete.wall = REGISTRATE.block(name + "_wall", WallBlock::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.requiresCorrectToolForDrops())
                .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, "concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.WALLS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(concrete.block.get()), RecipeCategory.BUILDING_BLOCKS, c::get, 1))
                .item()
                .tag(ItemTags.WALLS)
                .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, "concrete"))
                .build()
                .register();

        concrete.stairs = REGISTRATE.block(name + "_stairs", p -> new StairBlock(() -> concrete.block.get().defaultBlockState(), p))
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.requiresCorrectToolForDrops())
                .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, "concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.STAIRS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(concrete.block.get()), RecipeCategory.BUILDING_BLOCKS, c::get, 1))
                .item()
                .tag(ItemTags.STAIRS)
                //.transform(b -> TFMGVanillaBlockStates.transformStairItem(b, "concrete"))
                .transform(customItemModel("concrete_stairs"))
                .register();


        concrete.block = REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate(simpleCubeAll("concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .transform(tagBlockAndItem("concrete"))
                .build()
                .register();

        concrete.slab = REGISTRATE.block(name + "_slab", SlabBlock::new)
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, "concrete"))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.SLABS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(concrete.block.get()), RecipeCategory.BUILDING_BLOCKS, c::get, 2))
                .item()
                .tag(ItemTags.SLABS)
                .transform(customItemModel("concrete_bottom"))
                .register();

        return concrete;
    }

    public static Map<String ,MaterialSet> generateColoredConcrete(boolean rebar) {

        String name = rebar ? "_rebar_concrete" : "_concrete";

        Map<String ,MaterialSet> list = new HashMap<>();

        for (String color : COLORS) {

            MaterialSet set = new MaterialSet();

            set.block=REGISTRATE.block(color + name, Block::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate(simpleCubeAll(color + "_concrete"))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .item()
                    .build()
                    .register();


            set.wall=REGISTRATE.block(color + name + "_wall", WallBlock::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, color + "_concrete"))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.WALLS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(set.block.asItem()), RecipeCategory.BUILDING_BLOCKS, c::get, 1))
                    .item()
                    .tag(ItemTags.WALLS)
                    .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, color + "_concrete"))
                    .build()
                    .register();

            set.stairs=REGISTRATE.block(color + name + "_stairs", p -> new StairBlock(() -> TFMGBlocks.CONCRETE.block.get().defaultBlockState(), p))
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, color + "_concrete"))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.STAIRS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(set.block.asItem()), RecipeCategory.BUILDING_BLOCKS, c::get, 1))
                    .item()
                    .tag(ItemTags.STAIRS)
                    // .transform(b -> TFMGVanillaBlockStates.transformStairItem(b, color + "_concrete"))
                    .transform(customItemModel(color + "_concrete_stairs"))
                    .register();


            set.slab=REGISTRATE.block(color + name + "_slab", SlabBlock::new)
                    .initialProperties(() -> Blocks.STONE)
                    .properties(p -> p.strength(rebar ? 12f : 3.5f, rebar ? 1200f : 3.5f))
                    .properties(p -> p.requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, color + "_concrete"))
                    .tag(BlockTags.NEEDS_STONE_TOOL)
                    .tag(BlockTags.SLABS)
                    .recipe((c, p) -> p.stonecutting(DataIngredient.items(set.block.asItem()), RecipeCategory.BUILDING_BLOCKS, c::get, 2))
                    .item()
                    .tag(ItemTags.SLABS)
                    .transform(customItemModel(color + "_concrete_bottom"))
                    .register();

            list.put(color,set);
        }

        return list;
    }

    public static MaterialSet makeVariants(BlockEntry<?> blockEntry) {
        return makeVariants(blockEntry, false);
    }

    public static MaterialSet makeVariants(BlockEntry<?> blockEntry, boolean recipe) {
        MaterialSet materialSet = new MaterialSet();

        materialSet.block = blockEntry;

        String name = blockEntry.getId().toString().replace("tfmg:", "");


        REGISTRATE.block(name + "_wall", WallBlock::new)
                .initialProperties(() -> blockEntry.get())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateWallBlockState(c, p, name))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.WALLS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(blockEntry.asItem()), RecipeCategory.BUILDING_BLOCKS, c::get, 1))
                .item()
                .tag(ItemTags.WALLS)
                .transform(b -> TFMGVanillaBlockStates.transformWallItem(b, name))
                .build()
                .register();

        REGISTRATE.block(name + "_slab", SlabBlock::new)
                .initialProperties(() -> blockEntry.get())
                .properties(p -> p.requiresCorrectToolForDrops())
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateSlabBlockState(c, p, name))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.SLABS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(blockEntry.asItem()), RecipeCategory.BUILDING_BLOCKS, c::get, 2))
                .item()
                .tag(ItemTags.SLABS)
                .transform(customItemModel(name + "_bottom"))
                .register();
        REGISTRATE.block(name + "_stairs", p -> new StairBlock(() -> TFMGBlocks.CONCRETE.block.get().defaultBlockState(), p))
                .initialProperties(() -> Blocks.STONE)
                .transform(pickaxeOnly())
                .blockstate((c, p) -> TFMGVanillaBlockStates.generateStairBlockState(c, p, name))
                .tag(BlockTags.NEEDS_STONE_TOOL)
                .tag(BlockTags.STAIRS)
                .recipe((c, p) -> p.stonecutting(DataIngredient.items(blockEntry.asItem()), RecipeCategory.BUILDING_BLOCKS, c::get, 1))
                .item()
                .tag(ItemTags.STAIRS)
                .transform(customItemModel(name + "_stairs"))
                .register();

        return materialSet;
    }
}