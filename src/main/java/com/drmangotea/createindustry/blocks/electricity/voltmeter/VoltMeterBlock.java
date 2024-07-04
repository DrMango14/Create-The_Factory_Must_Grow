package com.drmangotea.createindustry.blocks.electricity.voltmeter;


import com.drmangotea.createindustry.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;


public class VoltMeterBlock extends TFMGHorizontalDirectionalBlock implements IBE<VoltMeterBlockEntity>, IWrenchable {


    public VoltMeterBlock(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.VOLTMETER.get(pState.getValue(FACING));
    }


    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();


        return onBlockEntityUse(level, pos, be -> {

            be.range = be.getRange() + 100;

            if(be.getRange() > 2000)
                be.range = 100;

            return InteractionResult.SUCCESS;


        });


    }

    @Override
    public Class<VoltMeterBlockEntity> getBlockEntityClass() {
        return VoltMeterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends VoltMeterBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.VOLTMETER.get();
    }
}
