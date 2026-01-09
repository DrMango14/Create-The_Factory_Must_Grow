package com.drmangotea.tfmg.content.electricity.connection.cables;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.SpoolItem;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGCableTypes;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.theme.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.drmangotea.tfmg.base.blocks.WallMountBlock.FACING;
import static com.drmangotea.tfmg.content.electricity.connection.cables.CableConnectorBlock.EXTENSION;

public class CableConnectorBlockEntity extends ElectricBlockEntity implements IHaveHoveringInformation {

    //player held cable rendering
    public Player player;
    public int color = 0x000000;
    public LerpedFloat wireMovementX = LerpedFloat.linear();
    public LerpedFloat wireMovementY = LerpedFloat.linear();
    public LerpedFloat wireMovementZ = LerpedFloat.linear();
    //

    public List<CableConnection> connections = new ArrayList<>();
    public long id;
    public boolean removeWiresNextTick = false;

    public CableConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        wireMovementX.setValue(pos.getX());
        wireMovementY.setValue(pos.getY());
        wireMovementZ.setValue(pos.getZ());
        id = getBlockPos().asLong();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void remove() {
        super.remove();
        notifyRemoval();
    }

    @Override
    public int getMaxVoltage() {
        return 100000;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        if (getBlockState().getValue(EXTENSION))
            return direction.getAxis() == getBlockState().getValue(FACING).getAxis();

        return direction == getBlockState().getValue(FACING).getOpposite();
    }

    public void notifyRemoval() {

        if (level.isClientSide)
            return;

        for (CableConnection connection : connections) {
            ItemEntity itemToDrop = new ItemEntity(level, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 0.5f, getBlockPos().getZ() + 0.5f, new ItemStack(connection.type.getWire().asItem(), (int) (connection.getLength() / 8)));
            if (itemToDrop.getItem().getCount() > 0) {
                level.addFreshEntity(itemToDrop);
            }
            BlockPos pos = connection.blockPos1;

            // level.setBlock(connection.blockPos1.above(), Blocks.GOLD_BLOCK.defaultBlockState(),3);
            if (level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be) {
                if (be.getBlockPos() == getBlockPos())
                    continue;
                be.onPlaced();
                be.removeWiresNextTick = true;

            }
        }
    }

    //@Override
    //public float resistance() {
    //    float resistance = 0;

    //    for(CableConnection connection : connections){
    //        if(connection.visible){
    //            resistance+=connection.type.resistivity*connection.getLength();
    //        }
    //    }

    //    return resistance;
    //}


    public void removeConnection() {
        connections.removeIf(c -> {
            BlockPos pos = c.blockPos1;

            return !(level.getBlockEntity(pos) instanceof CableConnectorBlockEntity);

        });
        sendStuff();
    }

    @Override
    public void onConnected() {
        super.onConnected();

        for (CableConnectorBlockEntity be : getConnectedWires()) {

            if (be.getData().getId() != getData().getId()) {
                be.setNetwork(getData().getId());
                be.onConnected();
            }

            be.sendStuff();
        }
        sendStuff();

    }

    public List<CableConnectorBlockEntity> getConnectedWires() {
        return getConnectedWires(new ArrayList<>());
    }

    public List<CableConnectorBlockEntity> getConnectedWires(List<CableConnectorBlockEntity> foundList) {


        if (!foundList.contains(this)) {
            foundList.add(this);
        }

        for (CableConnection connection : connections) {
            BlockPos pos = connection.blockPos1;


            //level.setBlockAndUpdate(pos.above(), Blocks.GOLD_BLOCK.defaultBlockState());
            //level.setBlockAndUpdate(pos2.above(2), Blocks.DIAMOND_BLOCK.defaultBlockState());


            if (pos == getBlockPos()) {
                continue;
            }
            //  TFMGUtils.debugMessage(level, "Eﴤ "+connections.size());


            if (level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be && !foundList.contains(be)) {
                // TFMGUtils.debugMessage(level, "Bﴤ "+connections.size());
                be.getConnectedWires(foundList);
                sendStuff();
                be.sendStuff();

            }
        }
        sendStuff();
        return foundList;
    }


    @Override
    public void tick() {
        super.tick();
        if (removeWiresNextTick) {

            removeConnection();
            removeWiresNextTick = false;
        }
        if (player != null)
            managePlayerWire();


    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        compound.putInt("ConnectionCount", connections.size());


        for (int i = 0; i < connections.size(); i++) {
            CableConnection connection = connections.get(i);
            compound.put("Connection" + i, connection.saveConnection());
            //
        }

    }

    @Override
    public void lazyTick() {
        super.lazyTick();

    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        connections = new ArrayList<>();
        for (int i = 0; i < compound.getInt("ConnectionCount"); i++) {

            connections.add(CableConnection.loadConnection(compound.getCompound("Connection" + i)));
        }

    }

    public void managePlayerWire() {
        wireMovementX.chase(player.getX() - .5, 0.7, LerpedFloat.Chaser.EXP);
        wireMovementY.chase(player.getY() + (player.isCrouching() ? 0.6 : 0.9), 0.3, LerpedFloat.Chaser.EXP);
        wireMovementZ.chase(player.getZ() - .5, 0.7, LerpedFloat.Chaser.EXP);
        wireMovementX.tickChaser();
        wireMovementY.tickChaser();
        wireMovementZ.tickChaser();
    }

    public CablePos getCablePosition() {
        return new CablePos(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).inflate(32);
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


        if (!(level.getBlockState(pos).getBlock() instanceof CableConnectorBlock))
            return;

        if (!(heldItem.getItem() instanceof SpoolItem))
            return;

        Direction direction = level.getBlockState(pos).getValue(FACING);


        for (int i = 0; i < 64; i++) {
            if (level.getBlockEntity(pos.relative(direction)) instanceof CableConnectorBlockEntity) {
                pos = pos.relative(direction);

            } else break;

        }

        int length = 0;

        for (int i = 0; i < 64; i++) {
            if (level.getBlockEntity(pos.relative(direction.getOpposite(), i + 1)) instanceof CableConnectorBlockEntity) {
                length++;

            } else break;
        }



        Vec3 center = VecHelper.getCenterOf(pos);

        List<Direction.Axis> axis = new ArrayList<>(Arrays.stream(Direction.Axis.values()).toList());
        axis.remove(direction.getAxis());

        float size = TFMGBlocks.CABLE_CONNECTOR.has(level.getBlockState(pos)) ? 2.5f : 3.5f;

        Vec3 corner1 = center.relative(direction, 1 / 16f).relative(direction.getClockWise(axis.get(0)), size / 15f).relative(direction.getClockWise(axis.get(1)), size / 15f);
        Vec3 corner2 = center.relative(direction.getOpposite(), 8 / 16f + length).relative(direction.getCounterClockWise(axis.get(0)), size / 15f).relative(direction.getCounterClockWise(axis.get(1)), size / 15f);

        TFMGUtils.createOutline(corner1, corner2, "InsulatorOutline", Color.rainbowColor(AnimationTickHolder.getTicks() * 5));
    }
}

