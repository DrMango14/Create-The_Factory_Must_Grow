package com.drmangotea.createindustry.registry;


import com.drmangotea.createindustry.CreateTFMG;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum TFMGGuiTextures implements ScreenElement {




    // JEI
    DISTILLATION_TOWER_TOP("distillation_tower", 0, 0, 44, 12),
    DISTILLATION_TOWER_MIDDLE("distillation_tower", 0, 12, 44, 24),
    DISTILLATION_TOWER_BOTTOM("distillation_tower", 0, 36, 44, 24),
    DISTILLATION_TOWER_FIRE("distillation_tower", 0, 60, 44, 12),

    ;



    public final ResourceLocation location;
    public int width, height;
    public int startX, startY;



    private TFMGGuiTextures(String location, int startX, int startY, int width, int height) {
        this(CreateTFMG.MOD_ID, location, startX, startY, width, height);
    }

    private TFMGGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(namespace, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @OnlyIn(Dist.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, location);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack ms, int x, int y) {
        bind();
        GuiComponent.blit(ms, x, y, 0, startX, startY, width, height, 256, 256);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y, GuiComponent component) {
        bind();
        component.blit(ms, x, y, startX, startY, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(ms, c, x, y, startX, startY, width, height);
    }

}
