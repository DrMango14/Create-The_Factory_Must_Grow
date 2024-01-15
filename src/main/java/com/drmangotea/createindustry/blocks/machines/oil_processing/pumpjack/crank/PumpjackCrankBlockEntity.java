package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.crank;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.machine_input.MachineInputBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.ServerSpeedProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class PumpjackCrankBlockEntity extends KineticBlockEntity {


    public float angle=0;

    public Direction direction;
    public float heightModifier=0;

    protected float clientAngleDiff;


    public  float crankRadius = 0.7f;





    public PumpjackCrankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick(){
        super.tick();
        direction = this.getBlockState().getValue(FACING);
        setAngle();



        heightModifier = (float) (crankRadius * Math.sin(Math.toRadians(angle)));


    }



    public float getMachineInputSpeed(){

        if(level.getBlockEntity(getBlockPos().below()) instanceof MachineInputBlockEntity)
            return ((MachineInputBlockEntity)level.getBlockEntity(getBlockPos().below())).getSpeed();
        return 0;
    }


    private void setAngle() {

        if(level.getBlockEntity(getBlockPos().below()) instanceof MachineInputBlockEntity) {
            float time;
            if(level.isClientSide) {
                time = AnimationTickHolder.getRenderTime(getLevel());
            }else time = level.getBlockTicks().hashCode();
            float speed_amogus = Math.min(getMachineInputSpeed() /6 , (float) 10);



            if(speed_amogus!=0) {
                angle = (time * speed_amogus * 3 / 10f) % 360;
                angle = angle / 180f * (float) Math.PI;
                angle = (float) Math.toDegrees(angle);
            }
            else angle = 180;
        }






    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

  //  @Override
  //  public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//
  //      Lang.translate("goggles.coke_oven.progress", angle)
  //              .add(Lang.translate("goggles.misc.percent_symbol"))
  //              .style(ChatFormatting.DARK_PURPLE)
  //              .forGoggles(tooltip,1);
  //  return true;
  //  }
}
