package com.drmangotea.tfmg.content.electricity.experimental.blocks;

import com.drmangotea.tfmg.content.electricity.connection.cables.CablePos;
import com.drmangotea.tfmg.content.electricity.experimental.ElectricalProperties;
import net.minecraft.core.Direction;

import java.util.List;

public abstract class DirectionalElectricalProperties extends ElectricalProperties {

    public Direction direction;

    public DirectionalElectricalProperties(long pos, Direction direction) {
        super(pos);
        this.direction = direction;
    }

    public abstract List<CablePos> getRotation(Direction direction);
}
