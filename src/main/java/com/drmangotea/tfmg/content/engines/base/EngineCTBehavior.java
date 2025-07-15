package com.drmangotea.tfmg.content.engines.base;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class EngineCTBehavior extends ConnectedTextureBehaviour.Base {

    protected CTSpriteShiftEntry topShift;
    protected CTSpriteShiftEntry bottomShift;
    protected CTSpriteShiftEntry sideShift;


    public EngineCTBehavior(CTSpriteShiftEntry topShift, CTSpriteShiftEntry bottomShift, CTSpriteShiftEntry sideShift){
        this.topShift = topShift;
        this.bottomShift = bottomShift;
        this.sideShift = sideShift;
    }

    @Override
    public @Nullable CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {

        if(direction.getAxis().isHorizontal())
            return sideShift;
        if(direction == Direction.UP)
            return topShift;
        if(direction == Direction.DOWN)
            return bottomShift;

        return null;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
       //if(reader.getBlockEntity(pos) instanceof AbstractEngineBlockEntity be && reader.getBlockEntity(otherPos) instanceof AbstractEngineBlockEntity be2){
       //    if(be.controller.asLong()==be2.controller.asLong())
       //        return true;
       //}
       // return false;
        return true;
    }

    @Override
    protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        if(face.getAxis().isVertical())
            return state.getValue(HORIZONTAL_FACING);
        return super.getUpDirection(reader, pos, state, face);
    }
}
