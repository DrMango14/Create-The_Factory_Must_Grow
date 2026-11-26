package com.drmangotea.tfmg.content.world.resevoir;

import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.ArrayList;
import java.util.List;

public class FluidReservoir {
    private int oilReserves;
    public List<BlockPos> deposits = new ArrayList<>();

    public static final Codec<FluidReservoir> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("OilReserves").forGetter(FluidReservoir::getReserves),
            BlockPos.CODEC.listOf().fieldOf("Deposits").forGetter(FluidReservoir::getDeposits)
    ).apply(instance, FluidReservoir::new));

    public static final StreamCodec<ByteBuf, FluidReservoir> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, reservoir -> reservoir.oilReserves,
            BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), reservoir -> reservoir.deposits,
            FluidReservoir::new
    );

    public static void createReservoir(ChunkAccess chunk, BlockPos origin, RandomSource random) {
        FluidReservoir reservoir = new FluidReservoir(origin);
        reservoir.setReserves(random.nextInt(1000, TFMGConfigs.common().worldGen.depositMaxReserves.get()));
        chunk.setData(TFMGDataAttachments.FLUID_RESERVOIR, reservoir);
    }

    public static void addToReservoir(ChunkAccess chunk, BlockPos deposit) {
        FluidReservoir reservoir = chunk.getData(TFMGDataAttachments.FLUID_RESERVOIR);
        reservoir.addDeposit(deposit);
		chunk.setData(TFMGDataAttachments.FLUID_RESERVOIR, reservoir);
    }

    public FluidReservoir(BlockPos origin) {
        deposits.add(origin);
    }

    public FluidReservoir(int oilReserves, List<BlockPos> deposits) {
        this.oilReserves = oilReserves;
        this.deposits = new ArrayList<>(deposits);
    }

    public FluidReservoir() {
        this.oilReserves = 0;
        this.deposits = new ArrayList<>();
    }

    public boolean isEmpty() {
        return this.oilReserves <= 0;
    }

    public boolean contains(BlockPos pos) {
        return this.deposits.contains(pos);
    }

    public void setReserves(int reserves) {
        this.oilReserves = reserves;
    }

    public int getReserves() {
        return this.oilReserves;
    }

    public void drain(int amount) {
        if (this.oilReserves > 0) {
            this.oilReserves -= Math.min(amount, this.oilReserves);
        }
    }

    public void addDeposit(BlockPos pos) {
        this.deposits.add(pos);
    }

    public List<BlockPos> getDeposits() {
        return new ArrayList<>(this.deposits);
    }

    public void removeEmptyDeposits(Level level) {
        if (!this.isEmpty()) return; //just in case
		this.deposits.forEach((pos) -> level.setBlockAndUpdate(pos, Blocks.BEDROCK.defaultBlockState()));
	}
}
