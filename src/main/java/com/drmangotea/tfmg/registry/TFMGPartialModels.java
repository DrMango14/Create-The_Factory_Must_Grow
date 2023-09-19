package com.drmangotea.tfmg.registry;


import com.drmangotea.tfmg.CreateTFMG;
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
                PUMPJACK_HAMMER = block("pumpjack/hammer_holder"),
                PUMPJACK_FRONT_ROPE = block("pumpjack/pumpjack_front_rope"),
                PUMPJACK_CONNECTOR = block("pumpjack/pumpjack_connector"),
                PUMPJACK_CRANK_BLOCK = block("pumpjack/pumpjack_crank_block"),
                TOWER_GAUGE = block("distillation_tower/gauge"),
                STEEL_FLUID_PIPE_CASING = block("steel_pipe/casing"),
                SURFACE_SCANNER_DIAL = block("surface_scanner/dial"),
                SURFACE_SCANNER_FLAG = block("surface_scanner/flag"),
                FORMWORK_BOTTOM = block("formwork_block/block_bottom"),
                FORMWORK_SIDE = block("formwork_block/block_side");

    public static final Map<ResourceLocation, Couple<PartialModel>> FOLDING_DOORS = new HashMap<>();

    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> STEEL_PIPE_ATTACHMENTS =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);


    static {
    //   for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
    //       Map<Direction, PartialModel> map = new HashMap<>();
    //       for (Direction d : Iterate.directions) {
    //           String asId = Lang.asId(type.name());
    //           map.put(d, block("steel_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
    //       }
    //       STEEL_PIPE_ATTACHMENTS.put(type, map);
    //   }
        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("steel_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            STEEL_PIPE_ATTACHMENTS.put(type, map);
        }

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