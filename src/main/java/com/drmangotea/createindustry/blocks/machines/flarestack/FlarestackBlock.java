package com.drmangotea.createindustry.blocks.machines.flarestack;


import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
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
import java.util.Random;

public class FlarestackBlock extends Block implements IBE<FlarestackBlockEntity>, IWrenchable {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    //Direction facing = this.defaultBlockState().getValue(FACING);
    public FlarestackBlock(Properties p_55926_) {
        super(p_55926_);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)));

    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.FLARESTACK;
    }
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_51240_) {

        return this.defaultBlockState().setValue(LIT, Boolean.valueOf(false));
    }

    public static void makeParticles(Level level,BlockPos pos) {
        Random random = Create.RANDOM;
        int shouldSpawnSmoke = random.nextInt(7);
        if(shouldSpawnSmoke==0) {



            level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0.0D, 0.08D, 0.0D);
            level.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Create.RANDOM.nextDouble(0.28)-0.14D, 0.14D, Create.RANDOM.nextDouble(0.28)-0.14D);
            level.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Create.RANDOM.nextDouble(0.28)-0.14D, 0.14D, Create.RANDOM.nextDouble(0.28)-0.14D);
            level.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Create.RANDOM.nextDouble(0.28)-0.14D, 0.14D, Create.RANDOM.nextDouble(0.28)-0.14D);



        }

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
