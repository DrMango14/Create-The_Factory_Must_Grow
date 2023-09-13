package com.drmangotea.tfmg.content.machines.oil_processing.distillation.backup;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.*;

public class DistillationControllerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour inputTank;
    protected SmartInventory outputInventory;
    protected SmartFluidTankBehaviour outputTank;

    private boolean contentsChanged;


    private Couple<SmartFluidTankBehaviour> tanks;

    public LazyOptional<IItemHandlerModifiable> itemCapability;
    protected LazyOptional<IFluidHandler> fluidCapability;

    List<Direction> disabledSpoutputs;
    Direction preferredSpoutput;
    protected List<ItemStack> spoutputBuffer;
    protected List<FluidStack> spoutputFluidBuffer;
    int recipeBackupCheck;

    public static final int OUTPUT_ANIMATION_TIME = 10;
    List<IntAttached<ItemStack>> visualizedOutputItems;
    List<IntAttached<FluidStack>> visualizedOutputFluids;

    public DistillationControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        outputInventory = new SmartInventory(9, this).forbidInsertion()
                .withMaxStackSize(64);

        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper( outputInventory));
        contentsChanged = true;





        tanks = Couple.create(inputTank, outputTank);
        visualizedOutputItems = Collections.synchronizedList(new ArrayList<>());
        visualizedOutputFluids = Collections.synchronizedList(new ArrayList<>());
        disabledSpoutputs = new ArrayList<>();
        preferredSpoutput = null;
        spoutputBuffer = new ArrayList<>();
        spoutputFluidBuffer = new ArrayList<>();
        recipeBackupCheck = 20;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));



        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 2, 1000, true)
                .whenFluidUpdates(() -> contentsChanged = true)
                .forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = inputTank.getCapability();
            LazyOptional<? extends IFluidHandler> outputCap = outputTank.getCapability();
            return new CombinedTankWrapper(outputCap.orElse(null), inputCap.orElse(null));
        });
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        outputInventory.deserializeNBT(compound.getCompound("OutputItems"));

        preferredSpoutput = null;
        if (compound.contains("PreferredSpoutput"))
            preferredSpoutput = NBTHelper.readEnum(compound, "PreferredSpoutput", Direction.class);
        disabledSpoutputs.clear();
        ListTag disabledList = compound.getList("DisabledSpoutput", Tag.TAG_STRING);
        disabledList.forEach(d -> disabledSpoutputs.add(Direction.valueOf(((StringTag) d).getAsString())));
        spoutputBuffer = NBTHelper.readItemList(compound.getList("Overflow", Tag.TAG_COMPOUND));
        spoutputFluidBuffer = NBTHelper.readCompoundList(compound.getList("FluidOverflow", Tag.TAG_COMPOUND),
                FluidStack::loadFluidStackFromNBT);

        if (!clientPacket)
            return;

        NBTHelper.iterateCompoundList(compound.getList("VisualizedItems", Tag.TAG_COMPOUND),
                c -> visualizedOutputItems.add(IntAttached.with(OUTPUT_ANIMATION_TIME, ItemStack.of(c))));
        NBTHelper.iterateCompoundList(compound.getList("VisualizedFluids", Tag.TAG_COMPOUND),
                c -> visualizedOutputFluids
                        .add(IntAttached.with(OUTPUT_ANIMATION_TIME, FluidStack.loadFluidStackFromNBT(c))));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("OutputItems", outputInventory.serializeNBT());

        if (preferredSpoutput != null)
            NBTHelper.writeEnum(compound, "PreferredSpoutput", preferredSpoutput);
        ListTag disabledList = new ListTag();
        disabledSpoutputs.forEach(d -> disabledList.add(StringTag.valueOf(d.name())));
        compound.put("DisabledSpoutput", disabledList);
        compound.put("Overflow", NBTHelper.writeItemList(spoutputBuffer));
        compound.put("FluidOverflow",
                NBTHelper.writeCompoundList(spoutputFluidBuffer, fs -> fs.writeToNBT(new CompoundTag())));

        if (!clientPacket)
            return;

        compound.put("VisualizedItems", NBTHelper.writeCompoundList(visualizedOutputItems, ia -> ia.getValue()
                .serializeNBT()));
        compound.put("VisualizedFluids", NBTHelper.writeCompoundList(visualizedOutputFluids, ia -> ia.getValue()
                .writeToNBT(new CompoundTag())));
        visualizedOutputItems.clear();
        visualizedOutputFluids.clear();
    }
/*
    @Override
    public void destroy() {
        super.destroy();

        ItemHelper.dropContents(level, worldPosition, outputInventory);
        spoutputBuffer.forEach(is -> Block.popResource(level, worldPosition, is));
    }

 */

    @Override
    public void remove() {
        super.remove();
        onEmptied();
    }

    public void onEmptied() {
        getOperator().ifPresent(te -> te.basinRemoved = true);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
        fluidCapability.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCapability.cast();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        notifyUpdate();
        if (!level.isClientSide) {
            updateSpoutput();
            if (recipeBackupCheck-- > 0)
                return;
            recipeBackupCheck = 20;

        }




    }





    private void updateSpoutput() {
        BlockState blockState = getBlockState();
        Direction currentFacing = blockState.getValue(DistillationControllerBlock.FACING);
        Direction newFacing = Direction.DOWN;
        for (Direction test : Iterate.horizontalDirections) {
            boolean canOutputTo = DistillationControllerBlock.canOutputTo(level, worldPosition, test);
            if (canOutputTo && !disabledSpoutputs.contains(test))
                newFacing = test;
        }

        if (preferredSpoutput != null && DistillationControllerBlock.canOutputTo(level, worldPosition, preferredSpoutput)
                && preferredSpoutput != Direction.UP)
            newFacing = preferredSpoutput;

        if (newFacing == currentFacing)
            return;

        level.setBlockAndUpdate(worldPosition, blockState.setValue(DistillationControllerBlock.FACING, newFacing));

        if (newFacing.getAxis()
                .isVertical())
            return;

        for (int slot = 0; slot < outputInventory.getSlots(); slot++) {
            ItemStack extractItem = outputInventory.extractItem(slot, 64, true);
            if (extractItem.isEmpty())
                continue;
            if (acceptOutputs(ImmutableList.of(extractItem), Collections.emptyList(), true))
                acceptOutputs(ImmutableList.of(outputInventory.extractItem(slot, 64, false)), Collections.emptyList(),
                        false);
        }

        IFluidHandler handler = outputTank.getCapability()
                .orElse(null);
        for (int slot = 0; slot < handler.getTanks(); slot++) {
            FluidStack fs = handler.getFluidInTank(slot)
                    .copy();
            if (fs.isEmpty())
                continue;
            if (acceptOutputs(Collections.emptyList(), ImmutableList.of(fs), true)) {
                handler.drain(fs, IFluidHandler.FluidAction.EXECUTE);
                acceptOutputs(Collections.emptyList(), ImmutableList.of(fs), false);
            }
        }

        notifyChangeOfContents();
        notifyUpdate();
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) {
            tickVisualizedOutputs();

        }

        if ((!spoutputBuffer.isEmpty() || !spoutputFluidBuffer.isEmpty()) && !level.isClientSide)
            tryClearingSpoutputOverflow();
        if (!contentsChanged)
            return;

        contentsChanged = false;
        getOperator().ifPresent(te -> te.basinChecker.scheduleUpdate());

        for (Direction offset : Iterate.horizontalDirections) {
            BlockPos toUpdate = worldPosition.above()
                    .relative(offset);
            BlockState stateToUpdate = level.getBlockState(toUpdate);
            if (stateToUpdate.getBlock() instanceof DistillationControllerBlock
                    && stateToUpdate.getValue(DistillationControllerBlock.FACING) == offset.getOpposite()) {
                BlockEntity te = level.getBlockEntity(toUpdate);
                if (te instanceof DistillationControllerBlockEntity)
                    ((DistillationControllerBlockEntity) te).contentsChanged = true;
            }
        }
    }

    private void tryClearingSpoutputOverflow() {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof DistillationControllerBlock))
            return;
        Direction direction = blockState.getValue(DistillationControllerBlock.FACING);
        BlockEntity te = level.getBlockEntity(worldPosition.below()
                .relative(direction));

        FilteringBehaviour filter = null;
        InvManipulationBehaviour inserter = null;
        if (te != null) {
            filter = BlockEntityBehaviour.get(level, te.getBlockPos(), FilteringBehaviour.TYPE);
            inserter = BlockEntityBehaviour.get(level, te.getBlockPos(), InvManipulationBehaviour.TYPE);
        }

        IItemHandler targetInv = te == null ? null
                : te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite())
                .orElse(inserter == null ? null : inserter.getInventory());

        IFluidHandler targetTank = te == null ? null
                : te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite())
                .orElse(null);

        boolean update = false;

        for (Iterator<ItemStack> iterator = spoutputBuffer.iterator(); iterator.hasNext();) {
            ItemStack itemStack = iterator.next();

            if (direction == Direction.DOWN) {
                Block.popResource(level, worldPosition, itemStack);
                iterator.remove();
                update = true;
                continue;
            }

            if (targetInv == null)
                break;
            if (!ItemHandlerHelper.insertItemStacked(targetInv, itemStack, true)
                    .isEmpty())
                continue;
          //  if (filter != null && !filter.test(itemStack))
            //    continue;

            update = true;
            ItemHandlerHelper.insertItemStacked(targetInv, itemStack.copy(), false);
            iterator.remove();
            visualizedOutputItems.add(IntAttached.withZero(itemStack));
        }

        for (Iterator<FluidStack> iterator = spoutputFluidBuffer.iterator(); iterator.hasNext();) {
            FluidStack fluidStack = iterator.next();

            if (direction == Direction.DOWN) {
                iterator.remove();
                update = true;
                continue;
            }

            if (targetTank == null)
                break;

            for (boolean simulate : Iterate.trueAndFalse) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                int fill = targetTank instanceof SmartFluidTankBehaviour.InternalFluidHandler
                        ? ((SmartFluidTankBehaviour.InternalFluidHandler) targetTank).forceFill(fluidStack.copy(), action)
                        : targetTank.fill(fluidStack.copy(), action);
                if (fill != fluidStack.getAmount())
                    break;
                if (simulate)
                    continue;

                update = true;
                iterator.remove();
                visualizedOutputFluids.add(IntAttached.withZero(fluidStack));
            }
        }

        if (update) {
            notifyChangeOfContents();
            sendData();
        }
    }

    public float getTotalFluidUnits(float partialTicks) {
        int renderedFluids = 0;
        float totalUnits = 0;

        for (SmartFluidTankBehaviour behaviour : getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.getRenderedFluid()
                        .isEmpty())
                    continue;
                float units = tankSegment.getTotalUnits(partialTicks);
                if (units < 1)
                    continue;
                totalUnits += units;
                renderedFluids++;
            }
        }

        if (renderedFluids == 0)
            return 0;
        if (totalUnits < 1)
            return 0;
        return totalUnits;
    }

    private Optional<FluidProcessingBlockEntity> getOperator() {
        if (level == null)
            return Optional.empty();
        BlockEntity te = level.getBlockEntity(worldPosition.above());
        if (te instanceof FluidProcessingBlockEntity)
            return Optional.of((FluidProcessingBlockEntity) te);
        return Optional.empty();
    }



    public void notifyChangeOfContents() {
        contentsChanged = true;
    }



    public SmartInventory getOutputInventory() {
        return outputInventory;
    }

    public boolean canContinueProcessing() {
        return spoutputBuffer.isEmpty() && spoutputFluidBuffer.isEmpty();
    }

    public boolean acceptOutputs(List<ItemStack> outputItems, List<FluidStack> outputFluids, boolean simulate) {
        outputInventory.allowInsertion();
        outputTank.allowInsertion();
        boolean acceptOutputsInner = acceptOutputsInner(outputItems, outputFluids, simulate);
        outputInventory.forbidInsertion();
        outputTank.forbidInsertion();
        return acceptOutputsInner;
    }

    private boolean acceptOutputsInner(List<ItemStack> outputItems, List<FluidStack> outputFluids, boolean simulate) {
        BlockState blockState = getBlockState();
        if (!(blockState.getBlock() instanceof DistillationControllerBlock))
            return false;

        Direction direction = blockState.getValue(DistillationControllerBlock.FACING);
        if (direction != Direction.DOWN) {

            BlockEntity te = level.getBlockEntity(worldPosition.below()
                    .relative(direction));

            InvManipulationBehaviour inserter =
                    te == null ? null : BlockEntityBehaviour.get(level, te.getBlockPos(), InvManipulationBehaviour.TYPE);
            IItemHandler targetInv = te == null ? null
                    : te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite())
                    .orElse(inserter == null ? null : inserter.getInventory());
            IFluidHandler targetTank = te == null ? null
                    : te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite())
                    .orElse(null);
            boolean externalTankNotPresent = targetTank == null;

            if (!outputItems.isEmpty() && targetInv == null)
                return false;
            if (!outputFluids.isEmpty() && externalTankNotPresent) {
                // Special case - fluid outputs but output only accepts items
                targetTank = outputTank.getCapability()
                        .orElse(null);
                if (targetTank == null)
                    return false;
                if (!acceptFluidOutputsIntoBasin(outputFluids, simulate, targetTank))
                    return false;
            }

            if (simulate)
                return true;
            for (ItemStack itemStack : outputItems) {
                if (itemStack.hasCraftingRemainingItem() && itemStack.getCraftingRemainingItem()
                        .sameItem(itemStack))
                    continue;
                spoutputBuffer.add(itemStack.copy());
            }
            if (!externalTankNotPresent)
                for (FluidStack fluidStack : outputFluids)
                    spoutputFluidBuffer.add(fluidStack.copy());
            return true;
        }

        IItemHandler targetInv = outputInventory;
        IFluidHandler targetTank = outputTank.getCapability()
                .orElse(null);

        if (targetInv == null && !outputItems.isEmpty())
            return false;
        if (!acceptItemOutputsIntoBasin(outputItems, simulate, targetInv))
            return false;
        if (outputFluids.isEmpty())
            return true;
        if (targetTank == null)
            return false;
        if (!acceptFluidOutputsIntoBasin(outputFluids, simulate, targetTank))
            return false;

        return true;
    }

    private boolean acceptFluidOutputsIntoBasin(List<FluidStack> outputFluids, boolean simulate,
                                                IFluidHandler targetTank) {
        for (FluidStack fluidStack : outputFluids) {
            IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
            int fill = targetTank instanceof SmartFluidTankBehaviour.InternalFluidHandler
                    ? ((SmartFluidTankBehaviour.InternalFluidHandler) targetTank).forceFill(fluidStack.copy(), action)
                    : targetTank.fill(fluidStack.copy(), action);
            if (fill != fluidStack.getAmount())
                return false;
        }
        return true;
    }

    private boolean acceptItemOutputsIntoBasin(List<ItemStack> outputItems, boolean simulate, IItemHandler targetInv) {
        for (ItemStack itemStack : outputItems) {
            // Catalyst items are never consumed
            if (itemStack.hasCraftingRemainingItem() && itemStack.getCraftingRemainingItem()
                    .sameItem(itemStack))
                continue;
            if (!ItemHandlerHelper.insertItemStacked(targetInv, itemStack.copy(), simulate)
                    .isEmpty())
                return false;
        }
        return true;
    }

    public void readOnlyItems(CompoundTag compound) {

        outputInventory.deserializeNBT(compound.getCompound("OutputItems"));
    }

    public static BlazeBurnerBlock.HeatLevel getHeatLevelOf(BlockState state) {
        if (state.hasProperty(BlazeBurnerBlock.HEAT_LEVEL))
            return state.getValue(BlazeBurnerBlock.HEAT_LEVEL);
        return AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.matches(state) ? BlazeBurnerBlock.HeatLevel.SMOULDERING : BlazeBurnerBlock.HeatLevel.NONE;
    }

    public Couple<SmartFluidTankBehaviour> getTanks() {
        return tanks;
    }



    // client things

    private void tickVisualizedOutputs() {
        visualizedOutputFluids.forEach(IntAttached::decrement);
        visualizedOutputItems.forEach(IntAttached::decrement);
        visualizedOutputFluids.removeIf(IntAttached::isOrBelowZero);
        visualizedOutputItems.removeIf(IntAttached::isOrBelowZero);
    }



    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking,
                getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY));
    }

    class BasinValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 12, 15.75);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis()
                    .isHorizontal();
        }

    }
}
