package com.drmangotea.tfmg.content.electricity.experimental.blocks;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.electricity.experimental.packets.RealNetworkUpdatePacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThreePhaseGeneratorBlock extends DirectionalKineticBlock implements IBE<ThreePhaseGeneratorBlockEntity>{
    public ThreePhaseGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if(level.getBlockEntity(pos) instanceof ThreePhaseGeneratorBlockEntity be){
            be.updateNetwork(pos);
            if (level instanceof ServerLevel serverLevel)
                CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(pos), new RealNetworkUpdatePacket(BlockPos.of(pos.asLong())));
            be.sendData();

        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.GENERATOR.get(pState.getValue(FACING));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING);

    }
    public Direction getPreferredFacing(BlockPlaceContext context) {

        if(super.getPreferredFacing(context)==null)
            return null;

        return super.getPreferredFacing(context).getOpposite();
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state,level,pos,newState,isMoving);
    }

    @Override
    public Class<ThreePhaseGeneratorBlockEntity> getBlockEntityClass() {
        return ThreePhaseGeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ThreePhaseGeneratorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.THREE_PHASE_GENERATOR.get();
    }


}
