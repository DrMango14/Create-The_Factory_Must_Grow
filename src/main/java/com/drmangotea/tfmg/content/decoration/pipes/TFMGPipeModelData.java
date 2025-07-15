package com.drmangotea.tfmg.content.decoration.pipes;

import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;

public class TFMGPipeModelData {
    private FluidTransportBehaviour.AttachmentTypes[] attachments;
    private boolean encased;
    private BakedModel bracket;

    public TFMGPipeModelData() {
        attachments = new FluidTransportBehaviour.AttachmentTypes[6];
        Arrays.fill(attachments, FluidTransportBehaviour.AttachmentTypes.NONE);
    }

    public void putBracket(BlockState state) {
        if (state != null) {
            this.bracket = Minecraft.getInstance()
                    .getBlockRenderer()
                    .getBlockModel(state);
        }
    }

    public BakedModel getBracket() {
        return bracket;
    }

    public void putAttachment(Direction face, FluidTransportBehaviour.AttachmentTypes rim) {
        attachments[face.get3DDataValue()] = rim;
    }

    public FluidTransportBehaviour.AttachmentTypes getAttachment(Direction face) {
        return attachments[face.get3DDataValue()];
    }

    public void setEncased(boolean encased) {
        this.encased = encased;
    }

    public boolean isEncased() {
        return encased;
    }
}

