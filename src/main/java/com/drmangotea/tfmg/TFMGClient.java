package com.drmangotea.tfmg;

import com.drmangotea.tfmg.content.items.weapons.advanced_potato_cannon.AdvancedPotatoCannonRenderHandler;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerRenderHandler;
import com.drmangotea.tfmg.content.items.weapons.quad_potato_cannon.QuadPotatoCannonRenderHandler;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.ElectrodeHolderRenderer;
import com.drmangotea.tfmg.ponder.TFMGPonderPlugin;
import com.drmangotea.tfmg.registry.TFMGParticleTypes;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.potatoCannon.PotatoCannonRenderHandler;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import dev.engine_room.flywheel.lib.model.baked.EmptyVirtualBlockGetter;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.client.render.model.BakedModelBufferer;
import net.createmod.catnip.render.SuperBufferFactory;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.render.SuperByteBufferCache;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

import javax.annotation.Nullable;

@Mod(value = TFMG.MOD_ID, dist = Dist.CLIENT)
public class TFMGClient {

    /**
     * does not work, too bad!
     */
    public static final QuadPotatoCannonRenderHandler QUAD_POTATO_CANNON_RENDER_HANDLER = new QuadPotatoCannonRenderHandler();
    public static final AdvancedPotatoCannonRenderHandler ADVANCED_POTATO_CANNON_RENDER_HANDLER = new AdvancedPotatoCannonRenderHandler();

    public static final FlamethrowerRenderHandler FLAMETHROWER_RENDER_HANDLER = new FlamethrowerRenderHandler();

    public TFMGClient(IEventBus modEventBus) {
        onCtorClient(modEventBus);
    }

    public static void onCtorClient(IEventBus modEventBus) {
        IEventBus neoEventBus = NeoForge.EVENT_BUS;

        modEventBus.addListener(TFMGClient::clientInit);
        modEventBus.addListener(TFMGParticleTypes::registerFactories);

        QUAD_POTATO_CANNON_RENDER_HANDLER.registerListeners(neoEventBus);
        ADVANCED_POTATO_CANNON_RENDER_HANDLER.registerListeners(neoEventBus);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new TFMGPonderPlugin());
    }
}
