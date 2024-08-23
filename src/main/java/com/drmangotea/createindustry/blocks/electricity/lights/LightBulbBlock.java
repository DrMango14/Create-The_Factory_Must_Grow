package com.drmangotea.createindustry.blocks.electricity.lights;

import com.drmangotea.createindustry.blocks.electricity.base.WallMountBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightBulbBlock extends WallMountBlock implements IBE<LightBulbBlockEntity>, SimpleWaterloggedBlock, IWrenchable {

    public static final IntegerProperty LIGHT = BlockStateProperties.LEVEL;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;


    public LightBulbBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIGHT, 0).setValue(WATERLOGGED, false));
    }



    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.LIGHT_BULB.get(pState.getValue(FACING));
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153687_) {
        p_153687_.add(LIGHT, WATERLOGGED,FACING);
    }
    public BlockState updateShape(BlockState p_153680_, Direction p_153681_, BlockState p_153682_, LevelAccessor p_153683_, BlockPos p_153684_, BlockPos p_153685_) {
        if (p_153680_.getValue(WATERLOGGED)) {
            p_153683_.scheduleTick(p_153684_, Fluids.WATER, Fluids.WATER.getTickDelay(p_153683_));
        }

        return super.updateShape(p_153680_, p_153681_, p_153682_, p_153683_, p_153684_, p_153685_);
    }

    public FluidState getFluidState(BlockState p_153699_) {
        return p_153699_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153699_);
    }




    @Override
    public Class<LightBulbBlockEntity> getBlockEntityClass() {
        return LightBulbBlockEntity.class;
    }


    @Override
    public BlockEntityType<? extends LightBulbBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.LIGHT_BULB.get();
    }
}
