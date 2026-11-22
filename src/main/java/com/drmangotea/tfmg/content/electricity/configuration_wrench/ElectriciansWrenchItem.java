package com.drmangotea.tfmg.content.electricity.configuration_wrench;

import com.drmangotea.tfmg.base.annotation.NothingNullByDefault;
import com.drmangotea.tfmg.base.blocks.TFMGDirectionalBlock;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.ElectricalNetwork;
import com.drmangotea.tfmg.content.electricity.content.devices.base.ResistiveLoadBlockEntity;
import com.drmangotea.tfmg.content.electricity.content.devices.base.ResistorProperties;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ConnectableElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.ElectricalNode;
import com.drmangotea.tfmg.content.electricity.simulation.nodes.NodeType;
import net.createmod.catnip.gui.ScreenOpener;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@NothingNullByDefault
public class ElectriciansWrenchItem extends Item {
    public ElectriciansWrenchItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack itemStack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (level.isClientSide) {
                CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> openWandGUI(itemStack, hand));
                player.getCooldowns()
                        .addCooldown(this, 5);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStack);
        }



        return new InteractionResultHolder<>(InteractionResult.PASS, itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ElectricalNetwork network = ElectricNetworkManager.getNetwork(level);
        if (!(level.getBlockEntity(pos) instanceof ResistiveLoadBlockEntity be))
			return InteractionResult.PASS;
		
		for (ElectricalNode node : network.getNodes(pos).values()) {
			if (!(node instanceof ConnectableElectricalNode c && c.type.isPhase()))
				continue;
			
			network.members.remove(be.getPos());
			NodeType phase = switch(c.type) {
				case NodeType.PHASE_1 -> NodeType.PHASE_2;
				case NodeType.PHASE_2 -> NodeType.PHASE_3;
				case NodeType.PHASE_3 -> NodeType.PHASE_1;
				default -> throw new IllegalStateException("Attempted to switch phase of a non-phase node");
			};
			
			be.p = new ResistorProperties(be.getPos(),level.getBlockState(pos).getValue(TFMGDirectionalBlock.FACING),be.resistance(),phase);
			network.addMember(pos,be.p);
			network.update();
			
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openWandGUI(ItemStack itemStack, InteractionHand hand) {
        ScreenOpener.open(new ElectriciansWrenchScreen(itemStack, hand));
    }
}
