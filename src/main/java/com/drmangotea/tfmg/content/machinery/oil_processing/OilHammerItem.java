package com.drmangotea.tfmg.content.machinery.oil_processing;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class OilHammerItem extends Item {
    public OilHammerItem(Properties p_41383_) {
        super(p_41383_);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        for(int i = 0;i<300;i++){
            BlockPos posToCheck = pos.below(i);
            if(level.getBlockState(posToCheck).is(TFMGBlocks.OIL_DEPOSIT.get())){
                if(TFMG.DEPOSITS.getReservoirFor(posToCheck.asLong())==null)
                    return InteractionResult.SUCCESS;
                int oilReserves = TFMG.DEPOSITS.getReservoirFor(posToCheck.asLong()).oilReserves;

                if (level.isClientSide&&player!=null)
                    player.displayClientMessage(CreateLang.translateDirect("oil_hammer.reserves", oilReserves)
                            .withStyle(ChatFormatting.YELLOW), true);

                return InteractionResult.SUCCESS;
            }
        }



        return InteractionResult.SUCCESS;
    }
}
