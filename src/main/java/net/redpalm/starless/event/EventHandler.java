package net.redpalm.starless.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.custom.ObserveEntity;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Starless.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler extends Event {
    static Random random = new Random();
    // Playing cave noise for Observe upon him spawning
    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ObserveEntity) {
            event.getEntity().level().playSound(null, event.getEntity().getX(), event.getEntity().getY(),
                    event.getEntity().getZ(), SoundEvents.AMBIENT_CAVE.get(), SoundSource.PLAYERS, 1, 1);
        }
    }
    // Attempt to spawn Observe after some amount of ticks with 10% chance
    @SubscribeEvent
    public static void levelTickEvent (TickEvent.LevelTickEvent tick) {
        if (tick.phase == TickEvent.Phase.END) {
        if (!tick.level.isClientSide && tick.level.getDayTime() % 200 == 0 && random.nextInt(10) == 0) {
            ObserveEntity entity = ModEntities.OBSERVE.get().create(tick.level);
            if (entity != null) {
                entity.setPos(0, 80, 0);
                tick.level.addFreshEntity(entity);
                }
            }
        }
    }
}
