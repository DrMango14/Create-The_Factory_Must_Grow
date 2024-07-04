package com.drmangotea.createindustry.blocks.engines.compact;


import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGSoundEvents;
import com.drmangotea.createindustry.registry.TFMGTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("removal")
public class CompactEngineBlockEntity extends GeneratingKineticBlockEntity implements IHaveGoggleInformation, IWrenchable {

    protected LazyOptional<IFluidHandler> fluidCapability;
    protected FluidTank tankInventory;

    protected FluidTank lubricationOilTank;

    protected FluidTank coolantTank;

    protected int soundTimer=0;


    public int fuelConsumption =0;

    public float stressTotal=0;
    public float speed=0;
    public float stressBase=0;

    public int efficiency=1;
    public final int idealSpeed=12;

    private int consumptionTimer=0;


    public Fluid lubricationOil = TFMGFluids.LUBRICATION_OIL.get();
    public Fluid coolant = TFMGFluids.COOLING_FLUID.get();

    public float powerModifier=1;
    public float efficiencyModifier = 1.4f;
//
    int signal;
    boolean signalChanged;
//

   // protected ScrollValueBehaviour generatedSpeed;

    public CompactEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
         tankInventory = createInventory();

         lubricationOilTank = createUpgradeTankInventory(lubricationOil);
         coolantTank = createUpgradeTankInventory(coolant);


        //fluidCapability = LazyOptional.of(() -> tankInventory);
        fluidCapability = LazyOptional.of(() -> {
            return new CombinedTankWrapper(tankInventory,lubricationOilTank,coolantTank );
        });

        signal = 0;
        setLazyTickRate(40);

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



        if(!level.isClientSide){


            calculateEfficiency();
             fuelConsumption = (int)((speed/(efficiency/10)/13)+1);
            if(fuelConsumption<1)
                fuelConsumption=0;

            if(this.isOverStressed())
                fuelConsumption = 0;

        if(!tankInventory.isEmpty()) {

            if(consumptionTimer>=45) {
                if(signal!=0)
                    tankInventory.drain(fuelConsumption, IFluidHandler.FluidAction.EXECUTE);
                consumptionTimer=0;
            }
            consumptionTimer++;



          //  return convertToDirection((signal * signal), getBlockState().getValue(FACING))*Create.RANDOM.nextFloat(2);
            return ((signal*signal)*0.5f)*powerModifier;
                  //  *powerModifier;
        }}
        return 0;


    }


    public void calculateEfficiency(){


        if(signal==0||tankInventory.isEmpty()) {
            efficiency = 0;
            return;
        }
        efficiency=100;

        if(signal>=idealSpeed){
            efficiency= (int) ((100-(signal-idealSpeed)*5)/efficiencyModifier);
        }

        if(signal<idealSpeed){
            efficiency= (int) ((100-(idealSpeed-signal)*3)/efficiencyModifier);
        }


        if(efficiency>100)
            efficiency=100;

    }



    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        // boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        //     if (!IRotate.StressImpact.isEnabled())
        //        return added;
        //
              Lang.translate("goggles.engine_stats")
                        .forGoggles(tooltip);
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
                .add(Lang.translate("goggles.misc.percent_symbol"))
                .forGoggles(tooltip,1);


////////////////////////////////////////
        LazyOptional<IFluidHandler> handler = this.getCapability(ForgeCapabilities.FLUID_HANDLER);
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



    compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));
    compound.put("Coolant", coolantTank.writeToNBT(new CompoundTag()));
    compound.put("LubricationOil", lubricationOilTank.writeToNBT(new CompoundTag()));


    super.write(compound, clientPacket);
}

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {



            tankInventory.readFromNBT(compound.getCompound("TankContent"));
            coolantTank.readFromNBT(compound.getCompound("Coolant"));
            lubricationOilTank.readFromNBT(compound.getCompound("LubricationOil"));






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

        calculateUpgradeModifier();

        //
        int random1 = Create.RANDOM.nextInt(125);
        int random2 = Create.RANDOM.nextInt(200);

        if(random1 == 69)
            coolantTank.drain(1, IFluidHandler.FluidAction.EXECUTE);
        if(random2 == 69)
            lubricationOilTank.drain(1, IFluidHandler.FluidAction.EXECUTE);


        //



        ///
       // if(signal!=0&&hasBackPart()&&tankInventory.getFluidAmount()!=0&&!overStressed&&isExhaustTankFull()) {


            soundTimer++;

         //   if(!isExhaustTankFull()) {
        if (soundTimer >= (((16-signal)/0.8)+1)/2) {
               if(signal!=0&&
                       tankInventory.getFluidAmount()!=0 &&
                       !overStressed


               ){


              //  if(this.getGeneratedSpeed()!=0) {
                    if (level.isClientSide)
                        makeSound();

              }
           }
          //  }


        ///
        updateGeneratedRotation();
        calculateEfficiency();




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



    }


    public void calculateUpgradeModifier(){


        float newPowerModifier=1;
        float newEfficiencyModifier = 1.4f;

        if(lubricationOilTank.getFluidAmount()>0) {
            //newPowerModifier+=.3f;
            newEfficiencyModifier-=.1f;
        }
        if(coolantTank.getFluidAmount()>0) {
            newPowerModifier+=.3f;
            newEfficiencyModifier-=.3f;
        }

        ////////


        ////

        powerModifier=newPowerModifier;
        efficiencyModifier = newEfficiencyModifier;
    }




    @OnlyIn(Dist.CLIENT)
    private void makeSound(){
        soundTimer=0;


            TFMGSoundEvents.ENGINE.playAt(level, worldPosition, 0.04f, 1f, false);


    }
    public boolean playerInteract(Player pPlayer, InteractionHand pHand) {

        ItemStack stack = pPlayer.getItemInHand(pHand);

        if(stack.is(TFMGFluids.GASOLINE.getBucket().get())&&tankInventory.isEmpty()){

            tankInventory.setFluid(new FluidStack(TFMGFluids.GASOLINE.get(),1000));
            pPlayer.setItemInHand(pHand, Items.BUCKET.getDefaultInstance());

            return true;
        }

        return false;

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
                return stack.getFluid().is(validFuel());
            }
        };
    }
    protected SmartFluidTank createUpgradeTankInventory(Fluid validFluid) {
        return new SmartFluidTank(1000, this::onFluidStackChanged){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(validFluid);
            }
        };
    }


    protected void onFluidStackChanged(FluidStack newFluidStack) {}








    public float getFillState() {
        return (float) tankInventory.getFluidAmount() / tankInventory.getCapacity();
    }



    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == ForgeCapabilities.FLUID_HANDLER)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
    }



    public IFluidTank getTankInventory() {
        return tankInventory;
    }

    public TagKey<Fluid> validFuel(){
        return TFMGTags.TFMGFluidTags.GASOLINE.tag;
    };

}

