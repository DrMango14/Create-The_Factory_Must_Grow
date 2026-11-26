package com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.recipes.CokingRecipe;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static net.neoforged.neoforge.fluids.FluidStack.isSameFluidSameComponents;

public class CokeOvenBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, Clearable {

    public SmartInventory inventory;
    public ForceableFluidTank
		primaryTank,
		secondaryTank;
	public IFluidHandler fluidCapability;
    public LerpedFloat doorAngle = LerpedFloat.angular();
    public boolean createNextTick;
    public BlockPos controller = getBlockPos();
    public int size = 1;
    public boolean forceOpen = false;

    protected boolean updateCapability;

    private final RecipeManager.CachedCheck<RecipeWrapper, CokingRecipe> quickCheck;
    protected int recipeDuration;
    int timer = 0;
    public CokingRecipe currentRecipe;

    public CokeOvenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        inventory = new SmartInventory(1, this)
                .withMaxStackSize(64)
                .whenContentsChanged(i->this.onContentsChanged());
        primaryTank = new ForceableFluidTank(8000, this::onFluidChanged).blockInsertion();
        secondaryTank = new ForceableFluidTank(8000, this::onFluidChanged).blockInsertion();
        createNextTick = true;
        this.quickCheck = RecipeManager.createCheck(TFMGRecipeTypes.COKING.getType());
        updateCapability = false;
        refreshCapability();
    }

    public void onContentsChanged() {
        sendData();
        setChanged();
    }

    public @Nullable CokingRecipe findRecipe() {
        if (level == null)
            return null;
        RecipeHolder<CokingRecipe> recipeholder;
        if (!inventory.isEmpty()) {
            recipeholder = quickCheck.getRecipeFor(new RecipeWrapper(inventory), level).orElse(null);
        } else {
            return null;
        }
        if(recipeholder == null) {
            return null;
        }
        recipeDuration = recipeholder.value().getProcessingDuration();
        return recipeholder.value();
    }

    public void manageRecipe() {
        if (level == null || !this.isController() || currentRecipe == null || level.isClientSide && !isVirtual())
            return;
        CokingRecipe activeRecipe = currentRecipe;
        if (timer >= currentRecipe.getProcessingDuration()) {
            inventory.getItem(0).shrink(activeRecipe.getIngredients().getFirst().getItems()[0].getCount());

            // Drop output (Maybe separate this?)
            Direction direction = getBlockState().getValue(FACING);
            Vec3 dropVec = VecHelper.getCenterOf(worldPosition.relative(direction)).add(0,0.4,0);
            ItemEntity dropped = new ItemEntity(level, dropVec.x, dropVec.y, dropVec.z, activeRecipe.getResultItem(level.registryAccess()).copy());
            dropped.setDefaultPickUpDelay();
            dropped.setDeltaMovement(direction.getAxis() == Direction.Axis.X ? direction == Direction.WEST ? -.01f : .01f : 0, 0.05f, direction.getAxis() == Direction.Axis.Z ? direction == Direction.NORTH ? -.01f : .01f : 0);
            level.addFreshEntity(dropped);

            currentRecipe = null;
            timer = 0;
            recipeDuration = -1;

            onContentsChanged();
        } else {
            FluidStack primary = activeRecipe.getPrimaryResult().copyWithAmount(activeRecipe.getPrimaryResult().getAmount() * size);
            FluidStack secondary = activeRecipe.getSecondaryResult().copyWithAmount(activeRecipe.getSecondaryResult().getAmount() * size);
            if ((primaryTank.isEmpty() || isSameFluidSameComponents(primaryTank.getFluid(), primary)) && (secondaryTank.isEmpty() || isSameFluidSameComponents(secondaryTank.getFluid(), secondary))  &&
                    primaryTank.getSpace() >= primary.getAmount() && secondaryTank.getSpace() >= secondary.getAmount()) {
                primaryTank.forceFill(primary, IFluidHandler.FluidAction.EXECUTE);
                secondaryTank.forceFill(secondary, IFluidHandler.FluidAction.EXECUTE);
                timer += size;
                sendData();
            }
        }
    }

    private void onFluidChanged(FluidStack stack) {
        if (level == null)
            return;
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
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

        CokeOvenBlockEntity controllerOven = getController();
		if (level.isClientSide) {
			boolean timeCheck = controllerOven.timer < controllerOven.recipeDuration && controllerOven.timer > (controllerOven.recipeDuration * 0.95);
			doorAngle.chase(timeCheck || forceOpen ? 90 : 0, 0.1f, LerpedFloat.Chaser.EXP);
			doorAngle.tickChaser();
			if (!forceOpen)
				manageDoors(timeCheck);
		}
		
		if (createNextTick) {
            createMultiblock();
            createNextTick = false;
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (currentRecipe == null) {
            currentRecipe = findRecipe();
        }
        onContentsChanged();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        CokeOvenBlockEntity controller = getController();
        if (!isController()) {
            return controller.addToGoggleTooltip(tooltip, isPlayerSneaking);
        }

        TFMGTexts.header("coke_oven")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        TFMGTexts.progress(timer, recipeDuration).forGoggles(tooltip, 1);
		TFMGUtils.createFluidTooltip(tooltip, true, secondaryTank, primaryTank);
		TFMGUtils.createItemTooltip(tooltip, inventory);
        return true;
    }

    public void manageDoors(boolean open) {
        if(level == null)
            return;
        for(int i =0; i< size;i++){
            BlockPos pos = getBlockPos().above(i);
            if(level.getBlockEntity(pos) instanceof CokeOvenBlockEntity be && pos != getBlockPos()){
                be.forceOpen = open;
            }
        }
    }

    public boolean isController(){
        return controller == getBlockPos();
    }

    public void createMultiblock() {
		if(level == null)
			return;
		int maxSize = TFMGConfigs.common().machines.cokeOvenMaxSize.get();
		Direction facing = getBlockState().getValue(FACING);
		if(isOven(worldPosition.relative(facing)) || isOven(worldPosition.below()))
			return;
		
		//reset existing multiblocks
		for (BlockPos pos : getRangeAbove(worldPosition, facing, this.size-1)) {
			if (level.getBlockEntity(pos) instanceof CokeOvenBlockEntity be && be.controller!=be.getBlockPos()) {
				be.controller = be.getBlockPos();
				be.refreshCapability();
				be.forceOpen = false;
				be.doorAngle.setValue(0);
				setControllerState(be.getBlockPos(), CokeOvenBlock.ControllerType.CASUAL);
			}
		}
		
		//get new size
		int size = 1;
		SizeLoop:
		for (int i = 1; i<=maxSize; i++) {
			for(BlockPos pos : getRangeAbove(worldPosition, facing, i)) {
				if (!isOven(pos))
					break SizeLoop;
				if (level.getBlockState(pos).getValue(FACING) != facing)
					break SizeLoop;
			}
			size++;
		}
		
		//apply size
		for(BlockPos pos : getRangeAbove(worldPosition, facing, size-1)) {
			if(level.getBlockEntity(pos) instanceof CokeOvenBlockEntity be) {
				be.controller = worldPosition;
				be.refreshCapability();
			}
		}
		
		//set door blockstates
		if (size > 1) {
			setControllerState(worldPosition, CokeOvenBlock.ControllerType.BOTTOM_ON);
			setControllerState(worldPosition.above(size - 1), CokeOvenBlock.ControllerType.TOP_ON);
			for(int i = 1; i < size - 1; i++) {
				setControllerState(worldPosition.above(i), CokeOvenBlock.ControllerType.MIDDLE_ON);
			}
		} else setControllerState(worldPosition, CokeOvenBlock.ControllerType.CASUAL);
		
		this.size = size;
    }
	
	boolean isOven(BlockPos pos) {
		return level != null && level.getBlockState(pos).is(TFMGBlocks.COKE_OVEN.get());
	}
	
	Iterable<BlockPos> getRangeAbove(BlockPos pos, Direction facing, int size) {
		return BlockPos.betweenClosed(pos, pos.above(size).relative(facing.getOpposite(),size));
	}
	
	Iterable<BlockPos> getRangeBelow(BlockPos pos, Direction facing, int size) {
		return BlockPos.betweenClosed(pos, pos.below(size).relative(facing.getOpposite(),size));
	}
	
	void setControllerState(BlockPos pos, CokeOvenBlock.ControllerType type) {
		if (level == null) return;
		BlockState state = getBlockState().setValue(CokeOvenBlock.CONTROLLER_TYPE, type);
		level.setBlock(pos, state, 2);
	}
	
    public void onPlaced() {
        createNextTick = true;
        updateOvenBlocks();
        if (level instanceof ServerLevel serverLevel)
           CatnipServices.NETWORK.sendToClientsTrackingChunk(serverLevel, new ChunkPos(getBlockPos()), new CokeOvenPacket(getBlockPos()));
    }

    @Override
    public void remove() {
        super.remove();
        updateOvenBlocks();
    }

    @Override
    public void destroy() {
        super.destroy();
        if(isController())
            ItemHelper.dropContents(level, worldPosition, inventory);
    }

    public void updateOvenBlocks(){
        if (level == null) return;
        int maxSize = TFMGConfigs.common().machines.cokeOvenMaxSize.get();
        Direction facing = getBlockState().getValue(FACING);
        for(BlockPos pos : getRangeBelow(getBlockPos(), facing, maxSize)) {
            if (level.getBlockEntity(pos) instanceof CokeOvenBlockEntity be) {
                be.createMultiblock();
            }
        }
    }

    @Nonnull
	public CokeOvenBlockEntity getController() {
        CokeOvenBlockEntity cokeOven;
        if (level != null && level.getBlockEntity(controller) instanceof CokeOvenBlockEntity controllerOven) {
            cokeOven = controllerOven;
        } else {
            controller = getBlockPos();
            cokeOven = this;
        }
        return cokeOven;
    }

    private void refreshCapability() {
        if(level == null)
            return;
		fluidCapability = new CombinedTankWrapper(primaryTank, secondaryTank);
        invalidateCapabilities();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
			Capabilities.FluidHandler.BLOCK,
			TFMGBlockEntities.COKE_OVEN.get(),
			(be, dir) -> {
				be = be.getController();
				
				if (dir == null) return be.fluidCapability;
				if (dir == Direction.UP)
					return be.secondaryTank;
				return be.primaryTank;
			}
        );
        event.registerBlockEntity(
			Capabilities.ItemHandler.BLOCK,
			TFMGBlockEntities.COKE_OVEN.get(),
			(be, dir) -> {
				be = be.getController();
				return be.inventory;
			}
        );
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));

        if (isController()) {
            compound.put("Inventory", inventory.serializeNBT(registries));
            compound.put("PrimaryTankContent", primaryTank.writeToNBT(registries, new CompoundTag()));
            compound.put("SecondaryTankContent", secondaryTank.writeToNBT(registries, new CompoundTag()));
            compound.putInt("Timer", timer);
            compound.putInt("RecipeDuration", recipeDuration);
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        if (compound.contains("Controller", Tag.TAG_COMPOUND)) {
            controller = NbtUtils.readBlockPos(compound, "Controller").orElseThrow();
        } else if (compound.contains("Controller", Tag.TAG_LONG)) {
            controller = BlockPos.of(compound.getLong("Controller"));
        }

        if (isController()) {
            inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
            primaryTank.readFromNBT(registries, compound.getCompound("PrimaryTankContent"));
            secondaryTank.readFromNBT(registries, compound.getCompound("SecondaryTankContent"));
            timer = compound.getInt("Timer");
            recipeDuration = compound.getInt("RecipeDuration");

            updateCapability = true;
        }
    }


    @Override
    public void clearContent() {
        this.inventory.clearContent();
    }
}
