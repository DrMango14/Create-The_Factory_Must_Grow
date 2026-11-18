package com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.small;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.base.lang.TFMGTexts;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.simulation.Transformer;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransformerBlockEntity extends SmartBlockEntity implements IElectric {
    public ItemStack primaryCoil = ItemStack.EMPTY;
    public ItemStack secondaryCoil = ItemStack.EMPTY;

    public float coilRatio = 0;

    ElectricalProperties p;

    public double primaryVoltage = 0;

    public double secondaryVoltage = 0;

    public float primaryCurrent = 0;

    public float secondaryCurrent = 0;

    public TransformerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        p = new TransformerProperties(getPos(), state.getValue(TFMGHorizontalDirectionalBlock.FACING));
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public boolean makeMultimeterTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        TFMGTexts.Multimeter.transformerPrimaryVoltage((float) primaryVoltage).forGoggles(tooltip, 1);
        TFMGTexts.Multimeter.transformerSecondaryVoltage((float) secondaryVoltage).forGoggles(tooltip, 1);
        TFMGTexts.Multimeter.transformerRatio(coilRatio).forGoggles(tooltip, 1);
        return true;
    }

    @Override
    public void onPlace() {
        IElectric.super.onPlace();

        getNetwork().setTransformerRatio(this, 0, coilRatio);
    }

    @Override
    public void onUpdated() {

        ElectricalNetwork network = ElectricNetworkManager.getNetwork(level);


        Transformer t = network.getTransformer(getPos(), 0);
        if (coilRatio != 0) {
            primaryVoltage = network.nodeVoltages.get(t.nodeA.networkId).minus(network.nodeVoltages.get(t.nodeB.networkId)).abs();
            secondaryVoltage = network.nodeVoltages.get(t.sA.networkId).minus(network.nodeVoltages.get(t.sB.networkId)).abs();
        }
    }

    @Override
    public void destroy() {
        if (level == null) return;
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


    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    public void updateCoils() {
		float primaryTurns = (float) primaryCoil.getOrDefault(TFMGDataComponents.COIL_TURNS, 0);
		float secondaryTurns = (float) secondaryCoil.getOrDefault(TFMGDataComponents.COIL_TURNS, 0);
		
        if(primaryTurns < 50 || secondaryTurns < 50) {
            coilRatio = 0;
        } else {
			coilRatio = secondaryTurns / primaryTurns;
		}
		getNetwork().setTransformerRatio(this, 0, coilRatio);


    }

    @Override
    public ElectricalProperties getProperties() {
        return p;
    }

    @Override
    public Level getWorld() {
        return level;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        if (!primaryCoil.isEmpty())
            compound.put("PrimaryCoil", primaryCoil.save(registries));
        if (!secondaryCoil.isEmpty())
            compound.put("SecondaryCoil", secondaryCoil.save(registries));

        compound.putFloat("CoilRatio", coilRatio);

    }

    @Override
    public void remove() {
        super.remove();
        this.removeBlock();
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        if (compound.contains("PrimaryCoil")) {
            ItemStack.parse(registries, compound.getCompound("PrimaryCoil")).ifPresent(i -> primaryCoil = i);
        }
        if (compound.contains("SecondaryCoil")) {
            ItemStack.parse(registries, compound.getCompound("SecondaryCoil")).ifPresent(i -> secondaryCoil = i);
        }

        coilRatio = compound.getFloat("CoilRatio");
    }

    public static List<Direction> getCoilDirections(Level level, BlockPos pos, BlockHitResult result) {
        Direction direction = level.getBlockState(pos).getValue(TFMGHorizontalDirectionalBlock.FACING);
        Collection<Direction> validDirections = new ArrayList<>();
        validDirections.add(direction.getClockWise());
        validDirections.add(direction.getCounterClockWise());


        return IPlacementHelper.orderedByDistance(pos, result.getLocation(), validDirections);

    }

    @OnlyIn(Dist.CLIENT)
    public static void tickOutliner() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || !(mc.hitResult instanceof BlockHitResult result))
            return;

        BlockPos pos = result.getBlockPos();

        if (!TFMGBlocks.TRANSFORMER.has(mc.level.getBlockState(pos)))
            return;

		ItemStack heldItem = mc.player.getMainHandItem();
        if (!(TFMGItems.ELECTROMAGNETIC_COIL.isIn(heldItem) || heldItem.is(Items.AIR)))
            return;

        Direction direction = mc.level.getBlockState(pos).getValue(TFMGHorizontalDirectionalBlock.FACING);

        Direction coilDirection = getCoilDirections(mc.level, pos, result).getFirst();
        /////////

        Vec3 center = VecHelper.getCenterOf(pos);

        Vec3 corner1 = center.relative(coilDirection, 7 / 16f).relative(direction, 3 / 16f).relative(Direction.UP, 5.75 / 16f);
        Vec3 corner2 = center.relative(coilDirection, 1 / 16f).relative(direction.getOpposite(), 3 / 16f).relative(Direction.DOWN, 1.75 / 16f);

        TFMGUtils.createOutline(corner1, corner2, "CoilOutline", Color.rainbowColor(AnimationTickHolder.getTicks() * 5));
    }
}
