package com.drmangotea.tfmg.blocks.deposits.surface_scanner;


import com.drmangotea.tfmg.registry.TFMGBlocks;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.drmangotea.tfmg.base.TFMGTools.getDistance;


public class SurfaceScannerBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation, IWrenchable {
    public List<BlockPos> deposits = new ArrayList<>();

    public boolean foundDeposit=false;

    public Optional<BlockPos> closestDeposit = Optional.empty();


    public int locatingTimer = -1;
    public boolean noDepositFound=false;

    int dotTimer=0;
    int dotCount=1;



    public LerpedFloat visualAngle = LerpedFloat.angular();
    public float angle = 0;


    public LerpedFloat visualFlagAngle = LerpedFloat.angular();
    public float flagAngle = 0;

    public SurfaceScannerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }


    public void manageDots(){
        dotTimer++;
        if (dotTimer >= 90) {
            dotCount = 3;
            dotTimer = 0;

        } else if (dotTimer >= 60) {
            dotCount = 2;
        } else if (dotTimer >= 30)
            dotCount = 1;
    }

    public void setVisualAngles(){
        visualAngle.chase(angle, 0.2f, LerpedFloat.Chaser.EXP);
        visualAngle.tickChaser();

        visualFlagAngle.chase(flagAngle, 0.8f, LerpedFloat.Chaser.EXP);
        visualFlagAngle.tickChaser();
    }




    @Override
    public void tick() {
        super.tick();




        foundDeposit = !deposits.isEmpty();

        angle%=360;


        if (getSpeed() == 0) {
            locatingTimer = -1;
            foundDeposit=false;
            noDepositFound=false;
            closestDeposit = Optional.empty();
            return;
        }else {
            if(locatingTimer==-1&& deposits.isEmpty()) {
                locatingTimer = 20 * 30;
                closestDeposit = Optional.empty();

            }
        }



        int range = 50;



        if(locatingTimer>=0){
            //BlockPos checkedPos=getBlockPos().north(50).west(50);
            for(int i = 0;i<10;i++) {
                BlockPos checkedPos = new BlockPos(getBlockPos().getX()+level.random.nextInt(range * 2) - 50, -64, getBlockPos().getZ()+level.random.nextInt(range * 2) - 50);

                if (level.getBlockState(checkedPos).is(TFMGBlocks.OIL_DEPOSIT.get())) {
                    deposits.add(checkedPos);
                }

            }


            if(locatingTimer > 0)
                locatingTimer--;
            if(deposits.isEmpty()){
                closestDeposit = Optional.empty();
            }

        }



        setVisualAngles();
        manageDots();


    if(locatingTimer==0)
        if (foundDeposit) {

            float zDistance;
            float xDistance;


            closestDeposit= Optional.of(deposits.get(0));

            for(BlockPos deposit : deposits){
                float distanceClosest = getDistance(getBlockPos(),closestDeposit.get(),true);
                float distanceCurrent = getDistance(getBlockPos(),deposit,true);

                if(distanceCurrent<distanceClosest)
                    closestDeposit = Optional.of(deposit);

            }



            zDistance =  closestDeposit.get().getZ()-getBlockPos().getZ();



            xDistance =  closestDeposit.get().getX()-getBlockPos().getX();





            if(zDistance <10 && zDistance>-10&&xDistance <10 && xDistance>-10){
                flagAngle = 90;
                //  AllSoundEvents.CONTRAPTION_ASSEMBLE.play(level,null,getBlockPos().getX(),getBlockPos().getY(),getBlockPos().getZ(),50,1);
                return;
            }


            angle = (float) Math.toDegrees(Math.atan(xDistance/zDistance));

            if(this.getBlockPos().getZ()<closestDeposit.get().getZ())
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



    }
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        flagAngle=0;
        angle=0;
        foundDeposit=false;
        deposits = new ArrayList<>();
        locatingTimer = -1;

        return InteractionResult.SUCCESS;
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        //if(foundDeposit&& deposits.isEmpty())
        //    compound.put("DepositPos", NbtUtils.writeBlockPos(deposits.get(0)));
//
//
        //compound.putBoolean("FoundDeposit", foundDeposit);


        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound,clientPacket);
        //if(foundDeposit)
        //    deposits = NbtUtils.readBlockPos(compound.getCompound("DepositPos"));
        //foundDeposit = compound.getBoolean("FoundDeposit");

    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
       // Lang.translate("goggles.motor.usage",locatingTimer)
       //         .style(ChatFormatting.RED)
       //         .space()
       //         .forGoggles(tooltip);
        if(getSpeed()==0){
            Lang.translate("goggles.surface_scanner.no_rotation")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip);
            return true;
        }
        if(getSpeed()>0&&!foundDeposit&&locatingTimer==0){
            Lang.translate("goggles.surface_scanner.no_deposit")
                    .style(ChatFormatting.RED)
                    .space()
                    .forGoggles(tooltip);
            return true;
        }




        if(closestDeposit.isPresent()){
        if(foundDeposit) {

            Lang.translate("goggles.surface_scanner.deposit_found")
                    .style(ChatFormatting.DARK_GREEN)
                    .space()
                    .forGoggles(tooltip);


            Lang.translate("goggles.surface_scanner.distance", getDistance(getBlockPos(), closestDeposit.get(), true))
                    .style(ChatFormatting.GREEN)
                    .forGoggles(tooltip, 1);
        }

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

        }
        if(locatingTimer>0){

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




