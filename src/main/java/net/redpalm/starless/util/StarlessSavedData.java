package net.redpalm.starless.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import static net.redpalm.starless.event.EntitySpawnEventHandler.*;
import static net.redpalm.starless.event.custom.CitaseEventsAndReputation.isFamiliar;

public class StarlessSavedData extends SavedData {
    private boolean isFamiliarSD = false;
    private byte eventCountSD = 0;
    private int eventTypeSD;
    private boolean dailyObserveSpawnSD = true;
    private boolean dailyTerminalUsageSD = true;
    private boolean seekerSpawnsFirstTimeSD = true;
    private boolean dailySeekerSpawnSD = true;

    public static StarlessSavedData create() {
        return new StarlessSavedData();
    }

    public static StarlessSavedData load (CompoundTag tag, HolderLookup.Provider provider) {
        StarlessSavedData data = create();
        data.isFamiliarSD = tag.getBoolean("isFamiliar");
        data.eventCountSD = tag.getByte("eventCount");
        data.eventTypeSD = tag.getInt("eventType");
        data.dailyObserveSpawnSD = tag.getBoolean("dailyObserveSpawn");
        data.dailyTerminalUsageSD = tag.getBoolean("dailyTerminalUsage");
        data.seekerSpawnsFirstTimeSD = tag.getBoolean("seekerSpawnsFirstTime");
        data.dailySeekerSpawnSD = tag.getBoolean("dailySeekerSpawn");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putBoolean("isFamiliar", isFamiliarSD);
        compoundTag.putByte("eventCount", eventCountSD);
        compoundTag.putInt("eventType", eventTypeSD);
        compoundTag.putBoolean("dailyObserveSpawn", dailyObserveSpawnSD);
        compoundTag.putBoolean("dailyTerminalUsage", dailyTerminalUsageSD);
        compoundTag.putBoolean("seekerSpawnsFirstTime", seekerSpawnsFirstTimeSD);
        compoundTag.putBoolean("dailySeekerSpawn", dailySeekerSpawnSD);
        return compoundTag;
    }

    public static StarlessSavedData get (MinecraftServer server) {
        if (server == null) return create();
        return server.overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(StarlessSavedData::create, StarlessSavedData::load), "starless_data");
    }

    public void save() {
        this.setDirty();
    }

    public static void save(MinecraftServer server) {
            StarlessSavedData data = StarlessSavedData.get(server);
            data.isFamiliarSD = isFamiliar;
            data.eventCountSD = eventCount;
            data.eventTypeSD = eventType;
            data.dailyObserveSpawnSD = dailyObserveSpawn;
            data.dailyTerminalUsageSD = dailyTerminalUsage;
            data.seekerSpawnsFirstTimeSD = seekerSpawnsFirstTime;
            data.dailySeekerSpawnSD = dailySeekerSpawn;
            data.save();
    }

    public static void read (MinecraftServer server) {
            StarlessSavedData data = StarlessSavedData.get(server);
            isFamiliar = data.isFamiliarSD;
            eventCount = data.eventCountSD;
            eventType = data.eventTypeSD;
            dailyObserveSpawn = data.dailyObserveSpawnSD;
            dailyTerminalUsage = data.dailyTerminalUsageSD;
            seekerSpawnsFirstTime = data.seekerSpawnsFirstTimeSD;
            dailySeekerSpawn = data.dailySeekerSpawnSD;
    }
}
