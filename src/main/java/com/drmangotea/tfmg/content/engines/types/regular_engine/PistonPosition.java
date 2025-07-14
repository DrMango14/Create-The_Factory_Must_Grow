package com.drmangotea.tfmg.content.engines.types.regular_engine;

public class PistonPosition {

    private final float xOffset;
    private final float yOffset;
    private final float zOffset;
    private final float rotation;

    public PistonPosition(float x, float y,float z, float rotation){
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
        this.rotation = rotation;
    }
    public float getXOffset(){
        return xOffset;
    }
    public float getYOffset(){
        return yOffset;
    }
    public float getZOffset(){
        return zOffset;
    }
    public float getRotation(){
        return rotation;
    }
}
