package com.drmangotea.tfmg.base.spark;

import com.drmangotea.tfmg.registry.TFMGPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecs;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record ElectricitySparkPacket(Vec3 pos) implements ClientboundPacketPayload {
    public static final StreamCodec<ByteBuf, ElectricitySparkPacket> STREAM_CODEC = CatnipStreamCodecs.VEC3
            .map(ElectricitySparkPacket::new, ElectricitySparkPacket::pos);

    @Override
    public PacketTypeProvider getTypeProvider() {
        return TFMGPackets.ELECTRICITY_SPARK_EFFECT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handle(LocalPlayer player) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        double x = (level.random.nextDouble() - 0.5) * 0.02;
        double y = (level.random.nextDouble() - 0.5) * 0.02;
        double z = (level.random.nextDouble() - 0.5) * 0.02;
        level.addParticle(ParticleTypes.ELECTRIC_SPARK, pos.x + x, pos.y + y, pos.z + z, 0.02, 0.02, 0.02);
    }

    public static void send(Level level, Vec3 pos) {
        if (level instanceof ServerLevel serverLevel)
            CatnipServices.NETWORK.sendToClientsAround(serverLevel, pos, 32, new ElectricitySparkPacket(pos));
    }
}
