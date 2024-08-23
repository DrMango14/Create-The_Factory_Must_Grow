package com.drmangotea.createindustry.blocks.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

/**
 * makes fluid burn when close to fire,
 * not working for now
 */
public class FlammableFluid extends ForgeFlowingFluid {


    protected FlammableFluid(Properties properties) {
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
        //level.setBlock(pos,Blocks.FIRE.defaultBlockState(),3);
     //  if (!level.isClientSide) {

     //      Direction checkedDirection=Direction.NORTH;
     //      for(int i = 0; i < 4; i++) {

     //          checkedDirection=checkedDirection.getClockWise();

     //          BlockPos checkedPos = pos.relative(checkedDirection);

     //          if(level.getBlockEntity(checkedPos).getBlockState().is(Blocks.FIRE)) {
     //              level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
     //              level.setBlock(pos,Blocks.FIRE.defaultBlockState(),3);

     //          }
     //          }
     //  }
    }

    protected boolean isRandomlyTicking() {
        return true;
    }


    //
    public static class Flowing extends FlammableFluid {
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

    public static class Source extends FlammableFluid {
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
