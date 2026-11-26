package com.drmangotea.tfmg.datagen.integration.sable;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import dev.ryanhcode.sable.physics.config.block_properties.PhysicsBlockPropertiesDefinition;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;

@NothingNullByDefault
public abstract class BlockPhysicsProvider implements DataProvider {
	
	private final static ResourceLocation SABLE$mass = ResourceLocation.parse("sable:mass");
	private final static ResourceLocation SABLE$inertia = ResourceLocation.parse("sable:inertia");
	private final static ResourceLocation SABLE$volume = ResourceLocation.parse("sable:volume");
	private final static ResourceLocation SABLE$restitution = ResourceLocation.parse("sable:restitution");
	private final static ResourceLocation SABLE$friction = ResourceLocation.parse("sable:friction");
	private final static ResourceLocation SABLE$fragile = ResourceLocation.parse("sable:fragile");
	//TODO: Floating materials
	
	private final String modId;
	private final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<HolderLookup.Provider> registries;
	private final Map<ResourceLocation, Builder> builders = new LinkedHashMap<>();
	
	public BlockPhysicsProvider(String modId, PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		this.modId = modId;
		this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "physics_block_properties");
		this.registries = registries;
	}
	
	protected abstract void generate();
	
	@Override
	public CompletableFuture<?> run(CachedOutput pOutput) {
		builders.clear();
		generate();
		
		return this.registries.thenCompose(pProv ->
			CompletableFuture.allOf(this.builders.entrySet().stream()
				.map((e) -> DataProvider.saveStable(
					pOutput, pProv,
					PhysicsBlockPropertiesDefinition.CODEC,
					e.getValue().build(),
					pathProvider.json(e.getKey())
				)).toArray(CompletableFuture[]::new))
		);
	}
	
	@Override
	public String getName() {
		return "Block Physics for " + modId;
	}
	
	public void add(Holder<Block> block, UnaryOperator<Builder> builder) {
		ResourceLocation location = block.unwrapKey()
			.map(ResourceKey::location)
			.orElseThrow(() -> new IllegalStateException("Block "+block+" lacks resource location!"));
		builders.put(location, builder.apply(new Builder(location, false)));
	}
	
	public void add(TagKey<Block> tag, UnaryOperator<Builder> builder) {
		builders.put(tag.location(), builder.apply(new Builder(tag.location(), true)));
	}
	
	public static class Builder {
		private final ExtraCodecs.TagOrElementLocation selector;
		private int priority = 1001;
		private final Map<ResourceLocation, Object> properties = new HashMap<>();
		//TODO: Overrides
		
		public Builder (ResourceLocation location, boolean tag) {
			selector = new ExtraCodecs.TagOrElementLocation(location, tag);
		}
		
		public Builder priority (int priority) {
			this.priority = priority;
			return this;
		}
		
		public Builder mass (double mass) {
			properties.put(SABLE$mass, mass);
			return this;
		}
		
		public Builder inertia (Vec3 inertia) {
			properties.put(SABLE$inertia, inertia);
			return this;
		}
		
		public Builder inertia (double v) {
			properties.put(SABLE$inertia, new Vec3(v,v,v));
			return this;
		}
		
		public Builder inertia (double x, double y, double z) {
			properties.put(SABLE$inertia, new Vec3(x,y,z));
			return this;
		}
		
		public Builder volume (double volume) {
			properties.put(SABLE$volume, volume);
			return this;
		}
		
		public Builder restitution (double restitution) {
			properties.put(SABLE$restitution, restitution);
			return this;
		}
		
		public Builder friction (double friction) {
			properties.put(SABLE$friction, friction);
			return this;
		}
		
		public Builder fragile() {
			properties.put(SABLE$fragile, true);
			return this;
		}
		
		public Builder fragile(boolean fragile) {
			properties.put(SABLE$fragile, fragile);
			return this;
		}
		
		public PhysicsBlockPropertiesDefinition build() {
			return new PhysicsBlockPropertiesDefinition(selector, priority, properties, Optional.empty());
		}
	}
	
}
