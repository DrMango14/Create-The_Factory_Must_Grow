package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.wires;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlock;
import com.drmangotea.tfmg.content.electricity.connection.cables.IHaveCables;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.packets.AddElectricalComponentPacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RealConnectorBlock extends WallMountBlock implements IBE<RealConnectorBlockEntity> {

    public static final BooleanProperty EXTENSION = BooleanProperty.create("extension");

    public RealConnectorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(EXTENSION, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXTENSION);

    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        updateExtension(level, state, pos);
        BlockPos below = pos.relative(state.getValue(FACING).getOpposite());
        BlockState stateBelow = level.getBlockState(below);
        if (stateBelow.getBlock() instanceof RealConnectorBlock)
            updateExtension(level, stateBelow, below);


        if (level.getBlockEntity(pos) instanceof IRealisticElectric be) {
            be.onPlace();
            if (level instanceof ServerLevel serverLevel)
                CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(pos), new AddElectricalComponentPacket(BlockPos.of(pos.asLong())));


        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (pState.getValue(EXTENSION))
            return TFMGShapes.CABLE_CONNECTOR_MIDDLE.get(pState.getValue(FACING));


        return TFMGShapes.CABLE_CONNECTOR.get(pState.getValue(FACING));

    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {


        updateExtension((Level) level, state, pos);


        super.onNeighborChange(state, level, pos, neighbor);
    }

    public void updateExtension(Level level, BlockState state, BlockPos pos) {
        BlockPos above = pos.relative(state.getValue(FACING));
        BlockState stateAbove = level.getBlockState(above);

        if (stateAbove.getBlock() instanceof RealConnectorBlock && stateAbove.getValue(FACING) == state.getValue(FACING)) {
            level.setBlockAndUpdate(pos, state.setValue(EXTENSION, true));
        } else {
            level.setBlockAndUpdate(pos, state.setValue(EXTENSION, false));
        }
    }



    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public Class<RealConnectorBlockEntity> getBlockEntityClass() {
        return RealConnectorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RealConnectorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.DEBUG_CONNECTOR.get();
    }
}
