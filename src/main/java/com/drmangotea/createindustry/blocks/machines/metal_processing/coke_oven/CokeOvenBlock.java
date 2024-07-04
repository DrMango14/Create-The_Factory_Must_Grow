package com.drmangotea.createindustry.blocks.machines.metal_processing.coke_oven;

import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class CokeOvenBlock extends HorizontalDirectionalBlock implements IBE<CokeOvenBlockEntity>, IWrenchable {

    public static final EnumProperty<ControllerType> CONTROLLER_TYPE = EnumProperty.create("controller_type", ControllerType.class);

    public CokeOvenBlock(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(defaultBlockState().setValue(CONTROLLER_TYPE, ControllerType.CASUAL));

    }
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }
    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity be = level.getBlockEntity(pos);
        if(be instanceof CokeOvenBlockEntity)
            if(((CokeOvenBlockEntity) be).isValid()) {
                ((CokeOvenBlockEntity) be).isController = !((CokeOvenBlockEntity) be).isController;
                level.setBlock(pos,state.setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.CASUAL),2);
                if(level.getBlockEntity(pos.below())instanceof CokeOvenBlockEntity)
                    level.setBlock(pos.below(),level.getBlockState(pos.below()).setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.CASUAL).setValue(FACING,state.getValue(FACING)),2);
                if(level.getBlockEntity(pos.above())instanceof CokeOvenBlockEntity)
                    level.setBlock(pos.above(),level.getBlockState(pos.above()).setValue(CONTROLLER_TYPE, CokeOvenBlock.ControllerType.CASUAL).setValue(FACING,state.getValue(FACING)),2);

            }


    return InteractionResult.SUCCESS;

    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING,CONTROLLER_TYPE);
        super.createBlockStateDefinition(pBuilder);
    }
    public BlockState getStateForPlacement(BlockPlaceContext p_48781_) {
        return this.defaultBlockState().setValue(FACING, p_48781_.getHorizontalDirection().getOpposite()).setValue(CONTROLLER_TYPE,ControllerType.CASUAL);
    }
    @Override
    public Class<CokeOvenBlockEntity> getBlockEntityClass() {
        return CokeOvenBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CokeOvenBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.COKE_OVEN.get();
    }

    public enum ControllerType implements StringRepresentable {

        CASUAL("casual"),
        TOP_OFF("top_off"),
        TOP_ON("top_on"),
        MIDDLE_OFF("middle_off"),
        MIDDLE_ON("middle_on"),
        BOTTOM_OFF("bottom_off"),
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
