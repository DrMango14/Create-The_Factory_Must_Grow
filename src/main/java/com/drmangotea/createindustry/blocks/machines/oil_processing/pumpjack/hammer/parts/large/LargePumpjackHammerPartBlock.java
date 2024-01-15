package com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.large;

import com.drmangotea.createindustry.blocks.machines.oil_processing.pumpjack.hammer.parts.PumpjackHammerPartBlock;
import com.drmangotea.createindustry.registry.TFMGShapes;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.placement.PoleHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Predicate;

public class LargePumpjackHammerPartBlock extends PumpjackHammerPartBlock {

    public LargePumpjackHammerPartBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter worldIn, BlockPos pos, CollisionContext context) {

        return TFMGShapes.FULL;
    }

}
