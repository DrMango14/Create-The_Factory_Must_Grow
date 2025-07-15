package com.drmangotea.tfmg.content.engines.fuels;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.registry.TFMGPackets;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class EngineFuelTypeManager {
    public static final Map<ResourceLocation, FuelType> BUILTIN_TYPE_MAP = new HashMap<>();
    public static final Map<ResourceLocation, FuelType> CUSTOM_TYPE_MAP = new HashMap<>();
    public static final Map<ResourceLocation, FuelType> GLOBAL_TYPE_MAP = new HashMap<>();
    private static final Map<TagKey<Fluid>, FuelType> FLUID_TO_TYPE_MAP = new IdentityHashMap<>();
    
    public static void registerBuiltinType(ResourceLocation id, FuelType type) {
        synchronized (BUILTIN_TYPE_MAP) {
            BUILTIN_TYPE_MAP.put(id, type);
        }
        synchronized (GLOBAL_TYPE_MAP) {
            GLOBAL_TYPE_MAP.put(id, type);
        }
    }
    
    public static FuelType getBuiltinType(ResourceLocation id) {
        return BUILTIN_TYPE_MAP.get(id);
    }
    public static FuelType getCustomType(ResourceLocation id) {
        return CUSTOM_TYPE_MAP.get(id);
    }
    public static FuelType getGlobalType(ResourceLocation id) {
        return GLOBAL_TYPE_MAP.get(id);
    }
    public static FuelType getTypeForFluid(Fluid fluid) {
        return FLUID_TO_TYPE_MAP.get(fluid);
    }
    //public static FuelType getTypeForFluid(ResourceLocation fluidId) {
    //    return getTypeForFluid(BuiltInRegistries.FLUID.getValue(fluidId));
    //}
    
    public static Optional<FuelType> getTypeForStack(FluidStack fluidStack) {
        if (fluidStack.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(getTypeForFluid(fluidStack.getFluid()));
    }
    
    public static ResourceLocation getIdForType(FuelType type) {
        for (Map.Entry<ResourceLocation, FuelType> entry : GLOBAL_TYPE_MAP.entrySet()) {
            if (entry.getValue() == type)
                return entry.getKey();
        }
        return null;
    }
    public static FuelType getTypeForId(ResourceLocation id) {
        for (Map.Entry<ResourceLocation, FuelType> entry : GLOBAL_TYPE_MAP.entrySet()) {
            if (entry.getKey().equals(id))
                return entry.getValue();
        }
        return null;
    }
    
    public static void clear() {
        GLOBAL_TYPE_MAP.clear();
        CUSTOM_TYPE_MAP.clear();
        FLUID_TO_TYPE_MAP.clear();
    }
    
    //public static void fillFluidMap() {
    //    for (Map.Entry<ResourceLocation, FuelType> entry : BUILTIN_TYPE_MAP.entrySet()) {
    //        FuelType type = entry.getValue();
    //        for (Supplier<Fluid> delegate : type.getFluids()) {
    //            FLUID_TO_TYPE_MAP.put(delegate.get(), type);
    //        }
    //    }
    //    for (Map.Entry<ResourceLocation, FuelType> entry : CUSTOM_TYPE_MAP.entrySet()) {
    //        FuelType type = entry.getValue();
    //        for (Supplier<Fluid> delegate : type.getFluids()) {
    //            FLUID_TO_TYPE_MAP.put(delegate.get(), type);
    //        }
    //    }
    //}
    public static void fillGlobalMap() {
        GLOBAL_TYPE_MAP.putAll(BUILTIN_TYPE_MAP);
        GLOBAL_TYPE_MAP.putAll(CUSTOM_TYPE_MAP);
        TFMG.LOGGER.info("Added {} Engine Fuel Types", GLOBAL_TYPE_MAP.size());
    }
    
    public static void toBuffer(FriendlyByteBuf buffer) {
        buffer.writeVarInt(CUSTOM_TYPE_MAP.size());
        for (Map.Entry<ResourceLocation, FuelType> entry : CUSTOM_TYPE_MAP.entrySet()) {
            buffer.writeResourceLocation(entry.getKey());
            FuelType.toBuffer(entry.getValue(), buffer);
        }
    }
    
    public static void fromBuffer(FriendlyByteBuf buffer) {
        clear();
        
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            CUSTOM_TYPE_MAP.put(buffer.readResourceLocation(), FuelType.fromBuffer(buffer));
        }
        
      //  fillFluidMap();
        fillGlobalMap();
    }
    
   // public static void syncTo(ServerPlayer player) {
   //     TFMGPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new SyncPacket());
   // }
    
    //public static void syncToAll() {
    //    TFMGPackets.getChannel().send(PacketDistributor.ALL.noArg(), new SyncPacket());
    //}
    
    public static class ReloadListener extends SimpleJsonResourceReloadListener {
        
        private static final Gson GSON = new Gson();
        
        public static final ReloadListener INSTANCE = new ReloadListener();
        
        protected ReloadListener() {
            super(GSON, "tfmg_engine_fuels");
        }
        
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
            clear();
            
            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                JsonElement element = entry.getValue();
                if (element.isJsonObject()) {
                    ResourceLocation id = entry.getKey();
                    JsonObject object = element.getAsJsonObject();
                    FuelType type = FuelType.fromJson(object);
                    CUSTOM_TYPE_MAP.put(id, type);
                }
            }
            
         //   fillFluidMap();
            fillGlobalMap();
        }
        
    }
    
    //public static class SyncPacket extends SimplePacketBase {
    //
    //    private FriendlyByteBuf buffer;
    //
    //    public SyncPacket() {
    //    }
    //
    //    public SyncPacket(FriendlyByteBuf buffer) {
    //        this.buffer = buffer;
    //    }
    //
    //    @Override
    //    public void write(FriendlyByteBuf buffer) {
    //        toBuffer(buffer);
    //    }
    //
    //    @Override
    //    public boolean handle(NetworkEvent.Context context) {
    //        context.enqueueWork(() -> {
    //            fromBuffer(buffer);
    //        });
    //        return true;
    //    }
    //
    //}
}