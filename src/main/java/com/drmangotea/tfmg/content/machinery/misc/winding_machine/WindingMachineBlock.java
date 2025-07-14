package com.drmangotea.tfmg.content.machinery.misc.winding_machine;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WindingMachineBlock extends HorizontalKineticBlock implements IBE<WindingMachineBlockEntity> {

    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public WindingMachineBlock(Properties p_54120_) {
        super(p_54120_);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED,false));
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getCounterClockWise().getAxis();
    }

    @Override
    public boolean isSignalSource(BlockState p_60571_) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    public int getSignal(BlockState p_60483_, BlockGetter p_60484_, BlockPos p_60485_, Direction p_60486_) {
        return p_60483_.getValue(POWERED) ? 15 : 0;

    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        IBE.onRemove(pState,pLevel,pPos,pNewState);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return TFMGShapes.WINDING_MACHINE.get(p_60555_.getValue(HORIZONTAL_FACING));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {


        if(level.getBlockEntity(pos) instanceof WindingMachineBlockEntity be){
            if(player.getItemInHand(hand).isEmpty()){
                if(!be.inventory.isEmpty()&&!(be.inventory.getItem(0).getItem() instanceof SpoolItem)&&player.isShiftKeyDown()) {
                    player.setItemInHand(hand, be.inventory.getStackInSlot(0));
                    be.inventory.setItem(0, ItemStack.EMPTY);
                    return ItemInteractionResult.SUCCESS;
                }
                if(!be.spool.isEmpty()){
                    player.setItemInHand(hand, be.spool);
                    be.spool = ItemStack.EMPTY;
                    return ItemInteractionResult.SUCCESS;
                }
            }else {
                if(be.inventory.isEmpty()&&!(player.getItemInHand(hand).getItem() instanceof SpoolItem)){
                    ItemStack stack1 = player.getItemInHand(hand).copy();
                    stack1.setCount(1);
                    be.inventory.setItem(0, stack1);
                    player.getItemInHand(hand).shrink(1);
                    return ItemInteractionResult.SUCCESS;
                }
            }

        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }


    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getCounterClockWise();
    }

    @Override
    public Class<WindingMachineBlockEntity> getBlockEntityClass() {
        return WindingMachineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends WindingMachineBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.WINDING_MACHINE.get();
    }
}
