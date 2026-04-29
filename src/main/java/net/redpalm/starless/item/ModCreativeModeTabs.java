package net.redpalm.starless.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.redpalm.starless.Starless;
import net.redpalm.starless.block.ModBlocks;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create
            (Registries.CREATIVE_MODE_TAB, Starless.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> STARLESS_TAB = CREATIVE_MODE_TABS.register("starless_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.CORRUPTED_LAPIS.get()))
                    .title(Component.literal("Starless"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.CORRUPTED_LAPIS.get());
                        output.accept(ModBlocks.CORRUPTED_LAPIS_BLOCK.get());
                        output.accept(ModItems.FIERY_STAR.get());
                        output.accept(ModBlocks.PRIMITIVE_TERMINAL.get());
                        output.accept(ModItems.CORRUPTED_BREAD.get());
                        output.accept(ModItems.CORRUPTED_DAGGER.get());
                        output.accept(ModItems.OBSIDIAN_HANDLE.get());
                    })
                    .build());

    public static void register (IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
