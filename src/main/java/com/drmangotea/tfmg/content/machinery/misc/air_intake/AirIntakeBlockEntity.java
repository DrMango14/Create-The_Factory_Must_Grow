package com.drmangotea.tfmg.content.machinery.misc.air_intake;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.fluid.ForceableFluidTank;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.fluid.FluidHelper;


import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeBlock.INVISIBLE;
import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class AirIntakeBlockEntity extends KineticBlockEntity implements IWrenchable {

    int diameter = 1;
    boolean isController = false;
    public boolean hasShaft = true;
    boolean isUsedByController = false;
    public BlockPos controller;
    public List<AirIntakeBlockEntity> blockEntities = new ArrayList<>();
    public float maxShaftSpeed = 0;

    public float angle = 0;
    public LerpedFloat visual_angle = LerpedFloat.angular();

    protected ForceableFluidTank tankInventory;
    protected IFluidHandler fluidCapability;

    protected boolean updateCapability;

    private BlockPos capabilityController;
    private boolean capabilityResolved = false;
    private int syncedDiameter = -1;

    private int syncTimer = 0;
    private static final int SYNC_INTERVAL = 10;
    private static final int TANK_SYNC_STEP = 50;
    private int lastSyncedAmount = -1;

    public AirIntakeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        tankInventory = new ForceableFluidTank(8000, this::onFluidStackChanged).blockInsertion().withValidator(fs->fs.is(TFMGTags.Fluids.AIR.tag));
        fluidCapability = tankInventory;
        updateCapability = false;
        refreshCapability();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TFMGBlockEntities.AIR_INTAKE.get(),
                (be, context) -> be.fluidCapability
        );
    }

    public int getProduction() {
        if (controller != null && level != null && level.getBlockEntity(controller) instanceof AirIntakeBlockEntity intake) {
            return ((int) intake.maxShaftSpeed * (intake.diameter * intake.diameter)) / 40;
        }
        return ((int) maxShaftSpeed * ((diameter * diameter))) / 40;
    }

    public int getMinimumSpeed() {
        int rpm = (int) Math.ceil(40.0 / (diameter * diameter));
        if (controller == null) {
            return rpm;
        }
        if (level != null && level.getBlockEntity(controller) instanceof AirIntakeBlockEntity intake) {
            rpm = (int) Math.ceil(40.0 / (intake.diameter * intake.diameter));
        }
        return rpm;
    }

    public void tick(){
        super.tick();
        if (level == null) return;

        if (!level.isClientSide) {
            if (tankInventory.getFluidAmount() + Math.min(getProduction(), tankInventory.getSpace()) <= tankInventory.getCapacity()) {
                tankInventory.forceFill(new FluidStack(FluidHelper.convertToStill(TFMGFluids.AIR.get()), getProduction()), IFluidHandler.FluidAction.EXECUTE);
            }
        }

        if(isUsedByController) {
            boolean structureChanged = !capabilityResolved || !Objects.equals(capabilityController, controller) || syncedDiameter != diameter;
            if (structureChanged) {
                capabilityResolved = true;
                capabilityController = controller;
                syncedDiameter = diameter;
                refreshCapability();
                syncTimer = 0;
                sendData();
                setChanged();
            } else if (++syncTimer >= SYNC_INTERVAL) {
                syncTimer = 0;
                sendData();
                setChanged();
            }
        }

        if(diameter == 3){
            visual_angle.chase(angle, 0.1f, LerpedFloat.Chaser.EXP);
            visual_angle.tickChaser();
        }

        angle += maxShaftSpeed / 2;
        angle %= 360;

        if(isUsedByController)
            blockEntities.clear();

        if (level != null) {
            if(!this.getBlockState().getValue(INVISIBLE)){
                if(isController||isUsedByController){
                    level.setBlock(this.getBlockPos(),this.getBlockState().setValue(INVISIBLE,true),2);
                }
            }
            if(!isController && !isUsedByController && this.getBlockState().getValue(INVISIBLE))
                level.setBlock(this.getBlockPos(),this.getBlockState().setValue(INVISIBLE,false),2);
        }
        if(controller == null)
            controller = this.getBlockPos();

        diameter =getPossibleDiameter();

        if (controller != null && controller.equals(this.getBlockPos())) {
            isUsedByController = false;
        } else {
            isUsedByController = true;
            isController = false;
        }
        if(diameter ==1) {
            isController = false;
        }

        if (level != null) {
            if (controller != null) {
                if (!(level.getBlockEntity(controller) instanceof AirIntakeBlockEntity intakeController)) {
                    isUsedByController = false;
                    controller = this.getBlockPos();
                } else {
                    if (!intakeController.isController)
                        isUsedByController = false;
                }

                if (level.getBlockEntity(controller) instanceof AirIntakeBlockEntity intakeController)
                    if (intakeController.diameter == 2) {
                        int x = Math.abs(this.getBlockPos().getX() - controller.getX());
                        int y = Math.abs(this.getBlockPos().getY() - controller.getY());
                        int z = Math.abs(this.getBlockPos().getZ() - controller.getZ());
                        if (x > 1 || y > 1 || z > 1) {
                            isUsedByController = false;
                            controller = this.getBlockPos();
                        }
                    }
                if (level.getBlockEntity(controller) instanceof AirIntakeBlockEntity intakeController)
                    if (intakeController.diameter == 1) {
                        isUsedByController = false;
                        controller = this.getBlockPos();
                    }
            }
        }
        if (diameter == 1) {
            maxShaftSpeed = Math.abs(getSpeed());
        } else {
            maxShaftSpeed = Math.abs(getSpeed());
            List<Float> speeds = new ArrayList<>();
            for (AirIntakeBlockEntity be : blockEntities) {
                speeds.add(Math.abs(be.getSpeed()));
            }
            for(float testedSpeed : speeds){
                if(testedSpeed> maxShaftSpeed)
                    maxShaftSpeed = testedSpeed;
            }
        }
        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
        if (isUsedByController)
            return;
        if (diameter == 2){
            if(blockEntities.size() != 4)
                return;
        }
        if (diameter == 3){
            if(blockEntities.size() != 9)
                return;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateCapabilities();
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context){
        Direction direction = context.getClickedFace();
        if(direction == getBlockState().getValue(FACING).getOpposite()) {
            hasShaft = !hasShaft;
        }
        return InteractionResult.SUCCESS;
    }

    public void setController(BlockPos controllerPos) {
        controller  = controllerPos;
    }

    private void refreshCapability() {
        IFluidHandler handlerForCapability;
        if (level == null) return;
        if (controller == null || controller == this.getBlockPos()) {
            handlerForCapability = tankInventory;
        } else if (level.getBlockEntity(controller) instanceof AirIntakeBlockEntity intakeController) {
            handlerForCapability = intakeController.tankInventory;
        } else
            handlerForCapability = tankInventory;
        fluidCapability = handlerForCapability;
        invalidateCapabilities();
    }

    public int getPossibleDiameter() {
        if(controller != this.getBlockPos())
            return 1;
        if (level == null) return 0;

        BlockPos checkedPos;
        Direction direction = this.getBlockState().getValue(FACING);
        List<BlockPos> checkedPosses = new ArrayList<>();
        checkedPos = this.getBlockPos();
        boolean canBeMedium = true;
        for(int x = 0;x < 2; x++){
            for(int z = 0;z < 2; z++){
                checkedPosses.add(checkedPos);
                if(direction.getAxis().isHorizontal()) {
                    checkedPos = checkedPos.above();
                } else checkedPos = checkedPos.east();
            }
            if (direction.getAxis().isHorizontal()) {
                checkedPos = checkedPos.below(2);
                checkedPos = checkedPos.relative(direction.getClockWise());
            } else {
                checkedPos = checkedPos.west(2);
                checkedPos = checkedPos.south();
            }
        }
        List<BlockPos> checkedPossesLarge = new ArrayList<>();
        checkedPos = this.getBlockPos();
        boolean canBeLarge = true;
        for (int x = 0;x < 3; x++){
            for (int z = 0;z < 3; z++){
                checkedPossesLarge.add(checkedPos);
                if(direction.getAxis().isHorizontal()) {
                    checkedPos = checkedPos.above();
                }else checkedPos = checkedPos.east();
            }
            if (direction.getAxis().isHorizontal()) {
                checkedPos = checkedPos.below(3);
                checkedPos = checkedPos.relative(direction.getClockWise());
            } else {
                checkedPos = checkedPos.west(3);
                checkedPos = checkedPos.south();

            }
        }
        //LARGE
        for(BlockPos pos : checkedPossesLarge){
            if (!(level.getBlockEntity(pos) instanceof AirIntakeBlockEntity intake)) {
                canBeLarge = false;
                break;
            }
            if (intake.getBlockState().getValue(FACING) != this.getBlockState().getValue(FACING)) {
                canBeLarge = false;
                break;
            }
        }
        //MEDIUM
        for(BlockPos pos : checkedPosses){
            if (!(level.getBlockEntity(pos) instanceof AirIntakeBlockEntity intake)) {
                canBeMedium = false;
                break;
            }
            if (pos!=this.getBlockPos())
                if(intake.isController) {
                    canBeMedium = false;
                    break;
                }
            if (intake.getBlockState().getValue(FACING) != this.getBlockState().getValue(FACING)) {
                canBeMedium = false;
                break;
            }
        }
        if(canBeLarge) {
            this.blockEntities.clear();
            for (BlockPos pos : checkedPossesLarge) {
                if (level.getBlockEntity(pos) instanceof AirIntakeBlockEntity intake) {
                    if (intake.isUsedByController && intake.controller != this.getBlockPos() && pos != this.getBlockPos() || isController) {
                        intake.isUsedByController = true;
                        intake.isController = false;
                        intake.controller =this.getBlockPos();
                    }
                    intake.setController(this.getBlockPos());
                    this.blockEntities.add(intake);
                }
            }
            controller = this.getBlockPos();
            isController = true;
            return 3;
        }
        if(canBeMedium) {
            this.blockEntities.clear();
            for (BlockPos pos : checkedPosses) {
                if (level.getBlockEntity(pos) instanceof AirIntakeBlockEntity intake) {
                    if (intake.isUsedByController && intake.controller != this.getBlockPos() && pos != this.getBlockPos()) {
                        controller = this.getBlockPos();
                        isController = false;
                        return 1;
                    }
                    intake.setController(this.getBlockPos());
                    this.blockEntities.add(intake);
                }
            }
            controller = this.getBlockPos();
            isController = true;
            return 2;
        }
        controller = this.getBlockPos();
        isController = false;
        return 1;
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(3);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        float currentSpeed = maxShaftSpeed;
        if (controller != null && level != null && level.getBlockEntity(controller) instanceof AirIntakeBlockEntity intake) {
            currentSpeed = intake.maxShaftSpeed;
        }
        if (getProduction() == 0 && currentSpeed < getMinimumSpeed()) {
            TFMGTexts.CommonMachines.minRPM(getMinimumSpeed()).style(ChatFormatting.RED).forGoggles(tooltip);
        }
        TFMGTexts.fluidProduction(getProduction()).style(getProduction() > 0 ? ChatFormatting.AQUA : ChatFormatting.RED).forGoggles(tooltip);
        TFMGUtils.createFluidTooltip(tooltip, fluidCapability);
        return true;
    }



    protected void onFluidStackChanged(FluidStack newFluidStack) {
        int amount = newFluidStack.getAmount();
        boolean emptinessFlipped = (amount == 0) != (lastSyncedAmount == 0);
        if (amount != 8000 && !emptinessFlipped && lastSyncedAmount >= 0 && Math.abs(amount - lastSyncedAmount) < TANK_SYNC_STEP)
            return;

        lastSyncedAmount = amount;
        sendData();
    }


    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        diameter = compound.getInt("Diameter");
        isController = compound.getBoolean("IsController");
        isUsedByController = compound.getBoolean("IsUsed");
        hasShaft = compound.getBoolean("HasShaft");
        tankInventory.readFromNBT(registries,compound.getCompound("TankContent"));

        updateCapability = true;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);
        compound.putInt("Diameter", diameter);
        compound.putBoolean("IsController", isController);
        compound.putBoolean("IsUsed", isUsedByController);
        compound.putBoolean("HasShaft", hasShaft);
        compound.put("TankContent", tankInventory.writeToNBT(registries,new CompoundTag()));
    }
}
