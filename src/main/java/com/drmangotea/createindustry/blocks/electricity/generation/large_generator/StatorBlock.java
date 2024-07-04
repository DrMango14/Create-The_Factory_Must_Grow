package com.drmangotea.createindustry.blocks.electricity.generation.large_generator;

import com.drmangotea.createindustry.blocks.electricity.base.WallMountBlock;
import com.drmangotea.createindustry.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class StatorBlock extends WallMountBlock implements IBE<StatorBlockEntity>, IWrenchable {

    public static final BooleanProperty VALUE = BooleanProperty.create("value");

    public static final EnumProperty<StatorState> STATOR_STATE = EnumProperty.create("stator_state", StatorState.class);

    public StatorBlock(Properties properties) {
        super(properties);
    }




    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(STATOR_STATE).add(VALUE);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockEntity be1 = level.getBlockEntity(pos);
        if (be1 instanceof StatorBlockEntity be){
            if(be.rotor==null){

                level.setBlock(pos,state.setValue(STATOR_STATE,StatorState.UNUSED),2);


                return InteractionResult.SUCCESS;
            }else {
                if(state.getValue(STATOR_STATE) == StatorState.SIDE){

                    RotorBlockEntity rotor = (RotorBlockEntity) level.getBlockEntity(be.rotor);

                    if(!be.hasOutput) {

                        if (rotor == null)
                            return InteractionResult.PASS;

                        for (BlockPos otherPos : rotor.statorBlocks) {
                            if (level.getBlockEntity(otherPos) instanceof StatorBlockEntity otherStator) {
                                if (otherStator.hasOutput)
                                    return InteractionResult.PASS;
                            }
                        }

                        be.hasOutput = true;
                        return InteractionResult.SUCCESS;

                    }else {
                        be.hasOutput = false;
                        return InteractionResult.SUCCESS;
                    }
                }
            }

        }

        return InteractionResult.PASS;
    }

    @Override
    public Class<StatorBlockEntity> getBlockEntityClass() {
        return StatorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StatorBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.STATOR.get();
    }

    public enum StatorState implements StringRepresentable{


        UNUSED("unused"),
        SIDE("side"),
        CORNER("corner")


        ;
        final String name;
        StatorState(String name){
            this.name = name;
        }


        @Override
        public String getSerializedName() {
            return name;
        }
    }





}
