package com.drmangotea.tfmg.base;

import com.drmangotea.tfmg.TFMG;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

public record ToolSet (
	Tier tier,
	ItemEntry<? extends SwordItem> sword,
	ItemEntry<? extends AxeItem> axe,
	ItemEntry<? extends PickaxeItem> pickaxe,
	ItemEntry<? extends ShovelItem> shovel,
	ItemEntry<? extends HoeItem> hoe
) {
	
	public static Builder builder (TFMGRegistrate REGISTRATE, String material, TagKey<Item> ingot, Tier tier) {
		return new Builder(REGISTRATE, material, ingot, tier);
	}
	
	public static class Builder {
		private final TFMGRegistrate REGISTRATE;
		private final String material;
		private final TagKey<Item> ingot;
		private final Tier tier;
		
		private ItemBuilder<? extends SwordItem, CreateRegistrate> sword;
		private ItemBuilder<? extends AxeItem, CreateRegistrate> axe;
		private ItemBuilder<? extends PickaxeItem, CreateRegistrate> pickaxe;
		private ItemBuilder<? extends ShovelItem, CreateRegistrate> shovel;
		private ItemBuilder<? extends HoeItem, CreateRegistrate> hoe;
		
		public Builder(TFMGRegistrate REGISTRATE, String material, TagKey<Item> ingot, Tier tier) {
			this.REGISTRATE = REGISTRATE;
			this.material = material;
			this.ingot = ingot;
			this.tier = tier;
		}
		
		private void model (RegistrateItemModelProvider prov, String name) {
			prov.withExistingParent(name, "minecraft:item/handheld")
				.texture("layer0", REGISTRATE.getModid() + ":item/" + name);
		}
		
		private void recipe (
			DataGenContext<Item, ? extends Item> ctx, RegistrateRecipeProvider prov, String name, ItemLike stick,
			NonNullUnaryOperator<ShapedRecipeBuilder> builder
		) {
			builder.apply(
				ShapedRecipeBuilder
					.shaped(RecipeCategory.TOOLS, ctx.get())
					.unlockedBy("has ingot", RegistrateRecipeProvider.has(ingot))
					.define('C', ingot)
					.define('S', stick)
			).save(prov, TFMG.asResource("crafting/tools/"+name));
		}
		
		public Builder all () {
			return sword().axe().pickaxe().shovel().hoe();
		}
		
		public ToolSet build () {
			return new ToolSet(tier, sword.register(), axe.register(), pickaxe.register(), shovel.register(), hoe.register());
		}
		
		public Builder sword () { return sword(SwordItem::new, 2f, -2.4F, Items.STICK); }
		public Builder sword (
			NonNullBiFunction<Tier, Item.Properties, ? extends SwordItem> swordFactory,
			float attackDamage, float attackSpeed
		) {
			return sword(swordFactory, attackDamage, attackSpeed, Items.STICK);
		}
		public Builder sword (
			NonNullBiFunction<Tier, Item.Properties, ? extends SwordItem> swordFactory,
			float attackDamage, float attackSpeed,
			ItemLike stick
		) {
			String name = material + "_sword";
			sword = REGISTRATE.item(name, p -> swordFactory.apply(tier, p))
				.properties(p -> p.attributes(SwordItem.createAttributes(tier, attackDamage, attackSpeed)))
				.tag(ItemTags.SWORDS, Tags.Items.MELEE_WEAPON_TOOLS)
				.model((ctx, prov) -> model(prov, name))
				.recipe((ctx, prov) -> recipe(ctx, prov, name, stick, b -> b
					.pattern("C  ")
					.pattern("C  ")
					.pattern("S  "))
				);
			return this;
		}
		
		public Builder axe () { return axe(AxeItem::new, 6F, -3.2F, Items.STICK); }
		public Builder axe (
			NonNullBiFunction<Tier, Item.Properties, ? extends AxeItem> axeFactory,
			float attackDamage, float attackSpeed
		) {
			return axe(axeFactory, attackDamage, attackSpeed, Items.STICK);
		}
		public Builder axe (
			NonNullBiFunction<Tier, Item.Properties, ? extends AxeItem> axeFactory,
			float attackDamage, float attackSpeed,
			ItemLike stick
		) {
			String name = material + "_axe";
			axe = REGISTRATE.item(name, p -> axeFactory.apply(tier, p))
				.properties(p -> p.attributes(AxeItem.createAttributes(tier, attackDamage, attackSpeed)))
				.tag(ItemTags.AXES, Tags.Items.MELEE_WEAPON_TOOLS)
				.model((ctx, prov) -> model(prov, name))
				.recipe((ctx, prov) -> recipe(ctx, prov, name, stick, b -> b
					.pattern(" CC")
					.pattern(" SC")
					.pattern(" S "))
				);
			return this;
		}
		
		public Builder pickaxe () { return pickaxe(PickaxeItem::new, 1, 1, Items.STICK); }
		public Builder pickaxe (
			NonNullBiFunction<Tier, Item.Properties, ? extends PickaxeItem> pickaxeFactory,
			float attackDamage, float attackSpeed,
			ItemLike stick
		) {
			String name = material + "_pickaxe";
			pickaxe = REGISTRATE.item(name, p -> pickaxeFactory.apply(tier, p))
				.properties(p -> p.attributes(PickaxeItem.createAttributes(tier, attackDamage, attackSpeed)))
				.tag(ItemTags.PICKAXES, ItemTags.CLUSTER_MAX_HARVESTABLES, Tags.Items.MINING_TOOL_TOOLS)
				.model((ctx, prov) -> model(prov, name))
				.recipe((ctx, prov) -> recipe(ctx, prov, name, stick, b -> b
					.pattern("CCC")
					.pattern(" S ")
					.pattern(" S "))
				);
			return this;
		}
		
		public Builder shovel () { return shovel(ShovelItem::new, 1, 1, Items.STICK); }
		public Builder shovel (
			NonNullBiFunction<Tier, Item.Properties, ? extends ShovelItem> shovelFactory,
			float attackDamage, float attackSpeed,
			ItemLike stick
		) {
			String name = material + "_shovel";
			shovel = REGISTRATE.item(name, p -> shovelFactory.apply(tier, p))
				.properties(p -> p.attributes(ShovelItem.createAttributes(tier, attackDamage, attackSpeed)))
				.tag(ItemTags.SHOVELS)
				.model((ctx, prov) -> model(prov, name))
				.recipe((ctx, prov) -> recipe(ctx, prov, name, stick, b -> b
					.pattern("C  ")
					.pattern("S  ")
					.pattern("S  "))
				);
			return this;
		}
		
		public Builder hoe () { return hoe(HoeItem::new, 1, 1, Items.STICK); }
		public Builder hoe (
			NonNullBiFunction<Tier, Item.Properties, ? extends HoeItem> hoeFactory,
			float attackDamage, float attackSpeed,
			ItemLike stick
		) {
			String name = material + "_hoe";
			hoe = REGISTRATE.item(name, p -> hoeFactory.apply(tier, p))
				.properties(p -> p.attributes(HoeItem.createAttributes(tier, attackDamage, attackSpeed)))
				.tag(ItemTags.HOES)
				.model((ctx, prov) -> model(prov, name))
				.recipe((ctx, prov) -> recipe(ctx, prov, name, stick, b -> b
					.pattern("CC ")
					.pattern("S  ")
					.pattern("S  "))
				);
			return this;
		}
	}
}
