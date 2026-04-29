package net.redpalm.starless.event.custom;

import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.Locale;

import static net.redpalm.starless.entity.custom.WrongedEntity.canChat;

public class WrongedRegisterChatEvent extends Event {
    public static boolean fireAnswer = false;

    @SubscribeEvent
    public static void wrongedChatEvent(ServerChatEvent event) {
        if (canChat == true) {
            eventMethod(event, "friend", "<Wrong.ed> Of course.");
            eventMethod(event, "can i help", "<Wrong.ed> You don't have to, really.");
            eventMethod(event, "do you like", "<Wrong.ed> Sure.");
            eventMethod(event, "observe", "<Wrong.ed> " +
                    "You should avoid him when his eyes are wide open.");
            eventMethod(event, "no_light", "<Wrong.ed> I think I've seen him around...");
            eventMethod(event, "fuck you", "<Wrong.ed> ...Do you really have to be rude?");
            eventMethod(event, "love you", "<Wrong.ed> " +
                    "I'm happy to hear that. But don't get too used to me.");
            eventMethod(event, "thank you", "<Wrong.ed> You're always welcome.");
            eventMethod(event, "you look emo", "<Wrong.ed> ...Is this a good thing?");
            eventMethod(event, "why do you help me", "<Wrong.ed> " +
                    "Because I don't want you to die like others.");
            eventMethod(event, "items", "<Wrong.ed> " +
                    "Most of them are from people that are no longer with us.");
            eventMethod(event, "meep", "<Wrong.ed> It's silly.");
            eventMethod(event, "seeker", "<Wrong.ed> He likes to steal food, but he's harmless.");
            eventMethod(event, "sorry", "<Wrong.ed> It's okay.");
            eventMethod(event, "help me", "<Wrong.ed> I will try my best.");
            eventMethod(event, "inspector", "<...> <3");
            eventMethod(event, "i'm scared", "<Wrong.ed> I am here for now.");
            eventMethod(event, "are you scared", "<Wrong.ed> Maybe.");
            eventMethod(event, "how are you", "<Wrong.ed> I feel numb.");
            eventMethod(event, "separation", "<Wrong.ed> Defeated by her own pride.");
            eventMethod(event, "daybreak", "<Wrong.ed> Creature born from a star similar to our Sun.");
            eventMethod(event, "transitioned", "<Wrong.ed> Corrupted constellation.");
            eventMethod(event, "cassiopea", "<Wrong.ed> Corrupted constellation.");
            eventMethod(event, "cassie", "<Wrong.ed> Corrupted constellation.");
            eventMethod(event, "starry", "<Wrong.ed> Friendly little guy.");
            eventMethod(event, "red palm", "<...> Hey, no breaking the fourth wall!");
            eventMethod(event, "what happened", "<Wrong.ed> I forgot so much. " +
                    "A lot of time have passed.");
            eventMethodHello(event, "<Wrong.ed> Hello.");
            eventMethod(event, "citase", "<Wrong.ed> She seems to be looking for help and food. " +
                    "But mainly food.");
            eventMethod(event, "fire servant", "<Wrong.ed> They resemble creatures made by a " +
                    "certain entity, but they're slightly different from the original. They act like Allays.");
            eventMethod(event, "terminal", "<Wrong.ed> Used to be able to get a message from a" +
                    " random entity about once a day. Resets every start of the day.");
            eventMethod(event, "who are you", "<Wrong.ed> ...I myself don't really know to be fair. " +
                    "A creature I guess.");
            eventMethod(event, "chicken jockey", "<Wrong.ed> ...Sure");
            eventMethod(event, "corrupted lapis", "<Wrong.ed> Post-Ender Dragon item to craft " +
                    "some useful stuff.");
            eventMethod(event, "fiery star", "<Wrong.ed> Post-Ender Dragon item that can summon one " +
                    "Fire Servant.");
            eventMethod(event, "i like you", "<Wrong.ed> " +
                    "I'm happy to hear that. But don't get too used to me.");
            eventMethod(event, "you're beautiful", "<Wrong.ed> Thank you. I appreciate.");
            eventMethod(event, "you're cool", "<Wrong.ed> Thank you. I appreciate.");
        }
    }

    public static void eventMethod(ServerChatEvent event, String question, String answer) {
        if (event.getMessage().toString().toLowerCase(Locale.ROOT).contains(question) && event.getPlayer() != null) {
            WrongedChatEvent.wrongedResponse = answer;
            if (canChat == true) {
                fireAnswer = true;
            }
        }
    }
    public static void eventMethodHello(ServerChatEvent event, String answer) {
        if ((event.getMessage().toString().toLowerCase(Locale.ROOT).contains("hi") ||
                event.getMessage().toString().toLowerCase(Locale.ROOT).contains("hello"))
                && event.getPlayer() != null) {
            WrongedChatEvent.wrongedResponse = answer;
            if (canChat == true) {
                fireAnswer = true;
            }
        }
    }
}

