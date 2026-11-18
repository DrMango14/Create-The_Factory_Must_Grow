package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeTransformerBlockEntity;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.List;
import java.util.function.DoubleUnaryOperator;

import static com.drmangotea.tfmg.base.TFMGUtils.closestNode;

@OnlyIn(Dist.CLIENT)
public class ElectricNetworkRenderer {

    public void tickRender(RenderLevelStageEvent event) {
        tickOutlines();
        renderWires(event);
    }

    public void renderWires(RenderLevelStageEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
            return;
        if (player == null)
            return;
        ElectricalNetwork network = ElectricNetworkManager.getNetwork(mc.level);
        PoseStack ms = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer vc = buffer.getBuffer(RenderType.cutoutMipped());
        Camera camera = event.getCamera();
        Vec3 playerPos = player.getEyePosition();

        for (WireConnection connection : network.connections) {
			if (connection.render())
				renderWire(
					connection.cableType().getModel(),
					connection.node1().getPosition().add(Vec3.atLowerCornerOf(connection.node1().pos)),
					connection.node2().getPosition().add(Vec3.atLowerCornerOf(connection.node2().pos)),
					camera, ms, vc, playerPos
				);
        }
    }

    public void renderWire(PartialModel model, Vec3 pos1, Vec3 pos2, Camera camera, PoseStack ms, VertexConsumer vc, Vec3 playerPos) {
		Vec3 cameraInverse = Vec3.ZERO.subtract(camera.getPosition());

        int cableRenderDistance = TFMGConfigs.client().render.cableRenderDistance.get();

        BlockState air = Blocks.AIR.defaultBlockState();


		pos1.subtract(0.5d,0.5d,0.5d);
        Vec3 posA = pos1.subtract(0.5d,0.5d,0.5d);
        Vec3 posB = pos2.subtract(0.5d,0.5d,0.5d);
        Vec3 AtoB = posB.subtract(posA);
		
		if ((posA.distanceTo(playerPos) > cableRenderDistance) && (posB.distanceTo(playerPos) > cableRenderDistance))
			return;
		
        float yaw = (float) Math.atan2(AtoB.x, AtoB.z);
        float flat_length = (float) Math.sqrt(AtoB.x * AtoB.x + AtoB.z * AtoB.z);
        float pitch = (float) Math.atan2(AtoB.y, flat_length);
        float length = (float) AtoB.length();
        //

        //
        int segmentCount = (int) ((length * 16) / 4) + 1;
		double reciprocal = 1 / (double) segmentCount;
		DoubleUnaryOperator curveFunction   = x -> 0.2f * x * (x * reciprocal - 1);
		DoubleUnaryOperator curveDerivative = x -> 0.2f * (2 * x * reciprocal - 1);
		
		for (int i = 0; i < segmentCount; i++) {
			float angle = (float) Math.toDegrees(Math.atan(curveDerivative.applyAsDouble(i)));
			ms.pushPose();
			
			CachedBuffers.partial(model, air)
				.translate(cameraInverse)
				.translate(posA)
				.light(600)
				.translate(0.5f, 0.5f, 0.5f)
				.rotate(yaw, Direction.Axis.Y)
				.rotate(-pitch, Direction.Axis.X)
				.translateY((float) curveFunction.applyAsDouble(i) * 0.25f)
				.translate(0, 0, i * (4 / 16f))
				.rotateDegrees(-angle, Direction.Axis.X)
				.translate(-0.5f, -0.5f, -0.5f)
				.renderInto(ms, vc);
		}
    }

    public static void tickOutlines() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || !(mc.hitResult instanceof BlockHitResult result))
            return;

        BlockPos pos = result.getBlockPos();
		//ItemStack heldItem = mc.player.getMainHandItem();
		
		
		if (!(mc.level.getBlockEntity(pos) instanceof IElectric be)) return;
		
		if (be instanceof LargeTransformerBlockEntity t && !t.isMainPart) {
			IElectric newBE = t.getControlledBlock();
			if (newBE != null) {
				pos = newBE.getPos();
			}
		}

		ElectricalNetwork network = ElectricNetworkManager.getNetwork(mc.level);
		ConnectableElectricalNode closestNode = closestNode(network, pos, result.getLocation());
		List<ElectricalNode> nodes = network.getNodes(pos);
		if (nodes.isEmpty()) return;
		
		Vec3 basePos = Vec3.atLowerCornerOf(pos);
		nodes.parallelStream().forEach(n -> {
			if (!(n instanceof ConnectingElectricalNode node)) return;
			
			Vec3 cablePos = node.getPosition().add(basePos);
			
			float size = node == closestNode ? 0.1f : 0.05f;
			
			Vec3 corner1 = cablePos.add(size,size,size);
			Vec3 corner2 = cablePos.subtract(size,size,size);
			
			TFMGUtils.createOutline(corner1, corner2, "connector_" + node.localId, node.type.getColor());
		});
    }

}
