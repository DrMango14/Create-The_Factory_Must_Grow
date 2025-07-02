package com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.recipes.CokingRecipe;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.drmangotea.tfmg.registry.TFMGRecipeTypes;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class CokeOvenBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartInventory inventory;
    public FluidTank primaryTank;
    public FluidTank secondaryTank;
    protected LazyOptional<IFluidHandler> primaryFluidCapability;
    protected LazyOptional<IFluidHandler> secondaryFluidCapability;
    public LazyOptional<IItemHandlerModifiable> itemCapability;
    int timer = -1;
    public LerpedFloat doorAngle = LerpedFloat.angular();
    public boolean createNextTick;
    public BlockPos controller = getBlockPos();
    public int size = 1;
    public boolean forceOpen = false;

    public CokeOvenBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
        inventory = new SmartInventory(1, this)
                .withMaxStackSize(64)
                .whenContentsChanged(i -> this.onContentsChanged());
        primaryTank = new SmartFluidTank(8000, this::onFluidChanged);
        secondaryTank = new SmartFluidTank(8000, this::onFluidChanged);
        itemCapability = LazyOptional.of(() -> inventory);
        primaryFluidCapability = LazyOptional.of(() -> primaryTank);
        secondaryFluidCapability = LazyOptional.of(() -> secondaryTank);
        createNextTick = true;
    }

    public void onContentsChanged() {
        if (!inventory.isEmpty() && timer == -1) {
            executeRecipe();
        }
        if (inventory.isEmpty())
            timer = -1;
    }

    public void executeRecipe() {

        Optional<CokingRecipe> optional = TFMGRecipeTypes.COKING.find(new RecipeWrapper(inventory), level);

        if (optional.isEmpty())
            return;

        CokingRecipe recipe = optional.get();

        if (recipe.getIngredients().get(0).test(inventory.getItem(0)))
            timer = recipe.getProcessingDuration() / (Math.max(size / 2, 1));
    }

    private void onFluidChanged(FluidStack stack) {
        if (!hasLevel())
            return;
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
    }


    @Override
    public void tick() {
        super.tick();

        tickRecipe();

        if (level.isClientSide) {

            doorAngle.chase((timer > 0 && timer < 50) || forceOpen ? 90 : 0, 0.1f, LerpedFloat.Chaser.EXP);
            doorAngle.tickChaser();
            if (!forceOpen)
                manageDoors(timer > 0 && timer < 50);
        }
        if (createNextTick) {
            createMultiblock();
            createNextTick = false;
        }
    }

    public void tickRecipe() {
        if (inventory.isEmpty() || timer == -1)
            return;

        Optional<CokingRecipe> optional = TFMGRecipeTypes.COKING.find(new RecipeWrapper(inventory), level);

        if (optional.isEmpty()) {
            timer = -1;
            return;
        }
        CokingRecipe recipe = optional.get();

        if (timer == 0) {
            timer = -1;
            inventory.getItem(0).shrink(recipe.getIngredients().get(0).getItems()[0].getCount());

            Direction direction = getBlockState().getValue(FACING);

            Vec3 dropVec = VecHelper.getCenterOf(worldPosition.relative(direction))
                    .add(0, 0.4, 0);
            ItemEntity dropped = new ItemEntity(level, dropVec.x, dropVec.y, dropVec.z, recipe.getResultItem(level.registryAccess()).copy());
            dropped.setDefaultPickUpDelay();
            dropped.setDeltaMovement(direction.getAxis() == Direction.Axis.X ? direction == Direction.WEST ? -.01f : .01f : 0, 0.05f, direction.getAxis() == Direction.Axis.Z ? direction == Direction.NORTH ? -.01f : .01f : 0);
            level.addFreshEntity(dropped);

            if (!level.isClientSide) {

                setChanged();
                sendData();
            }
            onContentsChanged();
        }

        if (timer > 0 && primaryTank.getSpace() != 0 && secondaryTank.getSpace() != 0) {
            primaryTank.fill(recipe.getPrimaryResult(), IFluidHandler.FluidAction.EXECUTE);
            secondaryTank.fill(recipe.getSecondaryResult(), IFluidHandler.FluidAction.EXECUTE);
            timer--;
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        onContentsChanged();


    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        CreateLang.translate("goggles.coke_oven.header")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);

        if(level.getBlockEntity(controller) instanceof CokeOvenBlockEntity controller)
        if (controller.timer > 0)
            CreateLang.translate("goggles.coke_oven.progress", controller.timer / 20)
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip);

        createFluidTooltip(this,tooltip);
        TFMGUtils.createItemTooltip(this, tooltip);
        return true;
    }

    public static boolean createFluidTooltip(CokeOvenBlockEntity be, List<Component> tooltip) {
        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");

        /////////

        if(be.level.getBlockEntity(be.controller) instanceof CokeOvenBlockEntity controller) {


            IFluidHandler tank = new CombinedTankWrapper(controller.primaryTank, controller.secondaryTank);


            if (tank.getTanks() == 0) return false;

            CreateLang.translate("goggles.fluid_storage").style(ChatFormatting.GRAY).forGoggles(tooltip);


            boolean isEmpty = true;
            for (int i = 0; i < tank.getTanks(); i++) {
                FluidStack fluidStack = tank.getFluidInTank(i);
                if (fluidStack.isEmpty()) continue;
                CreateLang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                CreateLang.builder().add(CreateLang.number(fluidStack.getAmount()).add(mb).style(ChatFormatting.DARK_GREEN)).text(ChatFormatting.GRAY, " / ").add(CreateLang.number(tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                isEmpty = false;
            }
            if (tank.getTanks() > 1) {
                if (isEmpty) tooltip.remove(tooltip.size() - 1);
                return true;
            }
            if (!isEmpty) return true;

            CreateLang.translate("gui.goggles.fluid_container.capacity").add(CreateLang.number(tank.getTankCapacity(0)).add(mb).style(ChatFormatting.DARK_GREEN)).style(ChatFormatting.DARK_GRAY).forGoggles(tooltip, 1);

        }
        return true;
    }



    public void manageDoors(boolean open) {

        for (int i = 0; i < size; i++) {
            BlockPos pos = getBlockPos().above(i);

            if (level.getBlockEntity(pos) instanceof CokeOvenBlockEntity be && pos != getBlockPos()) {
                be.forceOpen = open;
            }
        }
    }

    public boolean isController() {
        return controller == getBlockPos();
    }

    public void createMultiblock() {

        if (level == null)
            return;
        int maxSize = TFMGConfigs.common().machines.cokeOvenMaxSize.get();
        Direction facing = getBlockState().getValue(FACING);
        if (level.getBlockState(getBlockPos().relative(facing)).is(TFMGBlocks.COKE_OVEN.get()) || level.getBlockState(getBlockPos().below()).is(TFMGBlocks.COKE_OVEN.get()))
            return;

        int size = 1;
        for (int i = 1; i <= maxSize; i++) {
            boolean cantBuildMultiblock = false;
            for (BlockPos pos : BlockPos.betweenClosed(getBlockPos(), getBlockPos().above(i).relative(facing.getOpposite(), i))) {
                if (!level.getBlockState(pos).is(TFMGBlocks.COKE_OVEN.get())) {
                    cantBuildMultiblock = true;
                } else if (level.getBlockState(pos).is(TFMGBlocks.COKE_OVEN.get()) && level.getBlockState(pos).getValue(FACING) != facing) {
                    cantBuildMultiblock = true;
                }
            }
            if (cantBuildMultiblock)
                break;
            size++;
        }
        for (BlockPos pos : BlockPos.betweenClosed(getBlockPos(), getBlockPos().above(size - 1).relative(facing.getOpposite(), size - 1))) {
            if (level.getBlockEntity(pos) instanceof CokeOvenBlockEntity be && (!level.getBlockState(getBlockPos().relative(facing)).is(TFMGBlocks.COKE_OVEN.get()) && !level.getBlockState(getBlockPos().below()).is(TFMGBlocks.COKE_OVEN.get()))) {

                be.controller = getBlockPos();
                be.refreshCapability();
            }
        }
        if (!level.getBlockState(getBlockPos().relative(facing)).is(TFMGBlocks.COKE_OVEN.get()) && !level.getBlockState(getBlockPos().below()).is(TFMGBlocks.COKE_OVEN.get()))
            setBlockStates(size);
        for (BlockPos pos : BlockPos.betweenClosed(getBlockPos(), getBlockPos().above(this.size - 1).relative(facing.getOpposite(), this.size - 1))) {
            if (level.getBlockEntity(pos) instanceof CokeOvenBlockEntity be) {
                if (Math.abs(getBlockPos().getX() - be.getBlockPos().getX()) >= size || Math.abs(getBlockPos().getY() - be.getBlockPos().getY()) >= size || Math.abs(getBlockPos().getZ() - be.getBlockPos().getZ()) >= size)
                    if (be.controller == getBlockPos() || be.controller != be.getBlockPos()) {
                        be.controller = be.getBlockPos();
                        be.refreshCapability();
                        be.forceOpen = false;
                        be.doorAngle.setValue(0);
                        level.setBlock(be.getBlockPos(), getBlockState().setValue(CokeOvenBlock.CONTROLLER_TYPE, CokeOvenBlock.ControllerType.CASUAL), 2);
                    }
            }
        }
        this.size = size;
    }

    public void setBlockStates(int size) {

        if (size > 1) {
            level.setBlock(getBlockPos(), getBlockState().setValue(CokeOvenBlock.CONTROLLER_TYPE, CokeOvenBlock.ControllerType.BOTTOM_ON), 2);
            level.setBlock(getBlockPos().above(size - 1), getBlockState().setValue(CokeOvenBlock.CONTROLLER_TYPE, CokeOvenBlock.ControllerType.TOP_ON), 2);
        } else
            level.setBlock(getBlockPos(), getBlockState().setValue(CokeOvenBlock.CONTROLLER_TYPE, CokeOvenBlock.ControllerType.CASUAL), 2);

        for (int i = 0; i < size; i++) {
            BlockPos pos = getBlockPos().above(i);

            if (i > 0 && i != size - 1) {
                level.setBlock(pos, getBlockState().setValue(CokeOvenBlock.CONTROLLER_TYPE, CokeOvenBlock.ControllerType.MIDDLE_ON), 2);
            }
        }
    }

    public void onPlaced() {
        createNextTick = true;
        updateOvenBlocks();
        if (!level.isClientSide)
            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new CokeOvenPacket(getBlockPos()));
    }

    @Override
    public void remove() {
        super.remove();
        updateOvenBlocks();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (isController())
            ItemHelper.dropContents(level, worldPosition, inventory);
    }

    public void updateOvenBlocks() {
        int maxSize = TFMGConfigs.common().machines.cokeOvenMaxSize.get();
        Direction facing = getBlockState().getValue(FACING);

        for (BlockPos pos : BlockPos.betweenClosed(getBlockPos(), getBlockPos().below(maxSize).relative(facing, maxSize))) {
            //
            if (level.getBlockEntity(pos) instanceof CokeOvenBlockEntity be) {
                be.createMultiblock();

            }
        }
    }

    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldPrimaryFluidCap = primaryFluidCapability;
        LazyOptional<IFluidHandler> oldSecondaryFluidCap = secondaryFluidCapability;
        LazyOptional<IItemHandlerModifiable> oldItemCap = itemCapability;

        CokeOvenBlockEntity be;
        if (level.getBlockEntity(controller) instanceof CokeOvenBlockEntity be1) {
            be = be1;
        } else {
            controller = getBlockPos();
            be = (CokeOvenBlockEntity) level.getBlockEntity(getBlockPos());
        }
        primaryFluidCapability = LazyOptional.of(() -> be.primaryTank);
        secondaryFluidCapability = LazyOptional.of(() -> be.secondaryTank);
        itemCapability = LazyOptional.of(() -> be.inventory);
        oldPrimaryFluidCap.invalidate();
        oldSecondaryFluidCap.invalidate();
        oldItemCap.invalidate();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return side == Direction.UP ? secondaryFluidCapability.cast() : primaryFluidCapability.cast();
        }
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return itemCapability.cast();

        return super.getCapability(cap, side);
    }



    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Timer", timer);
        compound.put("Inventory", inventory.serializeNBT());
        compound.put("PrimaryTankContent", primaryTank.writeToNBT(new CompoundTag()));
        compound.put("SecondaryTankContent", secondaryTank.writeToNBT(new CompoundTag()));
        compound.putLong("Controller", controller.asLong());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        timer = compound.getInt("Timer");
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        primaryTank.readFromNBT(compound.getCompound("PrimaryTankContent"));
        secondaryTank.readFromNBT(compound.getCompound("SecondaryTankContent"));
        controller = BlockPos.of(compound.getLong("Controller"));
    }
}
