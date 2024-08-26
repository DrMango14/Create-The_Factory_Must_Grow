package com.drmangotea.tfmg.blocks.electricity.generation.large_generator;


import com.drmangotea.tfmg.base.MaxBlockVoltage;
import com.drmangotea.tfmg.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.tfmg.blocks.electricity.base.cables.VoltagePacket;
import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.drmangotea.tfmg.registry.TFMGPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;


import static com.drmangotea.tfmg.blocks.electricity.generation.large_generator.StatorBlock.STATOR_STATE;
import static net.minecraft.world.level.block.DirectionalBlock.FACING;


public class StatorBlockEntity extends ElectricBlockEntity {


    public BlockPos rotor=null;

    public boolean hasOutput;

    public int updateVoltage = -1;

    public float generation;


    public StatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void tick(){
        super.tick();




        if(updateVoltage==0&&!level.isClientSide){
                TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new VoltagePacket(getBlockPos()));
                updateVoltage = -1;
            }

        if(updateVoltage>0)
            updateVoltage--;

        if(rotor!=null)
            if(!(level.getBlockEntity(rotor) instanceof RotorBlockEntity)) {
                rotor = null;
                generation = 0;
                setVoltage(0);

            }

        if(rotor == null||getBlockState().getValue(STATOR_STATE) != StatorBlock.StatorState.SIDE)
            return;


    }

    @Override
    public int maxVoltage() {
        return MaxBlockVoltage.MAX_VOLTAGES.get(TFMGBlockEntities.STATOR.get());
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if(hasOutput){
            getOrCreateElectricNetwork().voltage=0;
            setVoltage(0);


        }
        if(rotor!=null)
            if((level.getBlockEntity(rotor) instanceof RotorBlockEntity be)){
                if(be.getSpeed() == 0)
                    setVoltage(0);
        }
    }

    public void setRotor(RotorBlockEntity be){
        rotor = be.getBlockPos();

    }

    @Override
    public void onVoltageChanged() {
        super.onVoltageChanged();


    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putBoolean("Output",hasOutput);

    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        hasOutput = compound.getBoolean("Output");


    }


    @Override
    public int voltageGeneration() {
        return (int) (generation*5);
    }

    @Override
    public int FEProduction() {
        return (int) generation*8;
    }

    @Override
    public boolean hasElectricitySlot(Direction direction) {
        return direction == getBlockState().getValue(FACING).getOpposite()&&hasOutput;
    }


    @Override
    public int FECapacity() {
        return 25000;
    }







}
