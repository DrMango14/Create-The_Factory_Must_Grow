package com.drmangotea.createindustry.registry;


import com.drmangotea.createindustry.CreateTFMG;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


public class TFMGPartialModels {

        public static final PartialModel
                AIR_INTAKE_FAN = block("air_intake/fan"),
                AIR_INTAKE_FAN_MEDIUM = block("air_intake/fan_medium"),
                AIR_INTAKE_FAN_LARGE = block("air_intake/fan_large"),
                AIR_INTAKE_FRAME = block("air_intake/frame"),
                AIR_INTAKE_FRAME_CLOSED = block("air_intake/frame_closed"),
                AIR_INTAKE_MEDIUM = block("air_intake/block_medium"),
                AIR_INTAKE_LARGE = block("air_intake/block_large"),
                CASTING_SPOUT_CONNECTOR = block("casting_spout/connector"),
                CASTING_SPOUT_BOTTOM = block("casting_spout/bottom"),
                COKE_OVEN_DOOR_LEFT = block("coke_oven/door_left"),
                COKE_OVEN_DOOR_RIGHT = block("coke_oven/door_right"),
                COKE_OVEN_DOOR_LEFT_TOP = block("coke_oven/door_left_top"),
                COKE_OVEN_DOOR_RIGHT_TOP = block("coke_oven/door_right_top"),
                COAL_COKE_DUST_LAYER = block("coal_coke_dust_layer"),
                PUMPJACK_HAMMER = block("pumpjack/hammer_holder"),
                PUMPJACK_FRONT_ROPE = block("pumpjack/pumpjack_front_rope"),
                PUMPJACK_CONNECTOR = block("pumpjack/pumpjack_connector"),
                PUMPJACK_CRANK_BLOCK = block("pumpjack/pumpjack_crank_block"),
                PUMPJACK_CRANK = block("pumpjack_crank/crank"),
                TOWER_GAUGE = block("distillation_tower/gauge"),
                SURFACE_SCANNER_DIAL = block("surface_scanner/dial"),
                SURFACE_SCANNER_FLAG = block("surface_scanner/flag"),
                FORMWORK_BOTTOM = block("formwork_block/block_bottom"),
                FORMWORK_SIDE = block("formwork_block/block_side"),


                STEEL_FLUID_PIPE_CASING = block("steel_pipe/casing"),

                CAST_IRON_FLUID_PIPE_CASING = block("cast_iron_pipe/casing"),
                BRASS_FLUID_PIPE_CASING = block("brass_pipe/casing"),
                PLASTIC_FLUID_PIPE_CASING = block("plastic_pipe/casing"),
                ALUMINUM_FLUID_PIPE_CASING = block("aluminum_pipe/casing"),

                INGOT_MOLD = block("casting_basin/mold_base"),
                BlOCK_MOLD = block("casting_basin/block_mold"),

                STATOR_OUTPUT = block("stator/output"),
                VOLTMETER_DIAL = block("voltmeter/dial"),

                LIGHT_BULB = block("light_bulb/light"),

                INDUSTRIAL_LIGHT = block("light_bulb/light"),

                NEON_TUBE_LIGHT = block("neon_tube/light"),

                DIESEL_ENGINE_LINKAGE = block("diesel_engine/linkage"),
                DIESEL_ENGINE_PISTON = block("diesel_engine/piston"),


                STEEL_COGHWEEL = block("steel_cogwheel_shaftless"),
                LARGE_STEEL_COGHWEEL = block("large_steel_cogwheel_shaftless"),
                ALUMINUM_COGHWEEL = block("aluminum_cogwheel_shaftless"),
                LARGE_ALUMINUM_COGHWEEL = block("large_aluminum_cogwheel_shaftless")


                        ;




    public static final Map<ResourceLocation, Couple<PartialModel>> FOLDING_DOORS = new HashMap<>();

    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> STEEL_PIPE_ATTACHMENTS =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);

    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> CAST_IRON_PIPE_ATTACHMENTS =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);

    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> BRASS_PIPE_ATTACHMENTS =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);

    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> PLASTIC_PIPE_ATTACHMENTS =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);

    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> ALUMINUM_PIPE_ATTACHMENTS =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);


    static {

        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("steel_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            STEEL_PIPE_ATTACHMENTS.put(type, map);
        }

        putFoldingDoor("steel_door");

        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("plastic_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            PLASTIC_PIPE_ATTACHMENTS.put(type, map);
        }
        ////
        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("cast_iron_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            CAST_IRON_PIPE_ATTACHMENTS.put(type, map);
        }
        ////
        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("brass_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            BRASS_PIPE_ATTACHMENTS.put(type, map);
        }
        ////
        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("aluminum_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            ALUMINUM_PIPE_ATTACHMENTS.put(type, map);
        }
        ////





        ////////////////
        putFoldingDoor("steel_door");




    }
    private static void putFoldingDoor(String path) {
        FOLDING_DOORS.put(CreateTFMG.asResource(path),
                Couple.create(block(path + "/fold_left"), block(path + "/fold_right")));
    }
        private static PartialModel block(String path) {
            return new PartialModel(CreateTFMG.asResource("block/" + path));
        }

        public static void init() {}

    }