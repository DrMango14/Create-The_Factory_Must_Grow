package com.drmangotea.tfmg.content.items.weapons.flamethrover;

import com.drmangotea.tfmg.TFMGClient;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.spark.DryIceFlake;
import com.drmangotea.tfmg.base.spark.LithiumSpark;
import com.drmangotea.tfmg.base.spark.Spark;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGFlamethrowerFuelTypes;
import com.drmangotea.tfmg.registry.TFMGTags;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

@NothingNullByDefault
public class FlamethrowerItem extends Item implements CustomArmPoseItem {
    public static final int FUEL_CAPACITY = 4000;

    public FlamethrowerItem(Properties pProperties) {
        super(pProperties);
    }

    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {
        if (stack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY) == FlamethrowerFuel.EMPTY)
            return;

        int fuelAmount = stack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY).amount();

        if(fuelAmount==0) {
            stack.set(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY);
            entity.stopUsingItem();
            return;
        }

        Registry<FlamethrowerFuelType> registry = level.registryAccess().registryOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE);

        FlamethrowerFuel fuel = stack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY);
        FlamethrowerFuelType fuelType = getFuel(level.registryAccess(), stack);

        Vec3 barrelPos = TFMGUtils.getGunBarrelVec(entity, entity.getUsedItemHand() == InteractionHand.MAIN_HAND,
                    new Vec3(.75f, -0.65f, 1.5f));

        boolean isCold = fuel.fuelType() != null && registry.getHolder(fuel.fuelType().location()).filter((holder) -> holder.is(TFMGTags.FlamethrowerFuel.COLD.tag)).isPresent();
        boolean isHellfire = fuel.fuelType() != null && registry.getHolder(fuel.fuelType().location()).filter((holder) -> holder.is(TFMGTags.FlamethrowerFuel.HELLFIRE.tag)).isPresent();

        SoundEvent sound = isCold ? SoundEvents.SNOW_BREAK : SoundEvents.FIRE_EXTINGUISH;
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound, SoundSource.PLAYERS, 0.1F, 0.04F);

        if (fuelType != null && fuel.fuelType() != TFMGFlamethrowerFuelTypes.FALLBACK && fuel.amount() > 0) {
            int amountToFire = Math.min(fuelType.amount(), fuel.amount());
            for(int i =0; i < amountToFire; i++) {
                if (isCold) {
                    DryIceFlake flake = TFMGEntityTypes.DRY_ICE_FLAKE.create(level);
                    if (flake != null) {
                        flake.setPos(barrelPos.x, barrelPos.y, barrelPos.z);
                        flake.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, fuelType.speed(), fuelType.spread());
                        level.addFreshEntity(flake);
                    }
                } else if (isHellfire) {
                    LithiumSpark spark = TFMGEntityTypes.LITHIUM_SPARK.create(level);
                    if (spark != null) {
                        spark.setPos(barrelPos.x, barrelPos.y, barrelPos.z);
                        spark.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, fuelType.speed(), fuelType.spread());
                        level.addFreshEntity(spark);
                    }
                } else {
                    Spark spark = TFMGEntityTypes.SPARK.create(level);
                    if (spark != null) {
                        spark.setPos(barrelPos.x, barrelPos.y, barrelPos.z);
                        spark.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, fuelType.speed(), fuelType.spread());
                        level.addFreshEntity(spark);
                    }
                }
            }
            int fuelConsumed = level.random.nextIntBetweenInclusive(amountToFire / 2, amountToFire);
            stack.set(TFMGDataComponents.FLAMETHROWER, fuel.decrement(fuelConsumed));
        } else {
            stack.set(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY);
            entity.stopUsingItem();
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if(!stack.has(TFMGDataComponents.FLAMETHROWER))
            return false;

        return !stack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY).isEmpty();
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if(!stack.has(TFMGDataComponents.FLAMETHROWER))
            stack.set(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY);

        return stack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY).color();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if(!stack.has(TFMGDataComponents.FLAMETHROWER))
            stack.set(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY);

        return Math.round( 13* ((float)stack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY).amount()/(float)FUEL_CAPACITY));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY).isEmpty()) {
            player.startUsingItem(hand);
        }

        if (level.isClientSide) {
            TFMGClient.FLAMETHROWER_RENDER_HANDLER.dontAnimateItem(hand);
        }

        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 1000;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!stack.has(TFMGDataComponents.FLAMETHROWER)) stack.set(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY);

        FlamethrowerFuel existingFuel = stack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY);

        int containedFuel = existingFuel.amount();
        @Nullable ResourceKey<FlamethrowerFuelType> fuelType = existingFuel.fuelType();

        boolean foundFluid = false;

        if (blockEntity != null) {
            IFluidHandler capability = level.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity.getBlockPos(), context.getClickedFace());
            if (capability != null) {
                for (int i = 0; i < capability.getTanks(); i++) {
                    if (capability.getFluidInTank(i).isEmpty()) continue;
                    FluidStack fluidStack = capability.getFluidInTank(i);
                    int toDrain = Math.min(FUEL_CAPACITY - containedFuel, fluidStack.getAmount());
                    FluidStack stackToDrain = fluidStack.copyWithAmount(toDrain);
                    FlamethrowerFuel fuel = FlamethrowerFuel.createForType(level.registryAccess(), fluidStack.getFluid(), toDrain);
                    if (fuel == FlamethrowerFuel.EMPTY) continue;
                    if (fuelType != TFMGFlamethrowerFuelTypes.FALLBACK) {
                        if (fuelType.equals(fuel.fuelType())) {
                            FluidStack actuallyDrained = capability.drain(stackToDrain, IFluidHandler.FluidAction.EXECUTE);
                            stack.set(TFMGDataComponents.FLAMETHROWER, existingFuel.increment(actuallyDrained.getAmount(), FUEL_CAPACITY));
                            context.getPlayer().getCooldowns().addCooldown(stack.getItem(), 20);
                            foundFluid = true;
                        }
                    } else {
                        stack.set(TFMGDataComponents.FLAMETHROWER, fuel);
                        capability.drain(stackToDrain, IFluidHandler.FluidAction.EXECUTE);
                        context.getPlayer().getCooldowns().addCooldown(stack.getItem(), 20);
                        foundFluid = true;
                    }
                }
            }
        }

        return foundFluid ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Nullable
    public static FlamethrowerFuelType getFuel(RegistryAccess registryAccess, ItemStack heldStack) {
        var type = heldStack.getOrDefault(TFMGDataComponents.FLAMETHROWER, FlamethrowerFuel.EMPTY).getFuelType(registryAccess);
        return type.orElse(registryAccess.registryOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE).get(TFMGFlamethrowerFuelTypes.FALLBACK));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || newStack.getItem() != oldStack.getItem();
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        return true;
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
}
