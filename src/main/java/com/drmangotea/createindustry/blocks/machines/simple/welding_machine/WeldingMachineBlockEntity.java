package com.drmangotea.createindustry.blocks.machines.simple.welding_machine;

import com.drmangotea.createindustry.blocks.electricity.base.ElectricBlockEntity;
import com.drmangotea.createindustry.registry.TFMGRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

public class WeldingMachineBlockEntity extends ElectricBlockEntity implements WeldingBehaviour.WeldingBehaviourSpecifics {

	private static final Object weldingRecipesKey = new Object();

	public WeldingBehaviour weldingBehaviour;
	private int tracksCreated;

	public WeldingMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected AABB createRenderBoundingBox() {
		return new AABB(worldPosition).expandTowards(0, -1.5, 0)
			.expandTowards(0, 1, 0);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		weldingBehaviour = new WeldingBehaviour(this);
		behaviours.add(weldingBehaviour);

	}

	public void onItemPressed(ItemStack result) {
		award(AllAdvancements.PRESS);
		if (AllTags.AllBlockTags.TRACKS.matches(result))
			tracksCreated += result.getCount();
		if (tracksCreated >= 1000) {
			award(AllAdvancements.TRACK_CRAFTING);
			tracksCreated = 0;
		}
	}

	public WeldingBehaviour getWeldingBehaviour() {
		return weldingBehaviour;
	}




	@Override
	public boolean tryProcessInWorld(ItemEntity itemEntity, boolean simulate) {
		ItemStack item = itemEntity.getItem();
		Optional<WeldingRecipe> recipe = getRecipe(item);
		if (!recipe.isPresent())
			return false;
		if (simulate)
			return true;

		ItemStack itemCreated = ItemStack.EMPTY;
		weldingBehaviour.particleItems.add(item);
		if (canProcessInBulk() || item.getCount() == 1) {
			RecipeApplier.applyRecipeOn(itemEntity, recipe.get());
			itemCreated = itemEntity.getItem()
				.copy();
		} else {
			for (ItemStack result : RecipeApplier.applyRecipeOn(ItemHandlerHelper.copyStackWithSize(item, 1),
				recipe.get())) {
				if (itemCreated.isEmpty())
					itemCreated = result.copy();
				ItemEntity created =
					new ItemEntity(level, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), result);
				created.setDefaultPickUpDelay();
				created.setDeltaMovement(VecHelper.offsetRandomly(Vec3.ZERO, level.random, .05f));
				level.addFreshEntity(created);
			}
			item.shrink(1);
		}

		if (!itemCreated.isEmpty())
			onItemPressed(itemCreated);
		return true;
	}

	@Override
	public boolean tryProcessOnBelt(TransportedItemStack input, List<ItemStack> outputList, boolean simulate) {
		Optional<WeldingRecipe> recipe = getRecipe(input.stack);
		if (!recipe.isPresent())
			return false;
		if (simulate)
			return true;
		weldingBehaviour.particleItems.add(input.stack);
		List<ItemStack> outputs = RecipeApplier.applyRecipeOn(
			canProcessInBulk() ? input.stack : ItemHandlerHelper.copyStackWithSize(input.stack, 1), recipe.get());

		for (ItemStack created : outputs) {
			if (!created.isEmpty()) {
				onItemPressed(created);
				break;
			}
		}

		outputList.addAll(outputs);
		return true;
	}


	private static final RecipeWrapper pressingInv = new RecipeWrapper(new ItemStackHandler(1));

	public Optional<WeldingRecipe> getRecipe(ItemStack item) {
		Optional<WeldingRecipe> assemblyRecipe =
			SequencedAssemblyRecipe.getRecipe(level, item, TFMGRecipeTypes.WELDING.getType(), WeldingRecipe.class);
		if (assemblyRecipe.isPresent())
			return assemblyRecipe;

		pressingInv.setItem(0, item);
		return TFMGRecipeTypes.WELDING.find(pressingInv, level);
	}



	@Override
	public boolean canProcessInBulk() {
		return AllConfigs.server().recipes.bulkPressing.get();
	}


	@Override
	public int getParticleAmount() {
		return 15;
	}




}
