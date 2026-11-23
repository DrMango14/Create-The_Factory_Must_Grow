package com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large;

import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricalNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.simulation.Transformer;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.placement.IPlacementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.drmangotea.tfmg.content.electricity.content.power_transmission.switches.large_switch.LargeSwitchBlock.IS_MAIN_PART;

public class LargeTransformerBlockEntity extends SmartBlockEntity implements IElectric {
    public boolean updateInFront = false;
    public float turnRatio = 0;
    ElectricalProperties p;

    public double[] primaryVoltage = new double[3];

    public double[] secondaryVoltage = new double[3];

    public ItemStack primaryCoil = ItemStack.EMPTY;
    public ItemStack secondaryCoil = ItemStack.EMPTY;


    public final boolean isMainPart;

    public LargeTransformerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        p = state.getValue(IS_MAIN_PART) ? new LargeTransformerProperties(getPos(), state.getValue(TFMGHorizontalDirectionalBlock.FACING)) : null;
        isMainPart = state.getValue(IS_MAIN_PART);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
    public static List<Direction> getCoilDirections(Level level, BlockPos pos, BlockHitResult result) {
        Direction direction = level.getBlockState(pos).getValue(TFMGHorizontalDirectionalBlock.FACING);
        Collection<Direction> validDirections = new ArrayList<>();
        validDirections.add(Direction.UP);
        validDirections.add(Direction.DOWN);


        return IPlacementHelper.orderedByDistance(pos, result.getLocation(), validDirections);

    }
    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }
	
    @Override
    public void onPlace() {
        if (!isMainPart)
            return;
        IElectric.super.onPlace();

        getNetwork().setTransformerRatio(this, 0, turnRatio);
        getNetwork().setTransformerRatio(this, 1, turnRatio);
        getNetwork().setTransformerRatio(this, 2, turnRatio);
    }
	
	@Override
	public void updateNetwork(BlockPos pos) {
		if (!isMainPart) return;
		IElectric.super.updateNetwork(pos);
	}

    @Override
    public void destroy() {
        super.destroy();
        BlockPos pos = this.getBlockPos();
        if (!primaryCoil.isEmpty()) {
            ItemEntity item = new ItemEntity(level, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f, primaryCoil);
            level.addFreshEntity(item);
        }
        if (!secondaryCoil.isEmpty()) {
            ItemEntity item = new ItemEntity(level, pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f, secondaryCoil);
            level.addFreshEntity(item);
        }
    }
	
    public void updateCoils() {
        if (primaryCoil.get(TFMGDataComponents.COIL_TURNS) == null || secondaryCoil.get(TFMGDataComponents.COIL_TURNS) == null) {
            turnRatio = 0;
            getNetwork().setTransformerRatio(this, 0, turnRatio);
            getNetwork().setTransformerRatio(this, 1, turnRatio);
            getNetwork().setTransformerRatio(this, 2, turnRatio);
            return;
        }
        int primaryTurns = primaryCoil.get(TFMGDataComponents.COIL_TURNS);
        int secondaryTurns = secondaryCoil.get(TFMGDataComponents.COIL_TURNS);

        if (primaryCoil.isEmpty() || secondaryCoil.isEmpty() || primaryTurns < 50 || secondaryTurns < 50) {

            turnRatio = 0;

            return;
        }

        turnRatio = (float) secondaryTurns / (float) primaryTurns;

        getNetwork().setTransformerRatio(this, 0, turnRatio);
        getNetwork().setTransformerRatio(this, 1, turnRatio);
        getNetwork().setTransformerRatio(this, 2, turnRatio);


    }
    @Override
    public void onUpdated() {
        if (!isMainPart)
            return;
        ElectricalNetwork network = ElectricalNetworkManager.getNetwork(level);


        for (int i = 0; i < 3; i++) {
            Transformer t = network.getTransformer(getPos(), i);

            primaryVoltage[i] = t == null ? 0 : network.nodeVoltages.get(t.nodeA.networkId).minus(network.nodeVoltages.get(t.nodeB.networkId)).abs();
            secondaryVoltage[i] = t == null ? 0 : network.nodeVoltages.get(t.sA.networkId).minus(network.nodeVoltages.get(t.sB.networkId)).abs();

        }


    }

    public IElectric getControlledBlock() {
        Direction facing = getBlockState().getValue(TFMGHorizontalDirectionalBlock.FACING).getOpposite();
        if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof LargeTransformerBlockEntity be) {
            return be;
        }
        return null;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putFloat("Ratio", turnRatio);
        if (!primaryCoil.isEmpty())
            compound.put("PrimaryCoil", primaryCoil.saveOptional(registries));
        if (!secondaryCoil.isEmpty())
            compound.put("SecondaryCoil", secondaryCoil.save(registries));
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        turnRatio = compound.getFloat("Ratio");
        if (compound.contains("PrimaryCoil")) {
            ItemStack.parse(registries, compound.getCompound("PrimaryCoil")).ifPresent(i -> primaryCoil = i);
        }
        if (compound.contains("SecondaryCoil")) {
            ItemStack.parse(registries, compound.getCompound("SecondaryCoil")).ifPresent(i -> secondaryCoil = i);
        }
    }


    @Override
    public ElectricalProperties getProperties() {
        return p;
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public Level getWorld() {
        return level;
    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        if(!isMainPart){
            IElectric controller = getControlledBlock();
            if(controller!=null){
                return controller.makeMultimeterTooltip(tooltip,isPlayerSneaking);
            }
        }

        for (int i = 0; i < 3; i++) {
            TFMGLang.text(" ").forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.transformerPrimaryVoltage((float) primaryVoltage[i]).forGoggles(tooltip, 1);
            TFMGTexts.Multimeter.transformerSecondaryVoltage((float) secondaryVoltage[i]).forGoggles(tooltip, 1);
        }

        TFMGTexts.Multimeter.transformerRatio(turnRatio).forGoggles(tooltip, 1);

        //IRealisticElectric.super.makeMultimeterTooltip(tooltip, isPlayerSneaking);
        return true;
    }


}
