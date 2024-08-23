package com.drmangotea.createindustry.blocks.electricity.electrical_switch;

import com.drmangotea.createindustry.registry.TFMGShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ElectricalSwitchBlock extends LeverBlock {
    public ElectricalSwitchBlock(Properties p_54633_) {
        super(p_54633_);
    }
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        AttachFace face = state.getValue(FACE);
        Direction direction = state.getValue(FACING);
        return face == AttachFace.CEILING ? TFMGShapes.ELECTRICAL_SWITCH_CEILING.get(direction.getAxis())
                : face == AttachFace.FLOOR ? TFMGShapes.ELECTRICAL_SWITCH.get(direction.getAxis())
                : TFMGShapes.ELECTRICAL_SWITCH_WALL.get(direction);
    }
}
