package com.drmangotea.tfmg.mixin;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.content.decoration.tanks.aluminum.AluminumTankBlock;
import com.drmangotea.tfmg.content.decoration.tanks.cast_iron.CastIronTankBlock;
import com.drmangotea.tfmg.mixin.accessor.FluidTankBlockEntityAccessor;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static java.lang.Math.abs;

@Mixin(FluidTankBlockEntity.class)
public class FluidTankBlockEntityMixin extends SmartBlockEntity {

    @Shadow
    protected int height;
    @Shadow
    protected int width;
    @Shadow
    protected BlockPos controller;
    @Shadow
    protected boolean window;

    public FluidTankBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Inject(at = @At("HEAD"), method = "toggleWindows",cancellable = true , remap = false)
    public void toggleWindows(CallbackInfo ci) {
        FluidTankBlockEntity be = getControllerBE();
        if (be == null)
            return;
        if (be.boiler.isActive())
            return;
        be.setWindows(((FluidTankBlockEntityAccessor)be).tfmg$getWindow());

        return;
    }

    @Inject(at = @At("HEAD"), method = "notifyMultiUpdated",cancellable = true , remap = false)
    public void notifyMultiUpdated(CallbackInfo ci) {
        BlockState state = this.getBlockState();
        if (AluminumTankBlock.isTank(state)|| CastIronTankBlock.isTank(state)) {
            state = state.setValue(FluidTankBlock.BOTTOM, getController().getY() == getBlockPos().getY());
            state = state.setValue(FluidTankBlock.TOP, getController().getY() + height - 1 == getBlockPos().getY());
            level.setBlock(getBlockPos(), state, 6);

        }
    }
    @Inject(at = @At("HEAD"), method = "removeController",cancellable = true , remap = false)
    public void removeController(boolean keepFluids, CallbackInfo ci) {
        if (level.isClientSide)
            return;

        BlockState state = getBlockState();
        if (AluminumTankBlock.isTank(state)|| CastIronTankBlock.isTank(state)) {
            state = state.setValue(FluidTankBlock.BOTTOM, true);
            state = state.setValue(FluidTankBlock.TOP, true);
            state = state.setValue(FluidTankBlock.SHAPE, window ? FluidTankBlock.Shape.WINDOW : FluidTankBlock.Shape.PLAIN);
            getLevel().setBlock(worldPosition, state, 22);
        }

    }
    @Inject(at = @At("HEAD"), method = "setWindows",cancellable = true , remap = false)
    public void setWindows(boolean window, CallbackInfo ci) {
        if(getBlockState().is(TFMGBlocks.ALUMINUM_FLUID_TANK.get())||getBlockState().is(TFMGBlocks.CAST_IRON_FLUID_TANK.get())) {
            this.window = window;
            for (int yOffset = 0; yOffset < height; yOffset++) {
                for (int xOffset = 0; xOffset < width; xOffset++) {
                    for (int zOffset = 0; zOffset < width; zOffset++) {

                        BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                        BlockState blockState = level.getBlockState(pos);

                        if (!AluminumTankBlock.isTank(blockState)&&!CastIronTankBlock.isTank(blockState))
                            continue;
                        FluidTankBlock.Shape shape = FluidTankBlock.Shape.PLAIN;
                        if (window) {
                            // SIZE 1: Every tank has a window
                            if (width == 1)
                                shape = FluidTankBlock.Shape.WINDOW;
                            // SIZE 2: Every tank has a corner window
                            if (width == 2)
                                shape = xOffset == 0 ? zOffset == 0 ? FluidTankBlock.Shape.WINDOW_NW : FluidTankBlock.Shape.WINDOW_SW
                                        : zOffset == 0 ? FluidTankBlock.Shape.WINDOW_NE : FluidTankBlock.Shape.WINDOW_SE;
                            // SIZE 3: Tanks in the center have a window
                            if (width == 3 && abs(abs(xOffset) - abs(zOffset)) == 1)
                                shape = FluidTankBlock.Shape.WINDOW;
                        }
                        level.setBlock(pos, blockState.setValue(FluidTankBlock.SHAPE, shape), 22);
                        level.getChunkSource()
                                .getLightEngine()
                                .checkBlock(pos);
                    }
                }
            }
            return;
        }
    }

    @Shadow
    public FluidTankBlockEntity getControllerBE() {
        if (isController())
            return (FluidTankBlockEntity) level.getBlockEntity(getBlockPos());
        BlockEntity blockEntity = level.getBlockEntity(controller);
        if (blockEntity instanceof FluidTankBlockEntity)
            return (FluidTankBlockEntity) blockEntity;
        return null;
    }
    @Shadow
    public boolean isController() {
        return controller == null || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }
    @Shadow
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    @Override
    @Shadow
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        registerAwardables(behaviours, AllAdvancements.STEAM_ENGINE_MAXED, AllAdvancements.PIPE_ORGAN);
    }
}
