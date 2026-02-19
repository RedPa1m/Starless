package net.redpalm.starless.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.WrongedEntity;

import java.util.Random;

import static net.redpalm.starless.misc.WrongedItemList.wrongedItemList;

@Mod.EventBusSubscriber(modid = Starless.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler extends Event {
    static int randomIndex;
    static Random random = new Random();

    @SubscribeEvent
    public static void interactWronged(PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide) return;
        Player player = event.getEntity();
        if (event.getTarget() instanceof WrongedEntity && event.getHand() == InteractionHand.MAIN_HAND) {
            if (((WrongedEntity) event.getTarget()).getCanGiveItem() == true) {
                randomIndex = random.nextInt(wrongedItemList.size());
                ItemStack item = new ItemStack(wrongedItemList.get(randomIndex), 1);
                player.addItem(item);
                player.sendSystemMessage(Component.literal("<Wrong.ed> I hope you will find use for this."));
                ((WrongedEntity) event.getTarget()).setCanGiveItem(false);
            }
            else {
                player.sendSystemMessage(Component.literal("<Wrong.ed> Sorry, that's all I have for now."));
            }
        }
    }
}

