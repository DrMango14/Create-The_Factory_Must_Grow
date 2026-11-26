package com.drmangotea.tfmg.content.machinery.oil_processing.surface_scanner;

import com.drmangotea.tfmg.base.TFMGShapes;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SurfaceScannerBlock extends Block implements IBE<SurfaceScannerBlockEntity>, IWrenchable {
    public SurfaceScannerBlock(Properties p) {
        super(p);
    }

    @Override
    public Class<SurfaceScannerBlockEntity> getBlockEntityClass() {
        return SurfaceScannerBlockEntity.class;
    }

    @Override @NotNull
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return TFMGShapes.SLAB;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return InteractionResult.PASS;
    }

	@Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
		if (!(level.getBlockEntity(pos) instanceof SurfaceScannerBlockEntity be))
			return 0;
        return be.getDirectionalSignal(side);
    }
	
	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
		return this.getSignal(state, level, pos, direction);
	}
	
	@Override
	public boolean shouldCheckWeakPower(final BlockState state, final SignalGetter level, final BlockPos pos, final Direction side) {
		return false;
	}
	
	@Override
	public boolean canConnectRedstone(final BlockState state, final BlockGetter level, final BlockPos pos, @Nullable final Direction direction) {
		return !(direction == null || direction.getAxis().isVertical());
	}
	
	@Override
    public BlockEntityType<? extends SurfaceScannerBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.SURFACE_SCANNER.get();
    }
}
