package com.drmangotea.tfmg.blocks.concrete;

import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class ConcreteFluid extends ForgeFlowingFluid {


    protected ConcreteFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState p_76140_) {
        return true;
    }

    @Override
    public int getAmount(FluidState p_164509_) {
        return 8;
    }

    @Override
    public void randomTick(Level level, BlockPos pos, FluidState p_230574_, RandomSource randomSource) {
        int random = randomSource.nextInt(7) ;

        if(random==2) {
            level.setBlock(pos, TFMGBlocks.CONCRETE_TEST.get().defaultBlockState(), 3);
        }
    }

    protected boolean isRandomlyTicking() {
        return true;
    }


    //
    public static class Flowing extends ConcreteFluid {
        public Flowing(Properties properties) {
            super(properties);
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> p_76260_) {
            super.createFluidStateDefinition(p_76260_);
            p_76260_.add(LEVEL);
        }

        public int getAmount(FluidState p_76264_) {
            return p_76264_.getValue(LEVEL);
        }

        public boolean isSource(FluidState p_76262_) {
            return false;
        }
    }

    public static class Source extends ConcreteFluid {
        public Source(Properties properties) {
            super(properties);
        }

        public int getAmount(FluidState p_76269_) {
            return 8;
        }

        public boolean isSource(FluidState p_76267_) {
            return true;
        }
    }
}
