package com.drmangotea.tfmg.content.machinery.misc.air_intake;

import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeBlock.INVISIBLE;
import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class AirIntakeBlockEntity extends KineticBlockEntity implements IWrenchable {
    private static final int SMALL_FAN_DIAMETER = 1;
    private static final int MEDIUM_FAN_DIAMETER = 2;
    private static final int LARGE_FAN_DIAMETER = 3;

    int diameter = SMALL_FAN_DIAMETER;
    boolean isController = false;
    public boolean hasShaft = true;
    boolean isUsedByController = false;
    public BlockPos controller;
    public List<AirIntakeBlockEntity> blockEntities = new ArrayList<>();
    public float maxShaftSpeed = 0;
    public float angle = 0;
    public LerpedFloat visual_angle = LerpedFloat.angular();

    protected FluidTank tankInventory;
    protected LazyOptional<IFluidHandler> fluidCapability;

    public AirIntakeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);
    }

    public void tick() {
        super.tick();

        produceAir();
        updateVisuals();
        updateBlockState();
        validateController();
        updateShaftSpeed();
        validateMultiblock();
    }

    private void produceAir() {
        int production = ((int) maxShaftSpeed * (diameter * diameter)) / 40;
        if (tankInventory.getFluidAmount() + production <= tankInventory.getCapacity()) {
            tankInventory.setFluid(new FluidStack(TFMGFluids.AIR.getSource(), production + tankInventory.getFluidAmount()));
        }

        if (isUsedByController) {
            refreshCapability();
            sendData();
            setChanged();
        }
    }

    private void updateVisuals() {
        if (diameter == LARGE_FAN_DIAMETER) {
            visual_angle.chase(angle, 0.1f, LerpedFloat.Chaser.EXP);
            visual_angle.tickChaser();
        }
        angle += maxShaftSpeed / 2;
        angle %= 360;
    }

    private void updateBlockState() {
        if (!this.getBlockState().getValue(INVISIBLE)) {
            if (isController || isUsedByController) {
                level.setBlock(this.getBlockPos(), this.getBlockState().setValue(INVISIBLE, true), 2);
            }
        }
        if (!isController && !isUsedByController) {
            level.setBlock(this.getBlockPos(), this.getBlockState().setValue(INVISIBLE, false), 2);
        }
    }

    private void validateController() {
        if (controller == null) controller = this.getBlockPos();
        diameter = getPossibleDiameter();

        if (controller == this.getBlockPos()) {
            isUsedByController = false;
        } else {
            isUsedByController = true;
            isController = false;
        }

        if (diameter == SMALL_FAN_DIAMETER) {
            isController = false;
        }

        if (!(level.getBlockEntity(controller) instanceof AirIntakeBlockEntity)) {
            isUsedByController = false;
            controller = this.getBlockPos();
        } else if (!(((AirIntakeBlockEntity) level.getBlockEntity(controller)).isController)) {
            isUsedByController = false;
        }

        validateControllerDistance();
    }

    private void validateControllerDistance() {
        if (controller == null || level.getBlockEntity(controller) == null) return;

        AirIntakeBlockEntity controllerBE = (AirIntakeBlockEntity) level.getBlockEntity(controller);
        if (controllerBE.diameter == MEDIUM_FAN_DIAMETER) {
            int x = Math.abs(this.getBlockPos().getX() - controller.getX());
            int y = Math.abs(this.getBlockPos().getY() - controller.getY());
            int z = Math.abs(this.getBlockPos().getZ() - controller.getZ());

            if (x > 1 || y > 1 || z > 1) {
                isUsedByController = false;
                controller = this.getBlockPos();
            }
        } else if (controllerBE.diameter == SMALL_FAN_DIAMETER) {
            isUsedByController = false;
            controller = this.getBlockPos();
        }
    }

    private void updateShaftSpeed() {
        maxShaftSpeed = Math.abs(getSpeed());

        if (diameter > SMALL_FAN_DIAMETER) {
            for (AirIntakeBlockEntity be : blockEntities) {
                float testedSpeed = Math.abs(be.getSpeed());
                if (testedSpeed > maxShaftSpeed) {
                    maxShaftSpeed = testedSpeed;
                }
            }
        }
    }

    private void validateMultiblock() {
        if (isUsedByController) return;

        if ((diameter == MEDIUM_FAN_DIAMETER && blockEntities.size() != MEDIUM_FAN_DIAMETER * MEDIUM_FAN_DIAMETER)
                || (diameter == LARGE_FAN_DIAMETER && blockEntities.size() != LARGE_FAN_DIAMETER * LARGE_FAN_DIAMETER)) return;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Direction direction = context.getClickedFace();
        if (direction == getBlockState().getValue(FACING).getOpposite()) {
            hasShaft = !hasShaft;
        }
        return InteractionResult.SUCCESS;
    }

    public void setController(BlockPos controllerPos) {
        controller = controllerPos;
    }

    @Nonnull
    @Override
    @SuppressWarnings("removal")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (!fluidCapability.isPresent()) {
            refreshCapability();
            sendData();
            setChanged();
        }

        if (cap == ForgeCapabilities.FLUID_HANDLER) return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    private void refreshCapability() {
        IFluidHandler handlerForCapability;

        if (controller == null || controller == this.getBlockPos()) {
            handlerForCapability = tankInventory;
        } else if (level.getBlockEntity(controller) != null) {
            handlerForCapability = ((AirIntakeBlockEntity) level.getBlockEntity(controller)).tankInventory;
        } else {
            handlerForCapability = tankInventory;
        }

        IFluidHandler finalHandlerForCapability = handlerForCapability;
        fluidCapability = LazyOptional.of(() -> finalHandlerForCapability);
    }

    public int getPossibleDiameter() {
        if (controller != this.getBlockPos()) return SMALL_FAN_DIAMETER;

        boolean canBeMedium = checkMediumDiameter();
        boolean canBeLarge = checkLargeDiameter();

        if (canBeLarge) {
            setupMultiblock(LARGE_FAN_DIAMETER);
            return LARGE_FAN_DIAMETER;
        }
        if (canBeMedium) {
            setupMultiblock(MEDIUM_FAN_DIAMETER);
            return MEDIUM_FAN_DIAMETER;
        }

        controller = this.getBlockPos();
        isController = false;
        return SMALL_FAN_DIAMETER;
    }

    private boolean checkMediumDiameter() {
        List<BlockPos> checkedPosses = new ArrayList<>();
        BlockPos checkedPos = this.getBlockPos();
        Direction direction = this.getBlockState().getValue(FACING);

        for (int x = 0; x < MEDIUM_FAN_DIAMETER; x++) {
            for (int z = 0; z < MEDIUM_FAN_DIAMETER; z++) {
                checkedPosses.add(checkedPos);
                if (direction.getAxis().isHorizontal()) {
                    checkedPos = checkedPos.above();
                } else {
                    checkedPos = checkedPos.east();
                }
            }
            if (direction.getAxis().isHorizontal()) {
                checkedPos = checkedPos.below(MEDIUM_FAN_DIAMETER);
                checkedPos = checkedPos.relative(direction.getClockWise());
            } else {
                checkedPos = checkedPos.west(MEDIUM_FAN_DIAMETER);
                checkedPos = checkedPos.south();
            }
        }

        for (BlockPos pos : checkedPosses) {
            if (!(level.getBlockEntity(pos) instanceof AirIntakeBlockEntity)) return false;

            AirIntakeBlockEntity checkedBE = (AirIntakeBlockEntity) level.getBlockEntity(pos);
            if (pos != this.getBlockPos() && checkedBE.isController) return false;
            if (checkedBE.getBlockState().getValue(FACING) != this.getBlockState().getValue(FACING)) return false;
        }
        return true;
    }

    private boolean checkLargeDiameter() {
        List<BlockPos> checkedPosses = new ArrayList<>();
        BlockPos checkedPos = this.getBlockPos();
        Direction direction = this.getBlockState().getValue(FACING);

        for (int x = 0; x < LARGE_FAN_DIAMETER; x++) {
            for (int z = 0; z < LARGE_FAN_DIAMETER; z++) {
                checkedPosses.add(checkedPos);
                if (direction.getAxis().isHorizontal()) {
                    checkedPos = checkedPos.above();
                } else {
                    checkedPos = checkedPos.east();
                }
            }
            if (direction.getAxis().isHorizontal()) {
                checkedPos = checkedPos.below(LARGE_FAN_DIAMETER);
                checkedPos = checkedPos.relative(direction.getClockWise());
            } else {
                checkedPos = checkedPos.west(LARGE_FAN_DIAMETER);
                checkedPos = checkedPos.south();
            }
        }

        for (BlockPos pos : checkedPosses) {
            if (!(level.getBlockEntity(pos) instanceof AirIntakeBlockEntity)) return false;

            AirIntakeBlockEntity checkedBE = (AirIntakeBlockEntity) level.getBlockEntity(pos);
            if (checkedBE.getBlockState().getValue(FACING) != this.getBlockState().getValue(FACING)) return false;
        }
        return true;
    }

    private void setupMultiblock(int diameter) {
        this.blockEntities.clear();
        List<BlockPos> positions = getMultiblockPositions(diameter);

        for (BlockPos pos : positions) {
            AirIntakeBlockEntity be = (AirIntakeBlockEntity) level.getBlockEntity(pos);
            if (be.isUsedByController && be.controller != this.getBlockPos() && pos != this.getBlockPos()) {
                be.isUsedByController = true;
                be.isController = false;
                be.controller = this.getBlockPos();
            }

            be.setController(this.getBlockPos());
            this.blockEntities.add(be);
        }

        controller = this.getBlockPos();
        isController = true;
    }

    private List<BlockPos> getMultiblockPositions(int diameter) {
        List<BlockPos> positions = new ArrayList<>();
        BlockPos checkedPos = this.getBlockPos();
        Direction direction = this.getBlockState().getValue(FACING);
        int size = diameter == MEDIUM_FAN_DIAMETER ? MEDIUM_FAN_DIAMETER : LARGE_FAN_DIAMETER;

        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                positions.add(checkedPos);
                if (direction.getAxis().isHorizontal()) {
                    checkedPos = checkedPos.above();
                } else {
                    checkedPos = checkedPos.east();
                }
            }
            if (direction.getAxis().isHorizontal()) {
                checkedPos = checkedPos.below(size);
                checkedPos = checkedPos.relative(direction.getClockWise());
            } else {
                checkedPos = checkedPos.west(size);
                checkedPos = checkedPos.south();
            }
        }
        return positions;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(3);
    }

    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) return false;

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0) return false;

        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
        boolean isEmpty = true;

        for (int i = 0; i < tank.getTanks(); i++) {
            FluidStack fluidStack = tank.getFluidInTank(i);
            if (fluidStack.isEmpty()) continue;

            CreateLang.fluidName(fluidStack)
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

            CreateLang.builder()
                    .add(CreateLang.number(fluidStack.getAmount())
                            .add(mb)
                            .style(ChatFormatting.DARK_GREEN))
                    .text(ChatFormatting.GRAY, " / ")
                    .add(CreateLang.number(tank.getTankCapacity(i))
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

            isEmpty = false;
        }

        if (tank.getTanks() > 1) {
            if (isEmpty) tooltip.remove(tooltip.size() - 1);
            return true;
        }

        if (!isEmpty) return true;

        CreateLang.translate("gui.goggles.fluid_container.capacity")
                .add(CreateLang.number(tank.getTankCapacity(0))
                        .add(mb)
                        .style(ChatFormatting.DARK_GREEN))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);
        return true;
    }

    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(8000, this::onFluidStackChanged) {
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(TFMGFluids.AIR.getSource());
            }
        };
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        setChanged();
        sendData();
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        diameter = compound.getInt("Diameter");
        isController = compound.getBoolean("IsController");
        isUsedByController = compound.getBoolean("IsUsed");
        hasShaft = compound.getBoolean("HasShaft");
        tankInventory.readFromNBT(compound.getCompound("TankContent"));
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Diameter", diameter);
        compound.putBoolean("IsController", isController);
        compound.putBoolean("IsUsed", isUsedByController);
        compound.putBoolean("HasShaft", hasShaft);
        compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));
    }
}
