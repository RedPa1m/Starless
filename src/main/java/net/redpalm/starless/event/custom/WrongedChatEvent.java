package net.redpalm.starless.event.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static net.redpalm.starless.Starless.queueServerWork;
import static net.redpalm.starless.event.custom.WrongedRegisterChatEvent.fireAnswer;

public class WrongedChatEvent extends Event {
    public static String wrongedResponse;

    @SubscribeEvent
    public static void wrongedSendMessage(TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (!(tick.level instanceof ServerLevel)) return;
        if (tick.level.isClientSide) return;

        if (fireAnswer == true) {
            queueServerWork(40, () -> {
                tick.level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                        (wrongedResponse), false);
            });
            fireAnswer = false;
            }
        }
    }
