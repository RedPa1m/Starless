package net.redpalm.starless.event.custom;

import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Locale;

import static net.redpalm.starless.entity.custom.WrongedEntity.canChat;

public class WrongedRegisterChatEvent extends Event {
    public static boolean fireAnswer = false;

    @SubscribeEvent
    public static void wrongedChatEvent(ServerChatEvent event) {
        if (canChat == true) {
            eventMethod(event, "friends", "<Wrong.ed> Of course.");
            eventMethod(event, "can i help", "<Wrong.ed> You don't have to, really.");
            eventMethod(event, "do you like", "<Wrong.ed> Sure.");
            eventMethod(event, "void", "<Wrong.ed> ...");
            eventMethod(event, "observe", "<Wrong.ed> " +
                    "You should avoid him when his eyes are wide open.");
            eventMethod(event, "no_light", "<Wrong.ed> I think I've seen him around...");
            eventMethod(event, "fuck you", "<Wrong.ed> ...Do you really have to be rude?");
            eventMethod(event, "skibidi toilet", "<Wrong.ed> " +
                    "Don't say this to other entities, please...");
            eventMethod(event, "i love you", "<Wrong.ed> " +
                    "I'm happy to hear that. But don't get too used to me.");
            eventMethod(event, "thank you", "<Wrong.ed> You're always welcome.");
            eventMethod(event, "you look emo", "<Wrong.ed> ...Is this a good thing?");
            eventMethod(event, "lesser soul", "<Wrong.ed> I am one of them.");
            eventMethod(event, "why do you help me", "<Wrong.ed> " +
                    "Because I don't want you to die like others.");
            eventMethod(event, "items", "<Wrong.ed> " +
                    "Most of them are from people that are no longer with us.");
            eventMethod(event, "meep", "<Wrong.ed> It's silly.");
            eventMethod(event, "seeker", "<Wrong.ed> He likes to steal food, but he's mostly harmless.");
            eventMethod(event, "sorry", "<Wrong.ed> It's okay.");
            eventMethod(event, "help me", "<Wrong.ed> I will try my best.");
            eventMethod(event, "freedom", "<Wrong.ed> I wish...");
            eventMethod(event, "nothing_left", "<Wrong.ed> Lovely guy.");
            eventMethod(event, "alone", "<Wrong.ed> Lost and controlled.");
            eventMethod(event, "finale", "???");
            eventMethod(event, "inspector", "<...> <3");
            eventMethod(event, "nothingisreal", "<Wrong.ed> What do you mean?");
            eventMethod(event, "billy", "<Wrong.ed> Powerful.");
            eventMethod(event, "shattered", "<Wrong.ed> Be alerted.");
            eventMethod(event, "administrator", "<Wrong.ed> He's watching.");
            eventMethod(event, "i'm scared", "<Wrong.ed> I am here for now.");
            eventMethod(event, "hello", "<Wrong.ed> Hello.");
            eventMethod(event, "hi", "<Wrong.ed> Hello.");
            eventMethod(event, "are you scared", "<Wrong.ed> Maybe.");
            eventMethod(event, "how are you", "<Wrong.ed> I feel numb.");
            eventMethod(event, "separation", "<Wrong.ed> Defeated by her own pride.");
            eventMethod(event, "daybreak", "<Wrong.ed> Waits patiently until the Void is gone.");
            eventMethod(event, "transitioned", "<Wrong.ed> Corrupted constellation.");
            eventMethod(event, "cassiopea", "<Wrong.ed> Corrupted constellation.");
            eventMethod(event, "cassie", "<Wrong.ed> Corrupted constellation.");
            eventMethod(event, "starry", "<Wrong.ed> Friendly little guy.");
            eventMethod(event, "red palm", "<...> Hey, no breaking the fourth wall!");
            eventMethod(event, "what happened", "<Wrong.ed> I forgot so much.");
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
}

