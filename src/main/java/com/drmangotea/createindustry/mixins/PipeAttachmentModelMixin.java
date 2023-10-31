package com.drmangotea.createindustry.mixins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.drmangotea.createindustry.base.TFMGPipeModelData;
import com.drmangotea.createindustry.blocks.pipes.normal.plastic.PlasticPipeAttachmentModel;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import org.jetbrains.annotations.NotNull;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.fluids.FluidTransportBehaviour.AttachmentTypes;
import com.simibubi.create.content.fluids.FluidTransportBehaviour.AttachmentTypes.ComponentPartials;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.utility.Iterate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelData.Builder;
import net.minecraftforge.client.model.data.ModelProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.world.level.block.PipeBlock.PROPERTY_BY_DIRECTION;
@Mixin(PipeAttachmentModel.class)
public class PipeAttachmentModelMixin extends BakedModelWrapperWithData {

    private static final ModelProperty<TFMGPipeModelData> PIPE_PROPERTY = new ModelProperty<>();

    public PipeAttachmentModelMixin(BakedModel template) {
        super(template);
    }

    /**
     * @author DrMangoTea
     * @reason locked pipes
     */
    @Overwrite( remap = false)
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state,
                                                ModelData blockEntityData) {
        TFMGPipeModelData data = new TFMGPipeModelData();
        FluidTransportBehaviour transport = BlockEntityBehaviour.get(world, pos, FluidTransportBehaviour.TYPE);
        BracketedBlockEntityBehaviour bracket = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);

        if (transport != null)
            for (Direction d : Iterate.directions) {
                boolean shouldConnect = true;
                if(world.getBlockState(pos.relative(d)).getBlock() instanceof FluidPipeBlock) {

                    if(d.getAxis().isHorizontal())
                        shouldConnect = world.getBlockState(pos.relative(d)).getValue(PROPERTY_BY_DIRECTION.get(d.getOpposite()));



                }

                data.putAttachment(d, transport.getRenderedRimAttachment(world, pos, state, d));

                if(!shouldConnect)
                    if(state.getValue(PROPERTY_BY_DIRECTION.get(d)))
                        data.putAttachment(d, FluidTransportBehaviour.AttachmentTypes.RIM);

            }
        if (bracket != null)
            data.putBracket(bracket.getBracket());

        data.setEncased(FluidPipeBlock.shouldDrawCasing(world, pos, state));
        return builder.with(PIPE_PROPERTY, data);
    }

    @SuppressWarnings("removal")
    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        ChunkRenderTypeSet set = super.getRenderTypes(state, rand, data);
        if (set.isEmpty()) {
            return ItemBlockRenderTypes.getRenderLayers(state);
        }
        return set;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, data, renderType);
        if (data.has(PIPE_PROPERTY)) {
            TFMGPipeModelData pipeData = data.get(PIPE_PROPERTY);
            quads = new ArrayList<>(quads);
            addQuads(quads, state, side, rand, data, pipeData, renderType);
        }
        return quads;
    }

    private void addQuads(List<BakedQuad> quads, BlockState state, Direction side, RandomSource rand, ModelData data,
                          TFMGPipeModelData pipeData, RenderType renderType) {
        BakedModel bracket = pipeData.getBracket();
        if (bracket != null)
            quads.addAll(bracket.getQuads(state, side, rand, data, renderType));
        for (Direction d : Iterate.directions) {
            AttachmentTypes type = pipeData.getAttachment(d);
            for (ComponentPartials partial : type.partials) {
                quads.addAll(AllPartialModels.PIPE_ATTACHMENTS.get(partial)
                        .get(d)
                        .get()
                        .getQuads(state, side, rand, data, renderType));
            }
        }
        if (pipeData.isEncased())
            quads.addAll(AllPartialModels.FLUID_PIPE_CASING.get()
                    .getQuads(state, side, rand, data, renderType));
    }




}
