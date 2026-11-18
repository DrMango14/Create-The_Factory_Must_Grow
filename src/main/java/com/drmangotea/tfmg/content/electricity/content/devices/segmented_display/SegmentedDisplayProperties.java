package com.drmangotea.tfmg.content.electricity.content.devices.segmented_display;

import com.drmangotea.tfmg.content.electricity.ElectricalPropertiesType;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.ConnectorProperties;
import com.drmangotea.tfmg.registry.TFMGElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.Arrays;
import java.util.List;

//This prolly isn't needed, tbh. Not going to remove it until I'm sure I can get segment displays to work without it, though.
public class SegmentedDisplayProperties extends ConnectorProperties {
	public SegmentedDisplayProperties(BlockPos pos, Direction direction) {
		super(pos, direction);
	}
	
	public List<Direction> getConnectableDirections() {
		return Arrays.stream(Direction.values()).filter(d -> d != direction).toList();
	}
	
	@Override
	public ElectricalPropertiesType<?> getType() {
		return TFMGElectricalProperties.DISPLAY.get();
	}
	
	/*public boolean hasElectricityPort(Direction direction) {
		return direction != this.direction;
	}*/
}
