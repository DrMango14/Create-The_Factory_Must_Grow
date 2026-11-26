package com.drmangotea.tfmg.content.decoration.tanks;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public abstract class TFMGFluidTankItem<T extends TFMGFluidTankBlock<R>, R extends TFMGFluidTankBlockEntity> extends BlockItem {
	public TFMGFluidTankItem(Block b, Properties p) { super(b, p); }
	
	@Override @ParametersAreNonnullByDefault
	public InteractionResult place(BlockPlaceContext ctx) {
		InteractionResult initialResult = super.place(ctx);
		if (!initialResult.consumesAction())
			return initialResult;
		tryMultiPlace(ctx);
		return initialResult;
	}
	
	@Override
	protected boolean updateCustomBlockEntityTag(@NotNull BlockPos blockPos, Level level, Player player, @NotNull ItemStack itemStack, @NotNull BlockState blockState) {
		MinecraftServer minecraftserver = level.getServer();
		if (minecraftserver == null) return false;
		
		CustomData blockEntityData = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
		if (blockEntityData != null) {
			CompoundTag nbt = blockEntityData.copyTag();
			nbt.remove("Luminosity");
			nbt.remove("Size");
			nbt.remove("Height");
			nbt.remove("Controller");
			nbt.remove("LastKnownPos");
			if (nbt.contains("TankContent")) {
				FluidStack fluid = FluidStack.parseOptional(minecraftserver.registryAccess(), nbt.getCompound("TankContent"));
				if (!fluid.isEmpty()) {
					fluid.setAmount(Math.min(FluidTankBlockEntity.getCapacityMultiplier(), fluid.getAmount()));
					nbt.put("TankContent", fluid.saveOptional(minecraftserver.registryAccess()));
				}
			}
			BlockEntity.addEntityType(nbt, ((IBE<?>) this.getBlock()).getBlockEntityType());
			itemStack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(nbt));
		}
		return super.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState);
	}
	
	private void tryMultiPlace(BlockPlaceContext ctx) {
		Player player = ctx.getPlayer();
		Direction face = ctx.getClickedFace();
		if (player == null || player.isShiftKeyDown() || !face.getAxis().isVertical()) return;
		ItemStack stack = ctx.getItemInHand();
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockPos placedOnPos = pos.relative(face.getOpposite());
		BlockState placedOnState = world.getBlockState(placedOnPos);
		
		if (!T.isTank(placedOnState)) return;
		
		FluidTankBlockEntity tankAt = (FluidTankBlockEntity) world.getBlockEntity(placedOnPos);
		if (tankAt == null) return;
		FluidTankBlockEntity controllerTE = tankAt.getControllerBE();
		if (controllerTE == null) return;
		
		int width = controllerTE.getWidth();
		if (width == 1) return;
		
		int tanksToPlace = 0;
		BlockPos startPos = face == Direction.DOWN ?
			controllerTE.getBlockPos().below()
			: controllerTE.getBlockPos().above(controllerTE.getHeight());
		
		if (startPos.getY() != pos.getY())
			return;
		
		for (int xOffset = 0; xOffset < width; xOffset++) {
			for (int zOffset = 0; zOffset < width; zOffset++) {
				BlockPos offsetPos = startPos.offset(xOffset, 0, zOffset);
				BlockState blockState = world.getBlockState(offsetPos);
				if (T.isTank(blockState)) continue;
				if (!blockState.canBeReplaced()) return;
				tanksToPlace++;
			}
		}
		
		if (!player.isCreative() && stack.getCount() < tanksToPlace)
			return;
		
		for (int xOffset = 0; xOffset < width; xOffset++) {
			for (int zOffset = 0; zOffset < width; zOffset++) {
				BlockPos offsetPos = startPos.offset(xOffset, 0, zOffset);
				BlockState blockState = world.getBlockState(offsetPos);
				if (T.isTank(blockState)) continue;
				BlockPlaceContext context = BlockPlaceContext.at(ctx, offsetPos, face);
				player.getPersistentData()
					.putBoolean("SilenceTankSound", true);
				super.place(context);
				player.getPersistentData()
					.remove("SilenceTankSound");
			}
		}
	}
}