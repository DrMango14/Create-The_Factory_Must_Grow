package com.drmangotea.tfmg.registry;

import com.drmangotea.tfmg.base.TFMGBuilderTransformers;
import com.drmangotea.tfmg.base.TFMGSpriteShifts;
import com.drmangotea.tfmg.content.decoration.kinetics.encased.TFMGEncasedCogwheelBlock;
import com.drmangotea.tfmg.content.decoration.kinetics.encased.TFMGEncasedShaftBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.encasing.EncasingRegistry;
import com.simibubi.create.content.kinetics.simpleRelays.encased.EncasedCogCTBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.data.Couple;

import static com.drmangotea.tfmg.TFMG.REGISTRATE;
import static com.drmangotea.tfmg.content.decoration.kinetics.encased.TFMGEncasedCogwheelBlock.*;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class TFMGEncasedBlocks {

    public static final BlockEntry<TFMGEncasedShaftBlock> STEEL_ENCASED_SHAFT =
            REGISTRATE.block("steel_encased_shaft", p -> new TFMGEncasedShaftBlock(p, TFMGBlocks.STEEL_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedShaft("steel", () -> TFMGSpriteShifts.STEEL_CASING))
                    .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedShaftBlock> HEAVY_ENCASED_SHAFT =
            REGISTRATE.block("heavy_encased_shaft", p -> new TFMGEncasedShaftBlock(p, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedShaft("heavy_casing", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING))
                    .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedShaftBlock> INDUSTRIAL_ENCASED_SHAFT =
            REGISTRATE.block("industrial_encased_shaft", p -> new TFMGEncasedShaftBlock(p, TFMGBlocks.ALUMINUM_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedShaft("industrial", () -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING))
                    .transform(EncasingRegistry.addVariantTo(AllBlocks.SHAFT))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_COGWHEEL =
            REGISTRATE.block("steel_encased_cogwheel", p -> wood(p, false, TFMGBlocks.STEEL_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("steel", "wood", () -> TFMGSpriteShifts.STEEL_CASING, AllBlocks.COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(AllBlocks.COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.STEEL_CASING,
                            Couple.create(TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_ENCASED_COGWHEEL =
            REGISTRATE.block("heavy_encased_cogwheel", p -> wood(p, false, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("heavy_casing", "wood", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING, AllBlocks.COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(AllBlocks.COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                            Couple.create(TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> INDUSTRIAL_ENCASED_COGWHEEL =
            REGISTRATE.block("industrial_encased_cogwheel", p -> wood(p, false, TFMGBlocks.ALUMINUM_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("industrial", "wood", () -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING, AllBlocks.COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(AllBlocks.COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING,
                            Couple.create(TFMGSpriteShifts.INDUSTRIAL_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.INDUSTRIAL_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_LARGE_COGWHEEL = REGISTRATE
            .block("steel_encased_large_cogwheel", p -> wood(p, true, TFMGBlocks.STEEL_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("steel", "wood", () -> TFMGSpriteShifts.STEEL_CASING, AllBlocks.LARGE_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(AllBlocks.LARGE_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_ENCASED_LARGE_COGWHEEL = REGISTRATE
            .block("heavy_encased_large_cogwheel", p -> wood(p, true, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("heavy_casing", "wood", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING, AllBlocks.LARGE_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(AllBlocks.LARGE_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> INDUSTRIAL_ENCASED_LARGE_COGWHEEL = REGISTRATE
            .block("industrial_encased_large_cogwheel", p -> wood(p, true, TFMGBlocks.ALUMINUM_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("industrial", "wood", () -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING, AllBlocks.LARGE_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(AllBlocks.LARGE_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_STEEL_COGWHEEL =
            REGISTRATE.block("steel_encased_steel_cogwheel", p -> steel(p, false, TFMGBlocks.STEEL_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("steel", "steel", () -> TFMGSpriteShifts.STEEL_CASING, TFMGBlocks.STEEL_COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.STEEL_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.STEEL_CASING,
                            Couple.create(TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_ENCASED_STEEL_COGWHEEL =
            REGISTRATE.block("heavy_encased_steel_cogwheel", p -> steel(p, false, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("heavy_casing", "steel", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING, TFMGBlocks.STEEL_COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.STEEL_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                            Couple.create(TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> INDUSTRIAL_ENCASED_STEEL_COGWHEEL =
            REGISTRATE.block("industrial_encased_steel_cogwheel", p -> steel(p, false, TFMGBlocks.ALUMINUM_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("industrial", "steel", () -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING, TFMGBlocks.STEEL_COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.STEEL_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING,
                            Couple.create(TFMGSpriteShifts.INDUSTRIAL_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.INDUSTRIAL_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    //////
    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_LARGE_STEEL_COGWHEEL = REGISTRATE
            .block("steel_encased_large_steel_cogwheel", p -> steel(p, true, TFMGBlocks.STEEL_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("steel", "steel", () -> TFMGSpriteShifts.STEEL_CASING, TFMGBlocks.LARGE_STEEL_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_STEEL_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_ENCASED_LARGE_STEEL_COGWHEEL = REGISTRATE
            .block("heavy_encased_large_steel_cogwheel", p -> steel(p, true, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("heavy_casing", "steel", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING, TFMGBlocks.LARGE_STEEL_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_STEEL_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> INDUSTRIAL_ENCASED_LARGE_STEEL_COGWHEEL = REGISTRATE
            .block("industrial_encased_large_steel_cogwheel", p -> steel(p, true, TFMGBlocks.ALUMINUM_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("industrial", "steel", () -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING, TFMGBlocks.LARGE_STEEL_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_STEEL_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    ////////////////////////////
    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_ALUMINUM_COGWHEEL =
            REGISTRATE.block("steel_encased_aluminum_cogwheel", p -> aluminum(p, false, TFMGBlocks.STEEL_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("steel", "aluminum", () -> TFMGSpriteShifts.STEEL_CASING, TFMGBlocks.ALUMINUM_COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.ALUMINUM_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.STEEL_CASING,
                            Couple.create(TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.STEEL_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_ENCASED_ALUMINUM_COGWHEEL =
            REGISTRATE.block("heavy_encased_aluminum_cogwheel", p -> aluminum(p, false, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("heavy_casing", "aluminum", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING, TFMGBlocks.ALUMINUM_COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.ALUMINUM_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.HEAVY_MACHINERY_CASING,
                            Couple.create(TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.HEAVY_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> INDUSTRIAL_ENCASED_ALUMINUM_COGWHEEL =
            REGISTRATE.block("industrial_encased_aluminum_cogwheel", p -> aluminum(p, false, TFMGBlocks.ALUMINUM_CASING::get))
                    .transform(TFMGBuilderTransformers.encasedCogwheel("industrial", "aluminum", () -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING, TFMGBlocks.ALUMINUM_COGWHEEL::get))
                    .transform(EncasingRegistry.addVariantTo(TFMGBlocks.ALUMINUM_COGWHEEL))
                    .onRegister(CreateRegistrate.connectedTextures(() -> new EncasedCogCTBehaviour(TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING,
                            Couple.create(TFMGSpriteShifts.INDUSTRIAL_CASING_ENCASED_COGWHEEL_SIDE,
                                    TFMGSpriteShifts.INDUSTRIAL_CASING_ENCASED_COGWHEEL_OTHERSIDE))))
                    .transform(axeOrPickaxe())
                    .register();

    //////
    public static final BlockEntry<TFMGEncasedCogwheelBlock> STEEL_ENCASED_LARGE_ALUMINUM_COGWHEEL = REGISTRATE
            .block("steel_encased_large_aluminum_cogwheel", p -> aluminum(p, true, TFMGBlocks.STEEL_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("steel", "aluminum", () -> TFMGSpriteShifts.STEEL_CASING, TFMGBlocks.LARGE_ALUMINUM_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_ALUMINUM_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> HEAVY_ENCASED_LARGE_ALUMINUM_COGWHEEL = REGISTRATE
            .block("heavy_encased_large_aluminum_cogwheel", p -> aluminum(p, true, TFMGBlocks.HEAVY_MACHINERY_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("heavy_casing", "aluminum", () -> TFMGSpriteShifts.HEAVY_MACHINERY_CASING, TFMGBlocks.LARGE_ALUMINUM_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_ALUMINUM_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static final BlockEntry<TFMGEncasedCogwheelBlock> INDUSTRIAL_ENCASED_LARGE_ALUMINUM_COGWHEEL = REGISTRATE
            .block("industrial_encased_large_aluminum_cogwheel", p -> aluminum(p, true, TFMGBlocks.ALUMINUM_CASING::get))
            .transform(TFMGBuilderTransformers.encasedLargeCogwheel("industrial", "aluminum", () -> TFMGSpriteShifts.INDUSTRIAL_ALUMINUM_CASING, TFMGBlocks.LARGE_ALUMINUM_COGWHEEL::get))
            .transform(EncasingRegistry.addVariantTo(TFMGBlocks.LARGE_ALUMINUM_COGWHEEL))
            .transform(axeOrPickaxe())
            .register();

    public static void init() {}

}
