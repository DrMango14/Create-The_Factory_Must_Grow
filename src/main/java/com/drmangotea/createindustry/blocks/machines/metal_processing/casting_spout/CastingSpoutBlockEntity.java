package com.drmangotea.createindustry.blocks.machines.metal_processing.casting_spout;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.machines.TFMGMachineBlockEntity;
import com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingBasinBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Optional;

public class CastingSpoutBlockEntity extends TFMGMachineBlockEntity {

    CastingBasinBlockEntity basin;

    public boolean isRunning=false;

    public LerpedFloat movement = LerpedFloat.linear();
    public CastingSpoutBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        tank2.forbidInsertion();
        tank2.forbidExtraction();
        tank1.forbidExtraction();


    }
    public void tick(){
        super.tick();

        if(isRunning) {
            movement.chase(0.03, 0.1f, LerpedFloat.Chaser.EXP);
        }else
            movement.chase(0, 0.1f, LerpedFloat.Chaser.EXP);
        movement.tickChaser();


        if(tank1.isEmpty())
        {
            isRunning = false;
            return;
        }

        if(level.getBlockEntity(getBlockPos().below(2)) instanceof CastingBasinBlockEntity){
            basin = (CastingBasinBlockEntity) level.getBlockEntity(getBlockPos().below(2));
        }else         {
            isRunning = false;
            return;
        }
        if(basin.moldInventory.isEmpty())
        {
            isRunning = false;
            return;
        }
        if(basin.tank1.getPrimaryHandler().getFluidAmount()==basin.tank1.getPrimaryHandler().getCapacity())
        {
            isRunning = false;
            return;
        }

        if(basin.outputInventory.getStackInSlot(0).getCount()!=0)
        {
            isRunning = false;
            return;
        }

        if(basin.tank1.getPrimaryHandler().getCapacity()==4000)
        {
            isRunning = false;
            return;
        }


        basin.tank1.getPrimaryHandler().setFluid(new FluidStack(tank1.getPrimaryHandler().getFluid().getFluid(),basin.tank1.getPrimaryHandler().getFluidAmount()+1));
        tank1.getPrimaryHandler().drain(1, IFluidHandler.FluidAction.EXECUTE);
        isRunning = true;

    }

    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {



        //--Fluid Info--//
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0)
            return false;

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");

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
                            .style(ChatFormatting.DARK_GREEN))
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
                        .style(ChatFormatting.DARK_GREEN))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);


        return true;
    }
    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(1);
    }

}
