package com.drmangotea.tfmg.content.electricity.connection.cables;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.connection.cable_type.CableType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class CableConnection {

    public final CablePos pos1;
    public final CablePos pos2;
    public final BlockPos blockPos1;
    public final BlockPos blockPos2;
    public final boolean visible;
    public final CableType type;

    public CableConnection(CablePos pos1, CablePos pos2,BlockPos blockPos1, BlockPos blockPos2,CableType type, boolean visible){
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.blockPos1 = blockPos1;
        this.blockPos2 = blockPos2;
        this.visible = visible;
        this.type = type;
    }

    public CompoundTag saveConnection(){
        CompoundTag compoundTag = new CompoundTag();

        compoundTag.putDouble("X1", pos1.x());
        compoundTag.putDouble("Y1", pos1.y());
        compoundTag.putDouble("Z1", pos1.z());

        compoundTag.putDouble("X2", pos2.x());
        compoundTag.putDouble("Y2", pos2.y());
        compoundTag.putDouble("Z2", pos2.z());


        compoundTag.putLong("Pos", blockPos1.asLong());




        compoundTag.putBoolean("Visible", visible);

        compoundTag.putString("CableType", type.getKey().toString());

        return compoundTag;
    }
    public static CableConnection loadConnection(CompoundTag compoundTag){



        CablePos pos1  = new CablePos(compoundTag.getDouble("X1"),compoundTag.getDouble("Y1"),compoundTag.getDouble("Z1"));
        CablePos pos2 = new CablePos(compoundTag.getDouble("X2"),compoundTag.getDouble("Y2"),compoundTag.getDouble("Z2"));


        BlockPos blockPos1 = BlockPos.of(compoundTag.getLong("Pos"));
        BlockPos blockPos2 = BlockPos.of(compoundTag.getLong("Pos2"));

        boolean visible = compoundTag.getBoolean("Visible");
        CableType type = TFMGUtils.getCableType(ResourceLocation.parse(compoundTag.getString("CableType")));
        return new CableConnection(pos1,pos2,blockPos1, blockPos2,type,visible);
    }
    public float getLength(){
        return TFMGUtils.getDistance(new BlockPos((int) pos1.x(), (int) pos1.y(), (int) pos1.z()),new BlockPos((int) pos2.x(), (int) pos2.y(), (int) pos2.z()), false);
    }

}
