package com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon;



import com.drmangotea.tfmg.TFMGClient;
import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.projectile.NapalmPotato;
import com.drmangotea.tfmg.registry.TFMGEntityTypes;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AdvancedPotatoCannonItem extends ProjectileWeaponItem implements CustomArmPoseItem {

	public static ItemStack CLIENT_CURRENT_AMMO = ItemStack.EMPTY;
	public static final int MAX_DAMAGE = 500;

	public AdvancedPotatoCannonItem(Properties properties) {
		super(properties.durability(MAX_DAMAGE));
	}



	@Override
	public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player player) {
		return false;
	}



	@Override
	public InteractionResult useOn(UseOnContext context) {
		return use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return BacktankUtil.isBarVisible(stack, maxUses());
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return BacktankUtil.getBarWidth(stack, maxUses());
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return BacktankUtil.getBarColor(stack, maxUses());
	}

	private int maxUses() {
		return AllConfigs.server().equipment.maxPotatoCannonShots.get()*5;
	}

	public boolean isCannon(ItemStack stack) {
		return stack.getItem() instanceof AdvancedPotatoCannonItem;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		return findAmmoInInventory(player).map(itemStack -> {
			if (ShootableGadgetItemMethods.shouldSwap(player, stack, hand, this::isCannon))
				return InteractionResultHolder.fail(stack);

			if (world.isClientSide) {
				TFMGClient.ADVANCED_POTATO_CANNON_RENDER_HANDLER.dontAnimateItem(hand);
				return InteractionResultHolder.success(stack);
			}

			Vec3 barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(player, hand == InteractionHand.MAIN_HAND,
				new Vec3(.75f, -0.15f, 1.5f));
			Vec3 correction =
				ShootableGadgetItemMethods.getGunBarrelVec(player, hand == InteractionHand.MAIN_HAND, new Vec3(-.05f, 0, 0))
					.subtract(player.position()
						.add(0, player.getEyeHeight(), 0));


			Vec3 lookVec = player.getLookAngle();
			Vec3 motion = lookVec.add(correction)
				.normalize()
				.scale(2);

			float soundPitch = 1 + (Create.RANDOM.nextFloat() - .5f) / 4f;



				NapalmPotato projectile = TFMGEntityTypes.NAPALM_POTATO.create(world);


				projectile.setPos(barrelPos.x, barrelPos.y, barrelPos.z);
				projectile.setDeltaMovement(motion);
				projectile.setOwner(player);
				world.addFreshEntity(projectile);


			if (!player.isCreative()) {
				itemStack.shrink(1);
				if (itemStack.isEmpty())
					player.getInventory().removeItem(itemStack);
			}

			if (!BacktankUtil.canAbsorbDamage(player, maxUses()))
				stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

			Integer cooldown = 75;


			AllSoundEvents.FWOOMP.play(world,player,player.getX(),player.getY(),player.getZ(),1,0.2f);

			ShootableGadgetItemMethods.applyCooldown(player, stack, hand, this::isCannon, cooldown);
			ShootableGadgetItemMethods.sendPackets(player,
				b -> new AdvancedPotatoCannonPacket(barrelPos, lookVec.normalize(), itemStack, hand, soundPitch, b));
			return InteractionResultHolder.success(stack);
		})
			.orElse(InteractionResultHolder.pass(stack));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	protected static Optional<ItemStack> findAmmoInInventory(Player player) {
		for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
			ItemStack stack = player.getInventory().getItem(i);
			if(stack.is(TFMGItems.NAPALM_POTATO.get())){
				if (!player.isCreative())
					stack.shrink(1);
				return Optional.of(TFMGItems.NAPALM_POTATO.get().getDefaultInstance());
			}
		}


		return Optional.empty();

	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return stack -> stack.is(TFMGItems.NAPALM_POTATO.get());
	}

	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
		return true;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.NONE;
	}

	@Override
	@Nullable
	public ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
		if (!player.swinging) {
			return ArmPose.CROSSBOW_HOLD;
		}
		return null;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 15;
	}

	@Override
	protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i, float v, float v1, float v2, @Nullable LivingEntity livingEntity1) {

	}

	@SuppressWarnings("removal")
	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(SimpleCustomRenderer.create(this, new AdvancedPotatoCannonItemRenderer()));
	}

}
