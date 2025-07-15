package com.drmangotea.tfmg.content.engines.engine_controller.packets;

import com.drmangotea.tfmg.content.engines.engine_controller.EngineControllerBlockEntity;
import com.simibubi.create.AllItems;

import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;


public abstract class EngineControllerPacketBase implements ServerboundPacketPayload {
    @Nullable
    private final BlockPos controllerPos;

    public EngineControllerPacketBase(@Nullable BlockPos lecternPos) {
        this.controllerPos = lecternPos;
    }

    @Nullable
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public void handle(ServerPlayer player) {
        if (this.controllerPos != null) {
            BlockEntity be = player.level().getBlockEntity(controllerPos);
            if (!(be instanceof EngineControllerBlockEntity))
                return;
            handleLectern(player, (EngineControllerBlockEntity) be);
        } else {
            ItemStack controller = player.getMainHandItem();
            if (!AllItems.LINKED_CONTROLLER.isIn(controller)) {
                controller = player.getOffhandItem();
                if (!AllItems.LINKED_CONTROLLER.isIn(controller))
                    return;
            }
            handleItem(player, controller);
        }
    }

    protected abstract void handleItem(ServerPlayer player, ItemStack heldItem);
    protected abstract void handleLectern(ServerPlayer player, EngineControllerBlockEntity lectern);

}
