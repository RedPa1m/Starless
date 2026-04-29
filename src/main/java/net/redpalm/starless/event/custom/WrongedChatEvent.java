package net.redpalm.starless.event.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import static net.redpalm.starless.Starless.queueServerWork;
import static net.redpalm.starless.event.custom.WrongedRegisterChatEvent.fireAnswer;

public class WrongedChatEvent extends Event {
    public static String wrongedResponse;

    @SubscribeEvent
    public static void wrongedSendMessage(LevelTickEvent.Post tick) {
        if (!(tick.getLevel() instanceof ServerLevel)) return;
        if (tick.getLevel().isClientSide) return;

        if (fireAnswer == true) {
            queueServerWork(40, () -> {
                tick.getLevel().getServer().getPlayerList().broadcastSystemMessage(Component.literal
                        (wrongedResponse), false);
            });
            fireAnswer = false;
            }
        }
    }
