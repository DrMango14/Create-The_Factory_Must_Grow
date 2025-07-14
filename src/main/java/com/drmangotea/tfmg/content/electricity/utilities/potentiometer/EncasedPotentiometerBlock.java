package com.drmangotea.tfmg.content.electricity.utilities.potentiometer;

import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.base.IVoltageChanger;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EncasedPotentiometerBlock extends TFMGDirectionalBlock implements IBE<PotentiometerBlockEntity>, IVoltageChanger, EncasedBlock, IWrenchable {
    public EncasedPotentiometerBlock(Properties p_54120_) {
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
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        if (context.getLevel().isClientSide)
            return InteractionResult.SUCCESS;
        context.getLevel()
                .levelEvent(2001, context.getClickedPos(), Block.getId(state));

        KineticBlockEntity.switchToBlockState(context.getLevel(), context.getClickedPos(),
                TFMGBlocks.POTENTIOMETER.getDefaultState()
                        .setValue(FACING, state.getValue(FACING)));
        return InteractionResult.SUCCESS;
    }


    @Override
    public void handleEncasing(BlockState state, Level level, BlockPos pos, ItemStack heldItem, Player player, InteractionHand hand,
                               BlockHitResult ray) {
        level.setBlockAndUpdate(pos, this.defaultBlockState().setValue(FACING,state.getValue(FACING)));
    }


        @Override
    public Class<PotentiometerBlockEntity> getBlockEntityClass() {
        return PotentiometerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PotentiometerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.POTENTIOMETER.get();
    }

    @Override
    public Block getCasing() {
        return TFMGBlocks.HEAVY_MACHINERY_CASING.get();
    }
}
