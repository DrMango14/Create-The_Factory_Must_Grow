package com.drmangotea.createindustry.items.weapons.flamethrover;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FlamethrowerFuelType {
    private List<Supplier<Fluid>> fluids = new ArrayList<>();
    private int spread = 15;
    private float speed = 1;
    private int amount = 4;
    private boolean isCold = false;
    private boolean hellfire = false;
    private int color = 0xC4AA76;
    
    public FlamethrowerFuelType() {
    }
    
    public List<Supplier<Fluid>> getFluids() {
        return fluids;
    }
    
    public int getSpread() {
        return spread;
    }
    
    public float getSpeed() {
        return speed;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public int getColor() {
        return color;
    }
    
    public boolean isCold() {
        return isCold;
    }
    
    public boolean isHellfire() {
        return hellfire;
    }
    
    public static FlamethrowerFuelType fromJson(JsonObject object) {
        FlamethrowerFuelType type = new FlamethrowerFuelType();
        try {
            JsonElement itemsElement = object.get("fluids");
            if (itemsElement != null && itemsElement.isJsonArray()) {
                for (JsonElement element : itemsElement.getAsJsonArray()) {
                    if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();
                        if (primitive.isString()) {
                            try {
                                Optional<Holder.Reference<Fluid>> reference = ForgeRegistries.FLUIDS.getDelegate(new ResourceLocation(primitive.getAsString()));
                                if (reference.isPresent()) {
                                    type.fluids.add(reference.get());
                                }
                            } catch (ResourceLocationException e) {
                                //
                            }
                        }
                    }
                }
            }
            
            parseJsonPrimitive(object, "spread", JsonPrimitive::isNumber, primitive -> type.spread = primitive.getAsInt());
            parseJsonPrimitive(object, "speed", JsonPrimitive::isNumber, primitive -> type.speed = primitive.getAsFloat());
            parseJsonPrimitive(object, "amount", JsonPrimitive::isNumber, primitive -> type.amount = primitive.getAsInt());
            parseJsonPrimitive(object, "cold", JsonPrimitive::isBoolean, primitive -> type.isCold = primitive.getAsBoolean());
            parseJsonPrimitive(object, "hellfire", JsonPrimitive::isBoolean, primitive -> type.hellfire = primitive.getAsBoolean());
            parseJsonPrimitive(object, "color", JsonPrimitive::isString, primitive -> type.color = Integer.parseInt(primitive.getAsString()));
        } catch (Exception e) {
            //
        }
        return type;
    }
    
    private static void parseJsonPrimitive(JsonObject object, String key, Predicate<JsonPrimitive> predicate, Consumer<JsonPrimitive> consumer) {
        JsonElement element = object.get(key);
        if (element != null && element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (predicate.test(primitive)) {
                consumer.accept(primitive);
            }
        }
    }
    
    public static void toBuffer(FlamethrowerFuelType type, FriendlyByteBuf buffer) {
        buffer.writeVarInt(type.fluids.size());
        for (Supplier<Fluid> delegate : type.fluids) {
            buffer.writeResourceLocation(RegisteredObjects.getKeyOrThrow(delegate.get()));
        }
        buffer.writeInt(type.spread);
        buffer.writeFloat(type.speed);
        buffer.writeInt(type.amount);
        buffer.writeBoolean(type.isCold);
        buffer.writeBoolean(type.hellfire);
        buffer.writeInt(type.color);
    }
    
    public static FlamethrowerFuelType fromBuffer(FriendlyByteBuf buffer) {
        FlamethrowerFuelType type = new FlamethrowerFuelType();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            Optional<Holder.Reference<Fluid>> reference = ForgeRegistries.FLUIDS.getDelegate(buffer.readResourceLocation());
            if (reference.isPresent()) {
                type.fluids.add(reference.get());
            }
        }
        type.spread = buffer.readInt();
        type.speed = buffer.readFloat();
        type.amount = buffer.readInt();
        type.isCold = buffer.readBoolean();
        type.hellfire = buffer.readBoolean();
        type.color = buffer.readInt();
        return type;
    }
    
    public static class Builder {
        
        protected ResourceLocation id;
        protected FlamethrowerFuelType result;
        
        public Builder(ResourceLocation id) {
            this.id = id;
            this.result = new FlamethrowerFuelType();
        }
        
        public Builder spread(int spread) {
            result.spread = spread;
            return this;
        }
        
        public Builder speed(float speed) {
            result.speed = speed;
            return this;
        }
        
        public Builder amount(int amount) {
            result.amount = amount;
            return this;
        }
        
        public Builder cold() {
            result.isCold = true;
            result.hellfire = false;
            return this;
        }
        
        public Builder hellfire() {
            result.hellfire = true;
            result.isCold = false;
            return this;
        }
        
        public Builder color(int color) {
            result.color = color;
            return this;
        }
        
        @SafeVarargs
        public final Builder addFluids(Supplier<Fluid>... fluids) {
            for (Supplier<Fluid> fluid : fluids)
                result.fluids.add(ForgeRegistries.FLUIDS.getDelegateOrThrow(fluid.get()));
            return this;
        }
        
        public FlamethrowerFuelType register() {
            FlamethrowerFuelTypeManager.registerBuiltinType(id, result);
            return result;
        }
        
        @SafeVarargs
        public final FlamethrowerFuelType registerAndAssign(Supplier<Fluid>... fluids) {
            addFluids(fluids);
            register();
            return result;
        }
        
    }
}
