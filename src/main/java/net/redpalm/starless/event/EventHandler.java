package net.redpalm.starless.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.custom.ObserveEntity;
import net.redpalm.starless.entity.custom.WrongedEntity;

import java.util.Random;

import static net.redpalm.starless.misc.WrongedItemList.wrongedItemList;

@Mod.EventBusSubscriber(modid = Starless.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler extends Event {
    static Random random = new Random();
    static int randomIndex;
    static long lastSpawnWronged = 0;
    static long lastSpawnObserve = 0;
    static int clockWronged = 0;

    // Attempt to spawn Observe after some amount of ticks with 10% chance
    @SubscribeEvent
    public static void spawnObserve(TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (!(tick.level instanceof ServerLevel)) return;
        if (tick.level.isClientSide) return;
        if (tick.level.dimension() != Level.OVERWORLD) return;

        if (tick.level.getServer().getPlayerList().getPlayers().isEmpty()) return;

        int spawnTime = 8000;
        int spawnChance = 10;

        if (tick.level.getDayTime() % spawnTime == 0 && random.nextInt(spawnChance) == 0 &&
        tick.level.getDayTime() != lastSpawnObserve) {

            lastSpawnObserve = tick.level.getDayTime();
            ObserveEntity entity = ModEntities.OBSERVE.get().create(tick.level);
            if (entity == null) return;

            Player player = tick.level.getServer().getPlayerList().getPlayers().get
                    (random.nextInt(tick.level.getServer().getPlayerList().getPlayers().size()));

            spawnEntity(10, 10, entity, player, tick);

            tick.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.AMBIENT_CAVE.get(), SoundSource.HOSTILE, 2f, 0.85f);
        }
    }

    // Clock for Wronged
    @SubscribeEvent
    public static void clockWronged(TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (!(tick.level instanceof ServerLevel)) return;
        if (tick.level.isClientSide) return;
        if (tick.level.dimension() != Level.OVERWORLD) return;

        clockWronged++;
        if ((tick.level.getDayTime() % 24000) == 0) {
            clockWronged = 0;
        }
    }

    // Spawn Wronged at Midnight
    @SubscribeEvent
    public static void spawnWronged(TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (!(tick.level instanceof ServerLevel)) return;
        if (tick.level.isClientSide) return;
        if (tick.level.dimension() != Level.OVERWORLD) return;

        if (tick.level.getServer().getPlayerList().getPlayers().isEmpty()) return;

        int chanceSpawn = 2;
        int chancePhrase = 3;
        int Midnight = 18000;

        if (((clockWronged % Midnight) == 0 || (tick.level.getDayTime() == Midnight))  &&
                random.nextInt(chanceSpawn) == 0 && tick.level.getDayTime() != lastSpawnWronged) {

            lastSpawnWronged = tick.level.getDayTime();
            WrongedEntity entity = ModEntities.WRONGED.get().create(tick.level);
            if (entity == null) return;

            Player player = tick.level.getServer().getPlayerList().getPlayers().get
                    (random.nextInt(tick.level.getServer().getPlayerList().getPlayers().size()));

            spawnEntity(10, 10, entity, player, tick);

            tick.level.getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("<Wrong.ed> Hello."), false);

            if (random.nextInt(chancePhrase) == 0) {
                tick.level.getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> Can I give you something? " +
                                "I hope it's okay."), false);
            }
            else if (random.nextInt(chancePhrase) == 1) {
                tick.level.getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> I can give you an item. " +
                                "Not sure if it's as useful as I think it is."), false);
            }
            else {
                tick.level.getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> Can you take this item from me, if it's okay? " +
                                "It gives me painful memories, but I don't want it to go to waste..."),
                                false);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerWake(SleepFinishedTimeEvent event) {
        clockWronged = 0;
    }

    public static void spawnEntity(int extraX, int extraZ, LivingEntity entity,
                                   Player player, TickEvent.LevelTickEvent event) {
        int entityX;
        int entityZ;
        int chance = 2;
        if (random.nextInt(chance) == 0) {
            entityX = (int) player.getX() + random.nextInt(15) + extraX;
            entityZ = (int) player.getZ() + random.nextInt(15) + extraZ;
        }
        else {
            entityX = (int) player.getX() - random.nextInt(15) - extraX;
            entityZ = (int) player.getZ() - random.nextInt(15) - extraZ;
        }
        entity.setPos(entityX, event.level.getHeight(Heightmap.Types.WORLD_SURFACE,
                entityX, entityZ), entityZ);
        event.level.addFreshEntity(entity);
    }

    @SubscribeEvent
    public static void interactWronged(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide) return;
        Player player = event.getEntity();
        if (event.getTarget() instanceof WrongedEntity && event.getHand() == InteractionHand.MAIN_HAND) {
            if (WrongedEntity.canGiveItem == true) {
                randomIndex = random.nextInt(wrongedItemList.size());
                ItemStack item = new ItemStack(wrongedItemList.get(randomIndex), 1);
                player.addItem(item);
                player.sendSystemMessage(Component.literal("<Wrong.ed> I hope you will find use for this."));
                WrongedEntity.canGiveItem = false;
            }
            else {
                player.sendSystemMessage(Component.literal("<Wrong.ed> Sorry, that's all I have for now."));
            }
        }
    }
}

