package com.drmangotea.tfmg.blocks.engines.diesel.engine_expansion;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class DieselEngineExpansionBlock extends DirectionalBlock implements IBE<DieselEngineExpansionBlockEntity>, IWrenchable {



    public DieselEngineExpansionBlock(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.DOWN));
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_55125_) {
        p_55125_.add(FACING);
    }


    @Override
    public Class<DieselEngineExpansionBlockEntity> getBlockEntityClass() {
        return DieselEngineExpansionBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DieselEngineExpansionBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.DIESEL_ENGINE_EXPANSION.get();
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_55087_) {
        return this.defaultBlockState().setValue(FACING, p_55087_.getNearestLookingDirection().getOpposite().getOpposite());
    }
}
