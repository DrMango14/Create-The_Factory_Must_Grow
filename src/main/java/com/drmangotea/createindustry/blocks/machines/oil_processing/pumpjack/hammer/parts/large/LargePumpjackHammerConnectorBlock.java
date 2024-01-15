package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.large;

import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.PumpjackHammerConnectorBlock;
import com.drmangotea.createindustry.registry.TFMGShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LargePumpjackHammerConnectorBlock extends PumpjackHammerConnectorBlock {

    public LargePumpjackHammerConnectorBlock(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.FULL;
    }

}
