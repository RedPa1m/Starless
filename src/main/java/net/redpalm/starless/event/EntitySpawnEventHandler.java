package net.redpalm.starless.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.custom.ObserveAngryEntity;
import net.redpalm.starless.entity.custom.ObserveEntity;
import net.redpalm.starless.entity.custom.WrongedEntity;

import java.util.Random;

import static net.redpalm.starless.entity.custom.WrongedEntity.canChat;

@Mod.EventBusSubscriber(modid = Starless.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntitySpawnEventHandler extends Event {
    private static boolean startDay = false;
    private static byte eventCount = 0;
    private static boolean canAngryObserveSpawn = false;
    static Random random = new Random();
    private static int eventType;

    private static boolean dailyObserveSpawn = true;
    private static boolean dailyWrongedSpawn = true;

    @SubscribeEvent
    public static void worldTick (TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (!(tick.level instanceof ServerLevel)) return;
        if (tick.level.isClientSide) return;
        if (tick.level.dimension() != Level.OVERWORLD) return;
        if (tick.level.getServer().getPlayerList().getPlayers().isEmpty()) return;
        // make it so angry Observe can only spawn after 6 days
        if (!canAngryObserveSpawn && tick.level.getGameTime() > 24000 * 6) {
            canAngryObserveSpawn = true;
        }
        // reset day starting boolean
        if (tick.level.getGameTime() % 24000 == 0 && tick.level.getGameTime() != 0) {
            startDay = true;
        }
        // 1st day is set to peaceful type
        if (tick.level.getGameTime() == 20) {
            eventType = 0;
        }
        // start of the day event reset
        if (startDay) {
            eventCount = 0;
            dailyObserveSpawn = true;
            dailyWrongedSpawn = true;
            if (random.nextInt(3) == 0 || random.nextInt(3) == 1) {
                eventType = random.nextInt(4);
            }
            else {
                eventType = random.nextInt(2) + 4;
            }
            startDay = false;
        }
        // calling event type method
        fireEventType(tick);

        if (tick.level.getGameTime() % 24000 == 20500) {
            canChat = false;
        }
    }

    public enum DayType {
        PEACEFUL (0),
        CALM (1),
        RISKY (2),
        DANGEROUS (3),
        HARD (4),
        EXTREME (5)
        ;
        private int val;

        DayType(int i) {
            val = i;
        }
        public int getValue() {
            return val;
        }
    }

    private static void fireEventType(TickEvent.LevelTickEvent tick) {
        if (eventType == DayType.PEACEFUL.getValue()) {
            peacefulPreset(tick);
        }
        else if (eventType == DayType.CALM.getValue()) {
            calmPreset(tick);
        }
        else if (eventType == DayType.RISKY.getValue()) {
            riskyPreset(tick);
        }
        else if (eventType == DayType.DANGEROUS.getValue()) {
            dangerousPreset(tick);
        }
        else if (eventType == DayType.HARD.getValue()) {
            hardPreset(tick);
        }
        else if (eventType == DayType.EXTREME.getValue()) {
            extremePreset(tick);
        }
    }

    private static boolean canFireNewEvent() {
        return eventCount < 6;
    }

    private static void peacefulPreset (TickEvent.LevelTickEvent tick) {
        int observePeacefulSpawnTime = 10000;
        int observePeacefulSpawnChance = 10;
        int wrongedSpawnTime = 18000;

        spawnPresetEntity(tick, wrongedSpawnTime, dailyWrongedSpawn, 10, 10, 3, 3,
                "wronged");

        if (canFireNewEvent()) {
            spawnObserve(tick, observePeacefulSpawnTime, observePeacefulSpawnChance, false);
        }
    }

    private static void calmPreset (TickEvent.LevelTickEvent tick) {
        int observeCalmSpawnTime = 7500;
        int observeCalmSpawnChance = 10;
        int wrongedSpawnTime = 18000;

        if (random.nextInt(2) == 0) {
            spawnPresetEntity(tick, wrongedSpawnTime, dailyWrongedSpawn, 10, 10, 3, 3,
                    "wronged");
        }

        if (canFireNewEvent()) {
            spawnObserve(tick, observeCalmSpawnTime, observeCalmSpawnChance, false);
        }
    }

    private static void riskyPreset (TickEvent.LevelTickEvent tick) {
        int observeRiskySpawnTime = 7000;
        int observeRiskySpawnChance = 10;
        int wrongedSpawnTime = 18000;

        if (random.nextInt(2) == 0) {
            spawnPresetEntity(tick, wrongedSpawnTime, dailyWrongedSpawn, 10, 10, 3, 3,
                    "wronged");
        }

        if (canFireNewEvent()) {
            spawnObserve(tick, observeRiskySpawnTime, observeRiskySpawnChance, false);
        }
    }

    private static void dangerousPreset (TickEvent.LevelTickEvent tick) {
        int observeDangerousSpawnTime = 7000;
        int observeDangerousSpawnChance = 5;
        int wrongedSpawnTime = 18000;

        if (random.nextInt(2) == 0) {
            spawnPresetEntity(tick, wrongedSpawnTime, dailyWrongedSpawn, 10, 10, 3, 3,
                    "wronged");
        }

        if (canFireNewEvent()) {
            spawnObserve(tick, observeDangerousSpawnTime, observeDangerousSpawnChance, false);
        }
    }

    private static void hardPreset (TickEvent.LevelTickEvent tick) {
        int observeHardSpawnTime = 7000;
        int observeHardSpawnChance = 5;

        if (canFireNewEvent() && canAngryObserveSpawn) {
            spawnObserve(tick,observeHardSpawnTime, observeHardSpawnChance, true);
        }
    }

    private static void extremePreset (TickEvent.LevelTickEvent tick) {
        int observeExtremeSpawnTime = 7000;
        int observeExtremeSpawnChance = 2;

        if (canFireNewEvent() && canAngryObserveSpawn) {
            spawnObserve(tick,observeExtremeSpawnTime, observeExtremeSpawnChance, true);
        }
    }

    private static void spawnPresetEntity(TickEvent.LevelTickEvent tick, int spawnTime, boolean dailyEntitySpawn,
                                          int extraX, int extraZ, int eX, int eZ, String entityType) {
        if (tick.level.getGameTime() % 24000 == spawnTime && dailyEntitySpawn) {
            LivingEntity entity = entityCreate(tick, entityType);
            if (entity == null) return;

            Player player = tick.level.getServer().getPlayerList().getPlayers().get
                    (tick.level.getRandom().nextInt(tick.level.getServer().getPlayerList().getPlayers().size()));
            spawnEntity(extraX, extraZ, eX, eZ, entity, player, tick);

            dailyEntitySpawn = false;

            if (!entityType.equals("wronged")) {
                eventCount++;
            }
        }
    }

    private static LivingEntity entityCreate (TickEvent.LevelTickEvent tick, String entityType) {
        if (entityType.equals("wronged")) {
            WrongedEntity entity = ModEntities.WRONGED.get().create(tick.level);
            return entity;
        }
        else return null;
    }

    private static void spawnObserve(TickEvent.LevelTickEvent tick, int spawnTime, int spawnChance, boolean isAngry) {
        if ((tick.level.getGameTime() % spawnTime == 0) && tick.level.getRandom().nextInt(spawnChance) == 0 &&
                dailyObserveSpawn) {

            LivingEntity entity = observeCreate(tick, isAngry);
            if (entity == null) return;

            Player player = tick.level.getServer().getPlayerList().getPlayers().get
                    (tick.level.getRandom().nextInt(tick.level.getServer().getPlayerList().getPlayers().size()));

            if (isAngry) {
                spawnEntity(15, 15, 10, 10, entity, player, tick);
            }
            else {
                spawnEntity(10, 10, 3, 3, entity, player, tick);
            }

            tick.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.AMBIENT_CAVE.get(), SoundSource.HOSTILE, 2.3f, 0.85f);
            dailyObserveSpawn = false;
            eventCount++;
        }
    }

    private static LivingEntity observeCreate(TickEvent.LevelTickEvent tick, boolean isAngry) {
        if (!isAngry) {
            ObserveEntity entity = ModEntities.OBSERVE.get().create(tick.level);
            return entity;
        }
        else {
            ObserveAngryEntity entity = ModEntities.OBSERVE_ANGRY.get().create(tick.level);
            return entity;
        }
    }

    private static void spawnEntity(int extraX, int extraZ, int eX, int eZ, LivingEntity entity,
                                   Player player, TickEvent.LevelTickEvent event) {
        int entityX;
        int entityZ;
        int chance = 2;
        if (event.level.getRandom().nextInt(chance) == 0) {
            entityX = (int) player.getX() + event.level.getRandom().nextInt(15) + extraX + random.nextInt(eX);
            entityZ = (int) player.getZ() + event.level.getRandom().nextInt(15) + extraZ + random.nextInt(eZ);
        }
        else {
            entityX = (int) player.getX() - event.level.getRandom().nextInt(15) - extraX - random.nextInt(eX);
            entityZ = (int) player.getZ() - event.level.getRandom().nextInt(15) - extraZ - random.nextInt(eZ);
        }
        entity.setPos(entityX, event.level.getHeight(Heightmap.Types.WORLD_SURFACE,
                entityX, entityZ), entityZ);
        event.level.addFreshEntity(entity);
    }

}
