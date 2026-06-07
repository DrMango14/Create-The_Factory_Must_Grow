package com.drmangotea.tfmg.content.electricity.connection.cables;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class CableConnectorBlock extends WallMountBlock implements IBE<CableConnectorBlockEntity>, IHaveCables, IWrenchable {

    public static final BooleanProperty EXTENSION = BooleanProperty.create("extension");
    public static final BooleanProperty INPUT_MODE = BooleanProperty.create("input_mode");

    public CableConnectorBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(EXTENSION, false).setValue(INPUT_MODE, false));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState1, boolean b) {
        updateExtension(level, state, pos);
        withBlockEntityDo(level, pos, IElectric::onPlaced);
        BlockPos below = pos.relative(state.getValue(FACING).getOpposite());
        BlockState stateBelow = level.getBlockState(below);
        if (stateBelow.getBlock() instanceof IHaveCables)
            updateExtension(level, stateBelow, below);

        super.onPlace(state, level, pos, blockState1, b);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return handleChangingTypes(state,context.getLevel(),context.getClickedPos());
    }

    public InteractionResult handleChangingTypes(BlockState state, Level level, BlockPos pos){
        Direction facing = state.getValue(FACING).getOpposite();
        BlockState stateBelow = level.getBlockState(pos.relative(facing));
        if (stateBelow.getBlock() instanceof CableConnectorBlock) {
            return handleChangingTypes(stateBelow, level, pos.relative(facing));
        }

        return changeTypes(state, level, pos);
    }

    public InteractionResult changeTypes(BlockState state, Level level, BlockPos pos) {

        if(level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be){
            be.getData().energyGiven = 0;
            be.getData().energyTaken = 0;
        }

        TFMGUtils.playSound(level, pos, AllSoundEvents.WRENCH_ROTATE.getMainEvent(), SoundSource.BLOCKS);
        level.setBlock(pos, state.setValue(CableConnectorBlock.INPUT_MODE, !state.getValue(INPUT_MODE)), 3);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }


    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {


        updateExtension((Level) level, state, pos);
        if (level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be) {
            Direction facing = state.getValue(FACING);
            List<Direction> directions = new ArrayList<>();
            directions.add(facing.getOpposite());
            be.doActionNextTick(i -> be.checkForFEOutputs(directions));
        }

        super.onNeighborChange(state, level, pos, neighbor);
    }


    public void updateExtension(Level level, BlockState state, BlockPos pos) {
        BlockPos above = pos.relative(state.getValue(FACING));
        BlockState stateAbove = level.getBlockState(above);

        if (stateAbove.getBlock() instanceof IHaveCables && stateAbove.getValue(FACING) == state.getValue(FACING)) {
            level.setBlockAndUpdate(pos, state.setValue(EXTENSION, true));
        } else {
            level.setBlockAndUpdate(pos, state.setValue(EXTENSION, false));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXTENSION);
        builder.add(INPUT_MODE);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if (pState.getValue(EXTENSION))
            return TFMGShapes.CABLE_CONNECTOR_MIDDLE.get(pState.getValue(FACING));


        return TFMGShapes.CABLE_CONNECTOR.get(pState.getValue(FACING));

    }

    @Override
    public Class<CableConnectorBlockEntity> getBlockEntityClass() {
        return CableConnectorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CableConnectorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.CABLE_CONNECTOR.get();
    }
}
