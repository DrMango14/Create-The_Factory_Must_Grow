package com.drmangotea.createindustry.blocks.engines.small.lpg;


import com.drmangotea.createindustry.blocks.engines.small.AbstractEngineBlockEntity;
import com.drmangotea.createindustry.registry.TFMGBlocks;
import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class LPGEngineTileEntity extends AbstractEngineBlockEntity {


    public LPGEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }
    @Override
    public boolean hasBackPart(){
        BlockPos wantedLocation=this.getBlockPos();
        Direction direction = this.getBlockState().getValue(DirectionalBlock.FACING);


        if(direction == Direction.UP)
            wantedLocation=this.getBlockPos().below();
        if(direction == Direction.DOWN)
            wantedLocation=this.getBlockPos().above();
        if(direction == Direction.NORTH)
            wantedLocation=this.getBlockPos().south();
        if(direction == Direction.SOUTH)
            wantedLocation=this.getBlockPos().north();
        if(direction == Direction.WEST)
            wantedLocation=this.getBlockPos().east();
        if(direction == Direction.EAST)
            wantedLocation=this.getBlockPos().west();



        if(!level.getBlockState(wantedLocation).is(TFMGBlocks.LPG_ENGINE_BACK.get())) {
            return false;
        }else {
            if ( level.getBlockState(wantedLocation).getValue(DirectionalBlock.FACING) != direction)
                return false;
        }
        return true;
    }
    @Override
    public TagKey<Fluid> validFuel() {
        return TFMGTags.TFMGFluidTags.LPG.tag;
    }
}
