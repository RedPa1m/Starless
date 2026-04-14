package net.redpalm.starless.block.custom;

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
import org.jetbrains.annotations.Nullable;

import static net.redpalm.starless.Starless.queueServerWork;

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
            randomSpeech(pLevel);
        }
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    public void randomSpeech(Level level) {
        int x = level.getRandom().nextInt(5);
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
            default:
                speech(level, "<UNKNOWN_SOURCE>", "Don't get consumed by your own sins."); // observe
        }
        });
    }

    public void speech(Level level, String name, String answer) {
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                        (name + " " + answer), false);
    }
}
