package net.redpalm.starless;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.redpalm.starless.block.ModBlocks;
import net.redpalm.starless.entity.ModEntities;
import net.redpalm.starless.entity.client.ObserveRenderer;
import net.redpalm.starless.entity.client.WrongedRenderer;
import net.redpalm.starless.event.EventHandler;
import net.redpalm.starless.item.ModItems;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(Starless.MODID)
public class Starless
{
    public static final String MODID = "starless";

    private static final Logger LOGGER = LogUtils.getLogger();
    public Starless(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        GeckoLib.initialize();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);

        modEventBus.addListener(this::addCreative);


    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.CORRUPTED_LAPIS_BLOCK);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.CORRUPTED_LAPIS);
        }
        if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
            event.accept(ModBlocks.NOTHING_LEFT_PLUSH);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.OBSERVE.get(), ObserveRenderer::new);
            EntityRenderers.register(ModEntities.WRONGED.get(), WrongedRenderer::new);
        }
    }
}
