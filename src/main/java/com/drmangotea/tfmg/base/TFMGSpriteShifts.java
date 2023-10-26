package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.CreateTFMG;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;

public class TFMGSpriteShifts {

    public static final CTSpriteShiftEntry
            CAST_IRON_BLOCK = omni("cast_iron_block"),
            STEEL_BLOCK = omni("steel_block");
    public static final CTSpriteShiftEntry
            STEEL_CASING = omni("steel_casing");


    public static final CTSpriteShiftEntry STEEL_SCAFFOLD = horizontal("scaffold/steel_scaffold"),
            ALUMINUM_SCAFFOLD = horizontal("scaffold/aluminum_scaffold");
    public static final CTSpriteShiftEntry
            ALUMINUM_SCAFFOLD_TOP = omni("aluminum_casing");

    public static final CTSpriteShiftEntry
            HEAVY_MACHINERY_CASING = omni("heavy_machinery_casing");

    public static final CTSpriteShiftEntry
            STEEL_FLUID_TANK = getCT(AllCTTypes.RECTANGLE, "steel_fluid_tank"),
            STEEL_FLUID_TANK_TOP = getCT(AllCTTypes.RECTANGLE, "steel_fluid_tank_top"),
            STEEL_FLUID_TANK_INNER = getCT(AllCTTypes.RECTANGLE, "steel_fluid_tank_inner");

    public static final CTSpriteShiftEntry
            STEEL_SHEETMETAL = getCT(AllCTTypes.RECTANGLE, "steel_sheetmetal");

    public static final CTSpriteShiftEntry STEEL_SCAFFOLD_INSIDE = horizontal("scaffold/steel_scaffold_inside"),
            ALUMINUM_SCAFFOLD_INSIDE = horizontal("scaffold/aluminum_scaffold_inside");



    public static final CTSpriteShiftEntry
            STEEL_ENCASED_COGWHEEL_SIDE = vertical("steel_encased_cogwheel_side"),
            STEEL_ENCASED_COGWHEEL_OTHERSIDE = horizontal("steel_encased_cogwheel_side"),
            HEAVY_CASING_ENCASED_COGWHEEL_SIDE = vertical("heavy_casing_encased_cogwheel_side"),
            HEAVY_CASING_ENCASED_COGWHEEL_OTHERSIDE = horizontal("heavy_casing_encased_cogwheel_side");

    //////////////////////
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
        return CTSpriteShifter.getCT(type, CreateTFMG.asResource("block/" + blockTextureName), CreateTFMG.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
}
