package com.drmangotea.tfmg.content.engines.engine_controller;

import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.upgrades.TransmissionUpgrade;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


public class EngineControllerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, MenuProvider {

    private UUID user;
    private UUID prevUser;
    private boolean deactivatedThisTick;

    public TransmissionUpgrade.TransmissionState shift = TransmissionUpgrade.TransmissionState.NEUTRAL;
    public int accelerationRate = 0;
    public AbstractSmallEngineBlockEntity engine = null;
    public BlockPos enginePos = null;
    public boolean engineStarted = false;

    public boolean clutch = false;
    public boolean brake = false;
    public boolean gas = false;

    public ItemStackHandler frequencyItems = new ItemStackHandler(6);

    /// rendering
    public LerpedFloat transmissionLeverAngle = LerpedFloat.angular();
    public LerpedFloat steeringWheelAngle = LerpedFloat.angular();
    public LerpedFloat clutchPedalMotion = LerpedFloat.linear();
    public LerpedFloat gasPedalMotion = LerpedFloat.linear();
    public LerpedFloat brakePedalMotion = LerpedFloat.linear();
    public LerpedFloat fuelDial = LerpedFloat.angular();
    public LerpedFloat rpmDial = LerpedFloat.angular();

    public EngineControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public ItemStack getController() {
        return ItemStack.EMPTY;
    }

    public static Couple<RedstoneLinkNetworkHandler.Frequency> toFrequency(EngineControllerBlockEntity controller, int slot) {
        ItemStackHandler frequencyItems = controller.frequencyItems;
        return Couple.create(RedstoneLinkNetworkHandler.Frequency.of(frequencyItems.getStackInSlot(slot * 2)),
                RedstoneLinkNetworkHandler.Frequency.of(frequencyItems.getStackInSlot(slot * 2 + 1)));
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound,registries , clientPacket);

        if (enginePos != null)
            compound.putLong("EnginePos", enginePos.asLong());
        compound.putString("Shift", shift.name());

        compound.putBoolean("EngineStarted", engineStarted);

        compound.put("FrequencyItems", frequencyItems.serializeNBT(registries));

        if (user != null)
            compound.putUUID("User", user);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound,registries , clientPacket);
        enginePos = BlockPos.of(compound.getLong("EnginePos"));

        engineStarted = compound.getBoolean("EngineStarted");
        if (engineStarted && accelerationRate == 0)
            accelerationRate = 4;
        frequencyItems.deserializeNBT(registries,compound.getCompound("FrequencyItems"));
        shift = TransmissionUpgrade.TransmissionState.valueOf(compound.getString("Shift"));
        user = compound.hasUUID("User") ? compound.getUUID("User") : null;
        updateEngine();
    }

    public void shiftForward() {
        int max = TransmissionUpgrade.TransmissionState.values().length - 1;
        for (int i = 0; i < max; i++) {
            TransmissionUpgrade.TransmissionState state = TransmissionUpgrade.TransmissionState.values()[i];
            if (state == shift && i + 1 <= max) {
                shift = TransmissionUpgrade.TransmissionState.values()[i + 1];
                updateShift();
                break;
            }

        }
        sendData();
        setChanged();
    }

    public void updateShift() {
        if (enginePos != null)
            if (level.getBlockEntity(enginePos) instanceof AbstractSmallEngineBlockEntity be) {
                be.getControllerBE().shift = shift;
                be.getControllerBE().clutchPressed = clutch;
                be.getControllerBE().updateGeneratedRotation();
                if(be.getControllerBE().engineLength()>1){
                    if(level.getBlockEntity(BlockPos.of(be.getControllerBE().engines.get(be.getControllerBE().engineLength()-1)))instanceof AbstractSmallEngineBlockEntity be2){
                        be2.updateGeneratedRotation();
                    }
                }
            }
    }

    @Override
    public void destroy() {
        super.destroy();
        Entity playerEntity = ((ServerLevel) level).getEntity(user);
        if (playerEntity instanceof Player)
            stopUsing((Player) playerEntity);
    }

    public void shiftBack() {
        int max = TransmissionUpgrade.TransmissionState.values().length;
        for (int i = 0; i < max; i++) {
            TransmissionUpgrade.TransmissionState state = TransmissionUpgrade.TransmissionState.values()[i];
            if (state == shift && i - 1 >= 0) {
                shift = TransmissionUpgrade.TransmissionState.values()[i - 1];
                updateShift();
                break;
            }
        }
    }

    public void tickAcceleration() {

        if (gas) {
            if (engineStarted && accelerationRate < 15) {
                accelerationRate++;
                this.updateEngine();
            }
        } else {
            if ((accelerationRate > 4 || !engineStarted) && accelerationRate > 0) {
                accelerationRate--;
                this.updateEngine();
            }
        }


    }

    public void tickBraking() {

        if (brake && accelerationRate > 4) {
            accelerationRate--;
            updateEngine();
        }

    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        tickAcceleration();
    }

    public static ItemStackHandler getFrequencyItems(EngineControllerBlockEntity be) {
        return be.frequencyItems;
    }

    public void handleInput(Collection<Integer> currentlyPressed, boolean press) {
        if (currentlyPressed.contains(4)) {
            this.clutch = press;
            this.sendData();
            this.setChanged();
        }
        if (currentlyPressed.contains(0)) {
            this.gas = press;
            this.sendData();
            this.setChanged();
        }
        if (currentlyPressed.contains(1)) {
            this.brake = press;
            this.sendData();
            this.setChanged();
        }
    }

    public void toggleEngine() {
        this.engineStarted = !this.engineStarted;
        this.accelerationRate = this.engineStarted ? 4 : 0;
        this.updateEngine();
        this.sendData();
        this.setChanged();

    }

    @Override
    public void remove() {
        super.remove();
        disconnectEngine();
    }

    //@Override
    //public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//
    //    if (engine != null) {
    //        CreateLang.text("Linked").forGoggles(tooltip);
    //    }
//
//
    //    CreateLang.text(shift.name()).forGoggles(tooltip);
    //    CreateLang.text(engineStarted ? "Engine Started" : "Engine Stopped").forGoggles(tooltip);
    //    CreateLang.text("Acceleration: " + accelerationRate).forGoggles(tooltip);
//
//
    //    if (clutch)
    //        CreateLang.text("CLUTCH").forGoggles(tooltip);
    //    if (brake)
    //        CreateLang.text("BRAKE").forGoggles(tooltip);
    //    if (gas)
    //        CreateLang.text("GAS").forGoggles(tooltip);
//
    //    return true;
    //}


    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) {
            CatnipServices.PLATFORM.executeOnClientOnly(() -> this::tryToggleActive);
            prevUser = user;
            tickRendering();
        }

        if (enginePos != null && (engine == null)) {
            if (level.getBlockEntity(enginePos) instanceof AbstractSmallEngineBlockEntity be) {
                engine = be;
                engine.getControllerBE().highestSignal = 4;
            }
        }

        tickBraking();
        if (!level.isClientSide) {
            deactivatedThisTick = false;

            if (!(level instanceof ServerLevel))
                return;
            if (user == null)
                return;

            Entity entity = ((ServerLevel) level).getEntity(user);
            if (!(entity instanceof Player)) {
                stopUsing(null);
                return;
            }

            Player player = (Player) entity;
            if (!playerInRange(player, level, worldPosition) || !playerIsUsingEngineController(player))
                stopUsing(player);
        }
    }

    public void updateEngine() {
        if (engine == null)
            return;
        engine.getControllerBE().engineController = this.getBlockPos();
        engine.getControllerBE().highestSignal = accelerationRate;
        engine.getControllerBE().fuelInjectionRate = engine.getControllerBE().highestSignal / 15f;
        engine.getControllerBE().updateRotation();


    }

    public void disconnectEngine() {
        if (engine == null)
            return;
        engine.getControllerBE().highestSignal = 0;
        engine.getControllerBE().engineController = null;
        engine.getControllerBE().updateGeneratedRotation();
    }

    public void tickRendering() {
        if (Minecraft.getInstance()
                .isPaused())
            return;


        steeringWheelAngle.chase(isPressed(2) ? -40 : isPressed(3) ? 40 : 0, 0.25, LerpedFloat.Chaser.EXP);
        clutchPedalMotion.chase(isPressed(4) ? 2 / 16f : 0, 0.25, LerpedFloat.Chaser.EXP);
        gasPedalMotion.chase(isPressed(0) ? 2 / 16f : 0, 0.25, LerpedFloat.Chaser.EXP);
        brakePedalMotion.chase(isPressed(1) ? 2 / 16f : 0, 0.25, LerpedFloat.Chaser.EXP);
        transmissionLeverAngle.chase(shift == TransmissionUpgrade.TransmissionState.REVERSE ? -20 : (shift.value * 20), 0.25, LerpedFloat.Chaser.EXP);
        fuelDial.chase(engine == null ? 0 : ((double) engine.getControllerBE().fuelTank.getFluidAmount() / (double) engine.fuelTank.getCapacity()) * 180f, 0.25, LerpedFloat.Chaser.EXP);
        rpmDial.chase(engine == null ? 0 : ((double) engine.getControllerBE().rpm / 6000f) * 180f, 0.25, LerpedFloat.Chaser.EXP);

        transmissionLeverAngle.tickChaser();
        steeringWheelAngle.tickChaser();
        clutchPedalMotion.tickChaser();
        gasPedalMotion.tickChaser();
        brakePedalMotion.tickChaser();
        fuelDial.tickChaser();
        rpmDial.tickChaser();
    }

    public boolean isPressed(int id) {
        return EngineControllerClientHandler.currentlyPressed.contains(id);
    }

    @OnlyIn(Dist.CLIENT)
    private void tryToggleActive() {
        if (user == null && Minecraft.getInstance().player.getUUID().equals(prevUser)) {
            EngineControllerClientHandler.deactivateInLectern();
        } else if (prevUser == null && Minecraft.getInstance().player.getUUID().equals(user)) {
            EngineControllerClientHandler.activateInLectern(worldPosition);
        }
    }

    public void tryStopUsing(Player player) {
        if (isUsedBy(player))
            stopUsing(player);
    }

    public static boolean playerIsUsingEngineController(Player player) {
        return player.getPersistentData().contains("IsUsingEngineController");
    }

    public void tryStartUsing(Player player) {


        if (!deactivatedThisTick && !hasUser() && !playerIsUsingEngineController(player) && playerInRange(player, level, worldPosition)) {
            startUsing(player);
        }
    }


    private void startUsing(Player player) {

        user = player.getUUID();
        player.getPersistentData().putBoolean("IsUsingEngineController", true);
        sendData();
    }

    private void stopUsing(Player player) {


        user = null;
        if (player != null)
            player.getPersistentData().remove("IsUsingEngineController");
        deactivatedThisTick = true;
        sendData();
    }

    public InteractionResult use(Player player) {
        if (player == null)
            return InteractionResult.PASS;
        if (player instanceof FakePlayer)
            return InteractionResult.PASS;
        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        player.openMenu(this,getBlockPos());
        return InteractionResult.SUCCESS;
    }

    public boolean isUsedBy(Player player) {
        return hasUser() && user.equals(player.getUUID());
    }

    public boolean hasUser() {
        return user != null;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public static boolean playerInRange(Player player, Level world, BlockPos pos) {
        double reach = 0.4 * player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        return player.distanceToSqr(Vec3.atCenterOf(pos)) < reach * reach;
    }

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return EngineControllerMenu.create(pContainerId, pPlayerInventory, this);
    }
}
