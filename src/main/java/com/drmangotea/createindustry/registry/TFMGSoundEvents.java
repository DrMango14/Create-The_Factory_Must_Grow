
package com.drmangotea.createindustry.registry;


import com.drmangotea.createindustry.CreateTFMG;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.tterrag.registrate.fabric.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class TFMGSoundEvents {

    public static final Map<ResourceLocation, TFMGSoundEvents.SoundEntry> ALL = new HashMap<>();

    public static final TFMGSoundEvents.SoundEntry


            ENGINE = create("engine")
            .subtitle("Engine Sounds")
            .category(SoundSource.BLOCKS)
            .attenuationDistance(10)
            .build(),

            DIESEL_ENGINE = create("diesel_engine")
                    .subtitle("Diese Engine Sounds")
                    .category(SoundSource.BLOCKS)
                    .attenuationDistance(10)
                    .build();

    

    private static TFMGSoundEvents.SoundEntryBuilder create(String name) {
        return create(CreateTFMG.asResource(name));
    }

    public static TFMGSoundEvents.SoundEntryBuilder create(ResourceLocation id) {
        return new TFMGSoundEvents.SoundEntryBuilder(id);
    }

    public static void prepare() {
        for (TFMGSoundEvents.SoundEntry entry : ALL.values())
            entry.prepare();
    }

    public static void register() {
        for (TFMGSoundEvents.SoundEntry entry : ALL.values())
            entry.register();
    }

    public static JsonObject provideLangEntries() {
        JsonObject object = new JsonObject();
        for (TFMGSoundEvents.SoundEntry entry : ALL.values())
            if (entry.hasSubtitle())
                object.addProperty(entry.getSubtitleKey(), entry.getSubtitle());
        return object;
    }

    public static TFMGSoundEvents.SoundEntryProvider provider(DataGenerator generator) {
        return new TFMGSoundEvents.SoundEntryProvider(generator);
    }

    public static void playItemPickup(Player player) {
        player.level.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .2f,
                1f + Create.RANDOM.nextFloat());
    }

//	@SubscribeEvent
//	public static void cancelSubtitlesOfCompoundedSounds(PlaySoundEvent event) {
//		ResourceLocation soundLocation = event.getSound().getSoundLocation();
//		if (!soundLocation.getNamespace().equals(Create.ID))
//			return;
//		if (soundLocation.getPath().contains("_compounded_")
//			event.setResultSound();
//
//	}

    private static class SoundEntryProvider implements DataProvider {

        private DataGenerator generator;

        public SoundEntryProvider(DataGenerator generator) {
            this.generator = generator;
        }

        @Override
        public void run(CachedOutput cache) throws IOException {
            generate(generator.getOutputFolder(), cache);
        }

        @Override
        public String getName() {
            return "TFMG's Custom Sounds";
        }

        public void generate(Path path, CachedOutput cache) {
            Gson GSON = (new GsonBuilder()).setPrettyPrinting()
                    .disableHtmlEscaping()
                    .create();
            path = path.resolve("assets/createindustry");

            try {
                JsonObject json = new JsonObject();
                ALL.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByKey())
                        .forEach(entry -> {
                            entry.getValue()
                                    .write(json);
                        });
                DataProvider.saveStable(cache, json, path.resolve("sounds.json"));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public record ConfiguredSoundEvent(Supplier<SoundEvent> event, float volume, float pitch) {
    }

    public static class SoundEntryBuilder {

        protected ResourceLocation id;
        protected String subtitle = "unregistered";
        protected SoundSource category = SoundSource.BLOCKS;
        protected List<TFMGSoundEvents.ConfiguredSoundEvent> wrappedEvents;
        protected List<ResourceLocation> variants;
        protected int attenuationDistance;

        public SoundEntryBuilder(ResourceLocation id) {
            wrappedEvents = new ArrayList<>();
            variants = new ArrayList<>();
            this.id = id;
        }

        public TFMGSoundEvents.SoundEntryBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public TFMGSoundEvents.SoundEntryBuilder attenuationDistance(int distance) {
            this.attenuationDistance = distance;
            return this;
        }

        public TFMGSoundEvents.SoundEntryBuilder noSubtitle() {
            this.subtitle = null;
            return this;
        }

        public TFMGSoundEvents.SoundEntryBuilder category(SoundSource category) {
            this.category = category;
            return this;
        }

        public TFMGSoundEvents.SoundEntryBuilder addVariant(String name) {
            return addVariant(CreateTFMG.asResource(name));
        }

        public TFMGSoundEvents.SoundEntryBuilder addVariant(ResourceLocation id) {
            variants.add(id);
            return this;
        }

        public TFMGSoundEvents.SoundEntryBuilder playExisting(Supplier<SoundEvent> event, float volume, float pitch) {
            wrappedEvents.add(new TFMGSoundEvents.ConfiguredSoundEvent(event, volume, pitch));
            return this;
        }

        public TFMGSoundEvents.SoundEntryBuilder playExisting(SoundEvent event, float volume, float pitch) {
            return playExisting(() -> event, volume, pitch);
        }

        public TFMGSoundEvents.SoundEntryBuilder playExisting(SoundEvent event) {
            return playExisting(event, 1, 1);
        }

        public TFMGSoundEvents.SoundEntry build() {
            TFMGSoundEvents.SoundEntry entry =
                    wrappedEvents.isEmpty() ? new TFMGSoundEvents.CustomSoundEntry(id, variants, subtitle, category, attenuationDistance)
                            : new TFMGSoundEvents.WrappedSoundEntry(id, subtitle, wrappedEvents, category, attenuationDistance);
            ALL.put(entry.getId(), entry);
            return entry;
        }

    }

    public static abstract class SoundEntry {

        protected ResourceLocation id;
        protected String subtitle;
        protected SoundSource category;
        protected int attenuationDistance;

        public SoundEntry(ResourceLocation id, String subtitle, SoundSource category, int attenuationDistance) {
            this.id = id;
            this.subtitle = subtitle;
            this.category = category;
            this.attenuationDistance = attenuationDistance;
        }

        public abstract void prepare();

        public abstract void register();

        public abstract void write(JsonObject json);

        public abstract SoundEvent getMainEvent();

        public String getSubtitleKey() {
            return id.getNamespace() + ".subtitle." + id.getPath();
        }

        public ResourceLocation getId() {
            return id;
        }

        public boolean hasSubtitle() {
            return subtitle != null;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void playOnServer(Level world, Vec3i pos) {
            playOnServer(world, pos, 1, 1);
        }

        public void playOnServer(Level world, Vec3i pos, float volume, float pitch) {
            play(world, null, pos, volume, pitch);
        }

        public void play(Level world, Player entity, Vec3i pos) {
            play(world, entity, pos, 1, 1);
        }

        public void playFrom(Entity entity) {
            playFrom(entity, 1, 1);
        }

        public void playFrom(Entity entity, float volume, float pitch) {
            if (!entity.isSilent())
                play(entity.level, null, entity.blockPosition(), volume, pitch);
        }

        public void play(Level world, Player entity, Vec3i pos, float volume, float pitch) {
            play(world, entity, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, volume, pitch);
        }

        public void play(Level world, Player entity, Vec3 pos, float volume, float pitch) {
            play(world, entity, pos.x(), pos.y(), pos.z(), volume, pitch);
        }

        public abstract void play(Level world, Player entity, double x, double y, double z, float volume, float pitch);

        public void playAt(Level world, Vec3i pos, float volume, float pitch, boolean fade) {
            playAt(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, volume, pitch, fade);
        }

        public void playAt(Level world, Vec3 pos, float volume, float pitch, boolean fade) {
            playAt(world, pos.x(), pos.y(), pos.z(), volume, pitch, fade);
        }

        public abstract void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade);

    }

    private static class WrappedSoundEntry extends TFMGSoundEvents.SoundEntry {

        private List<TFMGSoundEvents.ConfiguredSoundEvent> wrappedEvents;
        private List<TFMGSoundEvents.WrappedSoundEntry.CompiledSoundEvent> compiledEvents;

        public WrappedSoundEntry(ResourceLocation id, String subtitle,
                                 List<TFMGSoundEvents.ConfiguredSoundEvent> wrappedEvents, SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.wrappedEvents = wrappedEvents;
            compiledEvents = new ArrayList<>();
        }

        @Override
        public void prepare() {
            for (int i = 0; i < wrappedEvents.size(); i++) {
                TFMGSoundEvents.ConfiguredSoundEvent wrapped = wrappedEvents.get(i);
                ResourceLocation location = getIdOf(i);
                SoundEvent event = new SoundEvent(location);
                compiledEvents.add(new TFMGSoundEvents.WrappedSoundEntry.CompiledSoundEvent(event, wrapped.volume(), wrapped.pitch()));
            }
        }

        @Override
        public void register() {
            for (TFMGSoundEvents.WrappedSoundEntry.CompiledSoundEvent event : compiledEvents) {
                Registry.register(Registry.SOUND_EVENT, event.event.getLocation(), event.event);
            }
        }

        @Override
        public SoundEvent getMainEvent() {
            return compiledEvents.get(0)
                    .event;
        }

        protected ResourceLocation getIdOf(int i) {
            return new ResourceLocation(id.getNamespace(), i == 0 ? id.getPath() : id.getPath() + "_compounded_" + i);
        }

        @Override
        public void write(JsonObject json) {
            for (int i = 0; i < wrappedEvents.size(); i++) {
                TFMGSoundEvents.ConfiguredSoundEvent event = wrappedEvents.get(i);
                JsonObject entry = new JsonObject();
                JsonArray list = new JsonArray();
                JsonObject s = new JsonObject();
                s.addProperty("name", event.event()
                        .get()
                        .getLocation()
                        .toString());
                s.addProperty("type", "event");
                if (attenuationDistance != 0)
                    s.addProperty("attenuation_distance", attenuationDistance);
                list.add(s);
                entry.add("sounds", list);
                if (i == 0 && hasSubtitle())
                    entry.addProperty("subtitle", getSubtitleKey());
                json.add(getIdOf(i).getPath(), entry);
            }
        }

        @Override
        public void play(Level world, Player entity, double x, double y, double z, float volume, float pitch) {
            for (TFMGSoundEvents.WrappedSoundEntry.CompiledSoundEvent event : compiledEvents) {
                world.playSound(entity, x, y, z, event.event, category, event.volume() * volume,
                        event.pitch() * pitch);
            }
        }

        @Override
        public void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade) {
            for (TFMGSoundEvents.WrappedSoundEntry.CompiledSoundEvent event : compiledEvents) {
                world.playLocalSound(x, y, z, event.event, category, event.volume() * volume,
                        event.pitch() * pitch, fade);
            }
        }

        private record CompiledSoundEvent(SoundEvent event, float volume, float pitch) {
        }

    }

    private static class CustomSoundEntry extends TFMGSoundEvents.SoundEntry {

        protected List<ResourceLocation> variants;
        protected SoundEvent event;

        public CustomSoundEntry(ResourceLocation id, List<ResourceLocation> variants, String subtitle,
                                SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.variants = variants;
        }

        @Override
        public void prepare() {
            event = new SoundEvent(id);
        }

        @Override
        public void register() {
            Registry.register(Registry.SOUND_EVENT, event.getLocation(), event);
        }

        @Override
        public SoundEvent getMainEvent() {
            return event;
        }

        @Override
        public void write(JsonObject json) {
            JsonObject entry = new JsonObject();
            JsonArray list = new JsonArray();

            JsonObject s = new JsonObject();
            s.addProperty("name", id.toString());
            s.addProperty("type", "file");
            if (attenuationDistance != 0)
                s.addProperty("attenuation_distance", attenuationDistance);
            list.add(s);

            for (ResourceLocation variant : variants) {
                s = new JsonObject();
                s.addProperty("name", variant.toString());
                s.addProperty("type", "file");
                if (attenuationDistance != 0)
                    s.addProperty("attenuation_distance", attenuationDistance);
                list.add(s);
            }

            entry.add("sounds", list);
            if (hasSubtitle())
                entry.addProperty("subtitle", getSubtitleKey());
            json.add(id.getPath(), entry);
        }

        @Override
        public void play(Level world, Player entity, double x, double y, double z, float volume, float pitch) {
            world.playSound(entity, x, y, z, event, category, volume, pitch);
        }

        @Override
        public void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade) {
            world.playLocalSound(x, y, z, event, category, volume, pitch, fade);
        }

    }

}
