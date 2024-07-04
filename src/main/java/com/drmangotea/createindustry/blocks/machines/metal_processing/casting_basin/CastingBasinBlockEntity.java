package com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin;

import com.drmangotea.createindustry.CreateTFMG;
import com.drmangotea.createindustry.blocks.machines.TFMGMachineBlockEntity;
import com.drmangotea.createindustry.recipes.casting.CastingRecipe;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.drmangotea.createindustry.blocks.machines.metal_processing.casting_basin.CastingBasinBlock.MOLD_TYPE;

public class CastingBasinBlockEntity extends TFMGMachineBlockEntity implements IHaveGoggleInformation {


    public MoldType type;

    private CastingRecipe recipe;

    public int timer = -1;

    public SmartInventory outputInventory;
    public SmartInventory moldInventory;

    public LazyOptional<IItemHandlerModifiable> itemCapability;

    private static final Object CastingRecipesKey = new Object();

    public CastingBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        outputInventory = new SmartInventory(1, this)
                .withMaxStackSize(64);

        moldInventory = new SmartInventory(1, this)
                .withMaxStackSize(1);

        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(outputInventory,moldInventory));
        tank1.getPrimaryHandler().setCapacity(4000);
        tank2.forbidExtraction();
        tank2.forbidInsertion();

        moldInventory.forbidExtraction();
        moldInventory.forbidInsertion();
        outputInventory.forbidInsertion();
    }


    public void tick(){
        super.tick();
        type = getBlockState().getValue(MOLD_TYPE);


        findRecipe();
        //

        setMold();

        //if(tank1.isEmpty()){
            setCapacity();
        //}

        if(type==MoldType.NONE)
            return;

        if(recipe==null)
            return;

        if(outputInventory.getStackInSlot(0).getCount()!=0)
            return;


        if(tank1.getPrimaryHandler().getCapacity()==tank1.getPrimaryHandler().getFluidAmount()){

            if(timer ==-1) {
                //if(type==MoldType.BLOCK)
                    timer = recipe.getProcessingDuration();
                //if(type==MoldType.INGOT)
                  //  timer = recipe.getProcessingDuration()/9;

            }

            if (timer >0)
                timer--;

            if(timer == 0){

                if(type==MoldType.INGOT)
                    outputInventory.setStackInSlot(0,recipe.getIngotResult());
                if(type==MoldType.BLOCK)
                    outputInventory.setStackInSlot(0,recipe.getBlockResult());


                tank1.getPrimaryHandler().setFluid(new FluidStack(Fluids.EMPTY,0));
                timer = -1;
            }

        }

    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, moldInventory);
        ItemHelper.dropContents(level, worldPosition, outputInventory);
    }

    public void setMold(){
        if(moldInventory.isEmpty())
            return;

        MoldType moldType = ((CastingMoldItem)moldInventory.getStackInSlot(0).getItem()).getType();
        MoldType oldMoldType = this.getBlockState().getValue(MOLD_TYPE);
        if(moldType==oldMoldType)
            return;

        level.setBlock(getBlockPos(),this.getBlockState().setValue(MOLD_TYPE,moldType),2);
        type = getBlockState().getValue(MOLD_TYPE);
    }

    public void findRecipe(){
        CombinedTankWrapper tankIn = new CombinedTankWrapper(tank1.getPrimaryHandler(),tank2.getPrimaryHandler());
        if (recipe == null || !recipe.matches(tankIn, level)) {
            CastingRecipe recipe = getMatchingRecipes();
            if (recipe!=null) {
                this.recipe = recipe;
                sendData();
            }
        }
    }

    public void setCapacity(){
        if(type == null){
            if(tank1.getPrimaryHandler().getCapacity()<4000)
                tank1.getPrimaryHandler().setCapacity(4000);
        }else {
            if(tank1.getPrimaryHandler().getCapacity()!=type.getRequiredFluidAmount())
                tank1.getPrimaryHandler().setCapacity(type.getRequiredFluidAmount());
        }
    }

    protected CastingRecipe getMatchingRecipes() {


        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);


        for(int i = 0; i < list.toArray().length;i++){
            CastingRecipe recipe = (CastingRecipe) list.get(i);
            for(int y = 0; y < recipe.getFluidIngredients().get(0).getMatchingFluidStacks().toArray().length;y++)
                if(tank1.getPrimaryHandler().getFluid().getFluid()==recipe.getFluidIngredients().get(0).getMatchingFluidStacks().get(y).getFluid())
                    if(tank1.getPrimaryHandler().getFluidAmount()>=recipe.getFluidIngredients().get(0).getRequiredAmount())
                        return recipe;
        }

        return null;
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


        return true;
    }
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r instanceof CastingRecipe;
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        outputInventory.deserializeNBT(compound.getCompound("OutputItems"));
        moldInventory.deserializeNBT(compound.getCompound("CurrentMold"));


        timer = compound.getInt("Timer");


    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.put("OutputItems", outputInventory.serializeNBT());
        compound.put("CurrentMold", moldInventory.serializeNBT());
        compound.putInt("Timer", timer);



    }
    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
    }
    @Nonnull
    @Override
    @SuppressWarnings("removal")
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemCapability.cast();
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }
    protected Object getRecipeCacheKey() {
        return CastingRecipesKey;
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }


    public enum MoldType implements StringRepresentable {
        INGOT("ingot",112),
        BLOCK("block",1000),

        NONE("none", 4000);



        private final String name;

        private final int fluidAmountNeeded;

        MoldType(String name, int amountNeeded) {
            this.name = name;
            this.fluidAmountNeeded = amountNeeded;
        }

        public int getRequiredFluidAmount(){return this.fluidAmountNeeded;}

        @Override
        public String getSerializedName() {
            return this.name;
        }

    }
}
