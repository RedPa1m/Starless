package net.redpalm.starless.event.custom;

public class CitaseEventsAndReputation {
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
}
