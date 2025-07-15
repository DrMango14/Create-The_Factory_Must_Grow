package com.drmangotea.tfmg.content.electricity.connection.cables;

import net.minecraft.core.Position;

public class SimplePos implements Position {
    private final double x;
    private final double y;
    private final double z;

    public SimplePos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double z() {
        return this.z;
    }
}