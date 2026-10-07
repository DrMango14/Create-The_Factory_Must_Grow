package com.drmangotea.tfmg.content.electricity.experimental.content.power_transmission.transformers.small;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.blocks.TFMGHorizontalDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.experimental.IRealisticElectric;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.experimental.RealElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.Resistance;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
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



public class TransformerBlockEntity extends SmartBlockEntity implements IRealisticElectric {
    boolean updateInFront = false;

    public ItemStack primaryCoil = ItemStack.EMPTY;
    public ItemStack secondaryCoil = ItemStack.EMPTY;

    public float coilRatio = 0;

    ElectricalProperties p;

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
    public void onUpdated() {

        RealElectricalNetwork network = RealElectricNetworkManager.getNetwork(level);

        double primaryPower = 0;
        double secondaryPower = 0;

        Resistance secondaryResistance = network.getResistance(getPos(), 0);
        if (secondaryResistance != null) {
            double resistance = secondaryResistance.resistance;
            double voltage = secondaryResistance.getVoltage(level);
            secondaryCurrent = (float) (voltage / resistance);
            secondaryPower = Math.pow(secondaryPower, 2) * resistance;
        }
        Resistance primaryResistance = network.getResistance(getPos(), 1);
        if (primaryResistance != null) {
            double resistance = primaryResistance.resistance;
            double voltage = primaryResistance.getVoltage(level);
            primaryCurrent = (float) (voltage / resistance);
            primaryPower = Math.pow(primaryCurrent, 2) * resistance;


            
        }





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

    @Override
    public ElectricalProperties getProperties() {
        return p;
    }

    @Override
    public long getPos() {
        return getBlockPos().asLong();
    }

    //@Override
    //public int getPowerUsage() {
    //    Direction facing = getDirection();
//
    //    if (level.getBlockEntity(getBlockPos().relative(facing)) instanceof IElectric be && be.getData().getId() != data.getId()) {
    //        if (be.hasElectricitySlot(facing.getOpposite())) {
//
    //                return Math.max(be.getNetworkPowerUsage(this), 0);
//
//
    //        }
    //    }
//
    //    return 0;
    //}


    public void updateCoils() {
        if (primaryCoil.get(TFMGDataComponents.COIL_TURNS) == null || secondaryCoil.get(TFMGDataComponents.COIL_TURNS) == null) {
            coilRatio = 0;

            return;
        }
        int primaryTurns = primaryCoil.get(TFMGDataComponents.COIL_TURNS);
        int secondaryTurns = secondaryCoil.get(TFMGDataComponents.COIL_TURNS);

        if (primaryCoil.isEmpty() || secondaryCoil.isEmpty() || primaryTurns < 50 || secondaryTurns < 50) {

            coilRatio = 0;

            return;
        }

        coilRatio = (float) (float) secondaryTurns / (float) primaryTurns;


    }


    @Override
    public Level getWorld() {
        return level;
    }


    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        if (!primaryCoil.isEmpty())
            compound.put("PrimaryCoil", primaryCoil.saveOptional(registries));
        if (!secondaryCoil.isEmpty())
            compound.put("SecondaryCoil", secondaryCoil.save(registries));

        compound.putFloat("CoilRation", coilRatio);

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
        ;

        coilRatio = compound.getFloat("CoilRation");
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

        ClientLevel level = mc.level;
        BlockPos pos = result.getBlockPos();
        Player player = mc.player;
        ItemStack heldItem = player.getMainHandItem();


        if (!TFMGBlocks.TRANSFORMER.has(level.getBlockState(pos)))
            return;

        if (!(TFMGItems.ELECTROMAGNETIC_COIL.isIn(heldItem) || heldItem.is(Items.AIR)))
            return;

        Direction direction = level.getBlockState(pos).getValue(TFMGHorizontalDirectionalBlock.FACING);


        Direction coilDirection = getCoilDirections(level, pos, result).get(0);
        /////////

        Vec3 center = VecHelper.getCenterOf(pos);

        Vec3 corner1 = center.relative(coilDirection, 7 / 16f).relative(direction, 3 / 16f).relative(Direction.UP, 5.75 / 16f);
        Vec3 corner2 = center.relative(coilDirection, 1 / 16f).relative(direction.getOpposite(), 3 / 16f).relative(Direction.DOWN, 1.75 / 16f);

        TFMGUtils.createOutline(corner1, corner2, "CoilOutline", Color.rainbowColor(AnimationTickHolder.getTicks() * 5));
    }
}
