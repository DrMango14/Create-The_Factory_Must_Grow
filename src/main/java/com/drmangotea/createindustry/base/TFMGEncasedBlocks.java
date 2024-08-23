package com.drmangotea.createindustry.base;

import com.drmangotea.createindustry.blocks.encased.TFMGEncasedCogwheelBlock;
import com.drmangotea.createindustry.blocks.encased.TFMGEncasedShaftBlock;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.Couple;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.MaterialColor;

import static com.drmangotea.createindustry.CreateTFMG.REGISTRATE;
import static com.drmangotea.createindustry.blocks.encased.TFMGEncasedCogwheelBlock.*;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class TFMGEncasedBlocks {





    public static final BlockEntry<TFMGEncasedShaftBlock> STEEL_ENCASED_SHAFT =
            REGISTRATE.block("steel_encased_shaft", p -> new TFMGEncasedShaftBlock(p, TFMGBlocks.STEEL_CASING::get))
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .transform(TFMGBuilderTransformers.encasedShaft("steel", () -> TFMGSpriteShifts.STEEL_CASING))
                    //     .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedShaftBlock> HEAVY_CASING_ENCASED_SHAFT =
            REGISTRATE.block("heavy_casing_encased_shaft", p -> new TFMGEncasedShaftBlock(p, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .transform(TFMGBuilderTransformers.encasedShaft("heavy_casing", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
                    //.transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                       .transform(axeOrPickaxe())
                    .register();

    /////////

    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_COGWHEEL =
            REGISTRATE.block("steel_encased_cogwheel", p -> regular(p, false, TFMGBlocks.STEEL_CASING::get))
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("steel", () -> TFMGSpriteShifts.STEEL_CASING))
                    //  .transform(EncasingRegistry.addVariantTo(AllBlocks.COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.STEEL_CASING,
                            Couple.create(TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_CASING_ENCASED_COGWHEEL =
            REGISTRATE.block("heavy_casing_encased_cogwheel", p -> regular(p, false, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("heavy_casing", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
                    //  .transform(EncasingRegistry.addVariantTo(AllBlocks.COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                            Couple.create(TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    //////
    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_LARGE_COGWHEEL = REGISTRATE
            .block("steel_encased_large_cogwheel",
                    p -> regular(p, true, TFMGBlocks.STEEL_CASING::get))
            .properties(p -> p.color(MaterialColor.PODZOL))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("steel", () -> TFMGSpriteShifts.STEEL_CASING))
           // .transform(EncasingRegistry.addVariantTo(AllBlocks.LARGE_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_CASING_ENCASED_LARGE_COGWHEEL = REGISTRATE
            .block("heavy_casing_encased_large_cogwheel", p -> regular(p, true, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("heavy_casing", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
            // .transform(EncasingRegistry.addVariantTo(AllBlocks.LARGE_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();


    /////////////



    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_STEEL_COGWHEEL =
            REGISTRATE.block("steel_encased_steel_cogwheel", p -> steel(p, false, TFMGBlocks.STEEL_CASING::get))
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("steel", () -> TFMGSpriteShifts.STEEL_CASING))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.STEEL_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.STEEL_CASING,
                            Couple.create(TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_CASING_ENCASED_STEEL_COGWHEEL =
            REGISTRATE.block("heavy_casing_encased_steel_cogwheel", p -> steel(p, false, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("heavy_casing", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.STEEL_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                            Couple.create(TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    //////
    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_LARGE_STEEL_COGWHEEL = REGISTRATE
            .block("steel_encased_large_steel_cogwheel",
                    p -> steel(p, true, TFMGBlocks.STEEL_CASING::get))
            .properties(p -> p.color(MaterialColor.PODZOL))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("steel", () -> TFMGSpriteShifts.STEEL_CASING))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_STEEL_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_CASING_ENCASED_LARGE_STEEL_COGWHEEL = REGISTRATE
            .block("heavy_casing_encased_large_steel_cogwheel", p -> steel(p, true, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("heavy_casing", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_STEEL_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();


    ////////////////////////////
    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_ALUMINUM_COGWHEEL =
            REGISTRATE.block("steel_encased_aluminum_cogwheel", p -> aluminum(p, false, TFMGBlocks.STEEL_CASING::get))
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("steel", () -> TFMGSpriteShifts.STEEL_CASING))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.ALUMINUM_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.STEEL_CASING,
                            Couple.create(TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_CASING_ENCASED_ALUMINUM_COGWHEEL =
            REGISTRATE.block("heavy_casing_encased_aluminum_cogwheel", p -> aluminum(p, false, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
                    .properties(p -> p.color(MaterialColor.COLOR_GRAY))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("heavy_casing", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.ALUMINUM_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                            Couple.create(TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    //////
    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_LARGE_ALUMINUM_COGWHEEL = REGISTRATE
            .block("steel_encased_large_aluminum_cogwheel",
                    p -> aluminum(p, true, TFMGBlocks.STEEL_CASING::get))
            .properties(p -> p.color(MaterialColor.PODZOL))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("steel", () -> TFMGSpriteShifts.STEEL_CASING))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_ALUMINUM_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_CASING_ENCASED_LARGE_ALUMINUM_COGWHEEL = REGISTRATE
            .block("heavy_casing_encased_large_aluminum_cogwheel", p -> aluminum(p, true, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
            .properties(p -> p.color(MaterialColor.TERRACOTTA_BROWN))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("heavy_casing", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_ALUMINUM_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();






    public static void register() {}

}
