package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;
import com.mojang.blaze3d.systems.RenderSystem;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.ScreenElement;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


public enum TFMGGuiTextures implements ScreenElement {


    // SCREENS
    ELECTRICIANS_WRENCH("electricians_wrench", 0, 0, 188, 101),
    ENGINE_CONTROLLER("engine_controller", 0, 0, 179, 109),
    // JEI
    DISTILLATION_TOWER_TOP("distillation_tower", 0, 0, 44, 12),
    DISTILLATION_TOWER_MIDDLE("distillation_tower", 0, 12, 44, 24),
    DISTILLATION_TOWER_BOTTOM("distillation_tower", 0, 36, 44, 24),
    DISTILLATION_TOWER_FIRE("distillation_tower", 0, 60, 44, 12),
    BLAST_STOVE("distillation_tower", 60, 0, 45, 105),
    VAT("chemical_vat", 0, 0, 110, 84),
    VAT_MACHINE("chemical_vat", 112, 0, 24, 24),
    SLOT("chemical_vat", 112, 24, 20, 20),
    MIXER("chemical_vat", 136, 0, 38, 29),
    CENTRIFUGE("chemical_vat", 143, 30, 24, 29),
    ELECTRODE("chemical_vat", 189, 0, 8, 29),
    GRAPHITE_ELECTRODE("chemical_vat", 176, 0, 8, 29),
    FIREPROOF_BRICK_OVERLAY("chemical_vat", 0, 84, 96, 72),
    CAST_IRON_VAT_OVERLAY("chemical_vat", 0, 156, 110, 84),
    VAT_HEATER("chemical_vat", 112, 44, 20, 14),
    VAT_SUPERHEATER("chemical_vat", 112, 58, 20, 14),

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
        this(TFMG.MOD_ID, location, startX, startY, width, height);
    }

    private TFMGGuiTextures(String namespace, String location, int startX, int startY, int width, int height) {
        this.location = ResourceLocation.fromNamespaceAndPath(namespace, "textures/gui/" + location + ".png");
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
