package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.content.electricity.packets.NetworkLoadPacket;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ElectricNetworkManager {
    public static Map<LevelAccessor, ElectricalNetwork> networks = new HashMap<>();

    public void onLoadWorld(LevelAccessor world) {
		networks.put(world, new ElectricalNetwork(world));
    }

    public void onUnloadWorld(LevelAccessor world) {
        networks.remove(world);
    }

    public static ElectricalNetwork getNetwork(Level level) {
        return networks.get(level);
    }

    public static void handleNetworkUpdate(ServerTickEvent.Post event) {

    }

    public static LevelAccessor getWorldFromNetwork(ElectricalNetwork network) {

        for (Map.Entry<LevelAccessor, ElectricalNetwork> entry : networks.entrySet()) {
            if (Objects.equals(network, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void playerLogin(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkLoadPacket packet = new NetworkLoadPacket(ElectricNetworkManager.networks.values().stream().toList());
            CatnipServices.NETWORK.sendToClient(serverPlayer, packet);

            ElectricalNetwork network = ElectricNetworkManager.getNetwork(player.level());
            network.update();


        }
    }


}
