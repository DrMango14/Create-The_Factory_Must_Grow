package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.content.electricity.experimental.packets.NetworkLoadPacket;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.HashMap;
import java.util.Map;

public class RealElectricNetworkManager {
    public static Map<LevelAccessor, RealElectricalNetwork> networks = new HashMap<>();

    public void onLoadWorld(LevelAccessor world) {
        networks.put(world, new RealElectricalNetwork(world));

    }

    public void onUnloadWorld(LevelAccessor world) {
        networks.remove(world);
    }

    public static RealElectricalNetwork getNetwork(Level level) {
        return networks.get(level);
    }

    public static void playerLogin(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkLoadPacket packet = new NetworkLoadPacket(RealElectricNetworkManager.networks.values().stream().toList());
            CatnipServices.NETWORK.sendToClient(serverPlayer, packet);
        }
    }


}
