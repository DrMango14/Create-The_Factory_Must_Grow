package com.drmangotea.tfmg.content.electricity.experimental;

import com.drmangotea.tfmg.content.electricity.experimental.simulation.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.experimental.simulation.ElectricalNode;

public record WireConnection(ConnectingElectricalNode node1, ConnectingElectricalNode node2, double resistance) {
}
