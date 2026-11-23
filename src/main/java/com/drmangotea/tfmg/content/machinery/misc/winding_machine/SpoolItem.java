package com.drmangotea.tfmg.content.machinery.misc.winding_machine;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.base.TFMGUtils;
import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.lang.TFMGLang;
import com.drmangotea.tfmg.content.electricity.IElectric;
import com.drmangotea.tfmg.content.electricity.ElectricalNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.WireConnection;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.transformers.large.LargeTransformerBlockEntity;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectingElectricalNode;
import com.drmangotea.tfmg.registry.TFMGDataComponents;
import com.drmangotea.tfmg.registry.TFMGItems;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Objects;

import static com.drmangotea.tfmg.base.TFMGUtils.closestNode;

@NothingNullByDefault
public class SpoolItem extends Item {
    public final int barColor;
    public final ResourceLocation cableTypeKey;

    public SpoolItem(Properties properties, int barColor, ResourceLocation cableTypeKey) {
        super(properties);
        this.barColor = barColor;
        this.cableTypeKey = cableTypeKey;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Vec3 clickPosition = context.getClickLocation();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		
		if (player == null)
			return InteractionResult.PASS;
		
		BlockEntity blockEntity = level.getBlockEntity(pos);
		
		if (blockEntity instanceof WindingMachineBlockEntity be) {
			if (player instanceof DeployerFakePlayer && stack.is(TFMGItems.EMPTY_SPOOL))
				return InteractionResult.PASS;
			
			ItemStack oldSpool = ItemStack.EMPTY;
			if (!be.getSpool().isEmpty()) {
				oldSpool = be.getSpool();
			}
			be.setSpool(stack);
			player.setItemInHand(context.getHand(), oldSpool);
			be.sendData();
			be.setChanged();
			return InteractionResult.SUCCESS;
		}
		
        if (!(blockEntity instanceof IElectric be))
			return InteractionResult.PASS;
		
		ConnectingElectricalNode node1 = closestNode(be, clickPosition);

		if (node1 == null) {
			return InteractionResult.PASS;
		}
		
		
		BlockPos pos2 = stack.get(TFMGDataComponents.POSITION);
		if (pos2 == null) {
			stack.set(TFMGDataComponents.POSITION, pos);
			stack.set(TFMGDataComponents.CONNECTOR_ID, node1.getLocalId());
		} else {
			ElectricalNetwork network = ElectricalNetworkManager.getNetwork(level);

			int id = stack.getOrDefault(TFMGDataComponents.CONNECTOR_ID, 0);
			
			if (!(level.getBlockEntity(pos2) instanceof IElectric b))
				return InteractionResult.PASS;
			
			if ((b instanceof LargeTransformerBlockEntity t &&!t.isMainPart)) {
				pos2 = t.getControlledBlock().getPos();
			}
			
			if (network.getNodes(pos2).get(id) instanceof ConnectingElectricalNode node2) {
				network.connections.add(new WireConnection(node1.pos, node2.pos, cableTypeKey, true));
				float distance = TFMGUtils.getDistance(node1.pos.getFullPos(),node2.pos.getFullPos())*4;
				
				stack.set(TFMGDataComponents.SPOOL_AMOUNT,(int)Math.max(0,stack.getOrDefault(TFMGDataComponents.SPOOL_AMOUNT,0)-distance));
				
				network.update();
				TFMG.ELECTRICAL_NETWORK_DATA.markDirty();
				stack.remove(TFMGDataComponents.CONNECTOR_ID);
				stack.remove(TFMGDataComponents.POSITION);
			}
		}
		return InteractionResult.SUCCESS;
	}
	
    public static boolean isDifferent(ItemStack using, ItemStack inserted) {
        if (using.is(inserted.getItem())) {
            return !using.getOrDefault(TFMGDataComponents.SPOOL_AMOUNT, 0).equals(inserted.getOrDefault(TFMGDataComponents.SPOOL_AMOUNT, 0));
        }
        return true;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        stack.set(TFMGDataComponents.SPOOL_AMOUNT, 1000);
        super.onCraftedBy(stack, level, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockPos fallback = new BlockPos(0, level.getMaxBuildHeight() + 1, 0); // Impossible block position, better than 0,0,0
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching() && stack.has(TFMGDataComponents.POSITION)) {
            BlockPos pos = stack.getOrDefault(TFMGDataComponents.POSITION, fallback);
            if (pos == fallback)
                return InteractionResultHolder.pass(stack);
            stack.remove(TFMGDataComponents.POSITION);
            stack.remove(TFMGDataComponents.CONNECTOR_ID);
            if (level.isClientSide)
                player.displayClientMessage(
					TFMGLang.translateDirect("wires.removed_data").withStyle(ChatFormatting.YELLOW),
					true
				);
            return InteractionResultHolder.success(stack);
        }
        return super.use(level, player, hand);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        String text = TFMGLang.translateDirect("tooltip.coils").getString();
        tooltip.add(TFMGLang.text(text + stack.getOrDefault(TFMGDataComponents.SPOOL_AMOUNT, 0)).component().withStyle(ChatFormatting.GREEN));

        if (stack.get(TFMGDataComponents.POSITION) == null)
            return;
        BlockPos pos = stack.get(TFMGDataComponents.POSITION);
        if (pos != null)
            tooltip.add(TFMGLang.text(pos.getX() + " " + pos.getY() + " " + pos.getZ()).component().withStyle(ChatFormatting.AQUA));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
		var amount = stack.get(TFMGDataComponents.SPOOL_AMOUNT);
        if (amount == null)
            return;

        if (amount == 0 && entity instanceof Player player && !stack.is(TFMGItems.EMPTY_SPOOL.get())) {
            player.getInventory().setItem(slot, TFMGItems.EMPTY_SPOOL.asStack());
        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !Objects.equals(cableTypeKey, TFMG.asResource("empty")) && TFMGRegistries.CABLE_TYPE_REGISTRY.containsKey(cableTypeKey);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return barColor;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (13f * ((float) stack.getOrDefault(TFMGDataComponents.SPOOL_AMOUNT, 1000) / 1000));
    }
}
