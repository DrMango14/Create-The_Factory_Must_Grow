package com.drmangotea.tfmg.content.electricity.configuration_wrench;

import com.drmangotea.tfmg.content.electricity.base.IElectric;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ElectricBlockItem extends BlockItem {
    public ElectricBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }



    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if(level.getBlockEntity(pos) instanceof IElectric be&&be.canBeInGroups()){
            be.getData().group.id = context.getItemInHand().get(TFMGDataComponents.CONFIGURATION_WRENCH_NUMBER);
            be.updateNextTick();
            be.sendStuff();
        }

        return super.place(context);
    }
}
