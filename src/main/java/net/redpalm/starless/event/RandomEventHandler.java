package net.redpalm.starless.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;

import java.util.Random;

import static net.redpalm.starless.event.custom.CitaseEventsAndReputation.isFamiliar;
import static net.redpalm.starless.misc.CitaseItemList.citaseItemList;

@Mod.EventBusSubscriber(modid = Starless.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RandomEventHandler extends Event {
    static Random random = new Random();
    private static boolean triedToCalculateRandomChance = false;

    @SubscribeEvent
    public static void worldTick(TickEvent.LevelTickEvent tick) {
        if (tick.phase != TickEvent.Phase.END) return;
        if (!(tick.level instanceof ServerLevel)) return;
        if (tick.level.isClientSide) return;
        if (tick.level.dimension() != Level.OVERWORLD) return;
        if (tick.level.getServer().getPlayerList().getPlayers().isEmpty()) return;

        if (tick.level.getGameTime() == 24000 * 3) {
            tick.level.getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("Who are you?"), false);
        }
        if (tick.level.getGameTime() == 24000 * 6) {
            tick.level.getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("Interesting.").withStyle(ChatFormatting.DARK_RED), false);
        }
        citaseRandomEvent(tick);
        if ((EntitySpawnEventHandler.eventType == 3 || EntitySpawnEventHandler.eventType == 4) &&
                random.nextInt(10) == 0) {
                if (tick.level.getGameTime() % 24000 == 12000) {
                    tick.level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                                    (isFamiliarString() + "Hello? Does anybody hear me? Crap, this thing..."),
                            false);
                }
                if (tick.level.getGameTime() % 24000 == 12040) {
                    tick.level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                            (isFamiliarString() + "Doesn't freaking work!!!").withStyle(ChatFormatting.OBFUSCATED),
                            false);
                }
        }
    }

    private static String isFamiliarString () {
        if (isFamiliar) return "<Citase> ";
        else return "<??????> ";
    }

    private static void citaseRandomEvent(TickEvent.LevelTickEvent tick) {
        if (EntitySpawnEventHandler.eventType == 5 && !triedToCalculateRandomChance && random.nextInt(2) == 0) {
            long currentTick = tick.level.getGameTime() % 24000;
            int size = tick.level.getServer().getPlayerList().getPlayers().size();
            tick.level.getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal(isFamiliarString() +
                            "Hello? Oh, this thing barely works. Do you hear me? You doing good over here?"),
                            false);
            if (tick.level.getGameTime() % 24000 == currentTick + 45) {
                tick.level.getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal(isFamiliarString() +
                                        "Take this while I'm at it! Might help you just a bit. It's crap for me, but" +
                                        " hey, may be useful for you!"),
                                false);
                int randomIndex = random.nextInt(citaseItemList.size());
                ItemStack item = new ItemStack(citaseItemList.get(randomIndex));
                for (int i = 0; i < size; i++) {
                    Player player = tick.level.getServer().getPlayerList().getPlayers().get(i);
                    player.addItem(item);
                }
            }
            triedToCalculateRandomChance = true;
        }
    }


}
