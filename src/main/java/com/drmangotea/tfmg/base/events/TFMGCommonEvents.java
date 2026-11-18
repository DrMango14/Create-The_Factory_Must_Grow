package com.drmangotea.tfmg.base.events;

import com.drmangotea.tfmg.TFMG;
import com.drmangotea.tfmg.TFMGRegistries;
import com.drmangotea.tfmg.config.TFMGConfigs;
import com.drmangotea.tfmg.content.decoration.tanks.TFMGFluidTankBlockEntity;
import com.drmangotea.tfmg.content.decoration.tanks.steel.SteelTankBlockEntity;
import com.drmangotea.tfmg.content.electricity.ElectricNetworkManager;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.accumulator_rack.AccumulatorRackBlockEntity;
import com.drmangotea.tfmg.content.electricity.content.power_transmission.energy_storage.cells.AccumulatorCellItem;
import com.drmangotea.tfmg.content.electricity.content.devices.polarizer.PolarizerBlockEntity;
import com.drmangotea.tfmg.content.engines.fuels.EngineFuelType;
import com.drmangotea.tfmg.content.engines.types.AbstractSmallEngineBlockEntity;
import com.drmangotea.tfmg.content.engines.types.large_engine.LargeEngineBlockEntity;
import com.drmangotea.tfmg.content.items.weapons.flamethrover.FlamethrowerFuelType;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceHatchBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_furnace.BlastFurnaceOutputBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.blast_stove.BlastStoveBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.casting_basin.CastingBasinBlockEntity;
import com.drmangotea.tfmg.content.machinery.metallurgy.coke_oven.CokeOvenBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.air_intake.AirIntakeBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.concrete_hose.ConcreteHoseBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.exhaust.ExhaustBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.firebox.FireboxBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.flarestack.FlarestackBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.gas_lamp.GasLampBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.smokestack.SmokestackBlockEntity;
import com.drmangotea.tfmg.content.machinery.misc.winding_machine.WindingMachineBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.controller.DistillationControllerBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.distillation_tower.output.DistillationOutputBlockEntity;
import com.drmangotea.tfmg.content.machinery.oil_processing.pumpjack.base.PumpjackBaseBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.base.VatBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.electrode_holder.ElectrodeHolderBlockEntity;
import com.drmangotea.tfmg.content.machinery.vat.industrial_mixer.IndustrialMixerBlockEntity;
import com.drmangotea.tfmg.content.world.resevoir.FluidReservoir;
import com.drmangotea.tfmg.integration.jei.util.EngineFuelsRecipe;
import com.drmangotea.tfmg.mixin.accessor.ChunkMapAccessor;
import com.drmangotea.tfmg.registry.TFMGDataAttachments;
import com.drmangotea.tfmg.registry.TFMGMobEffects;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.payload.SyncAttachmentsPayload;
import net.neoforged.neoforge.registries.NewRegistryEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;


@SuppressWarnings("unused")
@EventBusSubscriber
public class TFMGCommonEvents {
    @SubscribeEvent
    public static void onUnloadWorld(LevelEvent.Unload event) {
        LevelAccessor world = event.getLevel();


        TFMG.ELECTRIC_NETWORK_MANAGER.onUnloadWorld(world);
    }

    @SubscribeEvent
    public static void onLoadWorld(LevelEvent.Load event) {
        LevelAccessor world = event.getLevel();

        TFMG.ELECTRIC_NETWORK_MANAGER.onLoadWorld(world);

        TFMG.ELECTRICAL_NETWORK_DATA.levelLoaded(world);
    }

    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        ElectricNetworkManager.playerLogin(player);
    }
    @SubscribeEvent
    public static void playerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();

        ElectricNetworkManager.playerLogin(player);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ElectricNetworkManager.handleNetworkUpdate(event);
    }

    @SubscribeEvent
    public static void dataReload(OnDatapackSyncEvent event) {
        FlamethrowerFuelType.typeCache.clear();
        EngineFuelType.typeCache.clear();
        List<Holder<EngineFuelType>> fuels = new ArrayList<>();
        for (var holder : event.getPlayerList().getServer().registryAccess().registryOrThrow(TFMGRegistries.ENGINE_FUEL_TYPE).asHolderIdMap()) {
            fuels.add(holder);
        }
        CatnipServices.NETWORK.sendToClients(event.getRelevantPlayers().toList(), new EngineFuelsRecipe.SyncPacket(fuels));
    }

    @EventBusSubscriber
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            AccumulatorCellItem.registerCapabilities(event);
            AccumulatorRackBlockEntity.registerCapabilities(event);
            AbstractSmallEngineBlockEntity.registerCapabilities(event);
            DistillationOutputBlockEntity.registerCapabilities(event);
            ConcreteHoseBlockEntity.registerCapabilities(event);
            PumpjackBaseBlockEntity.registerCapabilities(event);
            PolarizerBlockEntity.registerCapabilities(event);
            LargeEngineBlockEntity.registerCapabilities(event);
            CastingBasinBlockEntity.registerCapabilities(event);
            FireboxBlockEntity.registerCapabilities(event);
            DistillationControllerBlockEntity.registerCapabilities(event);
            SteelTankBlockEntity.registerCapabilities(event);
            TFMGFluidTankBlockEntity.registerCapabilities(event);
            VatBlockEntity.registerCapabilities(event);
            BlastStoveBlockEntity.registerCapabilities(event);
            SmokestackBlockEntity.registerCapabilities(event);
            ExhaustBlockEntity.registerCapabilities(event);
            BlastFurnaceHatchBlockEntity.registerCapabilities(event);
            FlarestackBlockEntity.registerCapabilities(event);
            GasLampBlockEntity.registerCapabilities(event);
            BlastFurnaceOutputBlockEntity.registerCapabilities(event);
            CokeOvenBlockEntity.registerCapabilities(event);
            AirIntakeBlockEntity.registerCapabilities(event);
            WindingMachineBlockEntity.registerCapabilities(event);
            IndustrialMixerBlockEntity.registerCapabilities(event);
            ElectrodeHolderBlockEntity.registerCapabilities(event);
        }

        @SubscribeEvent
        public static void newRegistry(NewRegistryEvent event) {
            TFMGRegistries.FOR_INIT.forEach(event::register);
        }
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        MobEffectInstance instance = event.getEffectInstance();
        LivingEntity target = event.getEntity();
        if (instance.is(TFMGMobEffects.HELLFIRE)) {
            if (!target.isOnFire()) {
                target.setRemainingFireTicks(instance.getDuration());
            } else if (target.getRemainingFireTicks() < instance.getDuration()) {
                target.setRemainingFireTicks(instance.getDuration());
            }
        }
    }

    @SubscribeEvent
    public static void levelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
		
		try {
			for (ChunkHolder chunkHolder : ((ChunkMapAccessor) serverLevel.getChunkSource().chunkMap).getVisibleChunkMap().values()) {
				LevelChunk chunk = chunkHolder.getTickingChunk();
				if (chunk != null && !TFMGConfigs.common().worldGen.infiniteDeposits.get() &&
					chunk.hasData(TFMGDataAttachments.FLUID_RESERVOIR)
				) {
					FluidReservoir reservoir = chunk.getData(TFMGDataAttachments.FLUID_RESERVOIR);
					if (reservoir.isEmpty()) {
						reservoir.removeEmptyDeposits(serverLevel);
						chunk.removeData(TFMGDataAttachments.FLUID_RESERVOIR);
					}
				}
			}
		} catch (Exception exception) {
			TFMG.LOGGER.error("Error occurred while ticking fluid reservoirs.", exception);
		}
    }
}
