package com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillation_tower;

import com.drmangotea.tfmg.blocks.machines.oil_processing.distillation.distillery.DistilleryControllerBlockEntity;
import com.drmangotea.tfmg.blocks.tanks.SteelTankBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;

public class DistillationControllerBlockEntity extends DistilleryControllerBlockEntity implements IHaveGoggleInformation {

    public WeakReference<SteelTankBlockEntity> source;

    public int towerLevel =0;
    public boolean hasTank = false;
    public boolean isTallEnough = true;
    public boolean hasMainOutput = false;
    public DistillationOutputBlockEntity mainOutput;

    public DistillationControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        source = new WeakReference<>(null);
        super.tick();



        BlockEntity entityAbove = level.getBlockEntity(getBlockPos().above());

        if(entityAbove instanceof DistillationOutputBlockEntity){
            mainOutput = (DistillationOutputBlockEntity) entityAbove;

            hasMainOutput = true;
        }else {
            hasMainOutput = false;
        }


        @Nullable
        SteelTankBlockEntity tank = getTank();

        if(tank==null){
            hasTank=false;
            towerLevel = 0;
            return;
        }
        hasTank = true;


        towerLevel = tank.tower.towerLevel;



    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        LangBuilder mb = Lang.translate("generic.unit.millibuckets");


        Lang.translate("goggles.distillation_tower.status")
                .style(ChatFormatting.GRAY)
                .space()
                .forGoggles(tooltip, 1);


        if(!hasTank) {
            Lang.translate("goggles.distillation_tower.tank_not_found")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip);
            return true;

        }else {

            if(towerLevel<4) {
                Lang.translate("goggles.distillation_tower.level", this.towerLevel)
                        .style(ChatFormatting.DARK_RED)
                        .forGoggles(tooltip, 1);
                return true;
            }

            if(!hasMainOutput) {
                Lang.translate("goggles.distillation_tower.no_outputs")
                        .style(ChatFormatting.DARK_RED)
                        .forGoggles(tooltip, 1);
                return true;
            }

         //   if(getTank().getHeight()<((DistillationOutputBlockEntity)level.getBlockEntity(getBlockPos().above())).foundOutputs*2) {
         //       Lang.translate("goggles.distillation_tower.not_tall_enough")
         //               .style(ChatFormatting.DARK_RED)
         //               .forGoggles(tooltip, 1);
         //       return true;
         //   }

            Lang.translate("goggles.distillation_tower.level", this.towerLevel)
                    .style(ChatFormatting.GREEN)
                    .forGoggles(tooltip, 1);

            Lang.translate("goggles.distillation_tower.found_outputs", this.getOutputCount())
                    .style(ChatFormatting.GREEN)
                    .space()
                    .forGoggles(tooltip, 1);


        }


        ///////////////////////////////////////////
        LazyOptional<IFluidHandler> handler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0)
            return false;

        Lang.translate("goggles.fluid_in_tank")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);


        boolean isEmpty = true;
        for (int i = 0; i < tank.getTanks(); i++) {
            FluidStack fluidStack = tank.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;

            Lang.fluidName(fluidStack)
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

            Lang.builder()
                    .add(Lang.number(fluidStack.getAmount())
                            .add(mb)
                            .style(ChatFormatting.GOLD))
                    .text(ChatFormatting.GRAY, " / ")
                    .add(Lang.number(tank.getTankCapacity(i))
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

            isEmpty = false;
        }

        if (tank.getTanks() > 1) {
            if (isEmpty)
                tooltip.remove(tooltip.size() - 1);
            return true;
        }

        if (!isEmpty)
            return true;

        Lang.translate("gui.goggles.fluid_container.capacity")
                .add(Lang.number(tank.getTankCapacity(0))
                        .add(mb)
                        .style(ChatFormatting.GOLD))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);


        return true;
    }


    public int getOutputCount(){
        BlockPos checkedPos = this.getBlockPos().above(3);
        int outputCount = 1;

        for(int i = 0; i <5;i++){
            if(
                    level.getBlockState(checkedPos).is(TFMGBlocks.STEEL_DISTILLATION_OUTPUT.get())&&
                    level.getBlockState(checkedPos.below()).is(TFMGBlocks.INDUSTRIAL_PIPE.get())
            ){
                outputCount++;
                checkedPos = checkedPos.above(2);
                continue;
            }
            return outputCount;

        }


        return outputCount;
    }




    public SteelTankBlockEntity getTank() {
        SteelTankBlockEntity tank = source.get();
        if (tank == null || tank.isRemoved()) {
            if (tank != null)
                source = new WeakReference<>(null);
            Direction facing = DistillationControllerBlock.getFacing(getBlockState());
            BlockEntity be = level.getBlockEntity(worldPosition.relative(facing.getOpposite()));
            if (be instanceof SteelTankBlockEntity tankTe)
                source = new WeakReference<>(tank = tankTe);
        }
        if (tank == null)
            return null;
        return (SteelTankBlockEntity) tank.getControllerBE();
    }


}
