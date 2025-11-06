package com.drmangotea.tfmg.content.engines.upgrades;


import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Optional;

public class EnginePipingUpgrade extends EngineUpgrade {

    public Optional<FluidTankBlockEntity> tank = Optional.empty();


    public void findTank(AbstractSmallEngineBlockEntity be) {
        Level level = be.getLevel();

        for (Direction direction : Direction.values()) {
            BlockPos pos = be.getBlockPos().relative(direction);
            if (level.getBlockEntity(pos) instanceof FluidTankBlockEntity foundTank) {

                tank = Optional.of(foundTank);
                return;
            }
        }
        tank = Optional.empty();
    }

    @Override
    public void updateUpgrade(AbstractSmallEngineBlockEntity be) {
        findTank(be);
    }

    @Override
    public void lazyTickUpgrade(AbstractSmallEngineBlockEntity engine) {

        if (tank.isPresent()) {

            AbstractSmallEngineBlockEntity controller = engine.getControllerBE();

            FluidTankBlockEntity tankBE = tank.get();
            int maxOutput = tankBE.getTankInventory().drain(500, IFluidHandler.FluidAction.SIMULATE).getAmount();
            int maxInput = tankBE.getTankInventory().fill(new FluidStack(tankBE.getFluid(0), 500), IFluidHandler.FluidAction.SIMULATE);
            if(controller == null)
                return;
            if(controller.fuelTank == null)
                return;

            int amount = Math.min(maxInput, Math.min(maxOutput, controller.fuelTank.getSpace()));

            tankBE.getTankInventory().drain(amount, IFluidHandler.FluidAction.EXECUTE);
            controller.getControllerBE().fuelTank.fill(new FluidStack(tankBE.getFluid(0), amount), IFluidHandler.FluidAction.EXECUTE);

        }

    }

    @Override
    public Optional<? extends EngineUpgrade> createUpgrade() {
        return Optional.of(new EnginePipingUpgrade());
    }

    @Override
    public Item getItem() {
        return TFMGBlocks.INDUSTRIAL_PIPE.asItem();
    }
}
