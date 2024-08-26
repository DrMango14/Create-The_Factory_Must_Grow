package com.drmangotea.tfmg.blocks.electricity.base.cables;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class WireConnection {


    public final WireManager.Conductor material;

    public final float lenght;

    public final float resistance;

    public static final float CABLE_THICKNESS = 1.5f;

    public final BlockPos point1;

    public final BlockPos point2;

    public final boolean neighborConnection;

    public final boolean shouldRender;


    public WireConnection(WireManager.Conductor material, float lenght,BlockPos point1, BlockPos point2, boolean render, boolean neighborConnection){
        this.material = material;
        this.lenght = lenght;
        this.point1 = point1;
        this.point2 = point2;
        this.shouldRender = render;
        this.neighborConnection = neighborConnection;

        this.resistance = material.resistivity*(lenght/CABLE_THICKNESS);
    }


    public void saveConnection(CompoundTag compound,int value){
        compound.putInt("X1"+value,point1.getX());
        compound.putInt("Y1"+value,point1.getY());
        compound.putInt("Z1"+value,point1.getZ());
        compound.putInt("X2"+value,point2.getX());
        compound.putInt("Y2"+value,point2.getY());
        compound.putInt("Z2"+value,point2.getZ());

        //compound.putLong("Point1"+value, point1.asLong());
        //compound.putLong("Point2"+value, point2.asLong());

        compound.putFloat("Lenght"+value,lenght);

        compound.putBoolean("NeighborConnection"+value,neighborConnection);

        compound.putBoolean("ShouldRender"+value,shouldRender);

        compound.putString("Material"+value, material.name());


    }



}
