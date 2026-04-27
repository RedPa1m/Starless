package net.redpalm.starless.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.custom.*;

@Mod.EventBusSubscriber(modid = Starless.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.OBSERVE.get(), ObserveEntity.createAttributes().build());
        event.put(ModEntities.WRONGED.get(), WrongedEntity.createAttributes().build());
        event.put(ModEntities.OBSERVE_ANGRY.get(), ObserveAngryEntity.createAttributes().build());
        event.put(ModEntities.FIRE_SERVANT.get(), FireServantEntity.createAttributes().build());
        event.put(ModEntities.CITASE.get(), CitaseEntity.createAttributes().build());
        event.put(ModEntities.SEEKER.get(), SeekerEntity.createAttributes().build());
    }
}
