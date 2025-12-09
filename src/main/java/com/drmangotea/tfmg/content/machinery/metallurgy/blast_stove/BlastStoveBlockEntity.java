package com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove;


import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.recipes.HotBlastRecipe;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;


public class BlastStoveBlockEntity extends FluidTankBlockEntity implements IHaveGoggleInformation, IMultiBlockEntityContainer.Fluid {

    private static final int MAX_SIZE = 2;

    protected LazyOptional<IFluidHandler> primaryCapability;
    protected LazyOptional<IFluidHandler> secondaryCapability;
    public FluidTank primaryOutputInventory;
    public FluidTank secondaryOutputInventory;
    public FluidTank primaryInputInventory;
    public FluidTank secondaryInputInventory;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    public boolean updateConnectivity;
    public int width;
    public int height;
    private static final Object HotBlastRecipesKey = new Object();
    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;
    public int timer = 0;
    public BlastStoveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        primaryOutputInventory = TFMGUtils.createTank(8000, true, false, this::onFluidStackChanged);
        secondaryOutputInventory = TFMGUtils.createTank(8000, true, false, this::onFluidStackChanged);
        primaryInputInventory = TFMGUtils.createTank(8000, false, this::onFluidStackChanged);
        secondaryInputInventory = TFMGUtils.createTank(8000, false, this::onFluidStackChanged);
        primaryCapability = LazyOptional.of(()->new CombinedTankWrapper(primaryOutputInventory,secondaryInputInventory));
        secondaryCapability = LazyOptional.of(()->new CombinedTankWrapper(primaryInputInventory, secondaryOutputInventory));
        updateConnectivity = false;
        height = 1;
        width = 1;
        refreshCapability();
    }

    public void updateBoilerState() {}
    public void updateConnectivity() {
        updateConnectivity = false;
        if (!isController())
            return;

        for (int yOffset = 0; yOffset < height; yOffset++)
            for (int xOffset = 0; xOffset < width; xOffset++)
                for (int zOffset = 0; zOffset < width; zOffset++)
                    if (level.getBlockEntity(
                            worldPosition.offset(xOffset, yOffset, zOffset)) instanceof BlastStoveBlockEntity fbe)
                        fbe.refreshCapability();



        if (level.isClientSide)
            return;
        refreshCapability();

        ConnectivityHandler.formMulti(this);
    }



    @Override
    public void tick() {
        super.tick();



        if(isController()&&!primaryInputInventory.isEmpty()&&!secondaryInputInventory.isEmpty()&&primaryOutputInventory.getSpace()!=0&&secondaryOutputInventory.getSpace()!=0){
            HotBlastRecipe recipe = getMatchingRecipes();
            if(recipe!=null){
                if(timer>=getSpeedModifier()/(getTotalTankSize()*0.3f)) {
                    if ((primaryOutputInventory.isEmpty() || primaryOutputInventory.getFluid().isFluidEqual(recipe.getPrimaryResult())) && (secondaryOutputInventory.isEmpty() || secondaryOutputInventory.getFluid().isFluidEqual(recipe.getSecondaryResult()))) {


                        primaryInputInventory.setFluid(new FluidStack(primaryInputInventory.getFluid().copy(), primaryInputInventory.getFluidAmount()-recipe.getPrimaryIngredient().getRequiredAmount()));
                        secondaryInputInventory.setFluid(new FluidStack(secondaryInputInventory.getFluid().copy(), secondaryInputInventory.getFluidAmount()-recipe.getSecondaryIngredient().getRequiredAmount()));



                        primaryOutputInventory.setFluid(new FluidStack(recipe.getPrimaryResult(), primaryOutputInventory.getFluidAmount()+recipe.getPrimaryResult().getAmount()));
                        secondaryOutputInventory.setFluid(new FluidStack(recipe.getSecondaryResult(), secondaryOutputInventory.getFluidAmount()+recipe.getSecondaryResult().getAmount()));
                    }
                }else {
                    timer++;
                }

            }
        }


        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }

        if (updateConnectivity)
            updateConnectivity();

    }

    public int getSpeedModifier(){
        return 100;
    }


    protected Object getRecipeCacheKey() {
        return HotBlastRecipesKey;
    }
    protected HotBlastRecipe getMatchingRecipes() {

        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, r->r instanceof HotBlastRecipe);

        for(int i = 0; i < list.toArray().length;i++){
            HotBlastRecipe recipe = (HotBlastRecipe) list.get(i);
            if(recipe.getPrimaryIngredient().test(primaryInputInventory.getFluid())&&recipe.getSecondaryIngredient().test(secondaryInputInventory.getFluid()))
                            return recipe;
        }

        return null;
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public boolean isController() {
        return controller == null || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level.isClientSide)
            invalidateRenderBoundingBox();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }

    }


    @SuppressWarnings("unchecked")
    @Override
    public BlastStoveBlockEntity getControllerBE() {
        if (isController())
            return this;
        BlockEntity tileEntity = level.getBlockEntity(controller);
        if (tileEntity instanceof BlastStoveBlockEntity)
            return (BlastStoveBlockEntity) tileEntity;
        return null;
    }

    public void applyFluidTankSize(int blocks) {

    }

    public void removeController(boolean keepFluids) {
        if (level.isClientSide)
            return;
        updateConnectivity = true;
        if (!keepFluids)
            applyFluidTankSize(1);
        controller = null;
        width = 1;
        height = 1;

        onFluidStackChanged(primaryOutputInventory.getFluid());

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

        if (level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        refreshCapability();
        setChanged();
        sendData();
    }
    public void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = primaryCapability;
        LazyOptional<IFluidHandler> oldSecondaryCap = secondaryCapability;
        primaryCapability = LazyOptional.of(() -> handlerForCapability());
        secondaryCapability = LazyOptional.of(() -> handlerForSecondaryCapability());
        oldCap.invalidate();
        oldSecondaryCap.invalidate();

    }


    private IFluidHandler handlerForCapability() {
        return isController() ?
                new CombinedTankWrapper(primaryOutputInventory, secondaryInputInventory)
                : getControllerBE() != null ? getControllerBE().handlerForCapability() : new CombinedTankWrapper(primaryOutputInventory, secondaryInputInventory);
    }
    private IFluidHandler handlerForSecondaryCapability() {
        return isController() ?
                new CombinedTankWrapper(primaryInputInventory, secondaryOutputInventory)
                : getControllerBE() != null ? getControllerBE().handlerForSecondaryCapability() : new CombinedTankWrapper(primaryInputInventory, secondaryOutputInventory);
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
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        BlockPos controllerBefore = controller;
        int prevSize = width;
        int prevHeight = height;
        int prevLum = luminosity;

        updateConnectivity = compound.contains("Uninitialized");
        controller = null;
        lastKnownPos = null;


        if (compound.contains("LastKnownPos"))
            lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));
        if (compound.contains("Controller"))
            controller = NbtUtils.readBlockPos(compound.getCompound("Controller"));

        if (isController()) {
            width = compound.getInt("Size");
            height = compound.getInt("Height");
            primaryOutputInventory.readFromNBT(compound.getCompound("primaryOutputInventory"));
            primaryInputInventory.readFromNBT(compound.getCompound("primaryInputInventory"));
            secondaryOutputInventory.readFromNBT(compound.getCompound("secondaryOutputInventory"));
            secondaryInputInventory.readFromNBT(compound.getCompound("secondaryInputInventory"));
            if (primaryOutputInventory.getSpace() < 0)
                primaryOutputInventory.drain(-primaryOutputInventory.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }



        if (!clientPacket)
            return;

        boolean changeOfController =
                controllerBefore == null ? controller != null : !controllerBefore.equals(controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (hasLevel())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            invalidateRenderBoundingBox();
        }

    }

    public float getFillState() {
        return (float) primaryOutputInventory.getFluidAmount() / primaryOutputInventory.getCapacity();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");

        CreateLang.translate("goggles.blast_stove.header")
                .forGoggles(tooltip);
        CreateLang.builder()
                .add(CreateLang.translate("goggles.blast_stove.tank1"))
                .add(CreateLang.number(getControllerBE().secondaryCapability.orElseGet(null).getFluidInTank(0).getAmount())
                        .add(mb)
                        .add(getControllerBE().secondaryCapability.orElseGet(null).getFluidInTank(0).getFluid() == Fluids.EMPTY ? CreateLang.text("") :  CreateLang.text(" "+getControllerBE().secondaryCapability.orElseGet(null).getFluidInTank(0).getDisplayName().getString()))
                        .style(ChatFormatting.DARK_GREEN))
                .text(ChatFormatting.GRAY, " / ")
                .add(CreateLang.number(8000)
                        .add(mb)
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        CreateLang.builder()
                .add(CreateLang.translate("goggles.blast_stove.tank2"))
                .add(CreateLang.number(getControllerBE().primaryCapability.orElseGet(null).getFluidInTank(1).getAmount())
                        .add(mb)
                        .add(getControllerBE().primaryCapability.orElseGet(null).getFluidInTank(1).getFluid() == Fluids.EMPTY ? CreateLang.text("") :  CreateLang.text(" "+getControllerBE().primaryCapability.orElseGet(null).getFluidInTank(1).getDisplayName().getString()))
                        .style(ChatFormatting.DARK_GREEN))
                .text(ChatFormatting.GRAY, " / ")
                .add(CreateLang.number(8000)
                        .add(mb)
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        CreateLang.builder()
                .add(CreateLang.translate("goggles.blast_stove.tank3"))
                .add(CreateLang.number(getControllerBE().primaryCapability.orElseGet(null).getFluidInTank(0).getAmount())
                        .add(mb)
                        .add(getControllerBE().primaryCapability.orElseGet(null).getFluidInTank(0).getFluid() == Fluids.EMPTY ? CreateLang.text("") :  CreateLang.text(" "+getControllerBE().primaryCapability.orElseGet(null).getFluidInTank(0).getDisplayName().getString()))
                        .style(ChatFormatting.YELLOW))
                .text(ChatFormatting.GRAY, " / ")
                .add(CreateLang.number(8000)
                        .add(mb)
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        CreateLang.builder()
                .add(CreateLang.translate("goggles.blast_stove.tank4"))
                .add(CreateLang.number(getControllerBE().secondaryCapability.orElseGet(null).getFluidInTank(1).getAmount())
                        .add(mb)
                        .add(getControllerBE().secondaryCapability.orElseGet(null).getFluidInTank(1).getFluid() == Fluids.EMPTY ? CreateLang.text("") :  CreateLang.text(" "+getControllerBE().secondaryCapability.orElseGet(null).getFluidInTank(1).getDisplayName().getString()))
                        .style(ChatFormatting.YELLOW))
                .text(ChatFormatting.GRAY, " / ")
                .add(CreateLang.number(8000)
                        .add(mb)
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);






        return true;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);

        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.put("primaryOutputInventory", primaryOutputInventory.writeToNBT(new CompoundTag()));
            compound.put("primaryInputInventory", primaryInputInventory.writeToNBT(new CompoundTag()));
            compound.put("secondaryOutputInventory", secondaryOutputInventory.writeToNBT(new CompoundTag()));
            compound.put("secondaryInputInventory", secondaryInputInventory.writeToNBT(new CompoundTag()));
            compound.putInt("Size", width);
            compound.putInt("Height", height);
        }

        forEachBehaviour(tb -> tb.write(compound, clientPacket));

        if (!clientPacket)
            return;
        if (queuedSync)
            compound.putBoolean("LazySync", true);

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!primaryCapability.isPresent())
            refreshCapability();
        if (!secondaryCapability.isPresent())
            refreshCapability();
        if(side==null)
            return LazyOptional.empty();



        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if(side.getAxis() == Direction.Axis.Y) {
                return primaryCapability.cast();
            }else
            if(getController().getY()==getBlockPos().getY()){
                return secondaryCapability.cast();
            }
        }else
            return super.getCapability(cap, side);

        return LazyOptional.empty();
    }


    public FluidTank getTankInventory() {
        return primaryOutputInventory;
    }

    public int getTotalTankSize() {
        return width * width * height;
    }

    public static int getMaxSize() {
        return MAX_SIZE;
    }

    public static int getCapacityMultiplier() {
        return AllConfigs.server().fluids.fluidTankCapacity.get() * 1000;
    }

    public static int getMaxHeight() {
        return AllConfigs.server().fluids.fluidTankMaxHeight.get();
    }



    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        onFluidStackChanged(primaryOutputInventory.getFluid());
        updateBoilerState();
        setChanged();

        updateConnectivity = true;
        //if(isController())
        //    for (int yOffset = 0; yOffset < height; yOffset++)
        //        for (int xOffset = 0; xOffset < width; xOffset++)
        //            for (int zOffset = 0; zOffset < width; zOffset++)
        //                if (level.getBlockEntity(
        //                        worldPosition.offset(xOffset, yOffset, zOffset)) instanceof BlastStoveBlockEntity fbe)
        //                    fbe.refreshCapability();
        sendData();
        setChanged();
    }

    @Override
    public void setExtraData(@Nullable Object data) {
        if (data instanceof Boolean)
            window = (boolean) data;
    }

    @Override
    @Nullable
    public Object getExtraData() {
        return window;
    }

    @Override
    public Object modifyExtraData(Object data) {
        if (data instanceof Boolean windows) {
            windows |= window;
            return windows;
        }
        return data;
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
    public int getMaxWidth() {
        return MAX_SIZE;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public boolean hasTank() {
        return true;
    }

    @Override
    public void setTankSize(int tank, int blocks) {
        applyFluidTankSize(blocks);
    }


}

