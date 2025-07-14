package com.drmangotea.tfmg.content.electricity.configuration_wrench;

import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ElectriciansWrenchScreen extends AbstractSimiScreen {

    ItemStack wrench;
    protected InteractionHand hand;
    private IconButton addButton;
    private IconButton subtractButton;
    private IconButton confirmButton;

    int group;

    public ElectriciansWrenchScreen(ItemStack wrench, InteractionHand hand) {
        this.wrench = wrench;
        this.hand = hand;

        if (wrench.get(TFMGDataComponents.CONFIGURATION_WRENCH_NUMBER) != null)
            this.group = wrench.get(TFMGDataComponents.CONFIGURATION_WRENCH_NUMBER);
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = guiLeft;
        int y = guiTop;

        //int value = wrench.getOrCreateTag().getInt("Number");
        int value = group;
        String valueString = String.valueOf(value);
        background().render(graphics, x, y);
        graphics.drawString(font, valueString, x + 90 - (3 * valueString.length()), y + 39, 0xffffff, false);
        graphics.drawString(font, "Select Group Id", x + 51, y + 4, 0xffffff, false);

        GuiGameElement.of(wrench)
                .scale(4)
                .rotate(0, 0, 0)
                .at(x + 180, y + 50)
                .render(graphics);
    }

    @Override
    public void init() {
        setWindowSize(background().width, background().height);
        setWindowOffset(-20, 0);
        super.init();
        int x = guiLeft;
        int y = guiTop;


        confirmButton = new IconButton(x + background().width - 33, y + background().height - 24, AllIcons.I_CONFIRM);
        confirmButton.withCallback(this::onClose);
        addButton = new IconButton(x + background().width - 84, y + background().height - 67, AllIcons.I_MTD_RIGHT);
        addButton.withCallback(this::addNumber);
        subtractButton = new IconButton(x + background().width - 130, y + background().height - 67, AllIcons.I_MTD_LEFT);
        subtractButton.withCallback(this::substractNumber);
        addRenderableWidget(confirmButton);
        addRenderableWidget(addButton);
        addRenderableWidget(subtractButton);
    }

    public void addNumber() {
        group++;
        // CompoundTag tag = wrench.getOrCreateTag();
//
        // int number = tag.getInt("Number");
        // tag.putInt("Number", number+1);
//
        // ElectritiansWrenchPacket packet = new ElectritiansWrenchPacket(tag.getInt("Number"), hand);
        // packet.applyGroup(wrench);
        // TFMGPackets.getChannel().sendToServer(packet);
    }

    @Override
    public void onClose() {
        super.onClose();


        wrench.set(TFMGDataComponents.CONFIGURATION_WRENCH_NUMBER, group);

        ElectriciansWrenchPacket packet = new ElectriciansWrenchPacket(group, hand);
        packet.applyGroup(wrench);
        CatnipServices.NETWORK.sendToServer(packet);
    }

    public void substractNumber() {
        if (group > 0)
            group--;

        //CompoundTag tag = wrench.getOrCreateTag();
//
        //int number = tag.getInt("Number");
        //if(number>0)
        //    tag.putInt("Number", number-1);
//
        //ElectritiansWrenchPacket packet = new ElectritiansWrenchPacket(tag.getInt("Number"), hand);
        //packet.applyGroup(wrench);
        //TFMGPackets.getChannel().sendToServer(packet);
    }

    public TFMGGuiTextures background() {
        return TFMGGuiTextures.ELECTRICIANS_WRENCH;
    }

}
