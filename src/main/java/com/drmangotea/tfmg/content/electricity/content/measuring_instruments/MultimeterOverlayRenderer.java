package com.drmangotea.tfmg.content.electricity.content.measuring_instruments;

import com.drmangotea.tfmg.content.electricity.IHaveMultimeterInformation;
import com.drmangotea.tfmg.integration.curios.MultimeterCurios;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.api.equipment.goggles.IProxyHoveringInformation;
import com.simibubi.create.compat.Mods;
import com.simibubi.create.content.equipment.goggles.GoggleOverlayRenderer;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.gui.RemovedGuiUtils;
import com.simibubi.create.foundation.mixin.accessor.MouseHandlerAccessor;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CClient;
import net.createmod.catnip.gui.element.BoxElement;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.outliner.Outline;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.catnip.outliner.Outliner.OutlineEntry;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultimeterOverlayRenderer {

    public static final LayeredDraw.Layer OVERLAY = MultimeterOverlayRenderer::renderOverlay;
    private static final Map<Object, OutlineEntry> outlines = Outliner.getInstance().getOutlines();

    public static int hoverTicks = 0;
    public static BlockPos lastHovered = null;

    public static void renderOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();

        // If the player is a spectator, or he hides the GUI, do not render the overlay.
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            return;
        }

        if (!(mc.hitResult instanceof BlockHitResult result)) {
            lastHovered = null;
            hoverTicks = 0;
            return;
        }

        for (OutlineEntry entry : outlines.values()) {
            if (!entry.isAlive())
                continue;
            Outline outline = entry.getOutline();
            if (outline instanceof ValueBox box && !box.isPassive)
                return;
        }

        ClientLevel world = mc.level;
        BlockPos pos = result.getBlockPos();

        if (world == null || mc.player == null ||
			!(MultimeterCurios.getHeldByPlayer(mc.player) instanceof MultimeterItem multimeterItem)
		) {
            lastHovered = null;
            hoverTicks = 0;
            return;
        }

        boolean wearingGoggles = GogglesItem.isWearingGoggles(mc.player);

        hoverTicks++;
        lastHovered = pos;

        pos = proxiedOverlayPosition(world, pos);

        if(!(world.getBlockEntity(pos) instanceof IHaveMultimeterInformation info))
            return;

        List<Component> tooltip = new ArrayList<>();

        // The block entity is responsible for making it's own Tooltip.
		info.makeMultimeterTooltip(tooltip, mc.player.isCrouching());

        PoseStack poseStack = graphics.pose();
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

        CClient cfg = AllConfigs.client();
        int posX = width / 2 + cfg.overlayOffsetX.get();
        int posY = height / 2 + cfg.overlayOffsetY.get();

        posX = Math.min(posX, width - tooltipTextWidth - 20);
        posY = Math.min(posY, height - tooltipHeight - 20);

        int animationTicks = wearingGoggles ? GoggleOverlayRenderer.hoverTicks : hoverTicks;
        float fade = Mth.clamp((animationTicks + deltaTracker.getGameTimeDeltaPartialTick(false)) / 24f, 0, 1);
        Boolean useCustom = cfg.overlayCustomColor.get();
        Color colorBackground = useCustom ? new Color(cfg.overlayBackgroundColor.get())
                : BoxElement.COLOR_VANILLA_BACKGROUND.scaleAlpha(.75f);
        Color colorBorder = new Color(multimeterItem.color).setAlpha(100);

        if (fade < 1) {
            poseStack.translate(Math.pow(1 - fade, 3) * Math.signum(cfg.overlayOffsetX.get() + .5f) * 8, 0, 0);

            colorBackground.scaleAlpha(fade);
			colorBorder.scaleAlpha(fade);
        }

        // Add a multimeter to indicate both items are being utilised
        if (wearingGoggles) {
            GuiGameElement.of(multimeterItem)
                    .at(posX + 10, posY, 450)
                    .render(graphics);
            poseStack.popPose();
            return;
        }

        GuiGameElement.of(multimeterItem)
                .at(posX + 10, posY - 15, 450)
                .render(graphics);

        if (!Mods.MODERNUI.isLoaded()) {
            // default tooltip rendering when modernUI is not loaded
            RemovedGuiUtils.drawHoveringText(graphics, tooltip, posX, posY, width, height, -1, colorBackground.getRGB(),
				colorBorder.getRGB(), colorBorder.getRGB(), mc.font);

            poseStack.popPose();

            return;
        }

        MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;
        Window window = Minecraft.getInstance().getWindow();
        double guiScale = window.getGuiScale();
        double cursorX = mouseHandler.xpos();
        double cursorY = mouseHandler.ypos();
        ((MouseHandlerAccessor) mouseHandler).create$setXPos(Math.round(cursorX / guiScale) * guiScale);
        ((MouseHandlerAccessor) mouseHandler).create$setYPos(Math.round(cursorY / guiScale) * guiScale);

        RemovedGuiUtils.drawHoveringText(graphics, tooltip, posX, posY, width, height, -1, colorBackground.getRGB(),
			colorBorder.getRGB(), colorBorder.getRGB(), mc.font);

        ((MouseHandlerAccessor) mouseHandler).create$setXPos(cursorX);
        ((MouseHandlerAccessor) mouseHandler).create$setYPos(cursorY);

        poseStack.popPose();
    }

    public static BlockPos proxiedOverlayPosition(Level level, BlockPos pos) {
        BlockState targetedState = level.getBlockState(pos);
        if (targetedState.getBlock() instanceof IProxyHoveringInformation proxy)
            return proxy.getInformationSource(level, pos, targetedState);
        return pos;
    }

}
