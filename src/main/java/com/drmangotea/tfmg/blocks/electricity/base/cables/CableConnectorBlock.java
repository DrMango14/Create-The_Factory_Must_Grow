package com.drmangotea.tfmg.blocks.electricity.base.cables;


import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.WallMountBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class  CableConnectorBlock extends WallMountBlock implements IBE<CableConnectorBlockEntity>, IHaveCables, IWrenchable {


    public static final BooleanProperty EXTENSION = BooleanProperty.create("extension");

    public CableConnectorBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(defaultBlockState().setValue(EXTENSION, false));
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        if(pState.getValue(EXTENSION))
            return TFMGShapes.CABLE_CONNECTOR_MIDDLE.get(pState.getValue(FACING));


        return TFMGShapes.CABLE_CONNECTOR.get(pState.getValue(FACING));

    }
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();


        return onBlockEntityUse(level, pos, be -> {

            be.sendStuff();
            return InteractionResult.SUCCESS;


        });


    }


    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EXTENSION);
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
