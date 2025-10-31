package com.drmangotea.tfmg.content.engines.fuels;

import com.drmangotea.tfmg.registry.TFMGTags;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FuelType {
    TagKey<Fluid> fluid = TFMGTags.TFMGFluidTags.GASOLINE.tag;
    private float speed = 1;
    private float efficiency = 1;
    private float stress = 1;
    
    public FuelType() {
    }
    
    public TagKey<Fluid> getFluid() {
        return fluid;
    }

    
    public float getSpeed() {
        return speed;
    }
    
    public float getEfficiency() {
        return efficiency;
    }
    
    public float getStress() {
        return stress;
    }
    


    
    public static FuelType fromJson(JsonObject object) {
        FuelType type = new FuelType();
        try {
            parseJsonPrimitive(object, "fluid", JsonPrimitive::isString, primitive -> type.fluid = FluidTags.create(ResourceLocation.fromNamespaceAndPath("",primitive.getAsString())));

            parseJsonPrimitive(object, "speed", JsonPrimitive::isNumber, primitive -> type.speed = primitive.getAsFloat());
            parseJsonPrimitive(object, "efficiency", JsonPrimitive::isNumber, primitive -> type.efficiency = primitive.getAsFloat());
            parseJsonPrimitive(object, "stress", JsonPrimitive::isNumber, primitive -> type.stress = primitive.getAsFloat());
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
    
    public static void toBuffer(FuelType type, FriendlyByteBuf buffer) {

        buffer.writeResourceLocation(type.fluid.location());

        buffer.writeFloat(type.speed);
        buffer.writeFloat(type.efficiency);
        buffer.writeFloat(type.stress);
    }
    
    public static FuelType fromBuffer(FriendlyByteBuf buffer) {
        FuelType type = new FuelType();
        type.fluid = FluidTags.create(buffer.readResourceLocation());
        type.speed = buffer.readFloat();
        type.efficiency = buffer.readFloat();
        type.stress = buffer.readFloat();
        return type;
    }
    
    public static class Builder {
        
        protected ResourceLocation id;
        protected FuelType result;
        
        public Builder(ResourceLocation id) {
            this.id = id;
            this.result = new FuelType();
        }
        

        
        public Builder speed(float speed) {
            result.speed = speed;
            return this;
        }
        public Builder efficiency(float efficiency) {
            result.efficiency = efficiency;
            return this;
        }
        public Builder stress(float stress) {
            result.stress = stress;
            return this;
        }



        public final Builder addFluids(TagKey<Fluid> tag) {
            result.fluid = tag;
            return this;
        }
        
        public FuelType register() {
            EngineFuelTypeManager.registerBuiltinType(id, result);
            return result;
        }
        public final FuelType registerAndAssign(TagKey<Fluid> tag) {
            addFluids(tag);
            register();
            return result;
        }
        
    }
}