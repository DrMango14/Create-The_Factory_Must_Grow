package com.drmangotea.createindustry.blocks.electricity.lights.neon;

import com.drmangotea.createindustry.blocks.electricity.cable_blocks.CableTubeBlockEntity;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.drmangotea.createindustry.blocks.electricity.lights.LightBulbBlock.LIGHT;
import static com.drmangotea.createindustry.blocks.electricity.lights.neon.NeonTubeBlock.ACTIVE;

public class NeonTubeBlockEntity extends CableTubeBlockEntity {


    public DyeColor color;

    boolean shouldDestroy = false;

    int red = 250;
    int green = 0;
    int blue = 0;

    public LerpedFloat glow = LerpedFloat.linear();
    public NeonTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        color = DyeColor.WHITE;
    }

    @Override
    public void tick() {
        super.tick();

        if(shouldDestroy){
            level.destroyBlock(getBlockPos(),false);
            return;
        }

        if(energy.getEnergyStored()!=0&&voltage!=0) {
            glow.chase(voltage, 0.4, LerpedFloat.Chaser.EXP);
            glow.tickChaser();

            if(voltage!=0) {
                useEnergy(1);
                if (!getBlockState().getValue(ACTIVE))
                    level.setBlock(getBlockPos(), getBlockState().setValue(ACTIVE,true), 2);
            }
        }else {
            //if (getBlockState().getValue(ACTIVE))
                level.setBlock(getBlockPos(), getBlockState().setValue(ACTIVE,false), 2);
            glow.chase(0, 0.4, LerpedFloat.Chaser.EXP);
            glow.tickChaser();

        }



    }

    @Override
    public void explode() {

        voltage = 0;


        level.playSound(null, getBlockPos(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 2.0F,
                level.random.nextFloat() * 0.4F + 0.8F);

        level.destroyBlock(getBlockPos(),false);



        shouldDestroy = true;


        sendStuff();


    }
    public void setColor(DyeColor color) {


        if(color==DyeColor.BLACK||color == DyeColor.LIGHT_GRAY|| color == DyeColor.GRAY)
            changeColor();

        this.color = color;
        notifyUpdate();
    }


    @Override
    public float maxVoltage() {
        return 300;
    }
    public void changeColor(){
        red = Create.RANDOM.nextInt(255);
        green = Create.RANDOM.nextInt(255);
        blue = Create.RANDOM.nextInt(255);
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        NBTHelper.writeEnum(compound,"color",color);

        compound.putInt("red",red);
        compound.putInt("green",green);
        compound.putInt("blue",blue);


    }


    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        color = NBTHelper.readEnum(compound,"color",DyeColor.class);
        red = compound.getInt("red");
        green = compound.getInt("green");
        blue = compound.getInt("blue");

    }
}
