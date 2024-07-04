package com.drmangotea.createindustry.blocks.electricity.base.cables;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.base.TFMGTools;
import com.drmangotea.createindustry.blocks.electricity.base.*;
import com.drmangotea.createindustry.blocks.electricity.resistors.ResistorBlockEntity;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;

import java.util.ArrayList;
import java.util.List;


public class CableConnectorBlockEntity extends ElectricBlockEntity {


    Player player = null;



    public ArrayList<WireConnection> wireConnections = new ArrayList<>();

    public CableConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    @Override
    public float maxVoltage() {
        return 6000;
    }
    public void tick(){
        super.tick();







        updateBlockBelow();

        spreadCurrent();
//
         sendData();
         setChanged();
//

        if(Create.RANDOM.nextBoolean())
            removeInvalidConnections();
        changeToExtension();


    }


    @Override
    public void destroy() {
        super.destroy();



        ItemEntity itemToSpawn = new ItemEntity((Level) level, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 0.5f, getBlockPos().getZ() + 0.5f, new ItemStack(TFMGItems.COPPER_CABLE.get(),wireConnections.toArray().length));
        if(itemToSpawn.getItem().getCount()==0)
            return;
         level.addFreshEntity(itemToSpawn);





    }


    public void changeToExtension(){

        BlockState state = level.getBlockState(getBlockPos().relative(getBlockState().getValue(WallMountBlock.FACING)));

        if(state.getBlock() == getBlockState().getBlock()){
            if(state.getValue(WallMountBlock.FACING)==getBlockState().getValue(WallMountBlock.FACING))
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

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        int value = 0;

        for(WireConnection connection : wireConnections){
            value++;
            connection.saveConnection(compound,value-1);

        }
        compound.putInt("WireCount",wireConnections.toArray().length);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

    //    sendData();
        addConnection(WireManager.Conductor.COPPER,getBlockPos().above(2).north(),true);
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


//             CreateTFMG.LOGGER.debug("  X: "+pos.getX()+"  Y: "+pos.getY()+"  Z: "+pos.getZ());

             addConnection(WireManager.Conductor.COPPER,pos,compound.getBoolean("ShouldRender"+i));



        }




    }
    @Override
    public void sendCharge(Level level, BlockPos pos){

        int lowestDistance = Integer.MAX_VALUE;

        boolean getsVoltageFromNonTFMGBlock = false;




        for(Direction direction : Direction.values()){
            if(hasElectricitySlot(direction)){
                BlockEntity be1 =level.getBlockEntity(pos.relative(direction));

                if(be1 instanceof IElectricBlock be2){



                    if(be2.hasElectricitySlot(direction.getOpposite())) {



                        int distance = be2.getDistanceFromSource();


                        if(!isStorage()&&be2.isStorage()&&direction == Direction.UP)
                            distance = Integer.MAX_VALUE;


                        if(getVoltage()==0) {
                            if(distance>getDistanceFromSource())
                                be2.addVoltage(getVoltage());
                        }


                        if(!(isStorage()&&direction == Direction.DOWN))
                            if(!(!isStorage()&&be2.isStorage()&&direction == Direction.DOWN))
                                if(getVoltage()!=0) {
                                 //   if(Create.RANDOM.nextInt(3)==2)
                                        transferCharge(be2);
                                    if(distance>getDistanceFromSource()){
                                        be2.addVoltage(getVoltage());
                                    }
                                }

                        lowestDistance = Math.min(lowestDistance,distance);

                    }
                    if(direction.getAxis().isHorizontal()) {
                        if (be2 instanceof ResistorBlockEntity resistorBE)
                            if (resistorBE.hasElectricitySlot(direction) && resistorBE.getVoltageOutput() > 0) {
                                lowestDistance = 0;
                                addVoltage(resistorBE.getVoltageOutput());
                            }

                    }



                }else
                if(be1!=null) {

                    if (be1.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).isPresent()) {

                        if (!(be1.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).orElse(new EnergyStorage(0)) instanceof TFMGForgeEnergyStorage)) {




                            int i = be1.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).orElse(new EnergyStorage(0)).receiveEnergy(1000, true);
                            int y = getForgeEnergy().extractEnergy(1000, true);

                            int j = be1.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).orElse(new EnergyStorage(0)).receiveEnergy(Math.min(y,i), false);
                            getForgeEnergy().extractEnergy(j, false);

                        }
                    }

                }

            }

        }
        for(WireConnection connection : wireConnections){

            BlockPos pos1 = connection.point1 == getBlockPos() ? connection.point2 : connection.point1;

            if(level.getBlockEntity(pos1) instanceof CableConnectorBlockEntity)
                lowestDistance = Math.min(lowestDistance,((CableConnectorBlockEntity)level.getBlockEntity(pos1)).getDistanceFromSource());

        }




        if (lowestDistance != Integer.MAX_VALUE&&voltageGeneration()==0) {

            if( lowestDistance >= getDistanceFromSource()){


            }else {
                if(Create.RANDOM.nextInt(2)==0)
                    setDistanceFromSource(lowestDistance + 1);


            }
            //sendStuff();
        }



        //else   distanceFromSource = Integer.MAX_VALUE;
        if(voltageGeneration()>0){
            setDistanceFromSource(0);
            //sendStuff();
        }

        if(!level.isClientSide)
            if(lowestDistance == getDistanceFromSource()+1&&this.voltageGeneration()==0&&!getsVoltageFromNonTFMGBlock){
                setDistanceFromSource(Integer.MAX_VALUE);
                //   sendStuff();
            }



        if(!level.isClientSide)
            if(getDistanceFromSource()<lowestDistance&&getDistanceFromSource() >0&&this.voltageGeneration()==0&&!getsVoltageFromNonTFMGBlock){
                setDistanceFromSource(Integer.MAX_VALUE);
                //   sendStuff();
//
            }
        if(!level.isClientSide)
            if(lowestDistance == Integer.MAX_VALUE && voltageGeneration() == 0&&!getsVoltageFromNonTFMGBlock){
                setDistanceFromSource(Integer.MAX_VALUE);
                //  sendStuff();
            }


        //if(voltageGeneration()>0)
        //    setDistanceFromSource(0);

        if(lowestDistance>=getDistanceFromSource()&&getDistanceFromSource()!=0)
            setDistanceFromSource(Integer.MAX_VALUE);

        if(getDistanceFromSource()==Integer.MAX_VALUE)
            addVoltage(0);
    }
    public void spreadCurrent(){

       for(WireConnection connection : wireConnections){
           BlockPos pos = connection.point1;

           if(connection.point1 == this.getBlockPos())
               pos = connection.point2;

           if(level.getBlockEntity(pos) instanceof CableConnectorBlockEntity be) {
               transferCharge(be);


               if(be.getDistanceFromSource()>getDistanceFromSource())
                   be.addVoltage(getVoltage());
///
               BlockPos pos2 = pos == connection.point1 ? connection.point2 : connection.point1;
               CableConnectorBlockEntity be2= this;
                if(level.getBlockEntity(pos2) instanceof CableConnectorBlockEntity)
                     be2= (CableConnectorBlockEntity) level.getBlockEntity(pos2);

                ///





             //  //if(be.getDistanceFromSource() == Integer.MAX_VALUE){
             //      if(getDistanceFromSource() != Integer.MAX_VALUE) {
             //        //  be.setDistanceFromSource(getDistanceFromSource() + 1);
             //          be.setDistanceFromSource(Create.RANDOM.nextInt(200));
             //          be2.sendStuff();
             //          be.sendStuff();
             //          be2.updateBlockBelow();
             //          be.updateBlockBelow();
//
             //        //  level.setBlock(be.getBlockPos(),Blocks.DIAMOND_BLOCK.defaultBlockState(),3);
             //        //  level.setBlock(be2.getBlockPos(),Blocks.GOLD_BLOCK.defaultBlockState(),3);
//
             //      }
             // // }

           }




       }

    }

    public void updateBlockBelow(){
        if(level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(WallMountBlock.FACING).getOpposite())) instanceof  IElectricBlock be){
            be.sendStuff();
        }

    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.getBlockPos()).inflate(30);
    }


    public boolean addConnection(WireManager.Conductor material, BlockPos pos,boolean shouldRender){

        float lenght = TFMGTools.getDistance(this.getBlockPos(),pos,false);



        if(lenght<25) {
            wireConnections.add(new WireConnection(material, lenght, pos, this.getBlockPos(), shouldRender));
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
        return direction == getBlockState().getValue(WallMountBlock.FACING).getOpposite();
    }


}
