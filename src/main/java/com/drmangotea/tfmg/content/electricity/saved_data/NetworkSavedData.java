package com.drmangotea.tfmg.content.electricity.saved_data;

import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.WireConnection;
import net.minecraft.core.BlockPos;
import com.drmangotea.tfmg.content.electricity.content.DirectionalElectricalProperties;
import com.drmangotea.tfmg.content.electricity.simulation.ElectricalComponent;
import com.drmangotea.tfmg.content.electricity.simulation.IdealVoltageSource;
import com.drmangotea.tfmg.content.electricity.simulation.Resistance;
import com.drmangotea.tfmg.content.electricity.simulation.Transformer;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NetworkSavedData extends SavedData {

    public NetworkSavedData() {}

    @Override @NothingNullByDefault
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider provider) {
        List<ElectricalNetwork> list = ElectricNetworkManager.networks.values().stream().toList();

        compound = new CompoundTag();

        for (ElectricalNetwork network : list) {
			if (!(network.world instanceof ServerLevel serverLevel)) continue;
			
			CompoundTag networkNBT = new CompoundTag();
			
            List<Map.Entry<BlockPos, ElectricalProperties>> memberList = network.members.entrySet().stream().toList();
			
			networkNBT.putString("Dimension", serverLevel.dimension().location().toString());
 		
			CompoundTag members = new CompoundTag();
			networkNBT.putInt("Member Count", network.members.size());
			networkNBT.putInt("Node Count", network.totalNodes);
			for (int i = 0; i < network.members.size(); i++) {
				CompoundTag member = new CompoundTag();
				ElectricalProperties properties = memberList.get(i).getValue();
				BlockPos pos = memberList.get(i).getKey();
				
				member.put("Position", NbtUtils.writeBlockPos(pos));
				member.putString("Property Type", properties.getType().id().toString());
				for (ElectricalComponent component : properties.components) {
					if (component instanceof IdealVoltageSource source) {
						member.putInt("Voltage " + source.id, (int) source.amplitude);
					}
					if (component instanceof Resistance resistance) {
						member.putDouble("Resistance " + resistance.localId, resistance.resistance);
					}
					if (component instanceof Transformer transformer) {
						member.putDouble("Transformer_Ratio " + transformer.localId, transformer.turnsRatio);
					}
				}
				
				member.putIntArray("Connect Direction", properties.getConnectableDirections().stream()
					.map(Direction::get3DDataValue).toList()
				);
				
				if (properties instanceof DirectionalElectricalProperties p)
					member.putInt("direction", p.direction.get3DDataValue());
				
				members.put("member " + i, member);
			}
			networkNBT.put("blocks", members);
			///
			CompoundTag connections = new CompoundTag();
			networkNBT.putInt("Connection Count", network.connections.size());
			for (int i = 0; i < network.connections.size(); i++) {
				connections.put("connection " + i, network.connections.get(i).save());
			}
			
			networkNBT.put("connections", connections);
			
			compound.put(serverLevel.dimension().location().toLanguageKey(), networkNBT);
        }

        return compound;
    }

    public static NetworkSavedData load(CompoundTag compound, HolderLookup.Provider registries) {
        NetworkSavedData sd = new NetworkSavedData();

        List<ElectricalNetwork> list = ElectricNetworkManager.networks.values().stream().toList();
        for (ElectricalNetwork network : list) {
			if (!(network.world instanceof ServerLevel serverLevel)) continue;
			
			CompoundTag networkTag = compound.getCompound(serverLevel.dimension().location().toLanguageKey());
			network.totalNodes = networkTag.getInt("Node Count");
			int memberCount = networkTag.getInt("Member Count");
			CompoundTag blocks = networkTag.getCompound("blocks");
			
			for (int i = 0; i < memberCount; i++) {
				CompoundTag member = blocks.getCompound("member " + i);
				BlockPos pos = NbtUtils.readBlockPos(member, "Position").orElse(null);
				if (pos == null) continue;
				
				Direction direction = Direction.NORTH;
				
				if (member.contains("direction")) {
					direction = Direction.from3DDataValue(member.getInt("direction"));
				}
				List<Direction> directions = Arrays.stream(member.getIntArray("Connect Direction")).mapToObj(Direction::from3DDataValue).toList();
				
				ElectricalProperties properties = getElectricalProperties(ResourceLocation.parse(member.getString("Property Type")), direction, directions, pos);
				if (properties == null) continue;
				
				network.members.put(pos, properties);
				for (ElectricalComponent component : properties.components) {
					if (component instanceof Resistance r) {
						network.setResistance(pos, r.localId, member.getDouble("Resistance " + r.localId));
					}
					if (component instanceof IdealVoltageSource v) {
						network.setVoltageGen(pos, member.getInt("Voltage " + v.localId));
					}
					if (component instanceof Transformer t) {
						network.setTransformerRatio(pos, t.localId, member.getDouble("Transformer_Ratio " + t.localId));
					}
				}
			}
			int connectionCount = networkTag.getInt("Connection Count");
			CompoundTag connections = networkTag.getCompound("connections");
			for (int i = 0; i < connectionCount; i++) {
				WireConnection connection = WireConnection.fromTag(connections.getCompound("connection " + i));
				if (connection != null) {
					network.connections.add(connection);
				}
			}
        }

        return sd;
    }

    public static ElectricalProperties getElectricalProperties(ResourceLocation id, Direction direction, List<Direction> d, BlockPos pos) {
		var entry = TFMGRegistries.ELECTRICAL_TYPE_REGISTRY.get(id);
		if (entry == null) return null;
		return entry.create(pos, direction, d);
    }

    public static Factory<NetworkSavedData> factory() {
        return new Factory<>(NetworkSavedData::new, NetworkSavedData::load);
    }


    public static NetworkSavedData load(MinecraftServer server) {
        return server.overworld()
                .getDataStorage()
                .computeIfAbsent(factory(), "tfmg_networks");
    }


}
