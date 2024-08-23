package com.drmangotea.createindustry.blocks.electricity.transformer;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CoilBlock extends Block implements IBE<CoilBlockEntity>, IWrenchable {

    public static final BooleanProperty CAN_EXTRACT = BooleanProperty.create("can_extract");

    public CoilBlock(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(defaultBlockState().setValue(CAN_EXTRACT, false));
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context) {


        context.getLevel().setBlock(context.getClickedPos(),state.setValue(CAN_EXTRACT,!state.getValue(CAN_EXTRACT)),2);


        return InteractionResult.SUCCESS;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CAN_EXTRACT);
    }
    @Override
    public Class<CoilBlockEntity> getBlockEntityClass() {
        return CoilBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CoilBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.COIL.get();
    }
}
