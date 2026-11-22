package com.drmangotea.tfmg.content.electricity.content.power_transmission.cables.cable_hubs;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.content.decoration.encasing.EncasedBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

@NothingNullByDefault
public class CableHubBlock extends Block implements IBE<CableHubBlockEntity>, EncasedBlock {
	private Supplier<Block> casing = () -> null;
	
    public CableHubBlock(Properties properties) {
        super(properties);
    }
	
	public CableHubBlock(Properties properties, Supplier<Block> casing) {
		super(properties);
		this.casing = casing;
	}
	
	public Block getCasing() {
		return casing.get();
	}
	
	
	@Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
		withBlockEntityDo(level, pos, IElectric::onPlace);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
        super.onRemove(state,level,pos,newState,isMoving);
    }

    @Override
    public Class<CableHubBlockEntity> getBlockEntityClass() {
        return CableHubBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CableHubBlockEntity> getBlockEntityType() {
        return TFMGBlockEntities.DEBUG_CABLE_HUB.get();
    }
}
