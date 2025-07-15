package com.drmangotea.tfmg.content.electricity.connection.cables;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.blocks.WallMountBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
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

public class CableConnectorBlock extends WallMountBlock implements IBE<CableConnectorBlockEntity>, IHaveCables {

    public static final BooleanProperty EXTENSION = BooleanProperty.create("extension");


    public CableConnectorBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(EXTENSION, false));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState blockState1, boolean b) {
        updateExtension(level,state,pos);
        withBlockEntityDo(level,pos, IElectric::onPlaced);
        BlockPos below = pos.relative(state.getValue(FACING).getOpposite());
        BlockState stateBelow = level.getBlockState(below);
        if(stateBelow.getBlock() instanceof IHaveCables)
            updateExtension(level,stateBelow,below);

        super.onPlace(state, level, pos, blockState1, b);
    }



    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    //@Override
    //public InteractionResult use(BlockState state, Level level, BlockPos pos, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
    //    if(level.isClientSide)
    //        return InteractionResult.SUCCESS;
    //    if(level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be&&p_60506_.isCrouching()){
//
//
    //        Direction direction = Direction.NORTH;
//
//
//
    //        int numero = Create.RANDOM.nextInt(6);
    //        if(numero == 0)
    //            direction = Direction.NORTH;
//
    //        if(numero == 1)
    //            direction = Direction.SOUTH;
    //        if(numero == 2)
    //            direction = Direction.WEST;
    //        if(numero == 3)
    //            direction = Direction.EAST;
    //        if(numero == 4)
    //            direction = Direction.UP;
    //        if(numero == 5)
    //            direction = Direction.DOWN;
//
    //        be.addConnection(pos.relative(direction));
    //        return InteractionResult.SUCCESS;
    //    }
//
//
    //    return InteractionResult.SUCCESS;
    //}
    //@Override
    //public void onRemove(BlockState state, Level level, BlockPos pos, BlockState blockState1, boolean b) {
    //    updateExtension(level,state,pos);
    //    super.onRemove(state, level, pos, blockState1, b);
    //}

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {


        updateExtension((Level) level,state,pos);

        super.onNeighborChange(state, level, pos, neighbor);
    }

    public void updateExtension(Level level, BlockState state, BlockPos pos){
        BlockPos above = pos.relative(state.getValue(FACING));
        BlockState stateAbove = level.getBlockState(above);

        if(stateAbove.getBlock() instanceof IHaveCables&&stateAbove.getValue(FACING)==state.getValue(FACING)) {
            level.setBlockAndUpdate(pos, state.setValue(EXTENSION, true));
        } else {
            level.setBlockAndUpdate(pos, state.setValue(EXTENSION, false));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXTENSION);
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if(pState.getValue(EXTENSION))
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
