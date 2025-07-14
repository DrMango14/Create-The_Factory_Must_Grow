package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.base;

import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class DepositItem extends Item {
    public DepositItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if(level.getServer()==null)
            return InteractionResult.PASS;
        if(level.getBlockState(pos).is(TFMGBlocks.OIL_DEPOSIT.get())) {
            //DepositSavedData data = DepositSavedData.load(level.getServer());
           // if(data.getReservoirFor(pos.asLong())!=null) {
           //     data.getReservoirFor(pos.asLong()).oilReserves = level.getRandom().nextInt(1000, TFMGConfigs.common().deposits.depositMaxReserves.get());
           //     return InteractionResult.SUCCESS;
           // }
        }
        return InteractionResult.PASS;
    }
}
