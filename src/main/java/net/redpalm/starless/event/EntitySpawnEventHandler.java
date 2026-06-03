package net.redpalm.starless.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.custom.*;
import net.redpalm.starless.util.StarlessSavedData;

import java.util.Random;

import static net.redpalm.starless.entity.custom.WrongedEntity.canChat;

@EventBusSubscriber(modid = Starless.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EntitySpawnEventHandler extends Event {
    private static boolean startDay = false;
    public static byte eventCount = 0;
    private static boolean canAngryObserveSpawn = false;
    public static boolean canSmilerSpawn = false;
    public static boolean seekerSpawnsFirstTime = true;
    static Random random = new Random();
    public static int eventType;
    private static long lastDayTime = -1;
    public static int eventTypeOld;
    public static boolean specialSmilerSpawn = false;

    public static boolean dailyObserveSpawn = true;
    public static boolean dailyWrongedSpawn = true;
    public static boolean dailyCitaseSpawn = true;
    public static boolean dailyTerminalUsage = true;
    public static boolean dailySeekerSpawn = true;
    public static boolean dailySmilerSpawn = true;

    @SubscribeEvent
    public static void worldTick (LevelTickEvent.Post tick) {
        if (!(tick.getLevel() instanceof ServerLevel)) return;
        if (tick.getLevel().isClientSide) return;
        if (tick.getLevel().dimension() != Level.OVERWORLD) return;
        if (tick.getLevel().getServer().getPlayerList().getPlayers().isEmpty()) return;
        // make it so angry Observe can only spawn after 6 days
        if (!canAngryObserveSpawn && tick.getLevel().getGameTime() > 24000 * 6) {
            canAngryObserveSpawn = true;
        }
        // reset day starting boolean
        if (tick.getLevel().getGameTime() % 24000 == 0 && tick.getLevel().getGameTime() != 0) {
            startDay = true;
        }
        // 1st day is set to peaceful type
        if (tick.getLevel().getGameTime() == 20) {
            eventType = 0;
            StarlessSavedData.save(tick.getLevel().getServer());
        }
        // start of the day event reset
        if (startDay) {
            eventCount = 0;
            dailyObserveSpawn = true;
            dailyWrongedSpawn = true;
            dailyCitaseSpawn = true;
            dailySeekerSpawn = true;
            dailySmilerSpawn = true;
            eventTypeOld = eventType;
            if (random.nextInt(3) == 0 || random.nextInt(3) == 1) {
                eventType = random.nextInt(4);
                StarlessSavedData.save(tick.getLevel().getServer());
            }
            else if (eventTypeOld != 5) {
                eventType = random.nextInt(2) + 4;
                StarlessSavedData.save(tick.getLevel().getServer());
            }
            else {
                eventType = 0;
                StarlessSavedData.save(tick.getLevel().getServer());
            }
            startDay = false;
        }
        // calling event type method
        fireEventType(tick);

        if (tick.getLevel().getGameTime() % 24000 == 20500) {
            canChat = false;
        }
        terminalReset(tick);
        smilerChatEvent(tick);
        smilerSpawnFromItem(tick);
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

    private static void fireEventType(LevelTickEvent.Post tick) {
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

    private static void peacefulPreset (LevelTickEvent.Post tick) {
        int observePeacefulSpawnTime = 10000;
        int observePeacefulSpawnChance = 10;
        int wrongedSpawnTime = 18000;
        int citaseSpawnTime = 12500;

        spawnPresetEntity(0, tick, wrongedSpawnTime, dailyWrongedSpawn, "wronged");

        spawnPresetEntity(0, tick, citaseSpawnTime, dailyCitaseSpawn, "citase");

        if (canFireNewEvent()) {
            spawnObserve(tick, observePeacefulSpawnTime, observePeacefulSpawnChance, false);
        }
    }

    private static void calmPreset (LevelTickEvent.Post tick) {
        int observeCalmSpawnTime = 7500;
        int observeCalmSpawnChance = 10;
        int wrongedSpawnTime = 18000;
        int citaseSpawnTime = 12500;

        spawnPresetEntity(0, tick, wrongedSpawnTime, dailyWrongedSpawn, "wronged");

        spawnPresetEntity(0, tick, citaseSpawnTime, dailyCitaseSpawn, "citase");

        if (canFireNewEvent()) {
            spawnObserve(tick, observeCalmSpawnTime, observeCalmSpawnChance, false);
        }
    }

    private static void riskyPreset (LevelTickEvent.Post tick) {
        int observeRiskySpawnTime = 7000;
        int observeRiskySpawnChance = 10;
        int wrongedSpawnTime = 18000;
        int seekerSpawnTime = 5000;

        spawnSeeker(15, tick, seekerSpawnTime, "seeker");

        if (random.nextInt(2) == 0) {
            spawnPresetEntity(0, tick, wrongedSpawnTime, dailyWrongedSpawn, "wronged");
        }

        if (canFireNewEvent()) {
            spawnObserve(tick, observeRiskySpawnTime, observeRiskySpawnChance, false);
        }
    }

    private static void dangerousPreset (LevelTickEvent.Post tick) {
        int observeDangerousSpawnTime = 7000;
        int observeDangerousSpawnChance = 5;
        int wrongedSpawnTime = 18000;
        int seekerSpawnTime = 5000;
        int smilerSpawnTime = 11000;
        int smilerSpawnChance = 10;

        spawnSeeker(15, tick, seekerSpawnTime, "seeker");

        if (random.nextInt(2) == 0) {
            spawnPresetEntity(0, tick, wrongedSpawnTime, dailyWrongedSpawn, "wronged");
        }

        if (canFireNewEvent()) {
            spawnObserve(tick, observeDangerousSpawnTime, observeDangerousSpawnChance, false);
            if (canSmilerSpawn && random.nextInt(smilerSpawnChance) == 0 && tick.getLevel().isNight()) {
                spawnSmiler(15, tick, smilerSpawnTime, "smiler");
            }
        }
    }

    private static void hardPreset (LevelTickEvent.Post tick) {
        int observeHardSpawnTime = 7000;
        int observeHardSpawnChance = 5;
        int smilerSpawnTime = 4500;
        int smilerSpawnChance = 2;

        if (canFireNewEvent()) {
            spawnObserve(tick,observeHardSpawnTime, observeHardSpawnChance, canAngryObserveSpawn);
            if (canSmilerSpawn && random.nextInt(smilerSpawnChance) == 0 && tick.getLevel().isNight()) {
                spawnSmiler(15, tick, smilerSpawnTime, "smiler");
            }
        }
    }

    private static void extremePreset (LevelTickEvent.Post tick) {
        int observeExtremeSpawnTime = 7000;
        int observeExtremeSpawnChance = 2;
        int smilerSpawnTime = 4500;

        if (canFireNewEvent()) {
            spawnObserve(tick,observeExtremeSpawnTime, observeExtremeSpawnChance, canAngryObserveSpawn);
            if (canSmilerSpawn && tick.getLevel().isNight()) {
                spawnSmiler(15, tick, smilerSpawnTime, "smiler");
            }
        }
        if (canFireNewEvent() && tick.getLevel().getGameTime() % 22000 == 0 && dailyObserveSpawn) {
            spawnObserve(tick,observeExtremeSpawnTime, observeExtremeSpawnChance, canAngryObserveSpawn);
        }
    }

    private static void spawnPresetEntity(int i, LevelTickEvent.Post tick, int spawnTime, boolean dailyEntitySpawn,
                                          String entityType) {
        if (tick.getLevel().getGameTime() % 24000 == spawnTime && dailyEntitySpawn) {
            LivingEntity entity = entityCreate(tick, entityType);
            if (entity == null) return;
            Player player = tick.getLevel().getServer().getPlayerList().getPlayers().get
                    (tick.getLevel().getRandom().nextInt(tick.getLevel().getServer().getPlayerList().getPlayers().size()));
            if (player.getY() < 35 && !player.level().canSeeSky(player.blockPosition())) return;
            spawnEntity(i, entity, player, tick);

            if (!entityType.equals("wronged")) {
                eventCount++;
            }
            dailyEntitySpawn = false;
            StarlessSavedData.save(tick.getLevel().getServer());
        }
    }

    private static LivingEntity entityCreate (LevelTickEvent.Post tick, String entityType) {
        if (entityType.equals("wronged")) {
            WrongedEntity entity = ModEntities.WRONGED.get().create(tick.getLevel());
            return entity;
        }
        else if (entityType.equals("citase")) {
            CitaseEntity entity = ModEntities.CITASE.get().create(tick.getLevel());
            return entity;
        }
        else if (entityType.equals("seeker")) {
            SeekerEntity entity = ModEntities.SEEKER.get().create(tick.getLevel());
            return entity;
        }
        else if (entityType.equals("smiler")) {
            SmilerEntity entity = ModEntities.SMILER.get().create(tick.getLevel());
            return entity;
        }
        else return null;
    }

    private static void spawnObserve(LevelTickEvent.Post tick, int spawnTime, int spawnChance, boolean isAngry) {
        if ((tick.getLevel().getGameTime() % spawnTime == 0) && tick.getLevel().getRandom().nextInt(spawnChance) == 0 &&
                dailyObserveSpawn) {

            LivingEntity entity = observeCreate(tick, isAngry);
            if (entity == null) return;

            Player player = tick.getLevel().getServer().getPlayerList().getPlayers().get
                    (tick.getLevel().getRandom().nextInt(tick.getLevel().getServer().getPlayerList().getPlayers().size()));
            if (player.getY() < 35 && !player.level().canSeeSky(player.blockPosition())) return;
            if (isAngry) {
                spawnEntity(10, entity, player, tick);
            }
            else {
                spawnEntity(0, entity, player, tick);
            }

            tick.getLevel().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.AMBIENT_CAVE, SoundSource.HOSTILE, 2.9f, 0.85f);
            eventCount++;
            dailyObserveSpawn = false;
            StarlessSavedData.save(tick.getLevel().getServer());
        }
    }

    private static LivingEntity observeCreate(LevelTickEvent.Post tick, boolean isAngry) {
        if (!isAngry) {
            ObserveEntity entity = ModEntities.OBSERVE.get().create(tick.getLevel());
            return entity;
        }
        else {
            ObserveAngryEntity entity = ModEntities.OBSERVE_ANGRY.get().create(tick.getLevel());
            return entity;
        }
    }

    // credits to Chaaze for handling and explaining this particular part for me. used to have different thing that wasn't as good
    private static void spawnEntity(int i, LivingEntity entity, Player player, LevelTickEvent.Post event) {
        double angle = event.getLevel().random.nextDouble() * Math.PI * 2;
        double radius = 15 + event.getLevel().random.nextInt(20) + i;

        double entityX = player.getX() + Math.cos(angle) * radius;
        double entityZ = player.getZ() + Math.sin(angle) * radius;
        entity.setPos(entityX, event.getLevel().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (int)entityX, (int)entityZ) + 1, entityZ);
        event.getLevel().addFreshEntity(entity);
    }

    public static void terminalReset(LevelTickEvent.Post tick) {
        long dayTime = tick.getLevel().getDayTime() % 24000;

        if (lastDayTime != -1 && dayTime < lastDayTime) {
            dailyTerminalUsage = true;
            StarlessSavedData.save(tick.getLevel().getServer());
        }

        lastDayTime = dayTime;
    }

    public static void smilerChatEvent(LevelTickEvent.Post tick) {
        if (!canSmilerSpawn && tick.getLevel().getGameTime() % (24000 * 6) == 0) {
            smilerSpeech(tick);
        }
    }

    public static void smilerSpeech(LevelTickEvent.Post tick) {
        if (tick.getLevel().getServer().getPlayerList().getPlayers().isEmpty()) return;
        tick.getLevel().getServer().getPlayerList().broadcastSystemMessage
                (Component.literal("<UNKNOWN_SOURCE> Did you know you could get free diamonds from " +
                        "just one diamond? Just put a diamond, a redstone dust and an ender pearl on " +
                        "a crafting grid, use it and you will get FIVE free diamonds! Trust me, that works wonders, " +
                        "though you can only use it once."), false);
    }

    public static void smilerSpawnFromItem (LevelTickEvent.Post tick) {
        if (specialSmilerSpawn) {
            spawnEntityNoRequirements(20, tick, "smiler");
            specialSmilerSpawn = false;
        }
    }

    private static void spawnEntityNoRequirements(int i, LevelTickEvent.Post tick, String entityType) {
        LivingEntity entity = entityCreate(tick, entityType);
        if (entity == null) return;
        Player player = tick.getLevel().getServer().getPlayerList().getPlayers().get
                (tick.getLevel().getRandom().nextInt(tick.getLevel().getServer().getPlayerList().getPlayers().size()));
        spawnEntity(i, entity, player, tick);
    }

    private static void spawnSeeker(int i, LevelTickEvent.Post tick, int spawnTime, String entityType) {
        if (tick.getLevel().getGameTime() % 24000 == spawnTime && dailySeekerSpawn) {
            LivingEntity entity = entityCreate(tick, entityType);
            if (entity == null) return;
            Player player = tick.getLevel().getServer().getPlayerList().getPlayers().get
                    (tick.getLevel().getRandom().nextInt(tick.getLevel().getServer().getPlayerList().getPlayers().size()));
            if (player.getY() < 35 && !player.level().canSeeSky(player.blockPosition())) return;
            spawnEntity(i, entity, player, tick);

            if (!entityType.equals("wronged")) {
                eventCount++;
            }
            dailySeekerSpawn = false;
            StarlessSavedData.save(tick.getLevel().getServer());
        }
    }

    private static void spawnSmiler(int i, LevelTickEvent.Post tick, int spawnTime, String entityType) {
        if (tick.getLevel().getGameTime() % spawnTime == 0 && dailySmilerSpawn) {
            LivingEntity entity = entityCreate(tick, entityType);
            if (entity == null) return;
            Player player = tick.getLevel().getServer().getPlayerList().getPlayers().get
                    (tick.getLevel().getRandom().nextInt(tick.getLevel().getServer().getPlayerList().getPlayers().size()));
            if (player.getY() < 35 && !player.level().canSeeSky(player.blockPosition())) return;
            spawnEntity(i, entity, player, tick);

            if (!entityType.equals("wronged")) {
                eventCount++;
            }
            dailySmilerSpawn = false;
            StarlessSavedData.save(tick.getLevel().getServer());
        }
    }

}
