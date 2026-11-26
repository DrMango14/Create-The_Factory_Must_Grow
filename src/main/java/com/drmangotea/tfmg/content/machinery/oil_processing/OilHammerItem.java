package com.drmangotea.tfmg.content.machinery.oil_processing;

import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.world.resevoir.FluidReservoir;
import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import javax.annotation.Nonnull;

public class OilHammerItem extends Item {
    public OilHammerItem(Properties p) {
        super(p);
    }

    @Override @Nonnull
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        ChunkAccess chunk = level.getChunk(context.getClickedPos());
        Player player = context.getPlayer();

        FluidReservoir reservoir = chunk.getExistingDataOrNull(TFMGDataAttachments.FLUID_RESERVOIR);
        if (reservoir == null)
            return InteractionResult.sidedSuccess(level.isClientSide());

        if (level.isClientSide && player != null)
            player.displayClientMessage(TFMGLang
				.translateDirect("oil_hammer.reserves", reservoir.getReserves())
				.withStyle(ChatFormatting.YELLOW),
				true
			);

        return InteractionResult.SUCCESS;
    }
}
