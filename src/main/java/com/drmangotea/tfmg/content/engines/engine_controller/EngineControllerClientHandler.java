package com.drmangotea.tfmg.content.engines.engine_controller;

import com.drmangotea.tfmg.content.engines.engine_controller.packets.*;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGKeys;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.*;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.ControlsUtil;

import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class EngineControllerClientHandler {
    //public static final IGuiOverlay OVERLAY = LinkedControllerClientHandler::renderOverlay;

    public static Mode MODE = Mode.IDLE;
    public static int PACKET_RATE = 5;
    public static List<Integer> currentlyPressed = new ArrayList<>();
    public static BlockPos controllerPos;
    private static BlockPos selectedLocation = BlockPos.ZERO;
    private static int packetCooldown;

    public static void toggleBindMode(BlockPos location) {
        if (MODE == Mode.IDLE) {
            MODE = Mode.BIND;
            selectedLocation = location;
        } else {
            MODE = Mode.IDLE;
            onReset();
        }
    }

    public static void toggle() {
        if (MODE == Mode.IDLE) {
            MODE = Mode.ACTIVE;
            controllerPos = null;
        } else {
            MODE = Mode.IDLE;
            onReset();
        }
    }

    public static void activateInLectern(BlockPos lecternAt) {
        if (MODE == Mode.IDLE) {
            MODE = Mode.ACTIVE;
            controllerPos = lecternAt;
        }
    }

    public static void deactivateInLectern() {
        if (MODE == Mode.ACTIVE && isController()) {
            MODE = Mode.IDLE;
            onReset();
        }
    }

    public static boolean isController() {
        return controllerPos != null;
    }

    protected static void onReset() {
        getControls()
                .forEach(kb -> kb.setDown(ControlsUtil.isActuallyPressed(kb)));
        packetCooldown = 0;
        selectedLocation = BlockPos.ZERO;
        if (isController())
            CatnipServices.NETWORK.sendToServer(new EngineControllerStopControllerPacket(controllerPos));
        controllerPos = null;

        if (!currentlyPressed.isEmpty())
            CatnipServices.NETWORK.sendToServer(new EngineControllerInputPacket(currentlyPressed, false));
        currentlyPressed.clear();

        //      LinkedControllerItemRenderer.resetButtons();
    }

    public static void tick() {
        //    LinkedControllerItemRenderer.tick();

        if (MODE == Mode.IDLE)
            return;
        if (packetCooldown > 0)
            packetCooldown--;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ItemStack heldItem = player.getMainHandItem();

        if (player.isSpectator()) {
            MODE = Mode.IDLE;
            onReset();
            return;
        }

        if (!isController() && !AllItems.LINKED_CONTROLLER.isIn(heldItem)) {
            heldItem = player.getOffhandItem();
            if (!AllItems.LINKED_CONTROLLER.isIn(heldItem)) {
                MODE = Mode.IDLE;

                //onReset();
                return;
            }
        }

        if (isController() && TFMGBlocks.ENGINE_CONTROLLER.get()
                .getBlockEntityOptional(mc.level, controllerPos)
                .map(be -> !be.isUsedBy(mc.player))
                .orElse(true)) {
            deactivateInLectern();
            return;
        }

        if (mc.screen != null) {
            MODE = Mode.IDLE;
            onReset();
            return;
        }

        if (InputConstants.isKeyDown(mc.getWindow()
                .getWindow(), GLFW.GLFW_KEY_ESCAPE)) {
            MODE = Mode.IDLE;
            onReset();
            return;
        }

        Vector<KeyMapping> controls = getControls();
        List<Integer> pressedKeys = new ArrayList<>();
        for (int i = 0; i < controls.size(); i++) {
            if (ControlsUtil.isActuallyPressed(controls.get(i)))
                pressedKeys.add(i);
        }

        List<Integer> newKeys = new ArrayList<>(pressedKeys);
        List<Integer> releasedKeys = currentlyPressed;
        newKeys.removeAll(releasedKeys);
        releasedKeys.removeAll(pressedKeys);

        if (MODE == Mode.ACTIVE) {
            // Released Keys
            if (!releasedKeys.isEmpty()) {
                CatnipServices.NETWORK.sendToServer(new EngineControllerInputPacket(releasedKeys, false, controllerPos));
                if(player.level().getBlockEntity(controllerPos) instanceof EngineControllerBlockEntity be){

                    be.handleInput(releasedKeys,false);
                }
                AllSoundEvents.CONTROLLER_CLICK.playAt(player.level(), player.blockPosition(), 1f, .5f, true);

            }

            // Newly Pressed Keys
            if (!newKeys.isEmpty()) {
                CatnipServices.NETWORK.sendToServer(new EngineControllerInputPacket(newKeys, true, controllerPos));
                if(player.level().getBlockEntity(controllerPos) instanceof EngineControllerBlockEntity be){
                    be.handleInput(newKeys,true);
                }
                if (newKeys.contains(5) || newKeys.contains(6)) {
                    CatnipServices.NETWORK.sendToServer(new TransmissionShiftPacket(newKeys, controllerPos));
                    if(player.level().getBlockEntity(controllerPos) instanceof EngineControllerBlockEntity be){

                        if(newKeys.contains(6)) {
                            be.shiftBack();
                        }else {

                            be.shiftForward();

                        }
                    }
                }
                if(newKeys.contains(8)){
                    CatnipServices.NETWORK.sendToServer(new EngineStartPacket(controllerPos));
                    if(player.level().getBlockEntity(controllerPos) instanceof EngineControllerBlockEntity be){
                        be.toggleEngine();
                    }
                }
                packetCooldown = PACKET_RATE;

                AllSoundEvents.CONTROLLER_CLICK.playAt(player.level(), player.blockPosition(), 1f, .75f, true);
            }

            // Keepalive Pressed Keys
            if (packetCooldown == 0) {
                if (!pressedKeys.isEmpty()) {
                    CatnipServices.NETWORK.sendToServer(new EngineControllerInputPacket(pressedKeys, true, controllerPos));
                    if(player.level().getBlockEntity(controllerPos) instanceof EngineControllerBlockEntity be){
                        be.handleInput(newKeys,true);
                    }
                    packetCooldown = PACKET_RATE;
                }
            }
        }

        if (MODE == Mode.BIND) {
            VoxelShape shape = mc.level.getBlockState(selectedLocation)
                    .getShape(mc.level, selectedLocation);


            for (Integer integer : newKeys) {
                LinkBehaviour linkBehaviour = BlockEntityBehaviour.get(mc.level, selectedLocation, LinkBehaviour.TYPE);
                if (linkBehaviour != null) {
                   // CatnipServices.NETWORK.sendToServer(new EngineControllerBindPacket(integer, selectedLocation));
                    CreateLang.translate("linked_controller.key_bound", controls.get(integer)
                                    .getTranslatedKeyMessage()
                                    .getString())
                            .sendStatus(mc.player);
                }
                MODE = Mode.IDLE;
                break;
            }
        }

        currentlyPressed = pressedKeys;
        controls.forEach(kb -> kb.setDown(false));
    }

    private static Vector<KeyMapping> standardControls;

    public static Vector<KeyMapping> getControls() {
        if (standardControls == null) {
            Options gameSettings = Minecraft.getInstance().options;
            standardControls = new Vector<>(6);
            standardControls.add(gameSettings.keyUp);//0
            standardControls.add(gameSettings.keyDown);//1
            standardControls.add(gameSettings.keyLeft);//2
            standardControls.add(gameSettings.keyRight);//3
            standardControls.add(gameSettings.keyJump);//4
            standardControls.add(TFMGKeys.TRANSMISSION_SHIFT_UP.getKeybind());//5
            standardControls.add(TFMGKeys.TRANSMISSION_SHIFT_DOWN.getKeybind());//6
            standardControls.add(TFMGKeys.ENGINE_CONTROLLER_CUSTOM_BUTTON.getKeybind());//7
            standardControls.add(TFMGKeys.ENGINE_START.getKeybind());//8
            standardControls.add(gameSettings.keyShift);//9
        }
        return standardControls;
    }

    public enum Mode {
        IDLE, ACTIVE, BIND
    }
}
