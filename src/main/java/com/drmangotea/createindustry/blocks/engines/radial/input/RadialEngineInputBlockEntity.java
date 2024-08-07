package com.drmangotea.createindustry.blocks.engines.radial.input;


import com.drmangotea.createindustry.blocks.engines.radial.RadialEngineBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;

import static net.minecraft.world.level.block.DirectionalBlock.FACING;
//  :3
public class RadialEngineInputBlockEntity extends SmartBlockEntity {


    int timer = 10;
    boolean signalChanged;

    public int signal=0;

    RadialEngineBlockEntity engine;

    public RadialEngineInputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setEngine(RadialEngineBlockEntity engine) {
        this.engine = engine;
    }

    public void tick(){
        super.tick();

        if(timer>0){
            timer--;
        }



        if(engine!=null) {
            if (!(level.getBlockEntity(engine.getBlockPos()) instanceof RadialEngineBlockEntity)) {
                engine = null;
            }


            if(engine!=null) {
                    engine.setInputSingal(signal);

            }
        }

        if(engine == null) {
            if(timer ==0)
                level.setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), 3);


        }


        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged(level.getBestNeighborSignal(worldPosition));
        }

    }
    protected void analogSignalChanged(int newSignal) {

        signal = newSignal;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("Signal", signal);

        if(engine !=null) {
            compound.putInt("X", engine.getBlockPos().getX());
            compound.putInt("Y", engine.getBlockPos().getY());
            compound.putInt("Z", engine.getBlockPos().getZ());
        }



        super.write(compound, clientPacket);
    }


    public void neighbourChanged() {
        if (!hasLevel())
            return;
        int power = level.getBestNeighborSignal(worldPosition);
        if (power != signal)
            signalChanged = true;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {

        if(engine == null)
            engine = (RadialEngineBlockEntity) level.getBlockEntity(new BlockPos(
                    compound.getInt("X"),
                    compound.getInt("Y"),
                    compound.getInt("Z")
            ));



        signal = compound.getInt("Signal");


        super.read(compound, clientPacket);
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
