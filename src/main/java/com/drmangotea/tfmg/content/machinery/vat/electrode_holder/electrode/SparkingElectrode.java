package com.drmangotea.tfmg.content.machinery.vat.electrode_holder.electrode;

import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;

public class SparkingElectrode extends Electrode {
    public SparkingElectrode(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(VatBlockEntity controllerVat, Level level, BlockPos pos, boolean active, boolean clientTick) {
        if (active && clientTick) {
            if (level.getGameTime() % 10 != 0) return;
            var random = level.getRandom();

            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5);
            double y = pos.getY() + 0.5 + (random.nextDouble() - 0.5);
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5);

            double vx = (random.nextDouble() - 0.5) * 0.02;
            double vy = (random.nextDouble() - 0.5) * 0.02;
            double vz = (random.nextDouble() - 0.5) * 0.02;

            level.addParticle(ParticleTypes.END_ROD, x, y, z, vx, vy, vz);
        }
    }
}
