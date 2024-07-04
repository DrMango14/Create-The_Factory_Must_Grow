package com.drmangotea.tfmg.base;

import net.minecraft.core.BlockPos;

public class TFMGTools {

    public static float getDistance(BlockPos pos1, BlockPos pos2,boolean _2D){



        float x = Math.abs(pos1.getX()-pos2.getX());
        float y = Math.abs(pos1.getY()-pos2.getY());
        float z = Math.abs(pos1.getZ()-pos2.getZ());


        float distance2D = (float) Math.sqrt(x*x+z*z);

        if(_2D)
            return distance2D;


        return (float) Math.sqrt(distance2D*distance2D+y*y);


    }

}
