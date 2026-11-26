package com.drmangotea.tfmg.content.items;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.foundation.utility.RaycastHelper;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.ponder.api.PonderPalette;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@NothingNullByDefault
public class ScrewdriverItem extends Item {
    public ScrewdriverItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        BlockPos positionClicked = pContext.getClickedPos();
        Level level = pContext.getLevel();

        if (level.getBlockEntity(positionClicked) != null && player != null) {
            if (level.getBlockEntity(positionClicked) instanceof FluidPipeBlockEntity fp) {
                toggleLock(player, level, positionClicked, level.getBlockState(positionClicked), fp);
                pContext.getItemInHand().hurtAndBreak(1, player, LivingEntity.getSlotForHand(pContext.getHand()));
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(pContext);
    }

    public void toggleLock(Player player, Level world, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        world.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4f, 0.5f);
        setLocked(blockEntity, !isLocked(blockEntity));
        if (isLocked(blockEntity))
            return;

        BlockState newState;
        FluidTransportBehaviour.cacheFlows(world, pos);
        newState = updatePipe(world, pos, state).setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
        world.setBlock(pos, newState, 3);
        FluidTransportBehaviour.loadFlows(world, pos);
    }

    BlockState updatePipe(LevelAccessor world, BlockPos pos, BlockState state) {
        Direction side = Direction.UP;
        Map<Direction, BooleanProperty> facingToPropertyMap = FluidPipeBlock.PROPERTY_BY_DIRECTION;
        return AllBlocks.FLUID_PIPE.get()
                .updateBlockState(state.getBlock().defaultBlockState()
                        .setValue(facingToPropertyMap.get(side), true)
                        .setValue(facingToPropertyMap.get(side.getOpposite()), true), side, null, world, pos);
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick() {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;
        ItemStack heldItemMainhand = player.getMainHandItem();
        ItemStack heldItemOffhand = player.getOffhandItem();
        if (!(heldItemMainhand.getItem() instanceof ScrewdriverItem) && !(heldItemOffhand.getItem() instanceof ScrewdriverItem))
            return;

        Level world = Minecraft.getInstance().level;

        if (world == null)
            return;

        BlockHitResult block = RaycastHelper.rayTraceRange(world, player, player.blockInteractionRange());
        BlockPos targetedPos = block.getBlockPos();
        VoxelShape shape = world.getBlockState(targetedPos).getShape(world, targetedPos);
        BlockEntity blockEntity = world.getBlockEntity(targetedPos);

        if (blockEntity instanceof FluidPipeBlockEntity && !shape.isEmpty()) {
			Outliner.getInstance().showAABB("lockable_pipe", shape.bounds().move(targetedPos))
				.colored(isLocked(blockEntity) ? PonderPalette.RED.getColor() : PonderPalette.GREEN.getColor())
				.lineWidth(1 / 32f);
        }
    }

    public static void setLocked(BlockEntity blockEntity, boolean locked) {
        blockEntity.setData(TFMGDataAttachments.LOCKED_PIPE, locked);
    }

    public static boolean isLocked(BlockEntity blockEntity) {
        return blockEntity.getExistingData(TFMGDataAttachments.LOCKED_PIPE).orElse(false);
    }
}
