package com.drmangotea.tfmg.blocks.electricity.base.cables;

import net.minecraft.world.level.LevelAccessor;

import java.util.HashMap;
import java.util.Map;

public class ElectricNetworkManager{

    public static Map<LevelAccessor, Map<Long, ElectricalNetwork>> networks = new HashMap<>();

    public void onLoadWorld(LevelAccessor world) {
        networks.put(world, new HashMap<>());

    }

    public void onUnloadWorld(LevelAccessor world) {
        networks.remove(world);

    }

    public ElectricalNetwork getOrCreateNetworkFor(IElectric be) {
        Long id = be.getId();
        ElectricalNetwork network;
        Map<Long, ElectricalNetwork> map = networks.computeIfAbsent(be.getLevel(), $ -> new HashMap<>());


        if (!map.containsKey(id)) {
            network = new ElectricalNetwork(be.getId());





            if(be instanceof IElectric)
                network.add((IElectric) be);

            map.put(id, network);
        }
        network = map.get(id);
        return network;
    }
}
