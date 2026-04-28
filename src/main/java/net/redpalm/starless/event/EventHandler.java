package net.redpalm.starless.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.custom.*;
import net.redpalm.starless.item.ModItems;

import java.util.Random;

import static net.redpalm.starless.event.custom.CitaseEventsAndReputation.isFamiliar;
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

    @SubscribeEvent
    public static void giveFoodCitase (PlayerInteractEvent.EntityInteract event) {
        if (event.getLevel().isClientSide) return;
        Player player = event.getEntity();
        if (event.getTarget() instanceof CitaseEntity && event.getHand() == InteractionHand.MAIN_HAND) {
            if (event.getItemStack().isEdible() && (
                    (event.getItemStack().getItem() != Items.ROTTEN_FLESH) &&
                    (event.getItemStack().getItem() != Items.SPIDER_EYE) &&
                    (event.getItemStack().getItem() != Items.SUSPICIOUS_STEW) &&
                    (event.getItemStack().getItem() != Items.POISONOUS_POTATO) &&
                    (event.getItemStack().getItem() != Items.PUFFERFISH))) {
                if (((CitaseEntity) event.getTarget()).getCanAcceptFood()) {
                    if (!event.getEntity().isCreative()) {
                        event.getItemStack().shrink(1);
                    }
                    ((CitaseEntity) event.getTarget()).setCanAcceptFood(false);
                    ItemStack itemStack = event.getItemStack().copyWithCount(1);
                    ((CitaseEntity) event.getTarget()).setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                        switch (random.nextInt(3)) {
                            case 0:
                                citaseTalk(event, "Ahh, thank you so much! This is so nice.");
                                break;
                            case 1:
                                citaseTalk(event, "Amazing! I'm very-very-very grateful, thank you!");
                                break;
                            case 2:
                                citaseTalk(event, "Food!! Thank you, arigato, spasibo, danke-something... " +
                                        "You get what I mean! Appreciate a lot!!");
                                break;
                    }
                }
                else {
                    player.sendSystemMessage(Component.literal(isFamiliarString() + "This will be enough for me " +
                            "for now, thank you for offering though!"));
                }
            }
        }
    }

    private static void citaseTalk (PlayerInteractEvent.EntityInteract event, String speech) {
        if (event.getLevel().isClientSide) return;
        if (event.getLevel().getServer().getPlayerList().getPlayers().isEmpty()) return;
        event.getLevel().getServer().getPlayerList().broadcastSystemMessage
                (Component.literal(isFamiliarString() + speech), false);
    }

    private static String isFamiliarString () {
        if (isFamiliar) return "<Citase> ";
        else return "<??????> ";
    }

    @SubscribeEvent
    public static void despawnObserve(LivingDeathEvent event) {
        Entity entity = event.getSource().getEntity();
        if (event.getEntity() instanceof Player && event.getSource().getEntity() instanceof ObserveAngryEntity) {
            entity.remove(Entity.RemovalReason.KILLED);
        }
    }


    @SubscribeEvent
    public static void fieryStarPlacement(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide) return;
        if (event.getHand() == InteractionHand.MAIN_HAND && event.getItemStack().getItem() ==
                ModItems.FIERY_STAR.get()) {
            FireServantEntity entity = ModEntities.FIRE_SERVANT.get().create(event.getLevel());
            if (entity == null) return;
            entity.setPos(event.getPos().getCenter());
            event.getLevel().addFreshEntity(entity);
            if (!event.getEntity().isCreative()) {
                event.getItemStack().shrink(1);
            }
        }
    }
}

