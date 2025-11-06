package com.drmangotea.tfmg.base.fluid;


import com.drmangotea.tfmg.registry.TFMGFluids;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tterrag.registrate.builders.FluidBuilder;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;

import static com.drmangotea.tfmg.registry.TFMGFluids.getGasTexture;

public class GasFluidType extends TFMGFluids.SolidRenderedPlaceableFluidType {

    final int color;



    public GasFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, int color) {
        super(properties, stillTexture, flowingTexture);
        this.color = color;
    }

    public static FluidBuilder.FluidTypeFactory  create(int color) {
        return (p, s, f) -> {
            GasFluidType fluidType = new GasFluidType(p,s,f,color);
            return fluidType;
        };
    }
    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() {
                return getGasTexture();
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return getGasTexture();
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return color;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return 0xff99f22f;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                Vector3f customFogColor = GasFluidType.this.getCustomFogColor();
                return customFogColor == null ? fluidFogColor : customFogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                float modifier = GasFluidType.this.getFogDistanceModifier();
                float baseWaterFog = 96.0f;
                if (modifier != 1f) {
                    RenderSystem.setShaderFogShape(FogShape.CYLINDER);
                    RenderSystem.setShaderFogStart(-8);
                    RenderSystem.setShaderFogEnd(baseWaterFog * modifier);
                }
            }

        });
    }


    @Override
    public int getDensity() {
        return -1;
    }
}