package net.redpalm.starless.event.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WrongedChatEvent extends Event {
    public static String wrongedResponse;
    static int wrongedTickTimer = 0;

    @SubscribeEvent
    public static void wrongedSendMessage(TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (!(tick.level instanceof ServerLevel)) return;
        if (tick.level.isClientSide) return;

        wrongedTickTimer++;
        if (wrongedTickTimer == 80) {
            tick.level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                    (wrongedResponse), false);
            wrongedTickTimer = 0;
            MinecraftForge.EVENT_BUS.unregister(WrongedChatEvent.class);
        }
    }
}
