package com.drmangotea.tfmg.content.decoration.tanks;

import com.drmangotea.tfmg.base.TFMGBlockConnectivityHandler;
import com.drmangotea.tfmg.mixin.accessor.FluidTankBlockEntityAccessor;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.simibubi.create.content.fluids.tank.FluidTankBlock.Shape;

public abstract class TFMGFluidTankBlock<T extends TFMGFluidTankBlockEntity> extends Block implements IWrenchable, IBE<T> {
	public static final BooleanProperty TOP = FluidTankBlock.TOP;
	public static final BooleanProperty BOTTOM = FluidTankBlock.BOTTOM;
	public static final EnumProperty<Shape> SHAPE = FluidTankBlock.SHAPE;
	
	protected TFMGFluidTankBlock(Properties p) {
		super(p);
		registerDefaultState(defaultBlockState()
			.setValue(TOP, true)
			.setValue(BOTTOM, true)
			.setValue(SHAPE, FluidTankBlock.Shape.WINDOW)
		);
	}
	
	@Override
	abstract public Class<T> getBlockEntityClass();
	@Override
	abstract public BlockEntityType<? extends T> getBlockEntityType();
	
	public static boolean isTank(BlockState state) { return state.getBlock() instanceof TFMGFluidTankBlock; }
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> b) { b.add(TOP, BOTTOM, SHAPE); }
	
	@Override @ParametersAreNonnullByDefault
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved) {
		if (oldState.getBlock() == state.getBlock()) return;
		if (moved) return;
		withBlockEntityDo(world, pos, TFMGFluidTankBlockEntity::updateConnectivity);
	}
	
	@Override @ParametersAreNonnullByDefault
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
			if (!(world.getBlockEntity(pos) instanceof FluidTankBlockEntity tankTE))
				return;
			world.removeBlockEntity(pos);
			TFMGBlockConnectivityHandler.splitMulti(tankTE);
		}
	}
	
	@Override @ParametersAreNonnullByDefault
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
		FluidTankBlockEntity tankAt = TFMGBlockConnectivityHandler.partAt(getBlockEntityType(), world, pos);
		if (tankAt == null) return 0;
		FluidTankBlockEntity controllerTE = tankAt.getControllerBE();
		if (controllerTE == null || !((FluidTankBlockEntityAccessor)controllerTE).tfmg$getWindow())
			return 0;
		return ((FluidTankBlockEntityAccessor)tankAt).tfmg$getLuminosity();
	}
	
	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		withBlockEntityDo(context.getLevel(), context.getClickedPos(), FluidTankBlockEntity::toggleWindows);
		return InteractionResult.SUCCESS;
	}
	
	static final VoxelShape CAMPFIRE_SMOKE_CLIP = Block.box(0, 4, 0, 16, 16, 16);
	
	@Override @ParametersAreNonnullByDefault @NotNull
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		if (pContext == CollisionContext.empty())
			return CAMPFIRE_SMOKE_CLIP;
		return pState.getShape(pLevel, pPos);
	}
	
	@Override @ParametersAreNonnullByDefault @NotNull
	public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
		return Shapes.block();
	}
	
	@Override @ParametersAreNonnullByDefault @NotNull
	public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState,
	                              LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
		if (pDirection == Direction.DOWN && pNeighborState.getBlock() != this)
			withBlockEntityDo(pLevel, pCurrentPos, FluidTankBlockEntity::updateBoilerTemperature);
		return pState;
	}
	
	@Override @ParametersAreNonnullByDefault @NotNull
	public BlockState mirror(BlockState state, Mirror mirror) {
		if (mirror == Mirror.NONE) return state;
		boolean x = mirror == Mirror.FRONT_BACK;
		return switch (state.getValue(SHAPE)) {
			case WINDOW_NE -> state.setValue(SHAPE, x ? Shape.WINDOW_NW : Shape.WINDOW_SE);
			case WINDOW_NW -> state.setValue(SHAPE, x ? Shape.WINDOW_NE : Shape.WINDOW_SW);
			case WINDOW_SE -> state.setValue(SHAPE, x ? Shape.WINDOW_SW : Shape.WINDOW_NE);
			case WINDOW_SW -> state.setValue(SHAPE, x ? Shape.WINDOW_SE : Shape.WINDOW_NW);
			default -> state;
		};
	}
	
	@Override @ParametersAreNonnullByDefault @NotNull
	public BlockState rotate(BlockState state, Rotation rotation) {
		for (int i = 0; i < rotation.ordinal(); i++)
			state = rotateOnce(state);
		return state;
	}
	
	public BlockState rotateOnce(BlockState state) {
		return switch (state.getValue(SHAPE)) {
			case WINDOW_NE -> state.setValue(SHAPE, Shape.WINDOW_SE);
			case WINDOW_NW -> state.setValue(SHAPE, Shape.WINDOW_NE);
			case WINDOW_SE -> state.setValue(SHAPE, Shape.WINDOW_SW);
			case WINDOW_SW -> state.setValue(SHAPE, Shape.WINDOW_NW);
			default -> state;
		};
	}
	
	// Tanks are less noisy when placed in batch
	public static final SoundType SILENCED_METAL =
		new DeferredSoundType(0.1F, 1.5F, () -> SoundEvents.METAL_BREAK, () -> SoundEvents.METAL_STEP,
			() -> SoundEvents.METAL_PLACE, () -> SoundEvents.METAL_HIT, () -> SoundEvents.METAL_FALL);
	
	@Override @NotNull
	public SoundType getSoundType(@NotNull BlockState state, @NotNull LevelReader world, @NotNull BlockPos pos, Entity entity) {
		SoundType soundType = super.getSoundType(state, world, pos, entity);
		if (entity != null && entity.getPersistentData()
			.contains("SilenceTankSound"))
			return SILENCED_METAL;
		return soundType;
	}
	
	@Override @ParametersAreNonnullByDefault
	public boolean hasAnalogOutputSignal(BlockState state) { return true; }
	
	@Override @ParametersAreNonnullByDefault
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return getBlockEntityOptional(worldIn, pos)
			.map(T::getControllerBE)
			.map(te -> ComparatorUtil.fractionToRedstoneLevel(te.getFillState()))
			.orElse(0);
	}
	
	@Override  @ParametersAreNonnullByDefault @NotNull
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		boolean onClient = level.isClientSide;
		
		if (stack.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!player.isCreative()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		
		TFMGFluidTankBlockEntity be = TFMGBlockConnectivityHandler.partAt(getBlockEntityType(), level, pos);
		if (be == null) return ItemInteractionResult.FAIL;
		
		IFluidHandler tankCapability = level.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
		if (tankCapability == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		FluidStack prevFluidInTank = tankCapability.getFluidInTank(0).copy();
		
		FluidHelper.FluidExchange exchange = null;
		if (FluidHelper.tryEmptyItemIntoBE(level, player, hand, stack, be))
			exchange = FluidHelper.FluidExchange.ITEM_TO_TANK;
		else if (FluidHelper.tryFillItemFromBE(level, player, hand, stack, be))
			exchange = FluidHelper.FluidExchange.TANK_TO_ITEM;
		
		SoundEvent soundevent = null;
		BlockState fluidState = null;
		FluidStack fluidInTank = tankCapability.getFluidInTank(0);
		
		if (exchange == null) {
			if (GenericItemEmptying.canItemBeEmptied(level, stack)
				|| GenericItemFilling.canItemBeFilled(level, stack))
				return ItemInteractionResult.SUCCESS;
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		} else if (exchange == FluidHelper.FluidExchange.ITEM_TO_TANK) {
			fluidState = fluidInTank.getFluid().defaultFluidState().createLegacyBlock();
			soundevent = FluidHelper.getEmptySound(fluidInTank);
		} else if (exchange == FluidHelper.FluidExchange.TANK_TO_ITEM) {
			fluidState = prevFluidInTank.getFluid().defaultFluidState().createLegacyBlock();
			soundevent = FluidHelper.getFillSound(prevFluidInTank);
		}
		
		if (soundevent != null && !onClient) {
			float pitch = Mth.clamp(1 - (1f * fluidInTank.getAmount() / (FluidTankBlockEntity.getCapacityMultiplier() * 16)), 0, 1)/1.5f;
			pitch += .375f + 0.25f*(level.random.nextFloat());
			level.playSound(null, pos, soundevent, SoundSource.BLOCKS, .5f, pitch);
		}
		
		if (!FluidStack.isSameFluidSameComponents(fluidInTank, prevFluidInTank)) {
			FluidTankBlockEntity controllerBE = be.getControllerBE();
			if (controllerBE != null) {
				if (onClient) {
					BlockParticleOption blockParticleData = new BlockParticleOption(ParticleTypes.BLOCK, fluidState);
					float fluidLevel = (float) fluidInTank.getAmount() / tankCapability.getTankCapacity(0);
					
					boolean reversed = fluidInTank.getFluid()
						.getFluidType()
						.isLighterThanAir();
					if (reversed) fluidLevel = 1 - fluidLevel;
					
					Vec3 vec = hitResult.getLocation();
					vec = new Vec3(vec.x, controllerBE.getBlockPos()
						.getY() + fluidLevel * (((FluidTankBlockEntityAccessor)controllerBE).tfmg$getHeight() - .5f) + .25f, vec.z);
					Vec3 motion = player.position()
						.subtract(vec)
						.scale(1 / 20f);
					vec = vec.add(motion);
					level.addParticle(blockParticleData, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z);
					return ItemInteractionResult.SUCCESS;
				}
				
				controllerBE.sendDataImmediately();
				controllerBE.setChanged();
			}
		}
		
		return ItemInteractionResult.SUCCESS;
	}
}