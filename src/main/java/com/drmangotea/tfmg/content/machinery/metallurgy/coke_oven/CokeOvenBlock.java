package com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven;

import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class CokeOvenBlock extends TFMGHorizontalDirectionalBlock implements IBE<CokeOvenBlockEntity>, IWrenchable {

    public static final EnumProperty<ControllerType> CONTROLLER_TYPE = EnumProperty.create("controller_type", ControllerType.class);
    public CokeOvenBlock(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(defaultBlockState().setValue(CONTROLLER_TYPE, ControllerType.CASUAL));
    }


    @Override
    public void onPlace(BlockState pState, Level level, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        if(!pOldState.is(TFMGBlocks.COKE_OVEN.get()))
            withBlockEntityDo(level,pos, CokeOvenBlockEntity::onPlaced);
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }


    @Override
    public Class<CokeOvenBlockEntity> getBlockEntityClass() {
        return CokeOvenBlockEntity.class;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTROLLER_TYPE);
    }

    @Override
    public BlockEntityType<? extends CokeOvenBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.COKE_OVEN.get();
    }
    public enum ControllerType implements StringRepresentable {

        CASUAL("casual"),
        TOP_ON("top_on"),
        MIDDLE_ON("middle_on"),
        BOTTOM_ON("bottom_on");

        private final String name;

        ControllerType(String name) {
            this.name = name;
        }


        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
