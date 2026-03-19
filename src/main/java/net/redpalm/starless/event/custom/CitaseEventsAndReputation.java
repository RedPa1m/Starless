package net.redpalm.starless.event.custom;

import jdk.jfr.Event;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CitaseEventsAndReputation extends Event {
    public static int citaseReputation = 50;
    public static boolean isFamiliar = false;
    public static boolean reputationIsPerfect = false;
    public static boolean reputationIsHorrible = false;

    private static void reputationCheck () {
        if (citaseReputation > 200) {
            reputationIsPerfect = true;
            citaseReputation = 200;
        }
        if (citaseReputation < -10) {
            reputationIsHorrible = true;
            citaseReputation = -10;
        }
        if (citaseReputation > -10) {
            reputationIsHorrible = false;
        }
    }

    @SubscribeEvent
    public static void worldTick (TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (!(tick.level instanceof ServerLevel)) return;
        if (tick.level.isClientSide) return;

        reputationCheck();
    }
}
