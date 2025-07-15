package com.drmangotea.tfmg.content.electricity.connection.cables;



public class CablePos extends SimplePos {
    public CablePos(double x, double y, double z) {
        super(x, y, z);
    }
    public CablePos subtract(CablePos pos) {
        return this.offset(-pos.x(), -pos.y(), -pos.z());
    }

    public CablePos offset(double x, double y, double z) {
        return x == 0 && y == 0 && z == 0 ? this : new CablePos(this.x() + x, this.y() + y, this.z() + z);
    }
}
