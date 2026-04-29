package net.redpalm.starless.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.redpalm.starless.block.custom.PrimitiveTerminalBlock;
import net.redpalm.starless.item.ModItems;

import java.util.function.Supplier;

import static net.redpalm.starless.Starless.MODID;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> CORRUPTED_LAPIS_BLOCK = registerBlock("corrupted_lapis_block",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.AMETHYST).strength(1.5f)));

    public static final DeferredBlock<Block> PRIMITIVE_TERMINAL = registerBlock("primitive_terminal",
            () -> new PrimitiveTerminalBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(5f).noOcclusion()));

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
