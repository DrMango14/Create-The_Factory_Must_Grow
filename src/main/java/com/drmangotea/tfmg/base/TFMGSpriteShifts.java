package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class TFMGSpriteShifts {

    public static final CTSpriteShiftEntry CAST_IRON_BLOCK = omni("cast_iron_block"), LEAD_BLOCK = omni("lead_block"), STEEL_BLOCK = omni("steel_block");
    public static final CTSpriteShiftEntry HEAVY_MACHINERY_CASING = omni("heavy_machinery_casing"), ELECTRIC_CASING = omni("electric_casing"), STEEL_CASING = omni("steel_casing"), INDUSTRIAL_ALUMINUM_CASING = omni("industrial_aluminum_casing");
    public static final CTSpriteShiftEntry CAPACITOR = getCT(AllCTTypes.RECTANGLE, "capacitor_side"), ACCUMULATOR = getCT(AllCTTypes.RECTANGLE, "accumulator_side");
    public static final CTSpriteShiftEntry STEEL_SCAFFOLD = horizontal("scaffold/steel_scaffold"), ALUMINUM_SCAFFOLD = horizontal("scaffold/aluminum_scaffold");
    public static final CTSpriteShiftEntry ALUMINUM_SCAFFOLD_TOP = omni("aluminum_casing");
    public static final CTSpriteShiftEntry STEEL_SCAFFOLD_INSIDE = horizontal("scaffold/steel_scaffold_inside"), ALUMINUM_SCAFFOLD_INSIDE = horizontal("scaffold/aluminum_scaffold_inside");
    public static final CTSpriteShiftEntry STEEL_ENCASED_COGWHEEL_SIDE = vertical("steel_encased_cogwheel_side"), STEEL_ENCASED_COGWHEEL_OTHERSIDE = horizontal("steel_encased_cogwheel_side"), HEAVY_CASING_ENCASED_COGWHEEL_SIDE = vertical("heavy_machinery_encased_cogwheel_side"), HEAVY_CASING_ENCASED_COGWHEEL_OTHERSIDE = horizontal("heavy_machinery_encased_cogwheel_side");
    public static final CTSpriteShiftEntry COKE_OVEN_TOP = getCT(AllCTTypes.RECTANGLE, "coke_oven/top"), COKE_OVEN_BOTTOM = getCT(AllCTTypes.RECTANGLE, "coke_oven/bottom"), COKE_OVEN_BACK = getCT(AllCTTypes.RECTANGLE, "coke_oven/side"), COKE_OVEN_SIDE = getCT(AllCTTypes.RECTANGLE, "coke_oven/side");

    public static final CTSpriteShiftEntry FIREBOX_TOP = getCT(AllCTTypes.RECTANGLE, "firebox_top");

    public static final CTSpriteShiftEntry STEEL_FLUID_TANK = getCT(AllCTTypes.RECTANGLE, "steel_fluid_tank"), STEEL_FLUID_TANK_TOP = getCT(AllCTTypes.RECTANGLE, "steel_fluid_tank_top"), STEEL_FLUID_TANK_INNER = getCT(AllCTTypes.RECTANGLE, "steel_fluid_tank_inner");
    public static final CTSpriteShiftEntry ALUMINUM_FLUID_TANK = getCT(AllCTTypes.RECTANGLE, "aluminum_fluid_tank"), ALUMINUM_FLUID_TANK_TOP = getCT(AllCTTypes.RECTANGLE, "aluminum_fluid_tank_top"), ALUMINUM_FLUID_TANK_INNER = getCT(AllCTTypes.RECTANGLE, "aluminum_fluid_tank_inner");
    public static final CTSpriteShiftEntry CAST_IRON_FLUID_TANK = getCT(AllCTTypes.RECTANGLE, "cast_iron_fluid_tank"), CAST_IRON_FLUID_TANK_TOP = getCT(AllCTTypes.RECTANGLE, "cast_iron_fluid_tank_top"), CAST_IRON_FLUID_TANK_INNER = getCT(AllCTTypes.RECTANGLE, "cast_iron_fluid_tank_inner");

    public static final CTSpriteShiftEntry STEEL_VAT = getCT(AllCTTypes.RECTANGLE, "steel_vat"), STEEL_VAT_TOP = getCT(AllCTTypes.RECTANGLE, "steel_vat_top"), STEEL_VAT_INNER = getCT(AllCTTypes.RECTANGLE, "steel_vat_inner");
    public static final CTSpriteShiftEntry CAST_IRON_VAT = getCT(AllCTTypes.RECTANGLE, "cast_iron_vat"), CAST_IRON_VAT_TOP = getCT(AllCTTypes.RECTANGLE, "cast_iron_vat_top"), CAST_IRON_VAT_INNER = getCT(AllCTTypes.RECTANGLE, "cast_iron_vat_inner");
    public static final CTSpriteShiftEntry FIREPROOF_VAT = getCT(AllCTTypes.RECTANGLE, "firebrick_vat");

    public static final CTSpriteShiftEntry BLAST_FURNACE_REINFORCEMENT = vertical("blast_furnace_reinforcement");
    public static final CTSpriteShiftEntry RUSTED_BLAST_FURNACE_REINFORCEMENT = vertical("rusted_blast_furnace_reinforcement");
    public static final CTSpriteShiftEntry SEGMENTED_DISPLAY_SCREEN = horizontal("segmented_display_screen");

    public static final CTSpriteShiftEntry BLAST_STOVE_SIDE = getCT(AllCTTypes.RECTANGLE, "blast_stove_side"), BLAST_STOVE_TOP = getCT(AllCTTypes.RECTANGLE, "blast_stove_top");
    public static final CTSpriteShiftEntry
            REGULAR_ENGINE_TOP = vertical("engines/engine_top"),
            REGULAR_ENGINE_BOTTOM = vertical("engines/engine_bottom"),
            REGULAR_ENGINE_SIDE = horizontal("engines/engine_side");
    public static final SpriteShiftEntry WINDING_MACHINE_COPPER_WIRE = get("block/winding_machine_copper_wire", "block/winding_machine_copper_wire_scroll");


    ///////////////////////
    public static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    public static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL_KRYPPERS, name);
    }

    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.VERTICAL, name);
    }

    /////

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, TFMG.asResource("block/" + blockTextureName), TFMG.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(TFMG.asResource(originalLocation), TFMG.asResource(targetLocation));
    }

}
