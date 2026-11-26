package com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.base.fluid.InputOutputTankWrapper;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.recipes.HotBlastRecipe;
import com.drmangotea.tfmg.recipes.input.HotBlastRecipeInput;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.neoforged.neoforge.fluids.FluidStack.isSameFluidSameComponents;

public class BlastStoveBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IMultiBlockEntityContainer.Fluid {
    private static final int MAX_SIZE = 2;
	
	protected IFluidHandler
		primaryCapability,
		secondaryCapability,
		combinedCapability;
	protected ForceableFluidTank
		primaryOutputTank,
		exhaustOutputTank,
		AirInputTank,
		fuelInputTank;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    public boolean updateConnectivity;
    protected boolean updateCapability;
    private static final int SYNC_RATE = 4;
    protected int syncCooldown;
    protected boolean queuedSync;
	protected int height = 1, width = 1;

    private final RecipeManager.CachedCheck<HotBlastRecipeInput, HotBlastRecipe> quickCheck;
    protected int recipeDuration;
    int timer = 0;
    public HotBlastRecipe currentRecipe;

    public BlastStoveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
		int capacity = getCapacityMultiplier();
        primaryOutputTank = new ForceableFluidTank(capacity, this::onFluidStackChanged).blockInsertion(); //output (hot air)
        exhaustOutputTank = new ForceableFluidTank(capacity, this::onFluidStackChanged).blockInsertion();
        AirInputTank = new ForceableFluidTank(capacity, this::onFluidStackChanged);
        fuelInputTank = new ForceableFluidTank(capacity, this::onFluidStackChanged);
        primaryCapability = new InputOutputTankWrapper(primaryOutputTank, fuelInputTank);
        secondaryCapability = new InputOutputTankWrapper(exhaustOutputTank, AirInputTank);
		combinedCapability = new CombinedTankWrapper(primaryCapability, secondaryCapability);
		updateConnectivity = false;
        this.quickCheck = RecipeManager.createCheck(TFMGRecipeTypes.HOT_BLAST.getType());
        updateCapability = false;
        refreshCapability();
    }

    public void updateConnectivity() {
        updateConnectivity = false;
        if (!isController() || level == null)
            return;

        if (level.isClientSide)
            return;

        ConnectivityHandler.formMulti(this);
        currentRecipe = findRecipe();
    }

    @Override
	public void tick() {
        super.tick();
		if (level == null) return;
        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
        manageRecipe();

        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition)) {
            onPositionChanged();
            return;
        }
		
        if (updateConnectivity)
            updateConnectivity();
    }

    public void manageRecipe() {
        if (level == null || !this.isController() || currentRecipe == null || level.isClientSide && !isVirtual())
            return;
        if (timer >= currentRecipe.getProcessingDuration()) {
            HotBlastRecipe activeRecipe = currentRecipe;
            if ((primaryOutputTank.isEmpty() || isSameFluidSameComponents(primaryOutputTank.getFluid(), activeRecipe.getPrimaryResult())) && (exhaustOutputTank.isEmpty() || isSameFluidSameComponents(exhaustOutputTank.getFluid(), activeRecipe.getSecondaryResult()))  &&
                    primaryOutputTank.getSpace() >= activeRecipe.getPrimaryResult().getAmount() && exhaustOutputTank.getSpace() >= activeRecipe.getSecondaryResult().getAmount()) {
                AirInputTank.forceDrain(activeRecipe.getPrimaryIngredient().amount(), IFluidHandler.FluidAction.EXECUTE);
                fuelInputTank.forceDrain(activeRecipe.getSecondaryIngredient().amount(), IFluidHandler.FluidAction.EXECUTE);
                primaryOutputTank.forceFill(activeRecipe.getPrimaryResult(), IFluidHandler.FluidAction.EXECUTE);
                exhaustOutputTank.forceFill(activeRecipe.getSecondaryResult(), IFluidHandler.FluidAction.EXECUTE);
                currentRecipe = null;
                timer = 0;
                recipeDuration = -1;
            }
        } else {
            timer+= getTotalTankSize();
            sendData();
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (currentRecipe == null) {
            currentRecipe = findRecipe();
        }
        updateConnectivity = true;
    }
	
	public int getTotalTankSize() {
		return width * width * height;
	}

	//TODO: Rework heat system
    public int getSpeed () {
        //return 20 / getTotalTankSize(); //max size is 2x2x5
		return (int) (1000f / (getTotalTankSize() * 3));
    }

    public @Nullable HotBlastRecipe findRecipe() {
        if (level == null)
            return null;
        RecipeHolder<HotBlastRecipe> recipeholder;
        if (!AirInputTank.isEmpty() && !fuelInputTank.isEmpty()) {
            recipeholder = quickCheck.getRecipeFor(new HotBlastRecipeInput(AirInputTank.getFluid(), fuelInputTank.getFluid()), level).orElse(null);
        } else {
            return null;
        }
        if(recipeholder == null) {
            return null;
        }
        recipeDuration = recipeholder.value().getProcessingDuration();
        return recipeholder.value();
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public boolean isController() {
        return controller == null || worldPosition.equals(controller);
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level != null && level.isClientSide)
            invalidateRenderBoundingBox();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (level == null || level.isClientSide) return;
		
		setChanged();
		sendData();
	}

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    @Override
    public void destroy() {
        getControllerBE().updateCapability = true;
        super.destroy();
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlastStoveBlockEntity getControllerBE() {
        if (isController())
            return this;
        if (level != null && level.getBlockEntity(controller) instanceof BlastStoveBlockEntity be)
            return be;
        return null;
    }

	//TODO: This alone isn't enough to get things working properly
	// Blast Stoves, having four fluid tanks, might require their own custom Connectivity Handler.
    public void applyFluidTankSize(int blocks) {
		int capacity = getCapacityMultiplier() * blocks;
		
		primaryOutputTank.withCapacity(capacity);
		exhaustOutputTank.withCapacity(capacity);
		AirInputTank.withCapacity(capacity);
		fuelInputTank.withCapacity(capacity);
    }

    public void removeController(boolean keepFluids) {
        if (level == null || level.isClientSide)
            return;
        updateConnectivity = true;
        if (!keepFluids)
            applyFluidTankSize(1);
        controller = null;
        width = 1;
        height = 1;

        onFluidStackChanged(primaryOutputTank.getFluid());

        refreshCapability();
        setChanged();
        sendData();
    }

    public void sendDataImmediately() {
        syncCooldown = 0;
        queuedSync = false;
        sendData();
    }

    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }


    @Override
    public void setController(BlockPos controller) {
        if (level == null || level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        sendData();
    }

    public void refreshCapability() {
		BlastStoveBlockEntity controller = getControllerBE();
		if (isController() || controller == null) {
			primaryCapability = new InputOutputTankWrapper(primaryOutputTank, fuelInputTank);
			secondaryCapability = new InputOutputTankWrapper(exhaustOutputTank, AirInputTank);
			combinedCapability = new CombinedTankWrapper(primaryCapability, secondaryCapability);
		} else {
			controller.updateCapability = true; //controller will refresh next tick
			primaryCapability = null;
			secondaryCapability = null;
			combinedCapability = null;
		}
        invalidateCapabilities();
    }

    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        if (isController())
            return super.createRenderBoundingBox().expandTowards(width - 1, height - 1, width - 1);
        else
            return super.createRenderBoundingBox();
    }

    @Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		BlastStoveBlockEntity controller = getControllerBE();
        if (controller == null) { return false; }
        if (!isController()) {
            return controller.addToGoggleTooltip(tooltip, isPlayerSneaking);
        }
		
		IFluidHandler pri = controller.primaryCapability;
		IFluidHandler sec = controller.secondaryCapability;
		
		int capacity = getCapacityMultiplier() * controller.getTotalTankSize();

        TFMGTexts.header("blast_stove").forGoggles(tooltip);
        TFMGTexts.progress(timer, recipeDuration).forGoggles(tooltip, 1);
        tankTooltip(tooltip, "goggles.blast_stove.tank1", sec.getFluidInTank(1), capacity, ChatFormatting.DARK_GREEN); //input (air)
        tankTooltip(tooltip, "goggles.blast_stove.tank2", pri.getFluidInTank(1), capacity, ChatFormatting.DARK_GREEN); //fuel
        tankTooltip(tooltip, "goggles.blast_stove.tank3", pri.getFluidInTank(0), capacity, ChatFormatting.GOLD);       //output (hot air)
        tankTooltip(tooltip, "goggles.blast_stove.tank4", sec.getFluidInTank(0), capacity, ChatFormatting.GOLD);       //output (exhaust)
        return true;
    }
	
	private void tankTooltip (List<Component> tooltip, String key, FluidStack fluid, int capacity, ChatFormatting color) {
		TFMGLang.builder().add(TFMGLang.translate(key))
			.add(fluid.getFluid() == Fluids.EMPTY
				? TFMGLang.text("")
				:  TFMGLang.text(" "+fluid.getHoverName().getString())
			).style(color).forGoggles(tooltip, 1);
		TFMGUtils.fluidOutOfCapacity(fluid.getAmount(), ChatFormatting.GRAY, capacity).forGoggles(tooltip, 2);
	}

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        BlockPos controllerBefore = controller;
        int prevSize = width;
        int prevHeight = height;

        updateConnectivity = compound.contains("Uninitialized");
        lastKnownPos = NbtUtils.readBlockPos(compound, "LastKnownPos").orElse(null);
        controller = NbtUtils.readBlockPos(compound, "Controller").orElse(null);

        if (isController()) {
            width = compound.getInt("Size");
            height = compound.getInt("Height");
            applyFluidTankSize(width * width * height); //set capacity before fluids are filled
            primaryOutputTank.read(registries, compound.getCompound("primaryOutputInventory"));
            AirInputTank.read(registries, compound.getCompound("primaryInputInventory"));
            exhaustOutputTank.read(registries, compound.getCompound("secondaryOutputInventory"));
            fuelInputTank.read(registries, compound.getCompound("secondaryInputInventory"));
            timer = compound.getInt("Timer");
            recipeDuration = compound.getInt("RecipeDuration");
            updateCapability = true;
        }
        if (!clientPacket) return;

        boolean changeOfController = !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (level != null)
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            invalidateRenderBoundingBox();
        }
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.put("primaryOutputInventory", primaryOutputTank.writeToNBT(registries, new CompoundTag()));
            compound.put("primaryInputInventory", AirInputTank.writeToNBT(registries, new CompoundTag()));
            compound.put("secondaryOutputInventory", exhaustOutputTank.writeToNBT(registries, new CompoundTag()));
            compound.put("secondaryInputInventory", fuelInputTank.writeToNBT(registries, new CompoundTag()));
            compound.putInt("Size", width);
            compound.putInt("Height", height);
            compound.putInt("Timer", timer);
            compound.putInt("RecipeDuration", recipeDuration);
        }

        forEachBehaviour(tb -> tb.write(compound, registries, clientPacket));
        if (!clientPacket)
            return;
        if (queuedSync)
            compound.putBoolean("LazySync", true);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, TFMGBlockEntities.BLAST_STOVE.get(),
			(be, dir) -> {
				if (be.getControllerBE() instanceof BlastStoveBlockEntity controller)
					be = controller;

                if (be.primaryCapability == null || be.secondaryCapability == null || be.combinedCapability == null)
                    be.refreshCapability();
				
				if (dir == null)
					return be.combinedCapability;
				if (dir.getAxis().isVertical())
					return be.primaryCapability;
                if (be.getController().getY() == be.getBlockPos().getY())
                    return be.secondaryCapability;
				
				return null;
			}
        );
    }
	
	@Override
	public int getHeight() { return height; }
	
	@Override
	public void setHeight(int height) { this.height = height; }
	
	@Override
	public int getWidth() { return width; }
	
	@Override
	public void setWidth(int width) { this.width = width; }
	
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) { }

    public static int getCapacityMultiplier() {
		//TODO: should this have its own config?
		return AllConfigs.server().fluids.fluidTankCapacity.get() * 1000;
	}

    public static int getMaxHeight() {
        return 5;
    }

    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        onFluidStackChanged(primaryOutputTank.getFluid());
        setChanged();
        updateConnectivity = true;
		
		if (isController()) {
			applyFluidTankSize(width * width * height);
		}

        sendData();
        setChanged();
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y)
            return getMaxHeight();
        return getMaxWidth();
    }
	
	@Override
	public int getMaxWidth() { return MAX_SIZE; }
}
