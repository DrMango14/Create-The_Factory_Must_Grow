package com.drmangotea.tfmg.content.electricity.content.devices.polarizer;

import com.drmangotea.tfmg.content.electricity.content.devices.base.DirectionalResistiveLoadBlock;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@NothingNullByDefault
public class PolarizerBlock extends DirectionalResistiveLoadBlock implements IBE<PolarizerBlockEntity> {
    public PolarizerBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof PolarizerBlockEntity be)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
		if (player.getItemInHand(hand).getItem() instanceof SpoolItem)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack output = be.outputInventory.getStackInSlot(0);
        if (!output.isEmpty()) {
            if (!level.isClientSide) {
                ItemStack extracted = output.copy();
                be.outputInventory.setStackInSlot(0, ItemStack.EMPTY);
                if (!player.getInventory().add(extracted)) {
                    player.drop(extracted, false);
                }
                be.sendData();
                be.setChanged();
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (be.capacitorPercentage > 0 || TFMGUtils.returnItemToInventory(be.inventory, 0, player, hand)) {
            return ItemInteractionResult.SUCCESS;
        }

        if (be.inventory.isEmpty()
                && !stack.isEmpty()
                && be.outputInventory.getStackInSlot(0).isEmpty()
                && PolarizerCommons.getRecipe(level, stack).isPresent()) {
            if (!level.isClientSide) {
                ItemStack held = player.getItemInHand(hand);
                ItemStack toInsert = held.copyWithCount(1);
                be.inventory.insertItem(0, toInsert, false);
                held.shrink(1);
                be.sendData();
                be.setChanged();
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }


    //@Override
    //public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
    //    return TFMGShapes.POLARIZER.get(state.getValue(FACING));
    //}

    @Override
    public Class<PolarizerBlockEntity> getBlockEntityClass() {
        return PolarizerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PolarizerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.POLARIZER.get();
    }
}
