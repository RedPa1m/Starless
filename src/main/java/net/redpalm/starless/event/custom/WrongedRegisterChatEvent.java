package net.redpalm.starless.event.custom;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Locale;

public class WrongedRegisterChatEvent extends Event {

    @SubscribeEvent
    public static void wrongedChatEvent(ClientChatReceivedEvent event) {
        eventMethod(event, "friends", "<Wrong.ed> Of course.");
        eventMethod(event, "can i help", "<Wrong.ed> You don't have to, really.");
        eventMethod(event, "do you like", "<Wrong.ed> " +
                "Sure.");
        eventMethod(event, "void", "<Wrong.ed> ...");
        eventMethod(event, "observe", "<Wrong.ed> " +
                "Get away from him when his eyes are wide open.");
        eventMethod(event, "no_light", "<Wrong.ed> I think I've seen him around...");
        eventMethod(event, "fuck you", "<Wrong.ed> ...Do you really have to be rude?");
        eventMethod(event, "skibidi toilet", "<Wrong.ed> " +
                "Don't say this to other entities, please...");
        eventMethod(event, "i love you", "<Wrong.ed> " +
                "Thank you for that. But don't get too used to me.");
        eventMethod(event, "thank you", "<Wrong.ed> You're always welcome.");
        eventMethod(event, "you look emo", "<Wrong.ed> ...Is this a good thing?");
        eventMethod(event, "lesser soul", "<Wrong.ed> I am one of them.");
        eventMethod(event, "why do you help me", "<Wrong.ed> " +
                "Because I don't want you to die like others.");
        eventMethod(event, "where do you get items", "<Wrong.ed> " +
                "Most of them are from people that are no longer with us.");
        eventMethod(event, "meep", "<Wrong.ed> It's silly.");
        eventMethod(event, "seeker", "<Wrong.ed> He likes to steal food, but he's mostly harmless.");
        eventMethod(event, "i'm sorry", "<Wrong.ed> It's okay.");
    }

    public static void eventMethod(ClientChatReceivedEvent event, String question, String answer) {
        if (event.getMessage().toString().toLowerCase(Locale.ROOT).contains(question)) {
            WrongedChatEvent.wrongedResponse = answer;
            MinecraftForge.EVENT_BUS.register(WrongedChatEvent.class);
        }
    }
}

