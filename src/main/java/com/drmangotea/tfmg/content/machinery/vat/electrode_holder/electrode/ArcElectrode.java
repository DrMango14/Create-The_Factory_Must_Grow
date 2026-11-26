package com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode;

import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.registry.TFMGVatOperations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;

public class ArcElectrode extends Electrode {
    public ArcElectrode(Properties properties) {
        super(properties.operationId(TFMGVatOperations.GRAPHITE_ELECTRODE));
    }

    @Override
    public void tick(VatBlockEntity controllerVat, Level level, BlockPos pos, boolean active, boolean clientTick) {
        if (active && clientTick) {
            ParticleUtils.spawnParticlesAlongAxis(Direction.Axis.Y, level, pos, 0.25F, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(1, 2));
        }
    }
}
