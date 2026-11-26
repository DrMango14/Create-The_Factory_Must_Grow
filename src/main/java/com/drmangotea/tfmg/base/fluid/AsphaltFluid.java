package com.drmangotea.tfmg.base.fluid;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AsphaltFluid extends BaseFlowingFluid {


    protected AsphaltFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState state) {
        return true;
    }

    @Override
    public int getAmount(FluidState state) {
        return 8;
    }

    @Override
    public void randomTick(Level level, BlockPos pos, FluidState state, RandomSource randomSource) {
        int random = randomSource.nextInt(7) ;
        if(random == 2) {
            level.setBlock(pos, TFMGBlocks.ASPHALT.get().defaultBlockState(), 3);
        }
    }

    protected boolean isRandomlyTicking() {
        return true;
    }


    //
    public static class Flowing extends AsphaltFluid {
        public Flowing(Properties properties) {
            super(properties);
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> stateBuilder) {
            super.createFluidStateDefinition(stateBuilder);
            stateBuilder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends AsphaltFluid {
        public Source(Properties properties) {
            super(properties);
        }
    }
}
