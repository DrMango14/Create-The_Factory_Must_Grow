package com.drmangotea.tfmg.base;

import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class TFMGBlockConnectivityHandler {
	public static <T extends BlockEntity & IMultiBlockEntityContainer> void formMulti(T be) {
		SearchCache<T> cache = new SearchCache<>();
		List<T> frontier = new ArrayList<>();
		frontier.add(be);
		BlockEntityType<?> type = be.getType();
		BlockGetter level = be.getLevel();
		BlockPos pos = be.getBlockPos();
		PriorityQueue<Pair<Sizing, T>> creationQueue = new PriorityQueue<>(
			(one, two) -> two.getKey().volume - one.getKey().volume
		);
		Set<BlockPos> visited = new HashSet<>();
		Direction.Axis mainAxis = be.getMainConnectionAxis();
		
		// essentially, if it's a vertical multi then the search won't be restricted by Y
		// alternately, a horizontal multi search shouldn't be restricted by X or Z
		int minX = (mainAxis == Direction.Axis.Y ? pos.getX() - be.getMaxWidth() : Integer.MIN_VALUE);
		int minY = (mainAxis != Direction.Axis.Y ? pos.getY() - be.getMaxWidth() : Integer.MIN_VALUE);
		int minZ = (mainAxis == Direction.Axis.Y ? pos.getZ() - be.getMaxWidth() : Integer.MIN_VALUE);
		
		while (!frontier.isEmpty()) {
			T part = frontier.removeFirst();
			BlockPos partPos = part.getBlockPos();
			if (visited.contains(partPos))
				continue;
			
			visited.add(partPos);
			
			Sizing size = getSizing(part, cache);
			if (size != null && size.volume > 1) {
				creationQueue.add(Pair.of(size, part));
			}
			
			for (Direction.Axis axis : Iterate.axes) {
				Direction dir = Direction.get(Direction.AxisDirection.NEGATIVE, axis);
				BlockPos next = partPos.relative(dir);
				
				if (next.getX() <= minX || next.getY() <= minY || next.getZ() <= minZ)
					continue;
				if (visited.contains(next))
					continue;
				T nextBe = partAt(type, level, next);
				if (nextBe == null)
					continue;
				if (nextBe.isRemoved())
					continue;
				frontier.add(nextBe);
			}
		}
		visited.clear();
		
		while (!creationQueue.isEmpty()) {
			Pair<Sizing, T> pair = creationQueue.poll();
			Sizing size = pair.getKey();
			T toCreate = pair.getValue();
			if (visited.contains(toCreate.getBlockPos()))
				continue;
			
			visited.add(toCreate.getBlockPos());
			assembleMultiWithSize(toCreate, size, cache);
		}
	}
	
	private static <T extends BlockEntity & IMultiBlockEntityContainer> void assembleMultiWithSize(
		T be, Sizing size, SearchCache<T> cache
	) {
		if (!be.isController())
			return;
		Level level = be.getLevel();
		if (level == null)
			return;
		BlockEntityType<?> type = be.getType();
		BlockPos origin = be.getBlockPos();
		Direction.Axis axis = be.getMainConnectionAxis();
		
		if (be.getWidth() == size.width && be.getHeight() == size.height)
			return;
		
		splitMultiAndInvalidate(be, cache);
		if (be instanceof IMultiBlockEntityContainer.Fluid ifluid && ifluid.hasTank())
			ifluid.setTankSize(0, size.volume);
		
		// optional fluid handling
		IFluidTank beTank;
		if (be instanceof IMultiBlockEntityContainer.Fluid ifluid && ifluid.hasTank()) {
			beTank = ifluid.getTank(0);
		} else {
			beTank = null;
		}
		
		forEachPosition(origin, axis, size.height, size.width, (pos) -> {
			T part = partAt(type, level, pos);
			if (part == null || part == be)
				return;
			
			if (part instanceof IMultiBlockEntityContainer.Fluid ifluidPart && ifluidPart.hasTank()) {
				IFluidTank tankAt = ifluidPart.getTank(0);
				FluidStack fluidAt = tankAt.drain(tankAt.getCapacity(), IFluidHandler.FluidAction.EXECUTE);
				if (!fluidAt.isEmpty() && beTank != null) {
					beTank.fill(fluidAt, IFluidHandler.FluidAction.EXECUTE);
				}
			}
			
			splitMultiAndInvalidate(part, cache);
			part.setController(origin);
			part.preventConnectivityUpdate();
			cache.put(pos, be);
			part.setHeight(size.height);
			part.setWidth(size.width);
			part.notifyMultiUpdated();
		});
		be.preventConnectivityUpdate();
		be.setWidth(size.width);
		be.setHeight(size.height);
		be.notifyMultiUpdated();
	}
	
	private static <T extends BlockEntity & IMultiBlockEntityContainer> Sizing getSizing(
		T be, SearchCache<T> cache
	) {
		Level level = be.getLevel();
		if (level == null || !be.isController())
			return null;
		BlockEntityType<?> type = be.getType();
		BlockPos origin = be.getBlockPos();
		Direction.Axis axis = be.getMainConnectionAxis();
		
		Block block = level.getBlockState(origin).getBlock();
		
		// optional fluid handling
		FluidStack fluid = FluidStack.EMPTY;
		if (be instanceof IMultiBlockEntityContainer.Fluid ifluid && ifluid.hasTank()) {
			fluid = ifluid.getTank(0).getFluid();
		}
		
		Sizing bestFit = new Sizing(0,0,0);
		for (int width = 1; width <= be.getMaxWidth(); width++) {int height = 0;
			
			int maxLength = be.getMaxLength(axis, width);
			
			HeightSearch:
			for (int Y = 0; Y < maxLength; Y++) {
				for (int X = 0; X < width; X++) { for (int Z = 0; Z < width; Z++) {
					BlockPos pos = switch (axis) {
						case X -> origin.offset(Y, X, Z);
						case Y -> origin.offset(X, Y, Z);
						case Z -> origin.offset(X, Z, Y);
					};
					Optional<T> part = cache.getOrCache(type, level, pos);
					if (part.isEmpty())
						break HeightSearch;
					
					Block otherBlock = level.getBlockState(pos).getBlock();
					if (!block.equals(otherBlock))
						break HeightSearch;
					
					T controller = part.get();
					if (!sizedAlignedBounded(controller, origin, axis, width, maxLength))
						break HeightSearch;
					
					if (controller instanceof IMultiBlockEntityContainer.Fluid ifluidCon && ifluidCon.hasTank()) {
						FluidStack otherFluid = ifluidCon.getFluid(0);
						if (!fluid.isEmpty() && !otherFluid.isEmpty() && !FluidStack.isSameFluidSameComponents(fluid, otherFluid))
							break HeightSearch;
					}
				} }
				height++;
			}
			Sizing search = new Sizing(width, height);
			
			//test volume
			if (search.volume < bestFit.volume)
				continue;
			bestFit = search;
		}
		return bestFit;
	}
	
	private static <T extends BlockEntity & IMultiBlockEntityContainer> boolean sizedAlignedBounded(
		T controller, BlockPos origin, Direction.Axis axis, int width, int maxLength
	) {
		BlockPos pos = controller.getBlockPos();
		int otherWidth = controller.getWidth();
		if (otherWidth > width)
			return false;
		if (otherWidth == width && controller.getHeight() == maxLength)
			return false;
		
		if (axis != controller.getMainConnectionAxis())
			return false;
		
		if (!pos.equals(origin)) {
			if (axis != Direction.Axis.Y) {
				if (pos.getY() < origin.getY())
					return false;
				if (pos.getY() + otherWidth > origin.getY() + width)
					return false;
			}
			if (axis != Direction.Axis.X) {
				if (pos.getX() < origin.getX())
					return false;
				if (pos.getX() + otherWidth > origin.getX() + width)
					return false;
			}
			if (axis != Direction.Axis.Z) {
				if (pos.getZ() < origin.getZ())
					return false;
                return pos.getZ() + otherWidth <= origin.getZ() + width;
			}
		}
		
		return true;
	}
	
	public static <T extends BlockEntity & IMultiBlockEntityContainer> void splitMulti(T be) {
		splitMultiAndInvalidate(be, null);
	}
	
	private static <T extends BlockEntity & IMultiBlockEntityContainer> void splitMultiAndInvalidate(
		T be, @Nullable SearchCache<T> cache
	) {
		T controller = be.getControllerBE();
		if (controller == null)
			return;
		
		Level level = controller.getLevel();
		if (level == null)
			return;
		
		int height = controller.getHeight();
		int width = controller.getWidth();
		if (width == 1 && height == 1)
			return;
		
		BlockPos origin = controller.getBlockPos();
		Direction.Axis axis = controller.getMainConnectionAxis();
		
		// fluid handling, if present
		FluidStack toDistribute;
		int maxCapacity;
		if (controller instanceof IMultiBlockEntityContainer.Fluid ifluidBE && ifluidBE.hasTank()) {
			toDistribute = ifluidBE.getFluid(0);
			maxCapacity = ifluidBE.getTankSize(0);
			if (!toDistribute.isEmpty() && !be.isRemoved())
				toDistribute.shrink(maxCapacity);
			ifluidBE.setTankSize(0, 1);
		} else {
			maxCapacity = 0;
			toDistribute = FluidStack.EMPTY;
		}
		BlockEntityType<?> type = controller.getType();
		forEachPosition(origin, axis, height, width, (pos) -> {
			T partAt = partAt(type, level, pos);
			if (partAt == null)
				return;
			if (!partAt.getController().equals(origin))
				return;
					
			T controllerBE = partAt.getControllerBE();
			partAt.setExtraData((controllerBE == null ? null : controllerBE.getExtraData()));
			partAt.removeController(true);
					
			if (!toDistribute.isEmpty() && partAt != controller) {
				FluidStack copy = toDistribute.copy();
				IFluidTank tank =
					(partAt instanceof IMultiBlockEntityContainer.Fluid ifluidPart ? ifluidPart.getTank(0) : null);
					
				int split = Math.min(maxCapacity, toDistribute.getAmount());
				copy.setAmount(split);
				toDistribute.shrink(split);
				if (tank != null)
					tank.fill(copy, IFluidHandler.FluidAction.EXECUTE);
			}
			if (cache != null) cache.put(pos, partAt);
		});
		
		if ((controller instanceof IMultiBlockEntityContainer.Inventory inv && inv.hasInventory()) ||
			(controller instanceof IMultiBlockEntityContainer.Fluid fluid && fluid.hasTank()))
			level.invalidateCapabilities(controller.getBlockPos());
	}
	
	@Nullable
	public static <T extends BlockEntity & IMultiBlockEntityContainer> T partAt(
		BlockEntityType<?> type, BlockGetter level, BlockPos pos
	) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be != null && be.getType() == type && !be.isRemoved())
			return checked(be);
		return null;
	}
	
	public static <T extends BlockEntity & IMultiBlockEntityContainer> boolean isConnected(
		BlockGetter level, BlockPos pos, BlockPos other
	) {
		T one = checked(level.getBlockEntity(pos));
		T two = checked(level.getBlockEntity(other));
		if (one == null || two == null)
			return false;
		return one.getController().equals(two.getController());
	}
	
	@Nullable @SuppressWarnings("unchecked")
	private static <T extends BlockEntity & IMultiBlockEntityContainer> T checked(BlockEntity be) {
		return be instanceof IMultiBlockEntityContainer ? (T) be : null;
	}
	
	private static void forEachPosition (BlockPos origin, Direction.Axis axis, int height, int width, Consumer<BlockPos> func) {
		for (int Y = 0; Y < height; Y++) { for (int X = 0; X < width; X++) { for (int Z = 0; Z < width; Z++) {
			BlockPos pos = switch (axis) {
				case X -> origin.offset(Y, X, Z);
				case Y -> origin.offset(X, Y, Z);
				case Z -> origin.offset(X, Z, Y);
			};
			
			func.accept(pos);
		} } }
	}
	
	private static class SearchCache<T extends BlockEntity & IMultiBlockEntityContainer> {
		Map<BlockPos, Optional<T>> controllerMap;
		
		public SearchCache() {
			controllerMap = new HashMap<>();
		}
		
		void put(BlockPos pos, T target) {
			controllerMap.put(pos, Optional.of(target));
		}
		
		void putEmpty(BlockPos pos) {
			controllerMap.put(pos, Optional.empty());
		}
		
		Optional<T> getOrCache(BlockEntityType<?> type, BlockGetter level, BlockPos pos) {
			if (controllerMap.containsKey(pos)) {
				return controllerMap.get(pos);
			}
			
			T partAt = partAt(type, level, pos);
			if (partAt == null) {
				putEmpty(pos);
				return Optional.empty();
			}
			T controller = checked(level.getBlockEntity(partAt.getController()));
			if (controller == null) {
				putEmpty(pos);
				return Optional.empty();
			}
			put(pos, controller);
			return Optional.of(controller);
		}
	}
	
	private record Sizing (int width, int height, int volume) {
		public Sizing(int width, int height) {
			this(width, height, width * width * height);
		}
	}
}