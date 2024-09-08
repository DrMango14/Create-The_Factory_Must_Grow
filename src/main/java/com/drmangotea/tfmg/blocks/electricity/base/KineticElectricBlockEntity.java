package com.drmangotea.tfmg.blocks.electricity.base;


import com.drmangotea.tfmg.CreateTFMG;
import com.drmangotea.tfmg.base.TFMGTools;
import com.drmangotea.tfmg.blocks.electricity.base.cables.*;
import com.drmangotea.tfmg.blocks.electricity.cable_blocks.CableHubBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.resistors.ResistorBlockEntity;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static com.drmangotea.tfmg.blocks.electricity.base.WallMountBlock.FACING;


public class KineticElectricBlockEntity extends GeneratingKineticBlockEntity implements IElectric {
    public long network = getId();
    public Player player = null;

    public int voltage=0;

    boolean destroyed=false;

    public boolean networkUpdate = false;
    public boolean needsVoltageUpdate = false;


    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    public final TFMGForgeEnergyStorage energy = createEnergyStorage();






    public ArrayList<WireConnection> wireConnections = new ArrayList<>();

    public KineticElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

    }


    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergyHandler.invalidate();
    }

    public void useEnergy(int value){
        energy.extractEnergy(value,false);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {


        if (cap == ForgeCapabilities.ENERGY&&side == null) {
            return lazyEnergyHandler.cast();
        } else

        if (cap == ForgeCapabilities.ENERGY&& hasElectricitySlot(side)) {
            return lazyEnergyHandler.cast();
        }

        return super.getCapability(cap, side);
    }




    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyHandler = LazyOptional.of(() -> energy);
    }

    public void tick(){
        super.tick();

        if(getVoltage()>maxVoltage())
            voltageFailure();

        if(FEProduction()>0)
            energy.receiveEnergy(FEProduction(),false);

        if(networkUpdate){
            getOrCreateElectricNetwork().updateNetworkVoltage();
            networkUpdate = false;
        }
        if(needsVoltageUpdate){
            setVoltageFromNetwork();
            getOrCreateElectricNetwork().updateNetworkVoltage();
            needsVoltageUpdate = false;
        }



        getOrCreateElectricNetwork().add(this);
        //TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new EnergyNetworkUpdatePacket(getBlockPos(),network));



        // getOrCreateElectricNetwork().updateNetworkVoltage();


        sendData();
        setChanged();
//

        if(Create.RANDOM.nextBoolean())
            removeInvalidConnections();



    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        getOrCreateElectricNetwork().members.removeIf(member->!(level.getBlockEntity(BlockPos.of(member.getId()))instanceof IElectric));
        getOrCreateElectricNetwork().members.removeIf(member->member.getNetwork()!=getNetwork());

        for(Direction direction : Direction.values()){
            if(hasElectricitySlot(direction)){
                BlockEntity be = level.getBlockEntity(getBlockPos().relative(direction));
                if(be==null)
                    return;
                LazyOptional<IEnergyStorage> capability = be.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());

                if(capability.isPresent()&&capability.orElseGet(null).canReceive()&&!(be instanceof IElectric)){

                    int maxTransfer1 = this.getForgeEnergy().extractEnergy(this.getForgeEnergy().getEnergyStored(),true);
                    int maxTransfer2 = capability.orElseGet(null).receiveEnergy(this.getForgeEnergy().getEnergyStored(),true);


                    this.getForgeEnergy().extractEnergy(Math.min(maxTransfer1,maxTransfer2),false);
                    capability.orElseGet(null).receiveEnergy(Math.min(maxTransfer1,maxTransfer2),false);

                }


            }
        }
    }

    @Override
    public void remove() {
        super.destroy();
        getOrCreateElectricNetwork().remove(this);
        voltage = 0;

        onVoltageChanged();

        destroyed = true;



        if(network == getId()) {

            getOrCreateElectricNetwork().members.forEach(c->c.setNetwork(c.getNetwork(),false));

            ElectricNetworkManager.networks.get(getLevel())
                    .remove(network);
        }


        for(WireConnection connection : wireConnections){
            BlockPos pos = connection.point1 == getBlockPos() ? connection.point2 : connection.point1;

            if(level.getBlockEntity(pos) instanceof IElectric be){


                be.makeControllerAndSpread();
                be.needsNetworkUpdate();
                be.needsVoltageUpdate();
                be.getOrCreateElectricNetwork().updateVoltageFromNetwork();

            }


        }




        List<WireConnection> list = new ArrayList<>();

        for(WireConnection connection : wireConnections){
            if(!connection.neighborConnection)
                list.add(connection);
        }

        ItemEntity itemToSpawn = new ItemEntity((Level) level, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 0.5f, getBlockPos().getZ() + 0.5f, new ItemStack(TFMGItems.COPPER_CABLE.get(),list.size()));
        if(itemToSpawn.getItem().getCount()==0)
            return;
        level.addFreshEntity(itemToSpawn);



        super.remove();

    }

    @Override
    public boolean outputAllowed() {
        return true;
    }



    public void changeToExtension(){

        BlockState state = level.getBlockState(getBlockPos().relative(getBlockState().getValue(FACING)));

        if(state.getBlock() == getBlockState().getBlock()){
            if(state.getValue(FACING)==getBlockState().getValue(FACING))
                if(!getBlockState().getValue(CableConnectorBlock.EXTENSION))
                    level.setBlock(getBlockPos(),getBlockState().setValue(CableConnectorBlock.EXTENSION,true),2);
        } else
        if(getBlockState().getValue(CableConnectorBlock.EXTENSION))
            level.setBlock(getBlockPos(),getBlockState().setValue(CableConnectorBlock.EXTENSION,false),2);
    }


    public void  removeInvalidConnections(){


        wireConnections.removeIf(connection -> !(level.getBlockState(connection.point1).getBlock() instanceof IHaveCables)
                || !(level.getBlockState(connection.point2).getBlock() instanceof IHaveCables));


    }

    // public void findMachine(){
    //
    //     getOrCreateNetwork();
    //
    //
    //     if(getEnergySlot().isEmpty())
    //         return;
    //
    //     BlockPos pos = getBlockPos().relative(getEnergySlot().get());
    //
    //     if(level.getBlockEntity(pos) instanceof IElectricalBlock be){

    //         if(level.getBlockEntity(BlockPos.of(network)) instanceof IElectricalBlock&&network>=be.getNetwork())
    //             return;
    //
    //         setNetwork(be.getId(),false);
    //
    //     }
    // }


    public void makeControllerAndSpread(){

        if(level.isClientSide)
            return;

        getOrCreateElectricNetwork().members.remove(this);



        CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);
        setNetwork(getId(),false);
        network = getId();



        onConnected();




    }

    @Override
    public TFMGForgeEnergyStorage getForgeEnergy() {
        return energy;
    }



    public void onConnected(){
        needsVoltageUpdate=true;
        if(level.isClientSide)
            return;


        for(WireConnection connection : wireConnections){
            BlockPos pos = connection.point1 == getBlockPos() ? connection.point2 : connection.point1;

            if(level.getBlockEntity(pos) instanceof IElectric be){
                if(be instanceof CableHubBlockEntity be1 && be1.hasSignal)
                    continue;
                if(be.getNetwork() != network&&!be.destroyed()) {
                    be.setNetwork(network, true);
                    be.onConnected();
                } else if(be.destroyed())
                    getOrCreateElectricNetwork().remove(be);
            }


        }

    }

    @Override
    public int FECapacity() {
        return 1000;
    }

    @Override
    public int FEProduction() {
        return 0;
    }

    @Override
    public int FETransferSpeed() {
        return 2500;
    }

    @Override
    public int getVoltage() {
        return voltage;
    }

    @Override
    public int maxVoltage() {
        return 6000;
    }


    @Override
    public int voltageGeneration() {

        int maxResistorVoltage = 0;

        for(Direction direction : Direction.values()){
            if(hasElectricitySlot(direction)){

                if(level.getBlockEntity(getBlockPos().relative(direction)) instanceof ResistorBlockEntity be)
                    if(be.hasElectricitySlot(direction)){
                        maxResistorVoltage = Math.max(maxResistorVoltage,be.voltageOutput());
                    }
            }
        }

        return maxResistorVoltage;
    }
    public void setVoltage(int value){
        setVoltage(value,true);
    }
    @Override
    public void setVoltage(int value, boolean update) {
        voltage = value;
        if(update)
            onVoltageChanged();
    }

    @Override
    public void voltageFailure() {

    }


    @Override
    public void setNetwork(long newNetwork, boolean removeNetwork) {
        if (network == newNetwork)
            return;

        if(removeNetwork) {
            ElectricNetworkManager.networks.get(getLevel())
                    .remove(network);
        }else
            getOrCreateElectricNetwork().remove(this);
        long oldNetwork = network;
        network = newNetwork;
        setChanged();



        network = newNetwork;



        ElectricalNetwork network1 = getOrCreateElectricNetwork();
//
        if(network1.members.contains(this)) {
            network = oldNetwork;
            return;
        }

        TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new EnergyNetworkUpdatePacket(getBlockPos(),network));
        //network.initialized = true;
        network1.add(this);



    }
    public void onPlaced(){

        networkUpdate = true;

        connectNeighbors();

        // needsVoltageUpdate = true;

        setVoltage(voltageGeneration());

    }

    public void setVoltageFromNetwork(){
        setVoltage(Math.max(getOrCreateElectricNetwork().voltage,voltageGeneration()),false);
    }

    public void connectNeighbors(){
        for(Direction direction : Direction.values()){
            if(hasElectricitySlot(direction)) {
                BlockPos pos = getBlockPos().relative(direction);
                if (level.getBlockEntity(pos) instanceof IElectric be) {
                    if (be instanceof CableHubBlockEntity be1 && be1.hasSignal)
                        continue;
                    if (be.hasElectricitySlot(direction.getOpposite())) {
                        be.addConnection(WireManager.Conductor.COPPER, getBlockPos(), true, true);
                        be.sendStuff();
                        addConnection(WireManager.Conductor.COPPER, pos, false, true);
                        sendData();
                        setChanged();


                        be.makeControllerAndSpread();

                    }
                }
            }
        }
    }

    @Override
    public void needsVoltageUpdate() {
        needsVoltageUpdate = true;
    }

    @Override
    public boolean destroyed() {
        return destroyed;
    }

    @Override
    public void needsNetworkUpdate() {
        networkUpdate = true;
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putInt("Voltage", getVoltage());

        compound.putLong("Network", network);

        int value = 0;

        for(WireConnection connection : wireConnections){
            value++;
            connection.saveConnection(compound,value-1);

        }
        compound.putInt("WireCount",wireConnections.toArray().length);

        compound.putInt("ForgeEnergy", getForgeEnergy().getEnergyStored());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);



        voltage = compound.getInt("Voltage");





        //    sendData();
        addConnection(WireManager.Conductor.COPPER,getBlockPos().above(2).north(),true,true);
        //   wireConnections.add(new WireConnection(WireManager.Conductor.COPPER,1,getBlockPos().above(2).north(),this.getBlockPos(),true));
        if(wireConnections.isEmpty())
            return;

        wireConnections = new ArrayList<>();
        for(int i = 0; i < compound.getInt("WireCount");i++){

            //       level.setBlock(getBlockPos().above(1+i),Blocks.REINFORCED_DEEPSLATE.defaultBlockState(),3);





            BlockPos pos = new BlockPos(compound.getInt("X1"+i),compound.getInt("Y1"+i),compound.getInt("Z1"+i));
            //  level.setBlock(new BlockPos(compound.getInt("X1"+i),compound.getInt("Y1"+i),compound.getInt("Z1"+i)), Blocks.REINFORCED_DEEPSLATE.defaultBlockState(),3);
            if(pos == this.getBlockPos())
                pos = new BlockPos(compound.getInt("X2"+i),compound.getInt("Y2"+i),compound.getInt("Z2"+i));


            // level.setBlock(new BlockPos(compound.getInt("X2"+i),compound.getInt("Y2"+i),compound.getInt("Z2"+i)), Blocks.GOLD_BLOCK.defaultBlockState(),3);




            addConnection(WireManager.Conductor.COPPER,pos,compound.getBoolean("ShouldRender"+i),compound.getBoolean("NeighborConnection"+i));



        }




        network = compound.getLong("Network");

        //
        //setNetwork(network,false);
        setNetwork(network,false);

        //if(getId() != network&&!getOrCreateNetwork().cables.contains(this))
        //    CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric) level.getBlockEntity(BlockPos.of(network))).add(this);

        energy.setEnergy(compound.getInt("ForgeEnergy"));

    }

    public void onVoltageChanged(){

        getOrCreateElectricNetwork().updateNetworkVoltage();
    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(30);
    }


    public boolean addConnection(WireManager.Conductor material, BlockPos pos,boolean shouldRender,boolean neighborConnection){

        float lenght = TFMGTools.getDistance(this.getBlockPos(),pos,false);



        if(lenght<25) {
            wireConnections.add(new WireConnection(material, lenght, pos, this.getBlockPos(), shouldRender,neighborConnection));
            sendData();
            setChanged();
            return true;
        }else {
            sendData();
            setChanged();
            return false;
        }


    }




    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite();
    }

    @Override
    public long getNetwork() {
        return network;
    }


    @Override
    public long getId() {
        return getBlockPos().asLong();
    }





    @Override
    public ElectricalNetwork getOrCreateElectricNetwork() {



      //  TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new EnergyNetworkUpdatePacket(getBlockPos(),network));

        if((IElectric) level.getBlockEntity(BlockPos.of(network)) != null) {

            return CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor((IElectric) level.getBlockEntity(BlockPos.of(network)));

        } else
            return CreateTFMG.NETWORK_MANAGER.getOrCreateNetworkFor(this);

    }

    @Override
    public void setNetworkClient(long value) {
        network = value;
    }


    public void sendStuff() {
        sendData();
        setChanged();
    }
    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        getOrCreateElectricNetwork().updateNetworkVoltage();
        if(!level.isClientSide)
            TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new VoltagePacket(getBlockPos()));

    }

}
