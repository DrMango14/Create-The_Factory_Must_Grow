package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.energy_storage.accumulator_rack;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.registry.TFMGGuiTextures;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class AccumulatorRackScreen extends AbstractSimiContainerScreen<AccumulatorRackMenu> {

    private TFMGGuiTextures background;

    private ItemStack icon;

    private IconButton confirmButton;

    private List<Rect2i> extraAreas = Collections.emptyList();

    public AccumulatorRackScreen(AccumulatorRackMenu container, Inventory inv, Component title) {
        super(container, inv, TFMGLang.translateDirect("gui.threshold_switch.title"));
        background = TFMGGuiTextures.ACCUMULATOR_RACK;
        icon = new ItemStack(container.contentHolder.getBlockState()
                .getBlock()
                .asItem());
    }

    @Override
    protected void init() {
        setWindowSize(background.getWidth(), background.getHeight() + AllGuiTextures.PLAYER_INVENTORY.getHeight());
        super.init();
        clearWidgets();

        int x = getGuiLeft();
        int y = getGuiTop()+15;

        confirmButton =
                new IconButton(x + background.getWidth() - 33, y + background.getHeight()-59 , AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> minecraft.player.closeContainer());
        addRenderableWidget(confirmButton);


        containerTick();

        extraAreas = ImmutableList.of(new Rect2i(x + background.getWidth(), y + background.getHeight() - 80, 70, 60));
    }


    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = getGuiLeft();
        int y = getGuiTop();

        background.render(graphics, x, y-20);


        GuiGameElement.of(icon).<GuiGameElement
                        .GuiRenderBuilder>at(x + background.getWidth() + 6, y + background.getHeight() - 70, -200)
                .scale(4)
                .render(graphics);

        int invX = leftPos + 30;
        int invY = topPos + 8 + imageHeight-15 - AllGuiTextures.PLAYER_INVENTORY.getHeight();
        renderPlayerInventory(graphics, invX, invY);

    }


    @Override
    public List<Rect2i> getExtraAreas() {
        return extraAreas;
    }

}