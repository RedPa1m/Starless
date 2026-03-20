package net.redpalm.starless.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

import static net.redpalm.starless.event.custom.CitaseEventsAndReputation.isFamiliar;

public class CitaseSavedData extends SavedData {
    private boolean isFamiliarSD = false;

    public static CitaseSavedData create() {
        return new CitaseSavedData();
    }

    public static CitaseSavedData load (CompoundTag tag) {
        CitaseSavedData data = create();
        data.isFamiliarSD = tag.getBoolean("isFamiliar");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putBoolean("isFamiliar", isFamiliarSD);
        return compoundTag;
    }

    public static CitaseSavedData get (MinecraftServer server) {
            if (server == null) return create();
            return server.overworld().getDataStorage().computeIfAbsent(CitaseSavedData::load,
                    CitaseSavedData::create, "citase_data");
    }

    public void save() {
        this.setDirty();
    }

    public static void save(MinecraftServer server) {
            CitaseSavedData data = CitaseSavedData.get(server);
            data.isFamiliarSD = isFamiliar;
            data.save();
    }

    public static void read (MinecraftServer server) {
            CitaseSavedData data = CitaseSavedData.get(server);
            isFamiliar = data.isFamiliarSD;
    }
}
