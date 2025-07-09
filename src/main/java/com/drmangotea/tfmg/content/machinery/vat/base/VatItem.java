package com.drmangotea.tfmg.content.machinery.vat.base;

import com.drmangotea.tfmg.registry.TFMGBlockEntities;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class VatItem extends BlockItem {

    String vatType;

    public VatItem(Block block, Properties properties, String vatType) {
        super(block, properties);
        this.vatType = vatType;
    }

    public static class SteelVatItem extends VatItem {

        public SteelVatItem(Block block, Properties properties) {
            super(block, properties,"tfmg:steel_vat");
        }
    }

    public static class CastIronVatItem extends VatItem {

        public CastIronVatItem(Block block, Properties properties) {
            super(block, properties,"tfmg:cast_iron_vat");
        }
    }

    public static class FireproofVatItem extends VatItem {

        public FireproofVatItem(Block block, Properties properties) {
            super(block, properties,"tfmg:firebrick_lined_vat");
        }
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult initialResult = super.place(ctx);
        if (!initialResult.consumesAction())
            return initialResult;
        tryMultiPlace(ctx);
        return initialResult;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player,
                                                 ItemStack stack, BlockState state) {
        MinecraftServer minecraftserver = level.getServer();
        if (minecraftserver == null)
            return false;
        CompoundTag nbt = stack.getTagElement("BlockEntityTag");
        if (nbt != null) {
            nbt.remove("Luminosity");
            nbt.remove("Size");
            nbt.remove("Height");
            nbt.remove("Controller");
            nbt.remove("LastKnownPos");
        }

        return super.updateCustomBlockEntityTag(pos, level, player, stack, state);
    }

    private void tryMultiPlace(BlockPlaceContext ctx) {

        Player player = ctx.getPlayer();
        if (player == null)
            return;
        if (player.isShiftKeyDown())
            return;
        Direction face = ctx.getClickedFace();
        if (!face.getAxis()
                .isVertical())
            return;
        ItemStack stack = ctx.getItemInHand();
        Level world = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockPos placedOnPos = pos.relative(face.getOpposite());
        BlockState placedOnState = world.getBlockState(placedOnPos);

        if (!(placedOnState.getBlock() instanceof VatBlock placedOnVat)) return;
        if (!placedOnVat.vatType.equals(this.vatType)) return;


        VatBlockEntity vatAt = ConnectivityHandler.partAt(
                TFMGBlockEntities.CHEMICAL_VAT.get(), world, placedOnPos);
        if (vatAt == null)
            return;
        VatBlockEntity controllerTE =  vatAt.getControllerBE();
        if (controllerTE == null)
            return;

        int width = controllerTE.getWidth();
        if (width == 1)
            return;

        int vatsToPlace = 0;
        BlockPos startPos = face == Direction.DOWN ? controllerTE.getBlockPos()
                .below()
                : controllerTE.getBlockPos()
                .above(controllerTE.getHeight());

        if (startPos.getY() != pos.getY())
            return;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos offsetPos = startPos.offset(xOffset, 0, zOffset);
                BlockState blockState = world.getBlockState(offsetPos);
                if (VatBlock.isVat(blockState))
                    continue;
                if (!blockState.canBeReplaced())
                    return;
                vatsToPlace++;
            }
        }

        if (!player.isCreative() && stack.getCount() < vatsToPlace)
            return;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos offsetPos = startPos.offset(xOffset, 0, zOffset);
                BlockState blockState = world.getBlockState(offsetPos);
                if (VatBlock.isVat(blockState))
                    continue;
                BlockPlaceContext context = BlockPlaceContext.at(ctx, offsetPos, face);
                player.getPersistentData()
                        .putBoolean("SilenceTankSound", true);
                super.place(context);
                player.getPersistentData()
                        .remove("SilenceTankSound");
            }
        }
    }
}
