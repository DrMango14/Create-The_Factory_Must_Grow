package com.drmangotea.tfmg.base;


import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;
import java.util.Map;

public class MaxBlockVoltage {

    public static Map<BlockEntityType,Integer> MAX_VOLTAGES = new HashMap<>();


    static {

        add(TFMGBlockEntities.ACCUMULATOR.get(),2500);
        add(TFMGBlockEntities.COIL.get(),10000);
        add(TFMGBlockEntities.COPYCAT_CABLE.get(),1000);
        add(TFMGBlockEntities.DIAGONAL_CABLE_BLOCK.get(),6000);
        add(TFMGBlockEntities.CONVERTER.get(),2500);
        add(TFMGBlockEntities.STATOR.get(),Integer.MAX_VALUE);
        add(TFMGBlockEntities.CABLE_TUBE.get(),6000);
        add(TFMGBlockEntities.POLARIZER.get(),2500);
        add(TFMGBlockEntities.ELECTRIC_MOTOR.get(),2500);
        add(TFMGBlockEntities.RESISTOR.get(),2500);
        add(TFMGBlockEntities.CAPACITOR.get(),3500);
        add(TFMGBlockEntities.GENERATOR.get(),3500);
        add(TFMGBlockEntities.CABLE_CONNECTOR.get(),6000);
        add(TFMGBlockEntities.CABLE_HUB.get(),2500);
        add(TFMGBlockEntities.LIGHT_BULB.get(),500);
        add(TFMGBlockEntities.RGB_LIGHT_BULB.get(),500);
        add(TFMGBlockEntities.NEON_TUBE.get(),300);
        add(TFMGBlockEntities.CREATIVE_GENERATOR.get(),Integer.MAX_VALUE);


    }

    public static void add(BlockEntityType type, int voltage){
        MAX_VOLTAGES.put(type,voltage);
    }

}
