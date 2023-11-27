package com.drmangotea.tfmg.blocks.machines.exhaust;


import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RodBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Random;

public class ExhaustBlock extends RodBlock implements IBE<ExhaustBlockEntity>, IWrenchable {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    //Direction facing = this.defaultBlockState().getValue(FACING);
    public ExhaustBlock(Properties p_55926_) {
        super(p_55926_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }


    public BlockState getStateForPlacement(BlockPlaceContext p_153711_) {
        FluidState fluidstate = p_153711_.getLevel().getFluidState(p_153711_.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(FACING, p_153711_.getClickedFace()).setValue(WATERLOGGED, Boolean.valueOf(flag));
    }

    public BlockState updateShape(BlockState p_153739_, Direction p_153740_, BlockState p_153741_, LevelAccessor p_153742_, BlockPos p_153743_, BlockPos p_153744_) {
        if (p_153739_.getValue(WATERLOGGED)) {
            p_153742_.scheduleTick(p_153743_, Fluids.WATER, Fluids.WATER.getTickDelay(p_153742_));
        }

        return super.updateShape(p_153739_, p_153740_, p_153741_, p_153742_, p_153743_, p_153744_);
    }
    public FluidState getFluidState(BlockState p_153759_) {
        return p_153759_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_153759_);
    }

    public static void makeParticles(Level level,BlockPos pos, int particleRotation) {
        Random random = Create.RANDOM;
        int shouldSpawnSmoke = random.nextInt(7);
        if(shouldSpawnSmoke==0) {


            if(particleRotation==0)
                    level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0D, 0.08D, 0.0D);
            if(particleRotation==1)
                    level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0.0D, 0.08D, 0.0D);

            if(particleRotation==2)
                    level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ(), 0.0D, 0.08D, 0.0D);
            if(particleRotation==3)
                    level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 1, 0.0D, 0.08D, 0.0D);
            if(particleRotation==4)
                    level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 1, pos.getY() + 0.5, pos.getZ() + 0.5, 0.0D, 0.08D, 0.0D);
            if(particleRotation==5)
                    level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX(), pos.getY() + 0.5, pos.getZ() + 0.5, 0.0D, 0.08D, 0.0D);



        }

    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_153746_) {
        p_153746_.add(FACING, WATERLOGGED);
    }
    @Override
    public Class<ExhaustBlockEntity> getBlockEntityClass() {
        return ExhaustBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ExhaustBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.EXHAUST.get();
    }
}
