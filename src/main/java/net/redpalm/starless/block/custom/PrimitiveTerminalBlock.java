package net.redpalm.starless.block.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.redpalm.starless.util.StarlessSavedData;
import org.jetbrains.annotations.Nullable;

import static net.redpalm.starless.Starless.queueServerWork;
import static net.redpalm.starless.event.EntitySpawnEventHandler.dailyTerminalUsage;

public class PrimitiveTerminalBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PrimitiveTerminalBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pHand == InteractionHand.MAIN_HAND && !pLevel.isClientSide && pLevel.getServer().getPlayerList() != null) {
            randomSpeech(pLevel, pPlayer);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public void randomSpeech(Level level, Player player) {
        if (dailyTerminalUsage) {
            int x = level.getRandom().nextInt(11);
            int y = level.getRandom().nextInt(3);
            level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                    ("Loading..."), false);
            queueServerWork(40, () -> {
                level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                        ("Loading..."), false);
            });
            queueServerWork(80, () -> {
                level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                        ("§kLoading..."), false);
            });
            queueServerWork(120, () -> {
                if (level.getRandom().nextInt(10) != 0) {
                    switch (x) {
                        case 0:
                            speech(level, "<UNKNOWN_SOURCE>", "Hello? Who's that? I am busy with my bread! Please contact me later, beep-beep."); // citase
                            break;
                        case 1:
                            speech(level, "<UNKNOWN_SOURCE>", "Hello, Player. Hope you're doing alright. Wish you the best of luck."); // wronged
                            break;
                        case 2:
                            speech(level, "<UNKNOWN_SOURCE>", "Mesmerizing! I realized I can just use slices of cheese instead of brioche buns. Oh I'm so smart I'd kiss myself."); // citase
                            break;
                        case 3:
                            speech(level, "<UNKNOWN_SOURCE>", "I know who you are."); // observe edgy mf
                            break;
                        case 4:
                            speech(level, "<UNKNOWN_SOURCE>", "Don't get consumed by your own sins."); // observe
                            break;
                        case 5:
                            speech(level, "<UNKNOWN_SOURCE>", "Hello? Sorry, if you need something, I'm unable to help you in any way right now. But I will try later. Stay safe."); // no_light
                            break;
                        case 6:
                            speech(level, "<UNKNOWN_SOURCE>", "Huh? Hello. Can you send me a porkchop through this thing? Thanks in advance."); // seeker
                            break;
                        case 7:
                            speech(level, "<UNKNOWN_SOURCE>", "This mod changes Minecraft forever... This mod controls everything... Do you think I can be a good horror Minecraft youtuber?"); // citase
                            break;
                        case 8:
                            speech(level, "<UNKNOWN_SOURCE>", "If you hear this message, you're cool, strong, handsome and funny. And also very smart!"); // citase
                            break;
                        case 9:
                            speech(level, "<UNKNOWN_SOURCE>", "I know it will sound embarassing, but I start forgetting multiplication table..."); // citase
                            break;
                        case 10:
                            speech(level, "<UNKNOWN_SOURCE>", "I am aware of your presence. I will find you."); // observe
                            break;
                    }
                } else {
                    switch (y) {
                        case 0:
                            cassieSpeech(level, "Lovely. Hello there, little guy.");
                            break;
                        case 1:
                            cassieSpeech(level, "How peculiar. Do you like using this little terminal?");
                            break;
                        case 2:
                            cassieSpeech(level, "Curious. I like curious people. They're extremely fun to deal with.");
                            break;
                    }
                }
            });
            StarlessSavedData.save(level.getServer());
        }
        else {
            player.sendSystemMessage(Component.literal("Unable to use terminal. Please wait for the start of the next day."));
        }
    }

    public void speech(Level level, String name, String answer) {
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                        (name + " " + answer), false);
    }
    public void cassieSpeech(Level level, String answer) {
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                ("§4<" + "§kThe Transitioned" + "§4> " + answer), false);
    }
}
