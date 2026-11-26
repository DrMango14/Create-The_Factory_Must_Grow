package com.drmangotea.tfmg.base.fluid;

import com.drmangotea.tfmg.base.MaterialSet;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ConcreteFluid extends BaseFlowingFluid {
    private static final String FLOWING_PREFIX = "flowing_";
    private static final String COLOR_SUFFIX = "_liquid_concrete";

    private Block solidifiedBlock;
    protected @Nullable DyeColor dyeColor;

    protected ConcreteFluid(Properties properties) {
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
        if(!(level.getBlockState(pos).getBlock() instanceof LiquidBlock))
            return;
        int random = randomSource.nextInt(7);
        if(random == 2) {
            level.setBlock(pos, getSolidifiedBlock().defaultBlockState(), 3);
        }
    }

    protected Block getSolidifiedBlock() {
        if (solidifiedBlock == null) {
            solidifiedBlock = resolveSolidifiedBlock();
        }
        return solidifiedBlock;
    }

    private Block resolveSolidifiedBlock() {
        if (dyeColor == null) {
            return TFMGBlocks.CONCRETE.block.get();
        }
        MaterialSet coloredConcrete = TFMGBlocks.COLORED_CONCRETE.get(dyeColor);
        return coloredConcrete != null ? coloredConcrete.block.get() : TFMGBlocks.CONCRETE.block.get();
    }

    protected boolean isRandomlyTicking() {
        return true;
    }

    public static class Flowing extends ConcreteFluid {
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

    public static class Source extends ConcreteFluid {
        public Source(Properties properties) {
            super(properties);
        }

        public Source withColour(DyeColor dyeColor) {
            this.dyeColor = dyeColor;
            return this;
        }
    }
}
