package net.redpalm.starless.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.redpalm.starless.Starless;

import java.util.Locale;

import static net.redpalm.starless.Starless.queueServerWork;
import static net.redpalm.starless.entity.custom.WrongedEntity.callbackWronged;

@EventBusSubscriber(modid = Starless.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EntityCallbackHandler {

    @SubscribeEvent
    public static void registerCallback (ServerChatEvent event) {
        if (event.getMessage().toString().toLowerCase(Locale.ROOT).contains("where are you") && event.getPlayer() != null) {
            callbackWronged = true;
            queueServerWork(200, () -> {
                callbackWronged = false;
            });
        }
    }
}
