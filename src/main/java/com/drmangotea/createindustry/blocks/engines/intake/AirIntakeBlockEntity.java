package com.drmangotea.createindustry.blocks.engines.intake;

import com.drmangotea.createindustry.registry.TFMGFluids;
import com.drmangotea.createindustry.registry.TFMGTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.drmangotea.createindustry.blocks.engines.intake.AirIntakeBlock.INVISIBLE;
import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class AirIntakeBlockEntity extends KineticBlockEntity implements IWrenchable {


    int diameter = 1;

    boolean isController=false;

    public boolean hasShaft=true;

    boolean isUsedByController = false;

    int efficiency = 1;
    boolean allObstructed = false;
    boolean isObstructed = false;
    
    public BlockPos controller;

    public List<AirIntakeBlockEntity> blockEntities = new ArrayList<>();

    public float maxShaftSpeed =0;

    public float angle = 0;
    public LerpedFloat visual_angle = LerpedFloat.angular();

    protected FluidTank tankInventory;
    protected LazyOptional<IFluidHandler> fluidCapability;





    public AirIntakeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        tankInventory = createInventory();
        fluidCapability = LazyOptional.of(() -> tankInventory);


    }

    public void tick(){
        super.tick();

    //if(!level.isClientSide) {
        int production = ((int) maxShaftSpeed * ((diameter * diameter))) / 10;
        if (tankInventory.getFluidAmount() + production <= tankInventory.getCapacity() && !allObstructed) {
            tankInventory.setFluid(new FluidStack(TFMGFluids.AIR.getSource(), production + tankInventory.getFluidAmount()));
        }
        
        isObstructed = hasBlockInFront(this.getBlockPos());
        
   // }
        ////////////////

        if(isUsedByController) {
            refreshCapability();
            sendData();
            setChanged();
        }
        
        efficiency = getEfficiency();

        if(diameter == 3){
            visual_angle.chase(angle, 0.1f, LerpedFloat.Chaser.EXP);
            visual_angle.tickChaser();
        }

            angle+=maxShaftSpeed/2;


        angle %= 360;


        if(isUsedByController)
            blockEntities.clear();
        
        allObstructed = efficiency == 0;

        if(!this.getBlockState().getValue(INVISIBLE)){
            if(isController||isUsedByController){
                level.setBlock(this.getBlockPos(),this.getBlockState().setValue(INVISIBLE,true),2);
            }


        }
        if(!isController&&!isUsedByController)
            level.setBlock(this.getBlockPos(),this.getBlockState().setValue(INVISIBLE,false),2);

        if(controller == null)
            controller = this.getBlockPos();

        diameter =getPossibleDiameter();

        if(controller == this.getBlockPos()) {
            
            isUsedByController = false;
        } else {
            isUsedByController = true;
            isController = false;
        }

        if(diameter ==1) {
            isController = false;

        }

        if(!(level.getBlockEntity(controller) instanceof AirIntakeBlockEntity)) {
            isUsedByController = false;
            controller = this.getBlockPos();

        } else {

            if(!(((AirIntakeBlockEntity) level.getBlockEntity(controller)).isController))
                isUsedByController = false;
        }
        //else
        //    if(!(((AirIntakeBlockEntity) level.getBlockEntity(controller)).isController))
        //        controller = this.getBlockPos();

        if(controller!=null) {
            if(level.getBlockEntity(controller)!=null)
                if(((AirIntakeBlockEntity)level.getBlockEntity(controller)).diameter==2) {
                    int x = Math.abs(this.getBlockPos().getX() - controller.getX());
                    int y = Math.abs(this.getBlockPos().getY() - controller.getY());
                    int z = Math.abs(this.getBlockPos().getZ() - controller.getZ());

                    int distanceFromController = x + y + z;
                    if (x > 1 || y > 1 || z > 1) {
                        isUsedByController = false;
                        controller = this.getBlockPos();
                    }
                }
            if(level.getBlockEntity(controller)!=null)
            if(((AirIntakeBlockEntity)level.getBlockEntity(controller)).diameter==1) {
                isUsedByController = false;
                controller = this.getBlockPos();
            }

        }



        ////////////////////////

        if(diameter == 1){
            maxShaftSpeed = Math.abs(getSpeed());

        }else {
            maxShaftSpeed = Math.abs(getSpeed());
            List<Float> speeds = new ArrayList<>();
//
            for (AirIntakeBlockEntity be : blockEntities) {
                speeds.add(Math.abs(be.getSpeed()));
//
            }
//
            for(float testedSpeed : speeds){
                if(testedSpeed> maxShaftSpeed)
                    maxShaftSpeed = testedSpeed;
            }
            // maxShaftSpeed = getSpeed();
        }



        if(isUsedByController)
            return;

        if(diameter ==2){

            if(blockEntities.toArray().length!=4)
                return;
        }
        if(diameter ==3){
            if(blockEntities.toArray().length!=9)
                return;
        }




    }
    
    public int getEfficiency(){
        int result;
        if (diameter == 1) {
            result = 1;
            if (isObstructed) {
                result = 0;
            }
        } else {
            int fans = blockEntities.toArray().length;
            for (AirIntakeBlockEntity be : blockEntities) {
                if (be.isObstructed) {
                    fans--;
                }
            }
            result = fans;
        }
        return result;
    }
    
    public boolean hasBlockInFront(BlockPos pos){
        return !level.getBlockState(pos.relative(this.getBlockState().getValue(FACING))).is(TFMGTags.TFMGBlockTags.AIR_INTAKE_TRANSPARENT.tag) && !level.getBlockState(pos.relative(this.getBlockState().getValue(FACING))).isAir();
    }
    
    @Override
    public void invalidate() {
        super.invalidate();

        fluidCapability.invalidate();
    }

    public InteractionResult onWrenched(BlockState state, UseOnContext context){
        Direction direction = context.getClickedFace();

        if(direction == getBlockState().getValue(FACING).getOpposite()) {
            hasShaft = !hasShaft;
        }
        return InteractionResult.SUCCESS;
    }

    public void setController(BlockPos controllerPos) {
      //  isUsedByController = true;
        controller  = controllerPos;

    }


    @Nonnull
    @Override
    @SuppressWarnings("removal")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {



        if (!fluidCapability.isPresent()) {
            refreshCapability();
            sendData();
            setChanged();
        }



        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

    private void refreshCapability() {

        IFluidHandler handlerForCapability;

        if (controller == null || controller == this.getBlockPos()

        ) {
            handlerForCapability = tankInventory;
        } else
        if(((AirIntakeBlockEntity) level.getBlockEntity(controller))!=null) {
            handlerForCapability = ((AirIntakeBlockEntity) level.getBlockEntity(controller)).tankInventory;
        }else             handlerForCapability = tankInventory;


        LazyOptional<IFluidHandler> oldCap = fluidCapability;
        IFluidHandler finalHandlerForCapability = handlerForCapability;
        fluidCapability = LazyOptional.of(() -> finalHandlerForCapability);
        //oldCap.invalidate();
    }





    public int getPossibleDiameter(){

        if(controller !=this.getBlockPos())
            return 1;



        BlockPos checkedPos = this.getBlockPos();
        Direction direction = this.getBlockState().getValue(FACING);

        List<BlockPos> checkedPosses = new ArrayList<>();
        checkedPos = this.getBlockPos();

        boolean canBeMedium = true;
        for(int x = 0;x < 2; x++){
            for(int z = 0;z < 2; z++){
                checkedPosses.add(checkedPos);
            if(direction.getAxis().isHorizontal()) {
                checkedPos = checkedPos.above();
            }else checkedPos = checkedPos.east();
            }
            if(direction.getAxis().isHorizontal()) {
                checkedPos = checkedPos.below(2);
                checkedPos = checkedPos.relative(direction.getClockWise());
            } else {
                checkedPos = checkedPos.west(2);
                checkedPos = checkedPos.south();

            }
        }
        List<BlockPos> checkedPossesLarge = new ArrayList<>();
        checkedPos = this.getBlockPos();

        boolean canBeLarge = true;
        for(int x = 0;x < 3; x++){
            for(int z = 0;z < 3; z++){
                checkedPossesLarge.add(checkedPos);
                if(direction.getAxis().isHorizontal()) {
                    checkedPos = checkedPos.above();
                }else checkedPos = checkedPos.east();
            }
            if(direction.getAxis().isHorizontal()) {
                checkedPos = checkedPos.below(3);
                checkedPos = checkedPos.relative(direction.getClockWise());
            } else {
                checkedPos = checkedPos.west(3);
                checkedPos = checkedPos.south();

            }
        }
        //LARGE
        for(BlockPos pos : checkedPossesLarge){
            if(!(level.getBlockEntity(pos) instanceof AirIntakeBlockEntity)) {
                canBeLarge = false;
                break;
            }

            // ((AirIntakeBlockEntity) level.getBlockEntity(pos)).controller = this.getBlockPos();
            AirIntakeBlockEntity checkedBE = (AirIntakeBlockEntity) level.getBlockEntity(pos);

            //if(checkedBE.diameter<3)
            //    ((AirIntakeBlockEntity) level.getBlockEntity(pos)).isController = false;

           // if(pos!=this.getBlockPos())
           //     if(checkedBE.isController) {
//
//
           //         canBeLarge = false;
           //         break;
           //     }



            if(checkedBE.getBlockState().getValue(FACING) != this.getBlockState().getValue(FACING)) {
                canBeLarge = false;
                break;
            }

            //if(pos!=this.getBlockPos())
            //    ((AirIntakeBlockEntity) level.getBlockEntity(pos)).isUsedByController = true;


        }
        //MEDIUM
            for(BlockPos pos : checkedPosses){
                if(!(level.getBlockEntity(pos) instanceof AirIntakeBlockEntity)) {
                    canBeMedium = false;
                    break;
                }

                   // ((AirIntakeBlockEntity) level.getBlockEntity(pos)).controller = this.getBlockPos();
                AirIntakeBlockEntity checkedBE = (AirIntakeBlockEntity) level.getBlockEntity(pos);

                if(pos!=this.getBlockPos())
                    if(checkedBE.isController) {
                        canBeMedium = false;
                        break;
                }


                if(checkedBE.getBlockState().getValue(FACING) != this.getBlockState().getValue(FACING)) {
                    canBeMedium = false;
                    break;
                }

                //if(pos!=this.getBlockPos())
                //    ((AirIntakeBlockEntity) level.getBlockEntity(pos)).isUsedByController = true;


            }


            if(canBeLarge) {
                this.blockEntities.clear();
                for(BlockPos pos : checkedPossesLarge) {
                    //if(((AirIntakeBlockEntity) level.getBlockEntity(pos)).isUsedByController&&((AirIntakeBlockEntity) level.getBlockEntity(pos)).controller!=this.getBlockPos()&&pos!=this.getBlockPos()) {
                    //    controller = this.getBlockPos();
                    //    isController = false;
                    //    return 1;
                    //}

                    if((((AirIntakeBlockEntity) level.getBlockEntity(pos)).isUsedByController&&((AirIntakeBlockEntity) level.getBlockEntity(pos)).controller!=this.getBlockPos()&&pos!=this.getBlockPos())||isController) {

                        ((AirIntakeBlockEntity) level.getBlockEntity(pos)).isUsedByController = true;
                        ((AirIntakeBlockEntity) level.getBlockEntity(pos)).isController = false;
                        ((AirIntakeBlockEntity) level.getBlockEntity(pos)).controller =this.getBlockPos();

                    }

                    ((AirIntakeBlockEntity) level.getBlockEntity(pos)).setController(this.getBlockPos());
                    this.blockEntities.add((AirIntakeBlockEntity) level.getBlockEntity(pos));
                }

                controller = this.getBlockPos();
                isController = true;
                return 3;
            }


        if(canBeMedium) {
            this.blockEntities.clear();



            for(BlockPos pos : checkedPosses) {
                if(((AirIntakeBlockEntity) level.getBlockEntity(pos)).isUsedByController&&((AirIntakeBlockEntity) level.getBlockEntity(pos)).controller!=this.getBlockPos()&&pos!=this.getBlockPos()) {
                    controller = this.getBlockPos();
                    isController = false;
                    return 1;
                }
                ((AirIntakeBlockEntity) level.getBlockEntity(pos)).setController(this.getBlockPos());
                this.blockEntities.add((AirIntakeBlockEntity) level.getBlockEntity(pos));
            }

            controller = this.getBlockPos();
            isController = true;
            return 2;
        }

        controller = this.getBlockPos();
        isController = false;
        return 1;
    }
    @Override
    protected AABB createRenderBoundingBox() {


        return new AABB(this.getBlockPos()).inflate(3);
    }
    @Override
    @SuppressWarnings("removal")
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {



        //--Fluid Info--//
        LazyOptional<IFluidHandler> handler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        IFluidHandler tank = resolve.get();
        if (tank.getTanks() == 0)
            return false;

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");


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
                            .style(ChatFormatting.DARK_GREEN))
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
                        .style(ChatFormatting.DARK_GREEN))
                .style(ChatFormatting.DARK_GRAY)
                .forGoggles(tooltip, 1);
        
        if (isObstructed) {
            Lang.translate("gui.goggles.fluid_container.obstructed")
                    .style(ChatFormatting.RED)
                    .forGoggles(tooltip, 1);
        }
        
        Lang.number(getEfficiency())
                .style(ChatFormatting.DARK_GREEN)
                .forGoggles(tooltip, 1);


        return true;
    }
    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(8000, this::onFluidStackChanged){
            @Override
            public boolean isFluidValid(FluidStack stack) {
                return stack.getFluid().isSame(TFMGFluids.AIR.getSource());
            }
        //    @Override
        //    public FluidStack drain(FluidStack resource, FluidAction action) {
        //        return FluidStack.EMPTY;
        //    }
        };
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
            setChanged();
            sendData();
           //if(((AirIntakeBlockEntity) level.getBlockEntity(controller))!=null) {
           //    ((AirIntakeBlockEntity) level.getBlockEntity(controller)).setChanged();
           //    ((AirIntakeBlockEntity) level.getBlockEntity(controller)).sendData();
           //}

    }


    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        diameter = compound.getInt("Diameter");
        isController = compound.getBoolean("IsController");
        isUsedByController = compound.getBoolean("IsUsed");
        hasShaft = compound.getBoolean("HasShaft");
        tankInventory.readFromNBT(compound.getCompound("TankContent"));
        efficiency = compound.getInt("Efficiency");
        isObstructed = compound.getBoolean("IsObstructed");
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);


        compound.putInt("Diameter", diameter);
        compound.putBoolean("IsController", isController);
        compound.putBoolean("IsUsed", isUsedByController);
        compound.putBoolean("HasShaft", hasShaft);
        compound.put("TankContent", tankInventory.writeToNBT(new CompoundTag()));
        compound.putInt("Efficiency", efficiency);
        compound.putBoolean("IsObstructed", isObstructed);

    }
}
