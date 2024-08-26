package com.drmangotea.tfmg.blocks.electricity.base.cables;


import com.drmangotea.tfmg.blocks.electricity.base.IHaveCables;
import com.drmangotea.tfmg.blocks.electricity.base.WallMountBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.drmangotea.tfmg.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

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
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new ConnectNeightborsPacket(pos));
        withBlockEntityDo(level,pos,CableConnectorBlockEntity::onPlaced);

    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {

        IBE.onRemove(state, level, pos, newState);



    }
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if(pPlayer.getItemInHand(pHand).isEmpty()) {
            // withBlockEntityDo(pLevel, pPos, CableConnectorBlockEntity::updateGrid);
            return InteractionResult.SUCCESS;
        }

        return super.use(pState,pLevel,pPos,pPlayer,pHand,pHit);
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
