package com.drmangotea.tfmg.content.items.weapons.flamethrover;

import com.drmangotea.tfmg.TFMGClient;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.spark.Spark;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGFlamethrowerFuelTypes;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class FlamethrowerItem extends Item implements CustomArmPoseItem {


    public static final int FUEL_CAPACITY = 4000;


    public FlamethrowerItem(Properties pProperties) {
        super(pProperties);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(!stack.has(TFMGDataComponents.FLAMETHROWER_FUEL))
            stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);
    }

    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int time) {
        if (stack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY) == FlamethrowerFuel.EMPTY)
            return;

        int fuelAmount = stack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY).amount();

        if(fuelAmount==0) {
            stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);
            entity.stopUsingItem();
            return;
        }

        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 0.1F, 0.04F);

        FlamethrowerFuel fuel = stack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);
        FlamethrowerFuelType fuelType = getFuel(level.registryAccess(), stack);

        Vec3 barrelPos = getGunBarrelVec(entity, entity.getUsedItemHand() == InteractionHand.MAIN_HAND,
                    new Vec3(.75f, -0.65f, 1.5f));

        if (fuel.fuelType() != TFMGFlamethrowerFuelTypes.FALLBACK && fuel.amount() > 0) {
            int amountToFire = Math.min(fuelType.amount(), fuel.amount());
            for(int i =0; i < amountToFire; i++) {
                Spark spark = TFMGEntityTypes.SPARK.create(level);
                if (spark != null) {
                    spark.setPos(barrelPos.x, barrelPos.y, barrelPos.z);
                    spark.shoot(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z, fuelType.speed(), fuelType.spread());
                    level.addFreshEntity(spark);
                }
            }
            int fuelConsumed = level.random.nextIntBetweenInclusive(amountToFire / 2, amountToFire);
            stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, fuel.decrement(fuelConsumed));
        } else {
            stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);
            entity.stopUsingItem();
        }
    }

    public static Vec3 getGunBarrelVec(LivingEntity entity, boolean mainHand, Vec3 rightHandForward) {
        Vec3 start = entity.position()
                .add(0, entity.getEyeHeight(), 0);
        float yaw = (float) ((entity.getYRot()) / -180 * Math.PI);
        float pitch = (float) ((entity.getXRot()) / -180 * Math.PI);
        int flip = mainHand == (entity.getMainArm() == HumanoidArm.RIGHT) ? -1 : 1;
        Vec3 barrelPosNoTransform = new Vec3(flip * rightHandForward.x, rightHandForward.y, rightHandForward.z);
        Vec3 barrelPos = start.add(barrelPosNoTransform.xRot(pitch)
                .yRot(yaw));
        return barrelPos;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if(!stack.has(TFMGDataComponents.FLAMETHROWER_FUEL))
            return false;

        return !stack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY).isEmpty();
    }

    @Override
    public int getBarColor(ItemStack stack) {
        if(!stack.has(TFMGDataComponents.FLAMETHROWER_FUEL))
            stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);

        return stack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY).color();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if(!stack.has(TFMGDataComponents.FLAMETHROWER_FUEL))
            stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);

        return Math.round( 13* ((float)stack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY).amount()/(float)FUEL_CAPACITY));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);

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

        if (!stack.has(TFMGDataComponents.FLAMETHROWER_FUEL)) stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);

        FlamethrowerFuel existingFuel = stack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);

        int containedFuel = existingFuel.amount();
        @Nullable ResourceKey<FlamethrowerFuelType> fuelType = existingFuel.fuelType();

        boolean foundFluid = false;

        if (blockEntity != null) {
            IFluidHandler capability = level.getCapability(Capabilities.FluidHandler.BLOCK, blockEntity.getBlockPos(), context.getClickedFace());
            if (capability != null) {
                if (!foundFluid) {
                    for (int i = 0; i < capability.getTanks(); i++) {
                        if (capability.getFluidInTank(i).isEmpty()) continue;
                        FluidStack fluidStack = capability.getFluidInTank(i);
                        int toDrain = Math.min(FUEL_CAPACITY - containedFuel, fluidStack.getAmount());
                        FluidStack stackToDrain = fluidStack.copyWithAmount(toDrain);
                        FlamethrowerFuel fuel = FlamethrowerFuel.createForType(level.registryAccess(), fluidStack.getFluid(), toDrain);
                        if (fuel == FlamethrowerFuel.EMPTY) continue;
                        if (fuelType != TFMGFlamethrowerFuelTypes.FALLBACK) {
                            if (fuelType.equals(fuel.fuelType())) {
                                stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, existingFuel.increment(toDrain, FUEL_CAPACITY));
                                capability.drain(stackToDrain, IFluidHandler.FluidAction.EXECUTE);
                                context.getPlayer().getCooldowns().addCooldown(stack.getItem(), 20);
                                foundFluid = true;
                            }
                        } else {
                            stack.set(TFMGDataComponents.FLAMETHROWER_FUEL, fuel);
                            capability.drain(stackToDrain, IFluidHandler.FluidAction.EXECUTE);
                            context.getPlayer().getCooldowns().addCooldown(stack.getItem(), 20);
                            foundFluid = true;
                        }
                    }
                }
            }
        }

        return foundFluid ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    @Nullable
    public static FlamethrowerFuelType getFuel(RegistryAccess registryAccess, ItemStack heldStack) {
        var type = heldStack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY).getFuelType(registryAccess);
        return type.orElse(registryAccess.registryOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE).get(TFMGFlamethrowerFuelTypes.FALLBACK));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (!stack.has(TFMGDataComponents.FLAMETHROWER_FUEL) || stack.get(TFMGDataComponents.FLAMETHROWER_FUEL) == FlamethrowerFuel.EMPTY) {
            super.appendHoverText(stack, context, tooltip, flag);
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            super.appendHoverText(stack, context, tooltip, flag);
            return;
        }
        FlamethrowerFuelType fallback = player.registryAccess().registryOrThrow(TFMGRegistries.FLAMETHROWER_FUEL_TYPE).get(TFMGFlamethrowerFuelTypes.FALLBACK);
        FlamethrowerFuelType fuelType = getFuel(player.registryAccess(), stack);
        FlamethrowerFuel fuel = stack.getOrDefault(TFMGDataComponents.FLAMETHROWER_FUEL, FlamethrowerFuel.EMPTY);

        if (fuelType == fallback || !fuel.hasFuel()) {
            super.appendHoverText(stack, context, tooltip, flag);
            return;
        }

        FluidStack fuelFluid = new FluidStack(fuelType.fluids().get(0).value(), 1);

        String _spread = "flamethrower.fuel.spread";
        String _speed = "flamethrower.fuel.speed";
        String _amount = "flamethrower.fuel.amount";
        String _cold = "flamethrower.fuel.cold";
        String _hellfire = "flamethrower.fuel.hellfire";

        String _capacity = "flamethrower.fuel.capacity";

        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable(fuelFluid.getDescriptionId()).append(Component.literal(":")).withStyle(ChatFormatting.GRAY));
        MutableComponent spacing = CommonComponents.space();
        ChatFormatting green = ChatFormatting.GREEN;
        ChatFormatting darkGreen = ChatFormatting.DARK_GREEN;
        ChatFormatting red = ChatFormatting.RED;

        int spreadF = fuelType.spread();
        float speedF = fuelType.speed();
        int amountF = fuelType.amount();
        boolean coldF = fuelType.isCold();
        boolean hellfireF = fuelType.hellfire();

        MutableComponent spread = Component.literal("" + spreadF);
        MutableComponent speed = Component.literal(speedF == Mth.floor(speedF) ? "" + Mth.floor(speedF) : "" + speedF);
        MutableComponent amount = Component.literal("" + amountF);

        MutableComponent fuelCapacity = Component.literal(fuel.amount() + " / " + FUEL_CAPACITY + " mB");

        spread = spread.withStyle(spreadF > 20 ? green : darkGreen);
        speed = speed.withStyle(speedF > 1 ? green : darkGreen);
        amount = amount.withStyle(amountF > 10 ? green : darkGreen);
        fuelCapacity = fuelCapacity.withStyle(stack.get(TFMGDataComponents.FLAMETHROWER_FUEL).amount() == 0 ? red : green);

        tooltip.add(spacing.plainCopy()
                .append(CreateLang.translateDirect(_capacity, fuelCapacity)
                        .withStyle(darkGreen)));

        tooltip.add(spacing.plainCopy()
                .append(CreateLang.translateDirect(_spread, spread)
                        .withStyle(darkGreen)));
        tooltip.add(spacing.plainCopy()
                .append(CreateLang.translateDirect(_speed, speed)
                        .withStyle(darkGreen)));
        tooltip.add(spacing.plainCopy()
                .append(CreateLang.translateDirect(_amount, amount)
                        .withStyle(darkGreen)));
        if (coldF) {
            tooltip.add(spacing.plainCopy()
                    .append(CreateLang.translateDirect(_cold)
                            .withStyle(darkGreen)));
        } else if (hellfireF) {
            tooltip.add(spacing.plainCopy()
                    .append(CreateLang.translateDirect(_hellfire)
                            .withStyle(darkGreen)));
        }
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
