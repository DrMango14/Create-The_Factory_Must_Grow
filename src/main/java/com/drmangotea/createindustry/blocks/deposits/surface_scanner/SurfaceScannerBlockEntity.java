package com.drmangotea.createindustry.blocks.deposits.surface_scanner;


import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SurfaceScannerBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, IWrenchable {
    public BlockPos deposit;
    public BlockPos checkedPosition;
    public boolean foundDeposit=false;
    public BlockPos depositPos;

    public int locatingTimer=0;
    public boolean noDepositFound=false;

     int dotTimer=0;
     int dotCount=1;

     public int distanceFromDeposit=6969;

     public LerpedFloat visualAngle = LerpedFloat.angular();
     public float angle = 0;


    public LerpedFloat visualFlagAngle = LerpedFloat.angular();
    public float flagAngle = 0;

    public SurfaceScannerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        angle%=360;
        if (getSpeed() == 0) {
            foundDeposit=false;
            noDepositFound=false;
            locatingTimer=0;
            return;
        }
        if(deposit==null) {
            foundDeposit = false;
            distanceFromDeposit=6969;

        }
        visualAngle.chase(angle, 0.2f, LerpedFloat.Chaser.EXP);
        visualAngle.tickChaser();

        visualFlagAngle.chase(flagAngle, 0.8f, LerpedFloat.Chaser.EXP);
        visualFlagAngle.tickChaser();


        if (!foundDeposit) {
            dotTimer++;
            locatingTimer++;
            distanceFromDeposit=6969;
            if(locatingTimer>=2400){
                locatingTimer=2400;
                noDepositFound=true;
                return;
            }


            ///
            if (dotTimer >= 90) {
                dotCount = 3;
                dotTimer = 0;

            } else if (dotTimer >= 60) {
                dotCount = 2;
            } else if (dotTimer >= 30)
                dotCount = 1;
        }


        if (foundDeposit) {

            float zDistance;
            float xDistance;


                zDistance =  deposit.getZ()-getBlockPos().getZ();



                xDistance =  deposit.getX()-getBlockPos().getX();



                int positiveXsquared = Math.abs((int)xDistance)*Math.abs((int)xDistance);
                int positiveZsquared = Math.abs((int)zDistance)*Math.abs((int)xDistance);
                distanceFromDeposit = (int)Math.sqrt(positiveXsquared+positiveZsquared);


                if(zDistance <10 && zDistance>-10&&xDistance <10 && xDistance>-10){
                    flagAngle = 90;
                  //  AllSoundEvents.CONTRAPTION_ASSEMBLE.play(level,null,getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ(),50,1);
                    return;
                }


            angle = (float) Math.toDegrees(Math.atan(xDistance/zDistance));

            if(this.getBlockPos().getZ()<deposit.getZ())
            {
                angle +=180;
            }
            if(angle == 90){
                angle = -90;
            return;
            }

            if(angle == -90)
                angle = 90;

            return;
        }
        for (int i =0;i<100;i++){
        int random = Create.RANDOM.nextInt(200);
        random -= 100;
        int random1 = Create.RANDOM.nextInt(200);
        random1 -= 100;
            int random2 = Create.RANDOM.nextInt(2);
        for(int x = 0; x<2;x++)
            checkedPosition = new BlockPos(this.getBlockPos().getX() + random, -63-random2, this.getBlockPos().getZ() + random1);
        BlockState checkedState = level.getBlockState(checkedPosition);
        if (checkedState.is(TFMGBlocks.OIL_DEPOSIT.get())) {
            deposit = checkedPosition;
            if(level.getBlockState(checkedPosition.above()) != Blocks.BEDROCK.defaultBlockState()&&
                    level.getBlockState(checkedPosition.above(2)) != Blocks.BEDROCK.defaultBlockState()
                    && level.getBlockState(checkedPosition.above(3)) != Blocks.BEDROCK.defaultBlockState()
                    && level.getBlockState(checkedPosition.above(4)) != Blocks.BEDROCK.defaultBlockState())
            foundDeposit = true;
            depositPos = deposit;
        }
    }


    }
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        flagAngle=0;
        angle=0;
        foundDeposit=false;
        deposit=null;

        return InteractionResult.SUCCESS;
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
if(foundDeposit&&depositPos!=null)
        compound.put("DepositPos", NbtUtils.writeBlockPos(depositPos));


        compound.putBoolean("FoundDeposit", foundDeposit);


        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound,clientPacket);
        if(foundDeposit)
            depositPos = NbtUtils.readBlockPos(compound.getCompound("DepositPos"));
        foundDeposit = compound.getBoolean("FoundDeposit");

    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(getSpeed()==0){
            Lang.translate("goggles.surface_scanner.no_rotation")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip);
            return true;
        }
        if(noDepositFound){
            Lang.translate("goggles.surface_scanner.no_deposit")
                    .style(ChatFormatting.RED)
                    .space()
                    .forGoggles(tooltip);
            return true;
        }



        if(foundDeposit){

              Lang.translate("goggles.surface_scanner.deposit_found")
                   .style(ChatFormatting.DARK_GREEN)
                   .space()
                   .forGoggles(tooltip);



            Lang.translate("goggles.surface_scanner.distance", this.distanceFromDeposit)
                    .style(ChatFormatting.GREEN)
                    .forGoggles(tooltip,1);

//debug
/*
                Lang.translate("goggles.surface_scanner.coordinates")
                        .style(ChatFormatting.DARK_GREEN)
                        .space()
                        .forGoggles(tooltip);


            Lang.translate("goggles.misc.number", this.deposit.getBlockPos().getX())
                    .style(ChatFormatting.GREEN)
                    .forGoggles(tooltip,1);

            Lang.translate("goggles.misc.number", this.deposit.getBlockPos().getZ())
                    .style(ChatFormatting.GREEN)
                    .forGoggles(tooltip,1);


            Lang.translate("goggles.misc.number", angle)
                    .style(ChatFormatting.DARK_PURPLE)
                    .forGoggles(tooltip,1);

 */

        }else {

        if(dotCount==1)
            Lang.translate("goggles.surface_scanner.scanning_surface")
                    .style(ChatFormatting.GOLD)
                    .add(Lang.translate("goggles.misc.dot_one"))
                    .forGoggles(tooltip);

            if(dotCount==2)
                Lang.translate("goggles.surface_scanner.scanning_surface")
                        .style(ChatFormatting.GOLD)
                        .add(Lang.translate("goggles.misc.dot_two"))
                        .forGoggles(tooltip);

            if(dotCount==3)
                Lang.translate("goggles.surface_scanner.scanning_surface")
                        .style(ChatFormatting.GOLD)
                        .add(Lang.translate("goggles.misc.dot_three"))
                        .forGoggles(tooltip);

        }


        return true;



    }


}



