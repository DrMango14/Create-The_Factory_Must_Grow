package com.drmangotea.tfmg.content.machinery.misc.flarestack;


import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class FlarestackBlock extends Block implements IBE<FlarestackBlockEntity>, IWrenchable {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public FlarestackBlock(Properties p_55926_) {
        super(p_55926_);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, false));

    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.FLARESTACK;
    }
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_51240_) {

        return this.defaultBlockState().setValue(LIT, Boolean.valueOf(false));
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_51305_) {
        p_51305_.add(LIT);
    }

    @Override
    public Class<FlarestackBlockEntity> getBlockEntityClass() {
        return FlarestackBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FlarestackBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.FLARESTACK.get();
    }
}
