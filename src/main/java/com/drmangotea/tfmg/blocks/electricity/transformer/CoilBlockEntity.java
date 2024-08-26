package com.drmangotea.tfmg.blocks.electricity.transformer;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.VoltageAlteringBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.cables.ElectricalNetwork;
import com.drmangotea.tfmg.blocks.electricity.base.cables.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;

import static com.drmangotea.tfmg.blocks.electricity.transformer.CoilBlock.CAN_EXTRACT;


public class CoilBlockEntity extends VoltageAlteringBlockEntity {


    public boolean controller = false;
    public int height=1;

    public int generation = 0;




    ArrayList<CoilBlockEntity> coils = new ArrayList<>();


    public CoilBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }

    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.COIL.get());
    }





    @Override
    public void onPlaced() {



        if(level.getBlockEntity(getBlockPos().above()) instanceof IElectric be){
            if(be.hasElectricitySlot(Direction.DOWN)){
                be.needsVoltageUpdate();

            }
        }
        super.onPlaced();

    }







    @Override
    public void destroy() {

        super.destroy();

        for(CoilBlockEntity coil : coils){
            if(coil == null)
                return;

            coil.generation = 0;
            coil.needsVoltageUpdate();


        }

        if(level.getBlockEntity(getBlockPos().above()) instanceof IElectric be){
            if(be.hasElectricitySlot(Direction.DOWN)){
                be.getOrCreateElectricNetwork().updateNetworkVoltage();
                be.needsVoltageUpdate();


            }


        }
        BlockPos pos = getBlockPos().above();
        for(int i = 0; i < height;i++){

            if(level.getBlockEntity(pos) instanceof CoilBlockEntity be){
                if(be.controller)
                    be.needsVoltageUpdate();
            }

            pos = pos.above();
        }





    }



    @Override
    public int FECapacity() {
        return 10000;
    }

    @Override
    public void tick() {
        super.tick();
        if(!getBlockState().getValue(CAN_EXTRACT)) {
            getOrCreateElectricNetwork().requestEnergy(this);
        }else coils.forEach(be ->   ElectricalNetwork.sendEnergy(be,this));
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if(!(level.getBlockState(getBlockPos().above()).getBlock() instanceof CoilBlock)) {
            controller = true;

        }else controller = false;


        BlockPos pos = this.getBlockPos().below();
        int height = 1;
        for(int i = 0; i<4;i++){

            if(level.getBlockState(pos).getBlock() instanceof CoilBlock) {
                height++;
            }else break;

            pos = pos.below();


        }
        this.height = height;


        if(getBlockState().getValue(CAN_EXTRACT)&&!controller)
            level.setBlock(getBlockPos(),getBlockState().setValue(CAN_EXTRACT,false),2);

        if(!getBlockState().getValue(CAN_EXTRACT)) {
            return;
        }


        if(getVoltage() !=voltageGeneration()) {
            needsVoltageUpdate();
            needsNetworkUpdate();
        }



        ///
        coils = new ArrayList<>();
        if(controller)
            for (BlockPos checkedPos : BlockPos.betweenClosed(new BlockPos((int) reachZone().minX, (int) reachZone().minY, (int) reachZone().minZ), new BlockPos((int) reachZone().maxX, (int) reachZone().maxY, (int) reachZone().maxZ))) {

                if(level.getBlockEntity(checkedPos) instanceof CoilBlockEntity be&&checkedPos!=getBlockPos()){
                       if(be.controller&&!be.getBlockState().getValue(CAN_EXTRACT)){



                           coils.add((CoilBlockEntity) level.getBlockEntity(checkedPos));


                       }



                    }
//


            }

    }

    @Override
    public int voltageGeneration() {


        if(!getBlockState().getValue(CAN_EXTRACT)) {
            return super.voltageGeneration();
        }

        int maxCoilVoltage =0;

        for(CoilBlockEntity coil : coils){

            float heightRatio = (float) height / coil.height;



            maxCoilVoltage = (int) Math.max(maxCoilVoltage,heightRatio*coil.getVoltage());


        }



        return Math.max(super.voltageGeneration(),maxCoilVoltage);
    }

    @Override
    public int getOutputVoltage() {
        return generation;
    }

    public AABB reachZone(){

        AABB reach = new AABB(getBlockPos()).inflate((double) height /2);


        return reach;
    }


    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == Direction.UP;
    }
}
