package com.drmangotea.createindustry.base.util;

import com.drmangotea.createindustry.base.ElectricSparkParticle;
import com.drmangotea.createindustry.base.util.spark.Spark;
import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TFMGUtils {


    public static void createFireExplosion(Level level, Entity entity, BlockPos pos, int sparkAmount, float radius) {

        if (level.isClientSide&&entity!=null)
            level.broadcastEntityEvent(entity, (byte) 3);

        for (int i = 0; i < sparkAmount; i++) {
            float x = Create.RANDOM.nextFloat(360);
            float y = Create.RANDOM.nextFloat(360);
            float z = Create.RANDOM.nextFloat(360);
            Spark spark = TFMGEntityTypes.SPARK.create(level);
            spark.moveTo(pos.getX(), pos.getY() + 1, pos.getZ());

            float f = -Mth.sin(y * ((float) Math.PI / 180F)) * Mth.cos(x * ((float) Math.PI / 180F));
            float f1 = -Mth.sin((x + z) * ((float) Math.PI / 180F));
            float f2 = Mth.cos(y * ((float) Math.PI / 180F)) * Mth.cos(x * ((float) Math.PI / 180F));
            spark.shoot(f, f1, f2, 0.3f, 1);
            level.addFreshEntity(spark);
        }
        level.explode(null, pos.getX(), pos.getY(), pos.getZ(), radius, Explosion.BlockInteraction.BREAK);


    }

    public static String fromId(String key) {
        String s = key.replaceAll("_", " ");
        s = Arrays.stream(StringUtils.splitByCharacterTypeCamelCase(s)).map(StringUtils::capitalize).collect(Collectors.joining(" "));
        s = StringUtils.normalizeSpace(s);
        return s;
    }

    public static void spawnElectricParticles(Level level,BlockPos pos) {
        if (level == null)
            return;


        RandomSource r = level.getRandom();



        for(int i = 0; i < r.nextInt(40);i++) {
            float x = Create.RANDOM.nextFloat(2)-1;
            float y = Create.RANDOM.nextFloat(2)-1;
            float z = Create.RANDOM.nextFloat(2)-1;

            level.addParticle(new ElectricSparkParticle.Data(), pos.getX() + 0.5f+x, pos.getY() + 0.5f+y, pos.getZ() + 0.5f+z, x, y, z);


        }
    }

}
