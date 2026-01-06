package net.redpalm.starless.event;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.custom.ObserveEntity;
import net.redpalm.starless.entity.custom.WrongedEntity;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Starless.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler extends Event {
    static Random random = new Random();
    static ServerLevel serverlevel;

    // Attempt to spawn Observe after some amount of ticks with 10% chance
    @SubscribeEvent
    public static void spawnObserve(TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (tick.level.isClientSide) return;
        if (tick.level.dimension() != Level.OVERWORLD) return;

        int spawnTime = 8000;
        int spawnChance = 10;

        if (tick.level.getDayTime() % spawnTime == 0 && random.nextInt(spawnChance) == 0) {
            ObserveEntity entity = ModEntities.OBSERVE.get().create(tick.level);
            if (entity == null) return;

            if (serverlevel != null) {
                Player player = serverlevel.getRandomPlayer();
                if (player == null) return;
                spawnEntity(5, 5, entity, player, tick);
            } else {
                Player player = Minecraft.getInstance().player;
                if (player == null) return;
                spawnEntity(5, 5, entity, player, tick);
            }
            tick.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.AMBIENT_CAVE.get(), SoundSource.HOSTILE, 1.5f, 0.85f);
        }
    }

    // Spawn Wronged at Midnight
    @SubscribeEvent
    public static void spawnWronged(TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (tick.level.isClientSide) return;
        if (tick.level.dimension() != Level.OVERWORLD) return;

        int Midnight = 18000;
        int chance = 2;

        if (tick.level.getDayTime() == Midnight && random.nextInt(chance) == 0) {
            WrongedEntity entity = ModEntities.WRONGED.get().create(tick.level);
            if (entity == null) return;

            if (serverlevel != null) {
                Player player = serverlevel.getRandomPlayer();
                if (player == null) return;
                spawnEntity(10, 10, entity, player, tick);
            } else {
                Player player = Minecraft.getInstance().player;
                if (player == null) return;
                spawnEntity(10, 10, entity, player, tick);
            }
            tick.level.getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("<Wrong.ed> Hello."), false);
            tick.level.getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("<Wrong.ed> If you don't mind, I can give you something. " +
                            "I'm not sure if it's useful."), false);
        }
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

    public static void onEntityDespawn (EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof WrongedEntity) {
            event.getLevel().getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("<Wrong.ed> Goodbye."), false);
        }
    }
}

