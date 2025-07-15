package com.drmangotea.tfmg.mixin;


import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.content.electricity.measurement.MultimeterItem;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.api.equipment.goggles.IProxyHoveringInformation;
import com.simibubi.create.compat.Mods;
import com.simibubi.create.content.equipment.goggles.GoggleOverlayRenderer;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import com.simibubi.create.foundation.mixin.accessor.MouseHandlerAccessor;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CClient;
import net.createmod.catnip.gui.element.BoxElement;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(GoggleOverlayRenderer.class)
public class GoggleOverlayRendererMixin {

    /**
     * handles multimeter tooltips
     */

    @Shadow
    private static final Map<Object, Outliner.OutlineEntry> outlines = Outliner.getInstance().getOutlines();

    private static int tfmg$hoverTicks = 0;

    private static BlockPos tfmg$lastHovered = null;

    @Inject(at = @At("HEAD"), method = "renderOverlay", cancellable = true, remap = false)
    private static void renderOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        HitResult objectMouseOver = mc.hitResult;


        if (!(objectMouseOver instanceof BlockHitResult result)) {
            tfmg$lastHovered = null;
            tfmg$hoverTicks = 0;
            return;
        }


        BlockPos pos = result.getBlockPos();

        pos = proxiedOverlayPosition(world, pos);

        BlockEntity be = world.getBlockEntity(pos);


        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR)
            return;

        // for (Outliner.OutlineEntry entry : outlines.values()) {
        //     if (!entry.isAlive())
        //         continue;
        //     Outline outline = entry.getOutline();
        //     if (outline instanceof ValueBox && !((ValueBox) outline).isPassive)
        //         return;
        // }

        tfmg$hoverTicks++;
        tfmg$lastHovered = pos;


        boolean holdsMultimeter = MultimeterItem.isHeldByPlayer(mc.player);

        boolean hasGoggles = GogglesItem.isWearingGoggles(mc.player);

        boolean isShifting = mc.player.isShiftKeyDown();

        boolean isElectricBlock = be instanceof IElectric;


        if (isElectricBlock && !hasGoggles) {
            ItemStack item = TFMGItems.MULTIMETER.asStack();
            List<Component> tooltip = new ArrayList<>();

            ((IElectric) be).makeMultimeterTooltip(tooltip, isShifting);

            // break early if goggle or hover returned false when present
            if ((!isElectricBlock) || !holdsMultimeter) {
                tfmg$hoverTicks = 0;

            } else {

                if (tooltip.isEmpty()) {
                    tfmg$hoverTicks = 0;
                } else {

                    PoseStack poseStack = guiGraphics.pose();
                    poseStack.pushPose();

                    int tooltipTextWidth = 0;
                    for (FormattedText textLine : tooltip) {
                        int textLineWidth = mc.font.width(textLine);
                        if (textLineWidth > tooltipTextWidth)
                            tooltipTextWidth = textLineWidth;
                    }

                    int tooltipHeight = 8;
                    if (tooltip.size() > 1) {
                        tooltipHeight += 2; // gap between title lines and next lines
                        tooltipHeight += (tooltip.size() - 1) * 10;
                    }

                    int width = guiGraphics.guiWidth();
                    int height = guiGraphics.guiHeight();

                    CClient cfg = AllConfigs.client();
                    int posX = width / 2 + cfg.overlayOffsetX.get();
                    int posY = height / 2 + cfg.overlayOffsetY.get();

                    posX = Math.min(posX, width - tooltipTextWidth - 20);
                    posY = Math.min(posY, height - tooltipHeight - 20);

                    float fade = Mth.clamp((tfmg$hoverTicks + deltaTracker.getGameTimeDeltaPartialTick(false)) / 24f, 0, 1);
                    Boolean useCustom = cfg.overlayCustomColor.get();
                    Color colorBackground = useCustom ? new Color(cfg.overlayBackgroundColor.get())
                            : BoxElement.COLOR_VANILLA_BACKGROUND.scaleAlpha(.75f);
                    Color colorBorderTop = useCustom ? new Color(cfg.overlayBorderColorTop.get())
                            : BoxElement.COLOR_VANILLA_BORDER.getFirst().copy();
                    Color colorBorderBot = useCustom ? new Color(cfg.overlayBorderColorBot.get())
                            : BoxElement.COLOR_VANILLA_BORDER.getSecond().copy();

                    if (fade < 1) {
                        poseStack.translate(Math.pow(1 - fade, 3) * Math.signum(cfg.overlayOffsetX.get() + .5f) * 8, 0, 0);
                        colorBackground.scaleAlpha(fade);
                        colorBorderTop.scaleAlpha(fade);
                        colorBorderBot.scaleAlpha(fade);
                    }

                    GuiGameElement.of(item)
                            .at(posX + 10, posY - 16, 450)
                            .render(guiGraphics);

                    if (!Mods.MODERNUI.isLoaded()) {
                        // default tooltip rendering when modernUI is not loaded
                        RemovedGuiUtils.drawHoveringText(guiGraphics, tooltip, posX, posY, width, height, -1, colorBackground.getRGB(),
                                colorBorderTop.getRGB(), colorBorderBot.getRGB(), mc.font);

                        poseStack.popPose();

                        return;
                    }

                    /*
                     * special handling for modernUI
                     *
                     * their tooltip handler causes the overlay to jiggle each frame,
                     * if the mouse is moving, guiScale is anything but 1 and exactPositioning is enabled
                     *
                     * this is a workaround to fix this behavior
                     */
                    MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;
                    Window window = Minecraft.getInstance().getWindow();
                    double guiScale = window.getGuiScale();
                    double cursorX = mouseHandler.xpos();
                    double cursorY = mouseHandler.ypos();
                    ((MouseHandlerAccessor) mouseHandler).create$setXPos(Math.round(cursorX / guiScale) * guiScale);
                    ((MouseHandlerAccessor) mouseHandler).create$setYPos(Math.round(cursorY / guiScale) * guiScale);

                    RemovedGuiUtils.drawHoveringText(guiGraphics, tooltip, posX, posY, width, height, -1, colorBackground.getRGB(),
                            colorBorderTop.getRGB(), colorBorderBot.getRGB(), mc.font);

                    ((MouseHandlerAccessor) mouseHandler).create$setXPos(cursorX);
                    ((MouseHandlerAccessor) mouseHandler).create$setYPos(cursorY);
                    poseStack.popPose();

                }
            }
        }
    }

    @Shadow
    public static BlockPos proxiedOverlayPosition(Level level, BlockPos pos) {
        BlockState targetedState = level.getBlockState(pos);
        if (targetedState.getBlock() instanceof IProxyHoveringInformation proxy)
            return proxy.getInformationSource(level, pos, targetedState);
        return pos;
    }
}
