package com.drmangotea.tfmg.blocks.machines.metal_processing.blast_furnace;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MoltenMetalBlock extends Block implements IBE<MoltenMetalBlockEntity> {
    public MoltenMetalBlock(Properties p_49795_) {
        super(p_49795_);
    }
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        if(entity instanceof LivingEntity){

            entity.makeStuckInBlock(state, new Vec3((double)0.9F, 1.5D, (double)0.9F));

            int random = Create.RANDOM.nextInt(24);

            if(random==7)
                entity.lavaHurt();
            entity.setSecondsOnFire(10);

        }



    }






    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return TFMGShapes.EMPTY;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<MoltenMetalBlockEntity> getBlockEntityClass() {
        return MoltenMetalBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MoltenMetalBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.MOLTEN_METAL.get();
    }
}
