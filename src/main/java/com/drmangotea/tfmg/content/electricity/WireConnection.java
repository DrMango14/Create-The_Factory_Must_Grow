package com.drmangotea.tfmg.content.electricity;

import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.wires.cable_type.CableType;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodePos;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record WireConnection (
	NodePos node1,
	NodePos node2,
	ResourceLocation cableTypeKey,
	boolean render
) {
	
	public CableType cableType () {
		return TFMGUtils.getCableType(cableTypeKey);
	}
	
	public boolean connectedTo(BlockPos pos) {
		return node1.position() == pos || node2.position() == pos;
	}
	
	//just to prettify the codecs tbh
	private WireConnection(NodePos n1, NodePos n2, String t, Boolean r) {
		this(n1, n2, ResourceLocation.parse(t), r);
	}
	
	public static StreamCodec<ByteBuf, WireConnection> STREAM_CODEC = StreamCodec.composite(
		NodePos.STREAM_CODEC, WireConnection::node1,
		NodePos.STREAM_CODEC, WireConnection::node2,
		ByteBufCodecs.STRING_UTF8, c -> c.cableTypeKey.toString(),
		ByteBufCodecs.BOOL, WireConnection::render,
		WireConnection::new
	);
	
	public static Codec<WireConnection> CODEC = RecordCodecBuilder.create(i -> i.group(
		NodePos.CODEC.fieldOf("Node1").forGetter(WireConnection::node1),
		NodePos.CODEC.fieldOf("Node2").forGetter(WireConnection::node2),
		Codec.STRING.fieldOf("Cable").forGetter(c -> c.cableTypeKey.toString()),
		Codec.BOOL.fieldOf("Should Render").forGetter(WireConnection::render)
	).apply(i, WireConnection::new));
	
	public Tag save () {
		return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public static WireConnection fromTag(Tag connection) {
		return CODEC.decode(NbtOps.INSTANCE, connection)
			.mapOrElse(Pair::getFirst, e -> null);
	}
}
