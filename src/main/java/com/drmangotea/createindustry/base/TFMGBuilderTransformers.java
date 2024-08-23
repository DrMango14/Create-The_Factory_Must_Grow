package com.drmangotea.createindustry.base;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.decoration.doors.TFMGSlidingDoorBlock;
import com.drmangotea.createindustry.blocks.encased.TFMGEncasedCogwheelBlock;
import com.drmangotea.createindustry.blocks.encased.TFMGEncasedShaftBlock;
import com.drmangotea.createindustry.blocks.electricity.cable_blocks.copycat_cable_block.CopycatCableBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.behaviour.DoorMovingInteraction;
import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorMovementBehaviour;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.*;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.BlockStateGen.axisBlock;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@SuppressWarnings("removal")
public class TFMGBuilderTransformers {

    public static <B extends TFMGSlidingDoorBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> slidingDoor(String type) {
        return b -> b.initialProperties(Material.METAL) // for villager AI..
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
                .loot((lr, block) -> lr.add(block, BlockLoot.createDoorTable(block)))
                .item()
                .tag(ItemTags.DOORS)
                .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                .model((c, p) -> p.blockSprite(c, p.modLoc("item/" + type + "_door")))
                .build();
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> surfaceScanner() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.horizontalBlock(c.get(), p.models()
                        .getExistingFile(p.modLoc("block/surface_scanner/block"))))
                .addLayer(() -> RenderType::cutoutMipped)
                .item()
                .transform(ModelGen.customItemModel("surface_scanner", "item"));

}

    public static <B extends TFMGEncasedShaftBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedShaft(String casing,
                                                                                                             Supplier<CTSpriteShiftEntry> casingShift) {
        return builder -> encasedBase(builder, () -> TFMGBlocks.NEON_TUBE.get())
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
        return b -> encasedCogwheelBase(b, casing, casingShift, () -> AllBlocks.COGWHEEL.get(), false);
    }

    public static <B extends TFMGEncasedCogwheelBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> encasedLargeCogwheel(
            String casing, Supplier<CTSpriteShiftEntry> casingShift) {
        return b -> encasedCogwheelBase(b, casing, casingShift, () -> AllBlocks.LARGE_COGWHEEL.get(), true)
                .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(casingShift.get())));
    }

    private static <B extends TFMGEncasedCogwheelBlock, P> BlockBuilder<B, P> encasedCogwheelBase(BlockBuilder<B, P> b,
                                                                                      String casing, Supplier<CTSpriteShiftEntry> casingShift, Supplier<ItemLike> drop, boolean large) {
        String encasedSuffix;
        if(!large) {
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
                            .texture("casing", CreateTFMG.asResource("block/" + casing1 + "_casing"))
                            .texture("particle", CreateTFMG.asResource("block/" + casing1 + "_casing"))
                            .texture("4", CreateTFMG.asResource("block/" + gearbox))
                            .texture("1", CreateTFMG.asResource("block/" + wood))
                            .texture("side", CreateTFMG.asResource("block/" + casing1 + encasedSuffix));
                }, false))
                .item()
                .model((c, p) -> p.withExistingParent(c.getName(), p.modLoc("block/" + blockFolder + "/item"))
                        .texture("casing", CreateTFMG.asResource("block/" + casing1 + "_casing"))
                        .texture("particle", CreateTFMG.asResource("block/" + casing1 + "_casing"))
                        .texture("1", CreateTFMG.asResource("block/" + wood))
                        .texture("side", CreateTFMG.asResource("block/" + casing1 + encasedSuffix)))
                .build();
    }

    private static <B extends RotatedPillarKineticBlock, P> BlockBuilder<B, P> encasedBase(BlockBuilder<B, P> b,
                                                                                           Supplier<ItemLike> drop) {
        return b.initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::noOcclusion)
                .transform(BlockStressDefaults.setNoImpact())
                .loot((p, lb) -> p.dropOther(lb, drop.get()));
    }


    public static <B extends CopycatCableBlock, P> NonNullUnaryOperator<BlockBuilder<B, P>> copycatCable() {
        return b -> b.initialProperties(SharedProperties::softMetal)
                .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                        .getExistingFile(p.mcLoc("air"))))
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.noOcclusion()
                        .color(MaterialColor.NONE))
                .addLayer(() -> RenderType::solid)
                .addLayer(() -> RenderType::cutout)
                .addLayer(() -> RenderType::cutoutMipped)
               // .addLayer(() -> RenderType::translucent)
                .color(() -> CopycatCableBlock::wrappedColor)
                .transform(TagGen.axeOrPickaxe());
    }

}
