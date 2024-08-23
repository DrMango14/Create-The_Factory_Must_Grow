package com.drmangotea.createindustry.blocks.pipes.normal.aluminum;


import com.drmangotea.createindustry.base.TFMGPipes;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.EncasedPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.GlassFluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Map;

public class GlassAluminumPipeBlock extends GlassFluidPipeBlock {
    public GlassAluminumPipeBlock(Properties p_i48339_1_) {
        super(p_i48339_1_);
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity te) {
        return ItemRequirement.of(TFMGPipes.ALUMINUM_PIPE.getDefaultState(), te);
    }
    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos,
                                       Player player) {
        return TFMGPipes.ALUMINUM_PIPE.asStack();
    }
    @Override
    public BlockState toRegularPipe(LevelAccessor world, BlockPos pos, BlockState state) {
        Direction side = Direction.get(Direction.AxisDirection.POSITIVE, state.getValue(AXIS));
        Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.PROPERTY_BY_DIRECTION;
        return TFMGPipes.ALUMINUM_PIPE.get()
                .updateBlockState(TFMGPipes.ALUMINUM_PIPE.getDefaultState()
                        .setValue(facingToPropertyMap.get(side), true)
                        .setValue(facingToPropertyMap.get(side.getOpposite()), true), side, null, world, pos);
    }
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (!AllBlocks.COPPER_CASING.isIn(player.getItemInHand(hand)))
            return InteractionResult.PASS;
        if (world.isClientSide)
            return InteractionResult.SUCCESS;
        BlockState newState = TFMGPipes.COPPER_ENCASED_ALUMINUM_PIPE.getDefaultState();
        for (Direction d : Iterate.directionsInAxis(getAxis(state)))
            newState = newState.setValue(EncasedPipeBlock.FACING_TO_PROPERTY_MAP.get(d), true);
        FluidTransportBehaviour.cacheFlows(world, pos);
        world.setBlockAndUpdate(pos, newState);
        FluidTransportBehaviour.loadFlows(world, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public Class<StraightPipeBlockEntity> getBlockEntityClass() {
        return StraightPipeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StraightPipeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.GLASS_TFMG_PIPE.get();
    }
}
