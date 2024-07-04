package com.drmangotea.createindustry.blocks.electricity.lights.rgb;

import com.drmangotea.createindustry.blocks.electricity.lights.LightBulbBlockEntity;
import com.simibubi.create.Create;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RGBLightBulbBlockEntity extends LightBulbBlockEntity {

    int red = 250;
    int green = 0;
    int blue = 0;

    public RGBLightBulbBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }

    public void changeColor(){
        red = Create.RANDOM.nextInt(255);
        green = Create.RANDOM.nextInt(255);
        blue = Create.RANDOM.nextInt(255);
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);


        compound.putInt("red",red);
        compound.putInt("green",green);
        compound.putInt("blue",blue);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        red = compound.getInt("red");
        green = compound.getInt("green");
        blue = compound.getInt("blue");



    }

}
