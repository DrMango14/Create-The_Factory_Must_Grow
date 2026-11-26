package com.drmangotea.tfmg.content.electricity.content;

import com.drmangotea.tfmg.content.electricity.ElectricalProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class DirectionalElectricalProperties extends ElectricalProperties {

    public Direction direction;

    public DirectionalElectricalProperties(BlockPos pos, Direction direction) {
        super(pos);
        this.direction = direction;
    }

    public abstract List<Vec3> getRotation(Direction direction);
}
