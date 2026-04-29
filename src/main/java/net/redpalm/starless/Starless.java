package net.redpalm.starless;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.util.Tuple;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.redpalm.starless.block.ModBlocks;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.client.*;
import net.redpalm.starless.event.EntitySpawnEventHandler;
import net.redpalm.starless.event.EventHandler;
import net.redpalm.starless.event.RandomEventHandler;
import net.redpalm.starless.event.custom.WrongedChatEvent;
import net.redpalm.starless.event.custom.WrongedRegisterChatEvent;
import net.redpalm.starless.item.ModCreativeModeTabs;
import net.redpalm.starless.item.ModItems;
import net.redpalm.starless.util.StarlessSavedData;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(Starless.MODID)
public class Starless
{
    public static final String MODID = "starless";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Starless(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(EventHandler.class);
        NeoForge.EVENT_BUS.register(WrongedRegisterChatEvent.class);
        NeoForge.EVENT_BUS.register(WrongedChatEvent.class);
        NeoForge.EVENT_BUS.register(EntitySpawnEventHandler.class);
        NeoForge.EVENT_BUS.register(RandomEventHandler.class);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new Tuple<>(action, tick));
    }

    // credits to Chaaze for handling this one for me
    @SubscribeEvent
    public void tick(ServerTickEvent.Post event) {
        List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
        workQueue.forEach(work -> {
            work.setB(work.getB() - 1);
            if (work.getB() == 0)
                actions.add(work);
        });
        actions.forEach(e -> e.getA().run());
        workQueue.removeAll(actions);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        StarlessSavedData.read(event.getServer());
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.OBSERVE.get(), ObserveRenderer::new);
            EntityRenderers.register(ModEntities.WRONGED.get(), WrongedRenderer::new);
            EntityRenderers.register(ModEntities.OBSERVE_ANGRY.get(), ObserveAngryRenderer::new);
            EntityRenderers.register(ModEntities.FIRE_SERVANT.get(), FireServantRenderer::new);
            EntityRenderers.register(ModEntities.CITASE.get(), CitaseRenderer::new);
            EntityRenderers.register(ModEntities.SEEKER.get(), SeekerRenderer::new);
        }
    }
}