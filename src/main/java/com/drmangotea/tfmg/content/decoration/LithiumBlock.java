package com.drmangotea.tfmg.content.decoration;

import com.drmangotea.tfmg.content.items.weapons.lithium_blade.LithiumSpark;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.simibubi.create.Create;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class LithiumBlock extends Block {
    public LithiumBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public boolean isRandomlyTicking(BlockState p_49921_) {
        return true;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        super.randomTick(blockState, level, pos, randomSource);

        for(Direction direction : Direction.values()){
            if(level.getFluidState(pos.relative(direction)).is(Fluids.WATER)){
                for (int i = 0; i < 12; i++) {
                    float x = Create.RANDOM.nextFloat(360);
                    float y = Create.RANDOM.nextFloat(360);
                    float z = Create.RANDOM.nextFloat(360);
                    LithiumSpark spark = TFMGEntityTypes.LITHIUM_SPARK.create(level);
                    spark.moveTo(pos.getX(), pos.getY() + 0.5, pos.getZ());

                    float f = -Mth.sin(y * ((float) Math.PI / 180F)) * Mth.cos(x * ((float) Math.PI / 180F));
                    float f1 = -Mth.sin((x + z) * ((float) Math.PI / 180F));
                    float f2 = Mth.cos(y * ((float) Math.PI / 180F)) * Mth.cos(x * ((float) Math.PI / 180F));
                    spark.shoot(f, f1, f2, 0.3f, 1);
                    level.addFreshEntity(spark);
                }
                level.explode(null, pos.getX(), pos.getY(), pos.getZ(), 1, Level.ExplosionInteraction.NONE);

                level.destroyBlock(pos,false);
                break;
            }

        }


    }
}
