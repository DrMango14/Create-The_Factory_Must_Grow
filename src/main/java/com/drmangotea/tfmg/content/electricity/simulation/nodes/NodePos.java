package com.drmangotea.tfmg.content.electricity.simulation.nodes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record NodePos (
	BlockPos position,
	Vec3 offset,
	int localId
) {
	public NodePos (BlockPos pos, int id) { this(pos, new Vec3(0,0,0), id); }
	
	public static StreamCodec<ByteBuf, NodePos> STREAM_CODEC = StreamCodec.composite(
		BlockPos.STREAM_CODEC, NodePos::position,
		ByteBufCodecs.DOUBLE, np -> np.offset().x,
		ByteBufCodecs.DOUBLE, np -> np.offset().y,
		ByteBufCodecs.DOUBLE, np -> np.offset().z,
		ByteBufCodecs.INT, NodePos::localId,
		(p, x, y, z, id) -> new NodePos(p, new Vec3(x,y,z), id)
	);
	
	public static Codec<NodePos> CODEC = RecordCodecBuilder.create(i -> i.group(
		BlockPos.CODEC.fieldOf("position").forGetter(NodePos::position),
		Vec3.CODEC.fieldOf("offset").forGetter(NodePos::offset),
		Codec.INT.fieldOf("localId").forGetter(NodePos::localId)
	).apply(i, NodePos::new));
	
	public Tag save () {
		return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public static NodePos fromTag(Tag tag) {
		return CODEC.decode(NbtOps.INSTANCE, tag)
			.mapOrElse(Pair::getFirst, e -> null);
	}
	
	public Vec3 getFullPos () {
		return offset.add(position.getX(), position.getY(), position.getZ());
	}
}
