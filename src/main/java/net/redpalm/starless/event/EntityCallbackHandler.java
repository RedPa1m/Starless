package net.redpalm.starless.event;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;

import static net.redpalm.starless.Starless.queueServerWork;
import static net.redpalm.starless.entity.custom.WrongedEntity.callbackWronged;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = Starless.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
