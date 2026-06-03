package net.redpalm.starless.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.custom.*;
import net.redpalm.starless.item.ModItems;
import net.redpalm.starless.util.StarlessSavedData;

import java.util.Random;

import static net.redpalm.starless.Starless.queueServerWork;
import static net.redpalm.starless.event.EntitySpawnEventHandler.canSmilerSpawn;
import static net.redpalm.starless.event.EntitySpawnEventHandler.specialSmilerSpawn;
import static net.redpalm.starless.event.custom.CitaseEventsAndReputation.isFamiliar;
import static net.redpalm.starless.misc.WrongedItemList.wrongedItemList;

@EventBusSubscriber(modid = Starless.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EventHandler extends Event {
    static int randomIndex;
    static Random random = new Random();
    public static Player playerSmilerSpawn;

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
            if (event.getItemStack().getFoodProperties(player) != null && (
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

    @SubscribeEvent
    public static void suspiciousPearlUsage(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide) return;
        if (event.getHand() == InteractionHand.MAIN_HAND && event.getItemStack().getItem() ==
                ModItems.SUSPICIOUS_PEARL.get()) {
            if (event.getEntity().level().canSeeSky(event.getEntity().blockPosition())) {
                if (!canSmilerSpawn) {
                    canSmilerSpawn = true;
                    StarlessSavedData.save(event.getEntity().getServer());
                    citaseTalkNoEvent("Huh?! Did you for real believe that stuff with free diamonds? You " +
                            "can't be serious!", event.getLevel());
                    event.getEntity().addEffect(new MobEffectInstance(MobEffects.DARKNESS, 80));
                    playerSmilerSpawn = event.getEntity();
                    specialSmilerSpawn = true;
                    if (!event.getEntity().isCreative()) {
                        event.getItemStack().shrink(1);
                    }
                    queueServerWork(80, () -> {
                        citaseTalkNoEvent("Anyways... This guy can now spawn in your world. What? Crap, he's" +
                                " already there! Pretty sure he's near you as well...", event.getLevel());
                    });
                } else {
                    event.getEntity().sendSystemMessage(Component.literal("Item was already used."));
                }
            }
            else {
                if (!canSmilerSpawn) event.getEntity().sendSystemMessage
                        (Component.literal("Item can only be used on the surface under open sky."));
                else event.getEntity().sendSystemMessage(Component.literal("Item was already used."));
            }
        }
    }

    public static void citaseTalkNoEvent(String speech, Level level) {
        if (level.getServer().getPlayerList().getPlayers().isEmpty()) return;
        level.getServer().getPlayerList().broadcastSystemMessage
                (Component.literal(isFamiliarString() + speech), false);
    }
}

