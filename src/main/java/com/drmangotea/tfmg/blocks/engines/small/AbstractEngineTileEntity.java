package com.drmangotea.tfmg.blocks.engines.small;


import com.drmangotea.tfmg.blocks.engines.small.turbine.TurbineEngineTileEntity;
import com.drmangotea.tfmg.registry.TFMGBlocks;
import com.drmangotea.tfmg.registry.TFMGFluids;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public abstract class AbstractEngineTileEntity extends GeneratingKineticBlockEntity implements IHaveGoggleInformation, IWrenchable {

    protected LazyOptional<IFluidHandler> fluidCapability;
    protected FluidTank tankInventory;


    protected int soundTimer=0;

    protected BlockPos lastKnownPos;

    public int fuelConsumption =0;

    public float stressTotal=0;
    public float speed=0;
    public float stressBase=0;

    public int efficiency=1;
    public final int idealSpeed=12;

    private int consumptionTimer=0;




    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;

//
int signal;
boolean signalChanged;
//

   // protected ScrollValueBehaviour generatedSpeed;

    public AbstractEngineTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
         tankInventory = createInventory();

        fluidCapability = LazyOptional.of(() -> tankInventory);

        signal = 0;
        setLazyTickRate(40);
        refreshCapability();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (!hasSource() || getGeneratedSpeed() > getTheoreticalSpeed())
            updateGeneratedRotation();
    }

    @Override
    public float getGeneratedSpeed() {



        if(!hasBackPart())
            return 0;
        if(isExhaustTankFull())
            return 0;




        if(!level.isClientSide){


            calculateEfficiency();
             fuelConsumption = (int)((speed/(efficiency/10)/6)+1);
            if(fuelConsumption<1)
                fuelConsumption=0;
        if(!tankInventory.isEmpty()) {

            if(consumptionTimer>=10) {
                if(signal!=0)
                    tankInventory.drain(fuelConsumption, IFluidHandler.FluidAction.EXECUTE);
                consumptionTimer=0;
            }
            consumptionTimer++;




            return convertToDirection(signal * signal, getBlockState().getValue(FACING));
        }}
        return 0;


    }

    public boolean isExhaustTankFull(){
        if(!hasBackPart())
            return false;
        EngineBackBlockEntity back = (EngineBackBlockEntity) level.getBlockEntity(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite()));
        return back.tankInventory.getFluidAmount()+7>=500;

    }


    public void calculateEfficiency(){


        if(signal==0||tankInventory.isEmpty()) {
            efficiency = 0;
            return;
        }
        efficiency=100;

        if(signal>idealSpeed){
            efficiency=(100-(signal-idealSpeed)*5);
        }

        if(signal<idealSpeed){
            efficiency=(100-(idealSpeed-signal)*3);
        }


        if(efficiency>100)
            efficiency=100;

    }

    public boolean hasBackPart(){
        BlockPos wantedLocation=this.getBlockPos();
        Direction direction = this.getBlockState().getValue(DirectionalBlock.FACING);


        if(direction == Direction.UP)
            wantedLocation=this.getBlockPos().below();
        if(direction == Direction.DOWN)
            wantedLocation=this.getBlockPos().above();
        if(direction == Direction.NORTH)
            wantedLocation=this.getBlockPos().south();
        if(direction == Direction.SOUTH)
            wantedLocation=this.getBlockPos().north();
        if(direction == Direction.WEST)
            wantedLocation=this.getBlockPos().east();
        if(direction == Direction.EAST)
            wantedLocation=this.getBlockPos().west();



        if(!level.getBlockState(wantedLocation).is(TFMGBlocks.GASOLINE_ENGINE_BACK.get())) {
            return false;
        }else {
            if ( level.getBlockState(wantedLocation).getValue(DirectionalBlock.FACING) != direction)
                return false;
        }
            return true;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        // boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        //     if (!IRotate.StressImpact.isEnabled())
        //        return added;
        //
              Lang.translate("goggles.engine_stats")
                        .forGoggles(tooltip);
        if(!hasBackPart()){
            Lang.translate("goggles.engine.backpartmissing")
                    .style(ChatFormatting.DARK_RED)
                    .space()
                    .forGoggles(tooltip);
            return true;
        }



        stressBase = calculateAddedStressCapacity();
        //   if (Mth.equal(stressBase, 0))
        //   return added;


        Lang.translate("tooltip.capacityProvided")
                .style(ChatFormatting.GRAY)
                .space()
                .forGoggles(tooltip);

        speed = getTheoreticalSpeed();
        if (speed != getGeneratedSpeed() && speed != 0)
          //  stressBase *= getGeneratedSpeed() / speed;
        speed = Math.abs(speed);

        stressTotal = stressBase * speed;

        Lang.number(stressTotal)
                .translate("generic.unit.stress")
                .style(ChatFormatting.DARK_AQUA)

                .space()
                .add(Lang.translate("gui.goggles.at_current_speed")
                        .style(ChatFormatting.DARK_GRAY))

                .forGoggles(tooltip, 1);

        Lang.translate("goggles.engine_redstone_input")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        Lang.translate("tooltip.engine_analog_strength", this.signal)
                .style(ChatFormatting.DARK_AQUA)
                .forGoggles(tooltip,1);

/////

        Lang.translate("goggles.engine.efficiency")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        Lang.translate("goggles.get_engine_efficiency", this.efficiency)
                .style(ChatFormatting.DARK_AQUA)
                .add(Lang.translate("goggles.units.percent"))
                .forGoggles(tooltip,1);


////////////////////////////////////////
        LazyOptional<IFluidHandler> handler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0)
            return false;

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");
        Lang.translate("goggles.fuel_container")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);


        boolean isEmpty = true;
        for (int i = 0; i < tank.getTanks(); i++) {
            FluidStack fluidStack = tank.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;

            Lang.fluidName(fluidStack)
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

            Lang.builder()
                    .add(Lang.number(fluidStack.getAmount())
                            .add(mb)
                            .style(ChatFormatting.DARK_AQUA))
                    .text(ChatFormatting.GRAY, " / ")
                    .add(Lang.number(tank.getTankCapacity(i))
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);

            isEmpty = false;
        }

        if (tank.getTanks() > 1) {
            if (isEmpty)
                tooltip.remove(tooltip.size() - 1);
            return true;
        }

        if (!isEmpty)
            return true;

        Lang.translate("gui.goggles.fluid_container.capacity")
                .add(Lang.number(tank.getTankCapacity(0))
                        .add(mb)
                        .style(ChatFormatting.DARK_AQUA))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);


return true;
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {




    //    this.getBlockState().setValue(EngineBlock.BACKPART,true);

        return InteractionResult.SUCCESS;
    }


/////////////////////////////////////////////
@Override
public void write(CompoundTag compound, boolean clientPacket) {
    compound.putInt("Signal", signal);
    if (lastKnownPos != null)
        compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));


    compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));


    super.write(compound, clientPacket);
}

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {



            lastKnownPos = null;

            if (compound.contains("LastKnownPos"))
                lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));




            tankInventory.readFromNBT(compound.getCompound("TankContent"));
            if (tankInventory.getSpace() < 0)
                tankInventory.drain(-tankInventory.getSpace(), IFluidHandler.FluidAction.EXECUTE);




        signal = compound.getInt("Signal");
        super.read(compound, clientPacket);
    }

    public float getModifier() {
        return getModifierForSignal(signal);
    }

    public void neighbourChanged() {
        if (!hasLevel())
            return;
        int power = level.getBestNeighborSignal(worldPosition);
        if (power != signal)
            signalChanged = true;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        neighbourChanged();
    }


    @Override
    public void tick() {
        super.tick();

        ///
       // if(signal!=0&&hasBackPart()&&tankInventory.getFluidAmount()!=0&&!overStressed&&isExhaustTankFull()) {


            soundTimer++;

         //   if(!isExhaustTankFull()) {
           if (soundTimer >= ((16-signal)/2)+1) {
               if(signal!=0&&
                       hasBackPart()&&
                       tankInventory.getFluidAmount()!=0 &&
                       !overStressed


               ){
                   EngineBackBlockEntity back = (EngineBackBlockEntity) level.getBlockEntity(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite()));
                   if(back.tankInventory.getFluidAmount()+4<=500){

              //  if(this.getGeneratedSpeed()!=0) {
                    if (level.isClientSide)
                        makeSound();
                   }
              }
           }
          //  }


        ///

        if(!hasBackPart())
            return;
        if(!isExhaustTankFull()) {
            if(tankInventory.getFluidAmount()!=0) {

                EngineBackBlockEntity back = (EngineBackBlockEntity) level.getBlockEntity(this.getBlockPos().relative(this.getBlockState().getValue(FACING).getOpposite()));
              if(back!=null) {
                  if (efficiency != 0 && signal != 0 && !overStressed && !isExhaustTankFull())
                      back.tankInventory.setFluid(new FluidStack(TFMGFluids.CARBON_DIOXIDE.getSource(), back.tankInventory.getFluidAmount() + 6));
              }
            }
        }
        updateGeneratedRotation();
        calculateEfficiency();

        //if(signalChanged)


        stressBase = calculateAddedStressCapacity();
        speed = getTheoreticalSpeed();
        if (speed != getGeneratedSpeed() && speed != 0)
            stressBase *= getGeneratedSpeed() / speed;
        speed = Math.abs(speed);

        stressTotal = stressBase * speed;
       // if (level.isClientSide)
         //   return;
        if (signalChanged) {
            signalChanged = false;
            analogSignalChanged(level.getBestNeighborSignal(worldPosition));
        }
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
        }

    }

    @OnlyIn(Dist.CLIENT)
    private void makeSound(){
        soundTimer=0;
        if(this instanceof TurbineEngineTileEntity){
            AllSoundEvents.WHISTLE_CHIFF.playAt(level, worldPosition, 0.03f, .2f, false);
        }
        //else

       // CISoundEvents.DIESEL_ENGINE_SOUNDS.playAt(level, worldPosition, 0.9f, .1f, false);


    }

    protected void analogSignalChanged(int newSignal) {
        //removeSource();
        signal = newSignal;
    }

    protected float getModifierForSignal(int newPower) {
        if (newPower == 0)
            return 1;
        return 1 + ((newPower + 1) / 16f);
    }
    /////////////////////

    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(1000, this::onFluidStackChanged){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(validFuel());
            }
        };
    }










    private void onPositionChanged() {
        lastKnownPos = worldPosition;
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {}





    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }





    private void refreshCapability() {
        LazyOptional<IFluidHandler> oldCap = fluidCapability;
        fluidCapability = LazyOptional.of(() -> handlerForCapability());
        oldCap.invalidate();
    }

    private IFluidHandler handlerForCapability() {
        return tankInventory;
    }










    public float getFillState() {
        return (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();
    }



    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!fluidCapability.isPresent())
            refreshCapability();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }



    public IFluidTank getTankInventory() {
        return tankInventory;
    }

    public  Fluid validFuel(){
        return TFMGFluids.GASOLINE.get();
    };

}

