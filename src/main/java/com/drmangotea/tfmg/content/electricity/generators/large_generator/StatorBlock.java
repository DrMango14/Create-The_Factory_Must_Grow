package com.drmangotea.tfmg.content.electricity.generators.large_generator;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.connection.diagonal.DiagonalCableBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StatorBlock extends DirectionalBlock implements IBE<StatorBlockEntity> {

    public static final MapCodec<StatorBlock> CODEC = simpleCodec(StatorBlock::new);

    public static final BooleanProperty VALUE = BooleanProperty.create("value");
    public static final EnumProperty<StatorState> STATOR_STATE = EnumProperty.create("stator_state", StatorState.class);

    public StatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }


    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        withBlockEntityDo(level, pos, IElectric::onPlaced);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATOR_STATE,FACING,VALUE);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {

        StatorState statorState = state.getValue(STATOR_STATE);
        boolean rotated = state.getValue(VALUE);
        Direction direction = state.getValue(FACING);

        if(direction == Direction.UP)
            direction = Direction.NORTH;
        if(statorState == StatorState.CORNER){
            return rotated ? TFMGShapes.STATOR_ROTATED.get(direction) : TFMGShapes.STATOR.get(direction);
        }
        if(statorState == StatorState.CORNER_HORIZONTAL){


            return TFMGShapes.STATOR_VERTICAL.get(direction);
        }


        return super.getShape(state, blockGetter, pos, context);
    }

    @Override
    public Class<StatorBlockEntity> getBlockEntityClass() {
        return StatorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StatorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STATOR.get();
    }

    public enum StatorState implements StringRepresentable{


        SIDE("side"),
        CORNER_HORIZONTAL("corner_horizontal"),
        CORNER("corner")


        ;
        final String name;
        StatorState(String name){
            this.name = name;
        }


        @Override
        public String getSerializedName() {
            return name;
        }
    }





}
