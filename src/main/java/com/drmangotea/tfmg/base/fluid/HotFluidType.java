package com.drmangotea.tfmg.base.fluid;

import com.drmangotea.tfmg.registry.TFMGFluids;
import com.tterrag.registrate.builders.FluidBuilder;
import net.createmod.catnip.theme.Color;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class HotFluidType extends TFMGFluids.SolidRenderedPlaceableFluidType {

    public HotFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        super(properties, stillTexture, flowingTexture);
    }

    private Vector3f fogColor;
    private Supplier<Float> fogDistance;

    public static FluidBuilder.FluidTypeFactory  create(int fogColor, Supplier<Float> fogDistance) {
        return (p, s, f) -> {
            HotFluidType fluidType = new HotFluidType(p, s, f);
            fluidType.fogColor = new Color(fogColor, false).asVectorF();
            fluidType.fogDistance = fogDistance;
            return fluidType;
        };
    }

    @Override
    protected Vector3f getCustomFogColor() {
        return fogColor;
    }

    @Override
    protected float getFogDistanceModifier() {
        return fogDistance.get();
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public int getTemperature() {
        return 1270;
    }

    @Override
    public int getViscosity() {
        return 50;
    }

    @Override
    public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.6d));
        entity.setRemainingFireTicks(10);

        if(entity.getRandom().nextInt(30)==27)
            entity.lavaHurt();
        return false;
    }

    public boolean canExtinguish(Entity entity)
    {
        return false;
    }
}
