package net.redpalm.starless;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.util.Tuple;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.redpalm.starless.block.ModBlocks;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.client.*;
import net.redpalm.starless.event.EntitySpawnEventHandler;
import net.redpalm.starless.event.EventHandler;
import net.redpalm.starless.event.RandomEventHandler;
import net.redpalm.starless.event.custom.CitaseEventsAndReputation;
import net.redpalm.starless.event.custom.WrongedChatEvent;
import net.redpalm.starless.event.custom.WrongedRegisterChatEvent;
import net.redpalm.starless.item.ModCreativeModeTabs;
import net.redpalm.starless.item.ModItems;
import net.redpalm.starless.util.StarlessSavedData;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(Starless.MODID)
public class Starless
{
    public static final String MODID = "starless";

    private static final Logger LOGGER = LogUtils.getLogger();
    public Starless(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        GeckoLib.initialize();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
        MinecraftForge.EVENT_BUS.register(WrongedRegisterChatEvent.class);
        MinecraftForge.EVENT_BUS.register(WrongedChatEvent.class);
        MinecraftForge.EVENT_BUS.register(EntitySpawnEventHandler.class);
        MinecraftForge.EVENT_BUS.register(CitaseEventsAndReputation.class);
        MinecraftForge.EVENT_BUS.register(RandomEventHandler.class);


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
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
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

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
        }
    }
}
