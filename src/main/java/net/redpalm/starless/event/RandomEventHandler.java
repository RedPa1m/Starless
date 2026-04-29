package net.redpalm.starless.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.ObserveAngryEntity;

import java.util.Random;

import static net.redpalm.starless.event.EntitySpawnEventHandler.eventType;
import static net.redpalm.starless.misc.CitaseItemList.citaseItemList;

@EventBusSubscriber(modid = Starless.MODID, bus = EventBusSubscriber.Bus.GAME)
public class RandomEventHandler extends Event {
    static Random random = new Random();
    private static boolean noLightSpeechStart = false;

    @SubscribeEvent
    public static void worldTick(LevelTickEvent.Post tick) {
        if (!(tick.getLevel() instanceof ServerLevel)) return;
        if (tick.getLevel().isClientSide) return;
        if (tick.getLevel().dimension() != Level.OVERWORLD) return;
        if (tick.getLevel().getServer().getPlayerList().getPlayers().isEmpty()) return;

        if (tick.getLevel().getGameTime() == 24000 * 3) {
            tick.getLevel().getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("Who are you?"), false);
        }
        if (tick.getLevel().getGameTime() == 24000 * 6) {
            tick.getLevel().getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("Interesting.").withStyle(ChatFormatting.DARK_RED), false);
        }
        if ((eventType == 3 || eventType == 4) &&
                random.nextInt(8) == 0) {
            if (tick.getLevel().getGameTime() % 24000 == 13000) {
                int x = random.nextInt(4);
                switch (x) {
                    case 0:
                        saySpeech("§kMama mia, this terminal is shit!!!!", tick.getLevel());
                        break;
                    case 1:
                        saySpeech(".... . .-.. .-.. --- -.-.-- / -.. --- / -.-- --- ..- / .... . .- .-. / -- . ..--.. / --- .... / -- -.-- / --. --- -.. .-.-.-", tick.getLevel());
                        break;
                        default:
                tick.getLevel().getServer().getPlayerList().broadcastSystemMessage(Component.literal
                                ("<UNKNOWN_SOURCE> Hello? Does anybody hear me? Crap, this thing... " +
                                        "§kDoesn't freaking work!"),
                        false);
            }
        }
        }
        if (eventType == 5 && random.nextInt(2) == 0 && tick.getLevel().getGameTime() % 24000
        == 13000) {
            citaseRandomEvent(tick);
        }
        noLightSpeech(tick);
    }

    private static void saySpeech (String string, Level level) {
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                        ("<UNKNOWN_SOURCE> " + string),
                false);
    }

    private static void citaseRandomEvent(LevelTickEvent.Post tick) {
            int size = tick.getLevel().getServer().getPlayerList().getPlayers().size();
            tick.getLevel().getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("<UNKNOWN_SOURCE> " +
                            "Hello? Oh, this thing barely works. Do you hear me? You doing good over here? " +
                                    "Take this while I'm at it! Might help you just a bit. It's crap for me, but" +
                                    " hey, may be useful for you!"),
                            false);
                int randomIndex = random.nextInt(citaseItemList.size());
                ItemStack item = new ItemStack(citaseItemList.get(randomIndex));
                for (int i = 0; i < size; i++) {
                    Player player = tick.getLevel().getServer().getPlayerList().getPlayers().get(i);
                    player.addItem(item);
            }
    }

    @SubscribeEvent
    public static void noLightEventHeal (LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof Player player && event.getEntity().getLastAttacker() instanceof Monster &&
        random.nextInt(50) == 0 && !(event.getEntity().getLastAttacker() instanceof ObserveAngryEntity)
                && eventType == 5) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100));
            noLightSpeechStart = true;
        }
    }

    private static void noLightSpeech (LevelTickEvent.Post tick) {
        if (noLightSpeechStart) {
            tick.getLevel().getServer().getPlayerList().broadcastSystemMessage
                    (Component.literal("<UNKNOWN_SOURCE> Hello? I saw you struggle here. I hope that will help a little bit."), false);
            noLightSpeechStart = false;
        }
    }
}
