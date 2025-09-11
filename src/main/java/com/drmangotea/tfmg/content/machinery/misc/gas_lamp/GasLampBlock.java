package com.drmangotea.tfmg.content.machinery.misc.gas_lamp;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GasLampBlock extends Block implements IBE<GasLampBlockEntity> {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public GasLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.GAS_LAMP;
    }
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        return this.defaultBlockState().setValue(LIT, Boolean.FALSE);
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51305_) {
        p_51305_.add(LIT);
    }

    @Override
    public Class<GasLampBlockEntity> getBlockEntityClass() {
        return GasLampBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GasLampBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.GAS_LAMP.get();
    }
}
