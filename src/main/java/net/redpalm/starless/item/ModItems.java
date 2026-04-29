package net.redpalm.starless.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.redpalm.starless.Starless.MODID;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> CORRUPTED_LAPIS = ITEMS.register
            ("corrupted_lapis", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> FIERY_STAR = ITEMS.register
            ("fiery_star", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CORRUPTED_BREAD = ITEMS.register
            ("corrupted_bread", () -> new Item(new Item.Properties().food(ModFood.CORRUPTED_BREAD)));

    public static final DeferredItem<Item> CORRUPTED_DAGGER = ITEMS.register
            ("corrupted_dagger", () -> new SwordItem(Tiers.NETHERITE, new Item.Properties().attributes
                    (SwordItem.createAttributes(Tiers.NETHERITE, 6, 3))));

    public static final DeferredItem<Item> OBSIDIAN_HANDLE = ITEMS.register
            ("obsidian_handle", () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
