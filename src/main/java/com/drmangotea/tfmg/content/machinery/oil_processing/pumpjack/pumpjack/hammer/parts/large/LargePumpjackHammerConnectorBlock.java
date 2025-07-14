package com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.parts.large;



import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.pumpjack.hammer.parts.PumpjackHammerConnectorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
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
