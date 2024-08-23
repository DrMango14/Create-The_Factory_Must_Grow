package com.drmangotea.createindustry.blocks.machines.simple.welding_machine;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class WeldingBehaviour extends BeltProcessingBehaviour {

	public static final int CYCLE = 240;
	public static final int ENTITY_SCAN = 10;

	public List<ItemStack> particleItems = new ArrayList<>();

	public WeldingBehaviourSpecifics specifics;
	public int prevRunningTicks;
	public int runningTicks;
	public boolean running;
	public boolean finished;
	public WeldingMachineMode mode;

	int entityScanCooldown;

	public interface WeldingBehaviourSpecifics {

		public boolean tryProcessOnBelt(TransportedItemStack input, List<ItemStack> outputList, boolean simulate);

		public boolean tryProcessInWorld(ItemEntity itemEntity, boolean simulate);

		public boolean canProcessInBulk();


		public int getParticleAmount();

	}

	public <T extends SmartBlockEntity & WeldingBehaviourSpecifics> WeldingBehaviour(T be) {
		super(be);
		this.specifics = be;
		mode = WeldingMachineMode.WORLD;
		entityScanCooldown = ENTITY_SCAN;
		whenItemEnters((s, i) -> BeltWeldingCallbacks.onItemReceived(s, i, this));
		whileItemHeld((s, i) -> BeltWeldingCallbacks.whenItemHeld(s, i, this));
	}

	@Override
	public void read(CompoundTag compound, boolean clientPacket) {
		running = compound.getBoolean("Running");
		mode = WeldingMachineMode.values()[compound.getInt("Mode")];
		finished = compound.getBoolean("Finished");
		prevRunningTicks = runningTicks = compound.getInt("Ticks");
		super.read(compound, clientPacket);

		if (clientPacket) {
			NBTHelper.iterateCompoundList(compound.getList("ParticleItems", Tag.TAG_COMPOUND),
				c -> particleItems.add(ItemStack.of(c)));
			spawnParticles();
		}
	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.putBoolean("Running", running);
		compound.putInt("Mode", mode.ordinal());
		compound.putBoolean("Finished", finished);
		compound.putInt("Ticks", runningTicks);
		super.write(compound, clientPacket);

		if (clientPacket) {
			compound.put("ParticleItems", NBTHelper.writeCompoundList(particleItems, ItemStack::serializeNBT));
			particleItems.clear();
		}
	}

	public float getRenderedHeadOffset(float partialTicks) {
		if (!running)
			return 0;
		int runningTicks = Math.abs(this.runningTicks);
		float ticks = Mth.lerp(partialTicks, prevRunningTicks, runningTicks);
		if (runningTicks < (CYCLE * 2) / 3)
			return (float) Mth.clamp(Math.pow(ticks / CYCLE * 2, 3), 0, 1);
		return Mth.clamp((CYCLE - ticks) / CYCLE * 3, 0, 1);
	}

	public void start(WeldingMachineMode mode) {
		this.mode = mode;
		running = true;
		prevRunningTicks = 0;
		runningTicks = 0;
		particleItems.clear();
		blockEntity.sendData();
	}

	public boolean inWorld() {
		return mode == WeldingMachineMode.WORLD;
	}


	@Override
	public void tick() {
		super.tick();

		Level level = getWorld();
		BlockPos worldPosition = getPos();

		if (!running || level == null) {
			if (level != null && !level.isClientSide) {


				if (entityScanCooldown > 0)
					entityScanCooldown--;
				if (entityScanCooldown <= 0) {
					entityScanCooldown = ENTITY_SCAN;

					if (BlockEntityBehaviour.get(level, worldPosition.below(2),
						TransportedItemStackHandlerBehaviour.TYPE) != null)
						return;
					if (AllBlocks.BASIN.has(level.getBlockState(worldPosition.below(2))))
						return;

					for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class,
						new AABB(worldPosition.below()).deflate(.125f))) {
						if (!itemEntity.isAlive() || !itemEntity.isOnGround())
							continue;
						if (!specifics.tryProcessInWorld(itemEntity, true))
							continue;
						start(WeldingMachineMode.WORLD);
						return;
					}
				}

			}
			return;
		}

		if (level.isClientSide && runningTicks == -CYCLE / 2) {
			prevRunningTicks = CYCLE / 2;
			return;
		}

		if (runningTicks == CYCLE / 2) {
			if (inWorld())
				applyInWorld();

			if (level.getBlockState(worldPosition.below(2))
				.getSoundType() == SoundType.WOOL)
				AllSoundEvents.MECHANICAL_PRESS_ACTIVATION_ON_BELT.playOnServer(level, worldPosition);
			else
				AllSoundEvents.MECHANICAL_PRESS_ACTIVATION.playOnServer(level, worldPosition, .5f,
					.75f);

			if (!level.isClientSide)
				blockEntity.sendData();
		}

		if (!level.isClientSide && runningTicks > CYCLE) {
			finished = true;
			running = false;
			particleItems.clear();
			blockEntity.sendData();
			return;
		}

		prevRunningTicks = runningTicks;
		runningTicks += getRunningTickSpeed();
		if (prevRunningTicks < CYCLE / 2 && runningTicks >= CYCLE / 2) {
			runningTicks = CYCLE / 2;
			// Pause the ticks until a packet is received
			if (level.isClientSide && !blockEntity.isVirtual())
				runningTicks = -(CYCLE / 2);
		}
	}


	protected void applyInWorld() {
		Level level = getWorld();
		BlockPos worldPosition = getPos();
		AABB bb = new AABB(worldPosition.below(1));
		boolean bulk = specifics.canProcessInBulk();

		particleItems.clear();

		if (level.isClientSide)
			return;

		for (Entity entity : level.getEntities(null, bb)) {
			if (!(entity instanceof ItemEntity itemEntity))
				continue;
			if (!entity.isAlive() || !entity.isOnGround())
				continue;

			entityScanCooldown = 0;
			if (specifics.tryProcessInWorld(itemEntity, false))
				blockEntity.sendData();
			if (!bulk)
				break;
		}
	}

	public int getRunningTickSpeed() {
		float speed = 50;
		if (speed == 0)
			return 0;
		return (int) Mth.lerp(Mth.clamp(Math.abs(speed) / 512f, 0, 1), 1, 60);
	}

	protected void spawnParticles() {
		if (particleItems.isEmpty())
			return;

		BlockPos worldPosition = getPos();

if (mode == WeldingMachineMode.BELT)
			particleItems.forEach(stack -> makePressingParticleEffect(VecHelper.getCenterOf(worldPosition.below(2))
				.add(0, 8 / 16f, 0), stack));
		if (mode == WeldingMachineMode.WORLD)
			particleItems.forEach(stack -> makePressingParticleEffect(VecHelper.getCenterOf(worldPosition.below(1))
				.add(0, -1 / 4f, 0), stack));

		particleItems.clear();
	}

	public void makePressingParticleEffect(Vec3 pos, ItemStack stack) {
		makePressingParticleEffect(pos, stack, specifics.getParticleAmount());
	}

	public void makePressingParticleEffect(Vec3 pos, ItemStack stack, int amount) {
		Level level = getWorld();
		if (level == null || !level.isClientSide)
			return;
		for (int i = 0; i < amount; i++) {
			Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, level.random, .125f)
				.multiply(1, 0, 1);
			motion = motion.add(0, amount != 1 ? 0.125f : 1 / 16f, 0);
			level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.x, pos.y - .25f, pos.z, motion.x,
				motion.y, motion.z);
		}
	}

	public void makeCompactingParticleEffect(Vec3 pos, ItemStack stack) {
		Level level = getWorld();
		if (level == null || !level.isClientSide)
			return;
		for (int i = 0; i < 20; i++) {
			Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, level.random, .175f)
				.multiply(1, 0, 1);
			level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.x, pos.y, pos.z, motion.x,
				motion.y + .25f, motion.z);
		}
	}

	public enum WeldingMachineMode {
		WORLD(1), BELT(19f / 16f);

		public float headOffset;

		WeldingMachineMode(float headOffset) {
			this.headOffset = headOffset;
		}
	}

}
