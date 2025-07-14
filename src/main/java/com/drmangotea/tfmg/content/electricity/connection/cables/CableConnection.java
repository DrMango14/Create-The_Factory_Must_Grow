package com.drmangotea.tfmg.content.electricity.connection.cables;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.checkerframework.checker.units.qual.C;

public class CableConnection {

    public final CablePos pos1;
    public final CablePos pos2;
    public final BlockPos blockPos1;
    public final boolean visible;
    public final CableType type;

    public CableConnection(CablePos pos1, CablePos pos2,BlockPos blockPos1,CableType type, boolean visible){
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.blockPos1 = blockPos1;
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

        //compoundTag.putLong("BlockPos1", blockPos1.asLong());
        //compoundTag.putLong("BlockPos2", blockPos2.asLong());

        compoundTag.putLong("Pos", blockPos1.asLong());




        compoundTag.putBoolean("Visible", visible);

        compoundTag.putString("CableType", type.toString());

        return compoundTag;
    }
    public static CableConnection loadConnection(CompoundTag compoundTag){



        CablePos pos1  = new CablePos(compoundTag.getDouble("X1"),compoundTag.getDouble("Y1"),compoundTag.getDouble("Z1"));
        CablePos pos2 = new CablePos(compoundTag.getDouble("X2"),compoundTag.getDouble("Y2"),compoundTag.getDouble("Z2"));

        //BlockPos blockPos1 = BlockPos.of(compoundTag.getLong("BlockPos1"));
        //BlockPos blockPos2 = BlockPos.of(compoundTag.getLong("BlockPos2"));

        BlockPos blockPos1 = BlockPos.of(compoundTag.getLong("Pos"));

        boolean visible = compoundTag.getBoolean("Visible");
        CableType type = CableType.valueOf(compoundTag.getString("CableType"));
        return new CableConnection(pos1,pos2,blockPos1,type,visible);
    }
    public float getLength(){
        return TFMGUtils.getDistance(new BlockPos((int) pos1.x(), (int) pos1.y(), (int) pos1.z()),new BlockPos((int) pos2.x(), (int) pos2.y(), (int) pos2.z()), false);
    }



    public enum CableType{
        NONE(TFMGItems.COPPER_WIRE, 0,0xffffff),
        COPPER(TFMGItems.COPPER_WIRE, 0.00188f,0xD8735A),
        ALUMINUM(TFMGItems.ALUMINUM_WIRE, 0.0027f,0xEDEFEF),
        CONSTANTAN(TFMGItems.CONSTANTAN_WIRE, 1f,0xEDEFEF),
        STEEL_REINFORCED_ALUMINUM(TFMGItems.COPPER_WIRE, 0.0027f,0xB8A08D)
        ;
        public final ItemEntry<?> wire;
        public final float resistivity;
        public final int color;
        CableType(ItemEntry<?> wire, float resistivity, int color){
            this.wire = wire;
            this.resistivity = resistivity;
            this.color = color;
        }
    }
}
