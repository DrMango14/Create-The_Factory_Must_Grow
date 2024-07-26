package com.drmangotea.createindustry.items.weapons.flamethrover;

import com.drmangotea.createindustry.CreateTFMGClient;
import com.drmangotea.createindustry.base.util.spark.CoolSpark;
import com.drmangotea.createindustry.base.util.spark.Spark;
import com.drmangotea.createindustry.items.weapons.lithium_blade.LithiumSpark;
import com.drmangotea.createindustry.registry.TFMGCreativeModeTabs;
import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FlamethrowerItem extends Item implements CustomArmPoseItem {


    public static final int FUEL_CAPACITY = 4000;


    public FlamethrowerItem(Properties pProperties) {
        super(pProperties);
    }


    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {
        
        
        CoolSpark coolSpark = TFMGEntityTypes.COOL_SPARK.create(level);
        coolSpark.setPos(entity.getX(),entity.getY()+1.2f,entity.getZ());
        Spark spark = TFMGEntityTypes.SPARK.create(level);
        spark.setPos(entity.getX(),entity.getY()+1.2f,entity.getZ());
        LithiumSpark lithiumSpark = TFMGEntityTypes.LITHIUM_SPARK.create(level);
        lithiumSpark.setPos(entity.getX(),entity.getY()+1.2f,entity.getZ());

        CompoundTag nbt = stack.getOrCreateTag();

        if(nbt.getInt("amount")==0) {
            nbt.putString("fuel","");
            return;
        }

        //if(true)
        //    return;
        FlamethrowerFuelType fuel = FlamethrowerFuelTypeManager.getTypeForId(new ResourceLocation(nbt.getString("fuel")));
        
        if (fuel == null) {
            return;
        }
        
        SoundEvent sound = fuel.isCold() ? SoundEvents.SNOW_BREAK : SoundEvents.FIRE_EXTINGUISH;
        level.playSound((Player)null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.NEUTRAL, 0.1F, 0.04F);


        for(int i =0;i<fuel.getAmount();i++) {
            if(nbt.getInt("amount")==0) {
                nbt.putString("fuel","");
                return;
            }
            nbt.putInt("amount",nbt.getInt("amount")-1);

            //Snuck this in here, sorry Mango :3
            if (fuel.isCold()) {
                coolSpark.shoot(entity.getLookAngle().x,entity.getLookAngle().y,entity.getLookAngle().z,fuel.getSpeed(),fuel.getSpread());
                level.addFreshEntity(coolSpark);
            } else {
                spark.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, fuel.getSpeed(), fuel.getSpread());
                level.addFreshEntity(spark);
            }
            if (fuel.isHellfire()) {
                lithiumSpark.shoot(entity.getLookAngle().x,entity.getLookAngle().y,entity.getLookAngle().z,fuel.getSpeed(),fuel.getSpread());
                level.addFreshEntity(lithiumSpark);
            }
        }

    }



    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {

        if(group != TFMGCreativeModeTabs.TFMG_BASE)
            return;

        ItemStack stack = TFMGItems.FLAMETHROWER.asStack();
        stack.getOrCreateTag().putString("fuel","createindustry:napalm");
        stack.getOrCreateTag().putInt("amount",FUEL_CAPACITY);
        list.add(stack);
        super.fillItemCategory(group, list);
    }

    public int getUseDuration(ItemStack p_77626_1_) {
        return 696969;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrCreateTag().getInt("amount")!=0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return stack.getOrCreateTag().getString("fuel").isEmpty() ? 0xffffff : FlamethrowerFuelTypeManager.getGlobalType(new ResourceLocation(stack.getOrCreateTag().getString("fuel"))).getColor();
    }

    @Override
    public int getBarWidth(ItemStack stack) {

return Math.round( 13* ((float)((float)stack.getOrCreateTag().getInt("amount")/(float)FUEL_CAPACITY)));

    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);



        if (level.isClientSide) {
            CreateTFMGClient.FLAMETHROWER_RENDER_HANDLER.dontAnimateItem(hand);
        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        CompoundTag nbt = stack.getOrCreateTag();




        if(level.getBlockEntity(pos)!=null)
            if(level.getBlockEntity(pos) instanceof FluidTankBlockEntity fluidTankBe){
                
                
                FluidTankBlockEntity be = fluidTankBe.isController() ? fluidTankBe : fluidTankBe.getControllerBE();
                //String fluid = be.getFluid(0).getFluid().getFluidType().toString();
                //FlamethrowerFuelType fuelType = FlamethrowerFuelTypeManager.getTypeForStack(be.getFluid(0)).orElse(BuiltinFlamethrowerFuelTypes.FALLBACK);
                //int toDrain = Math.min(FUEL_CAPACITY - nbt.getInt("amount"), be.getFluid(0).getAmount());
                //nbt.putString("fuel", FlamethrowerFuelTypeManager.getIdForType(fuelType).toString());
                //be.getTankInventory().drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                //nbt.putInt("amount", nbt.getInt("amount") + toDrain);
                //context.getPlayer().getCooldowns().addCooldown(stack.getItem(), 20);
                for (FlamethrowerFuelType fuelBuiltin : FlamethrowerFuelTypeManager.GLOBAL_TYPE_MAP.values()) {
                    if (fuelBuiltin.getFluids().stream().anyMatch(supplier -> supplier.get().isSame(be.getFluid(0).getFluid()))) {
                        int toDrain = Math.min(FUEL_CAPACITY - nbt.getInt("amount"), be.getFluid(0).getAmount());
                        nbt.putString("fuel", FlamethrowerFuelTypeManager.getIdForType(fuelBuiltin).toString());
                        be.getTankInventory().drain(toDrain, IFluidHandler.FluidAction.EXECUTE);
                        nbt.putInt("amount", nbt.getInt("amount") + toDrain);
                        context.getPlayer().getCooldowns().addCooldown(stack.getItem(), 20);
                        return InteractionResult.SUCCESS;
                    }
                }
            }



        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public HumanoidModel.ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        if (!player.swinging) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
        return null;
    }


    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }

    enum FlamethrowerFuel{

        GASOLINE(15,1,3,0xC4AA76),
        DIESEL(7,2,3,0xBA9177),
        KEROSENE(10,1.3f,4,0x7876D5),

        NAPHTHA(20,0.8f,1,0x5E1B0A),

        LPG(35,0.6f,15,0xE0BB48),

        NAPALM(20,1.8f,15,0xA3C649),

        MOLTEN_SLAG(15,0.3f,15,0xFF9621),
        
        //Sorry Mango, I had to do it :3
        COOLING_FLUID(12,0.8f,15,0x4edbdb)

        ;

        public final float spread;
        public final float speed;
        public final int amount;

        public final int color;



        FlamethrowerFuel(float spread, float speed, int amount,int color){
            this.spread = spread;
            this.speed = speed;
            this.amount = amount;
            this.color = color;

        }

    }

}
