package com.drmangotea.createindustry.blocks.electricity.transformer;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;

public class CoilBlockEntity extends ElectricBlockEntity {


    public boolean controller = false;
    public int height=1;

    public float generation = 0;





    ArrayList<CoilBlockEntity> coils = new ArrayList<>();


    public CoilBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }

    @Override
    public float maxVoltage() {
        return 15000;
    }



    @Override
    public void tick() {
        super.tick();


        if(!(level.getBlockState(getBlockPos().above()).getBlock() instanceof CoilBlock)) {
            controller = true;

        }else controller = false;

/////

       // if(controller)
       //     CreateClient.OUTLINER.showAABB(skibidi, reachZone())
       //             .colored(500)
       //             .withFaceTextures(AllSpecialTextures.GLUE, AllSpecialTextures.GLUE)
       //             .disableLineNormals()
       //             .lineWidth(1 / 16f);
//

          //  for(BlockPos pos : coils) {

        if(getBlockState().getValue(CoilBlock.CAN_EXTRACT)&&!controller)
            level.setBlock(getBlockPos(),getBlockState().setValue(CoilBlock.CAN_EXTRACT,false),2);


        if(getBlockState().getValue(CoilBlock.CAN_EXTRACT))
            return;

        if(controller)
            for(CoilBlockEntity coil : coils){

            if(coil == null)
                continue;





            if (level.getBlockEntity(coil.getBlockPos()) instanceof CoilBlockEntity be){
                if(be.getBlockPos()!=this.getBlockPos()) {

                    float heightRatio = (float) be.height / height;

                    if(!be.getBlockState().getValue(CoilBlock.CAN_EXTRACT))
                        continue;

                    be.generation = voltage * heightRatio;

                    if(energy.getEnergyStored()!=0) {
                        int amount = getForgeEnergy().extractEnergy(transferSpeed()*10, true);
                        int amount2 = be.getForgeEnergy().receiveEnergy(transferSpeed()*10, true);


                        getForgeEnergy().extractEnergy(Math.min(amount, amount2), false);
                        be.getForgeEnergy().receiveEnergy(Math.min(amount, amount2), false);
                    }

                }
            }
        }

    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        BlockPos pos = this.getBlockPos().below();
        int height = 1;
        for(int i = 0; i<4;i++){

            if(level.getBlockState(pos).getBlock() instanceof CoilBlock) {
                height++;
            }else break;

            pos = pos.below();


        }
        this.height = height;
        ///
        coils = new ArrayList<>();
        if(controller)
            for (BlockPos checkedPos : BlockPos.betweenClosed(new BlockPos(reachZone().minX,reachZone().minY,reachZone().minZ), new BlockPos(reachZone().maxX,reachZone().maxY,reachZone().maxZ))) {

              //  BlockPos.betweenClosedStream(new BlockPos(reachZone().minX,reachZone().minY,reachZone().minZ), new BlockPos(reachZone().maxX,reachZone().maxY,reachZone().maxZ)
                   if(level.getBlockEntity(checkedPos) instanceof CoilBlockEntity be&&checkedPos!=getBlockPos()){
                       if(be.controller)
                           coils.add((CoilBlockEntity) level.getBlockEntity(checkedPos));

                    }
//


            }

    }

    public AABB reachZone(){

        AABB reach = new AABB(getBlockPos()).inflate((double) height /2);


        return reach;
    }


    @Override
    public int voltageGeneration() {
        return (int) generation;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == Direction.UP;
    }
}
