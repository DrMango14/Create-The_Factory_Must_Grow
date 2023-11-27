package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.CreateTFMG;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;


import net.minecraft.client.gui.GuiGraphics;
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

    private TFMGGuiTextures(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    private TFMGGuiTextures(int startX, int startY) {
        this("icons", startX * 16, startY * 16, 16, 16);
    }

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
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(location, x, y, startX, startY, width, height);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int x, int y, Color c) {
        bind();
        UIRenderHelper.drawColoredTexture(graphics, c, x, y, startX, startY, width, height);
    }

}
