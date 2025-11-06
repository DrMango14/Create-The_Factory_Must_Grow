package com.drmangotea.tfmg.content.electricity.utilities.diode;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.IVoltageChanger;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.decoration.encasing.EncasableBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ElectricDiodeBlock extends TFMGDirectionalBlock implements IBE<ElectricDiodeBlockEntity>, IVoltageChanger, EncasableBlock {
    public ElectricDiodeBlock(Properties p_54120_) {
        super(p_54120_);
    }
    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        withBlockEntityDo(level,pos, IElectric::onPlaced);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult ray) {
        ItemStack heldItem = player.getItemInHand(hand);
        InteractionResult result = tryEncase(state, world, pos, heldItem, player, hand, ray);
        if (result.consumesAction())
            return result;

        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return pState.getValue(FACING).getAxis().isVertical() ? TFMGShapes.RESISTOR_VERTICAL.get(pState.getValue(FACING)) : TFMGShapes.POTENTIOMETER.get(pState.getValue(FACING));
    }


        @Override
    public Class<ElectricDiodeBlockEntity> getBlockEntityClass() {
        return ElectricDiodeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ElectricDiodeBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.DIODE.get();
    }
}
