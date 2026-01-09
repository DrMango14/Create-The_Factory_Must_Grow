package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.decoration.pipes.TFMGPipes;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.*;


public class TFMGPartialModels {

    public static final PartialModel
            AIR_INTAKE_FAN_MEDIUM = block("air_intake/fan_medium"),
            AIR_INTAKE_FAN_LARGE = block("air_intake/fan_large"),
            AIR_INTAKE_FRAME = block("air_intake/frame"),
            AIR_INTAKE_FRAME_CLOSED = block("air_intake/frame_closed"),
            AIR_INTAKE_MEDIUM = block("air_intake/block_medium"),
            AIR_INTAKE_LARGE = block("air_intake/block_large"),
            COKE_OVEN_DOOR_LEFT = block("coke_oven/door_left"),
            COKE_OVEN_DOOR_RIGHT = block("coke_oven/door_right"),
            COKE_OVEN_DOOR_LEFT_BOTTOM = block("coke_oven/door_left_bottom"),
            COKE_OVEN_DOOR_RIGHT_BOTTOM = block("coke_oven/door_right_bottom"),
            COKE_OVEN_DOOR_LEFT_MIDDLE = block("coke_oven/door_left_middle"),
            COKE_OVEN_DOOR_RIGHT_MIDDLE = block("coke_oven/door_right_middle"),
            COKE_OVEN_DOOR_LEFT_TOP = block("coke_oven/door_left_top"),
            COKE_OVEN_DOOR_RIGHT_TOP = block("coke_oven/door_right_top"),
            COAL_COKE_DUST_LAYER = block("coal_coke_dust_layer"),
            POLARIZER_DIAL = block("polarizer/dial"),
            STEEL_FLYWHEEL = block("steel_flywheel/block"),
            ALUMINUM_FLYWHEEL = block("aluminum_flywheel/block"),
            CAST_IRON_FLYWHEEL = block("cast_iron_flywheel/block"),
            LEAD_FLYWHEEL = block("lead_flywheel/block"),
            NICKEL_FLYWHEEL = block("nickel_flywheel/block"),
            LARGE_SWITCH_SHAFT = block("large_switch/shaft"),
            LARGE_SWITCH_BUFFER = block("large_switch/buffer"),
            LARGE_SWITCH_ARM = block("large_switch/connector"),
            DISTILLATION_CONTROLLER_DIAL = block("steel_distillation_controller/dial"),
            PUMPJACK_CRANK = block("pumpjack_crank/crank"),
            TOWER_GAUGE = block("distillation_tower/gauge"),
            ROTOR = block("rotor/block"),
            VOLTMETER_DIAL = block("voltmeter/dial"),
            LIGHT_BULB = block("light_bulb/light"),
            ALUMINUM_LAMP = block("aluminum_lamp/light"),
            CIRCULAR_LIGHT = block("circular_light/light"),
            MODERN_LIGHT = block("modern_light/light"),
            TRAFFIC_LIGHT = block("traffic_light/light"),
            NEON_TUBE_LIGHT_CENTER = block("neon_tube/light_center"),
            NEON_TUBE_LIGHT_SIDE = block("neon_tube/light_side"),
            LARGE_ENGINE_LINKAGE = block("large_engine/linkage"),
            LARGE_ENGINE_PISTON = block("large_engine/piston"),
            SIMPLE_LARGE_ENGINE_LINKAGE = block("simple_large_engine/linkage"),
            SIMPLE_LARGE_ENGINE_PISTON = block("simple_large_engine/piston"),
            STEEL_COGHWEEL = block("steel_cogwheel"),
            LARGE_STEEL_COGHWEEL = block("large_steel_cogwheel_shaftless"),
            ALUMINUM_COGHWEEL = block("aluminum_cogwheel"),
            LARGE_ALUMINUM_COGHWEEL = block("large_aluminum_cogwheel"),
            SHAFTLESS_ALUMINUM_COGHWEEL = block("aluminum_cogwheel_shaftless"),
            SHAFTLESS_STEEL_COGHWEEL = block("steel_cogwheel_shaftless"),
            SHAFTLESS_LARGE_ALUMINUM_COGHWEEL = block("large_aluminum_cogwheel_shaftless"),
            SHAFTLESS_LARGE_STEEL_COGHWEEL = block("large_steel_cogwheel_shaftless"),
            SPOOL = block("winding_machine/spool"),
            SPOOL_WIRE = block("winding_machine/spool_wire"),
            CYLINDER = block("regular_engine/cylinder"),
            RADIAL_ENGINE_CYLINDER = block("radial_engine/cylinder"),
            SMALL_CYLINDER = block("regular_engine/cylinder_small"),
            TRANSFORMER_COIL = block("transformer/coil"),
            FUSE = block("fuse_block/fuse"),
            CONNNECTING_WIRE = block("winding_machine/connecting_wire"),
            CONNNECTING_WIRE_ANIMATED = block("winding_machine/connecting_wire_animated"),
            SMALL_MIXER = block("industrial_mixer/mixer_small"),
            MIXER = block("industrial_mixer/mixer"),
            MIXER_SHAFT = block("industrial_mixer/mixer_shaft"),
            ENGINE_GENERATOR = block("engine_upgrades/generator"),
            TRANSMISSION = PartialModel.of(TFMG.asResource("item/transmission_model")),
            TURBO = block("engine_upgrades/turbo"),
            TURBO_PROPELLER = block("engine_upgrades/turbo_propeller"),
            GOLDEN_TURBO = block("engine_upgrades/golden_turbo"),
            GOLDEN_TURBO_PROPELLER = block("engine_upgrades/golden_turbo_propeller"),
            STEERING_WHEEL = block("engine_controller/wheel"),
            PEDAL = block("engine_controller/pedal"),
            ENGINE_CONTROLLER_DIAL = block("engine_controller/dial"),
            TRANSMISSION_LEVER = block("engine_controller/transmission_lever"),
            SMALL_CENTRIFUGE_BOTTOM = block("industrial_mixer/small_centrifuge_bottom"),
            SMALL_CENTRIFUGE_MIDDLE = block("industrial_mixer/small_centrifuge_middle"),
            SMALL_CENTRIFUGE_TOP = block("industrial_mixer/small_centrifuge_top"),
            SMALL_CENTRIFUGE_ALONE = block("industrial_mixer/small_centrifuge_alone"),
            LARGE_CENTRIFUGE_BOTTOM = block("industrial_mixer/large_centrifuge_bottom"),
            LARGE_CENTRIFUGE_MIDDLE = block("industrial_mixer/large_centrifuge_middle"),
            LARGE_CENTRIFUGE_TOP = block("industrial_mixer/large_centrifuge_top"),
            LARGE_CENTRIFUGE_ALONE = block("industrial_mixer/large_centrifuge_alone"),
            SURFACE_SCANNER_LIGHT = block("surface_scanner/light");

    //Display Segments

    public static final List<PartialModel> SEGMENTS = new ArrayList<>();
    public static final Map<TFMGPipes.PipeMaterial, PartialModel> PIPE_CASINGS = new HashMap<>();
    public static final Map<ResourceLocation, Couple<PartialModel>> FOLDING_DOORS = new HashMap<>();
    public static final Map<TFMGPipes.PipeMaterial, Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>>> PIPE_ATTACHMENTS = new HashMap<>();

    static {

        for (int i = 0; i < 21; i++) {

            SEGMENTS.add(block("segmented_display/segments/" + i));

        }


    }

    static {

        for (TFMGPipes.PipeMaterial material : TFMGPipes.PipeMaterial.values()) {

            Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> attachments = new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);

            for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
                Map<Direction, PartialModel> map = new HashMap<>();
                for (Direction d : Iterate.directions) {
                    String asId = TFMGLang.asId(type.name());
                    map.put(d, block(material.name + "_pipe/" + asId + "/" + TFMGLang.asId(d.getSerializedName())));
                }
                attachments.put(type, map);
            }

            PIPE_ATTACHMENTS.put(material, attachments);

            PIPE_CASINGS.put(material, block(material.name + "_pipe/casing"));

        }
        ////////////////
        putFoldingDoor("steel_door");

    }


    private static void putFoldingDoor(String path) {
        FOLDING_DOORS.put(TFMG.asResource(path),
                Couple.create(block(path + "/fold_left"), block(path + "/fold_right")));
    }

    private static PartialModel block(String path) {
        return PartialModel.of(TFMG.asResource("block/" + path));
    }

    public static void init() {
    }

}