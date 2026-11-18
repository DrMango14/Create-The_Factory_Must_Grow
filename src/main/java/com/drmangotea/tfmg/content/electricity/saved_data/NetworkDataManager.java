package com.drmangotea.tfmg.content.electricity.saved_data;

import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.ArrayList;
import java.util.List;

public class NetworkDataManager {
        public List<ElectricalNetwork> list;
        private NetworkSavedData savedData;

        public NetworkDataManager() {
            list = new ArrayList<>();
            list.addAll(ElectricNetworkManager.networks.values());
        }

        public void levelLoaded(LevelAccessor level) {
            MinecraftServer server = level.getServer();
            if (server == null)
                return;
            savedData = null;
            loadNetworkData(server);
        }
		
        private void loadNetworkData(MinecraftServer server) {
            if (savedData != null)
                return;
            if(!areAllDimensionsLoaded(server)){
                return;
            }
            savedData = NetworkSavedData.load(server);
            list = savedData.getNetworks();
        }
		
        public void markDirty() {
            if (savedData != null)
                savedData.setDirty();
        }

    public static boolean areAllDimensionsLoaded(MinecraftServer server) {
        // Get all registered level stems / dimension keys from the server registry
        var levelStemRegistry = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);

        for (ResourceKey<LevelStem> stemKey : levelStemRegistry.registryKeySet()) {
            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, stemKey.location());
            ServerLevel level = server.getLevel(dimensionKey);

            // If server.getLevel() returns null, the dimension is not currently loaded in memory
            if (level == null) {
                return false;
            }
        }
        return true;
    }
}
