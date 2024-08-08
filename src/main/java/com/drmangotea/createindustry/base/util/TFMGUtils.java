package com.drmangotea.createindustry.base.util;

import com.drmangotea.createindustry.base.ElectricSparkParticle;
import com.drmangotea.createindustry.base.util.spark.Spark;
import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import com.simibubi.create.Create;
import io.github.fabricators_of_create.porting_lib.mixin.common.ProjectileUtilMixin;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
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

    public static void drain(FluidTank tank, long amount) {
        try (Transaction t = TransferUtil.getTransaction()) {
            tank.extract(tank.variant, amount, t);
            t.commit();
        }
    }

    public static long fill(FluidTank tank, FluidStack fluid) {
        if (!tank.isEmpty() && tank.variant != fluid.getType())
            throw new RuntimeException("fluid variant being filled into tank isn't the same variant as the fluid in the tank");
        try (Transaction t = TransferUtil.getTransaction()) {
            tank.insert(fluid.getType(), fluid.getAmount(), t);
            t.commit();
        }
        return fluid.getAmount();
    }

//    public static boolean mobGriefingEvent(Level level, Entity entity) {
//        EntityEvent event = ProjectileUtil.;
//        MinecraftForge.EVENT_BUS.post(event);
//        boolean result = event.getResult();
//        return result == Result.DEFAULT ? level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) : result == Result.ALLOW;
//    }


}
