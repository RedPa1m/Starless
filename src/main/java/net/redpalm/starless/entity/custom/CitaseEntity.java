package net.redpalm.starless.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;

import static net.redpalm.starless.event.custom.CitaseEventsAndReputation.isFamiliar;

public class CitaseEntity extends Mob implements GeoEntity {
    private int TimeAlive = 0;
    static Random random = new Random();

    public CitaseEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class,
                50f, 1f));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "blinking",
                0, animationState -> {
            return animationState.setAndContinue(RawAnimation.begin().then("blink",
                    Animation.LoopType.LOOP));
        }));
        controllerRegistrar.add(new AnimationController<>(this, "controller",
                0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "walking",
                0, animationState -> {
            return animationState.isMoving() ? animationState.setAndContinue(RawAnimation.begin().then
                    ("walk", Animation.LoopType.LOOP)) : PlayState.STOP;
        }));
    }

    private PlayState predicate(AnimationState<CitaseEntity> citaseEntityAnimationState) {
        citaseEntityAnimationState.getController().setAnimation(RawAnimation.begin().then
                ("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 15D)
                .add(Attributes.FOLLOW_RANGE, 50f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource != damageSources().genericKill() && pSource != damageSources().fellOutOfWorld()
        && pSource != damageSources().playerAttack(lastHurtByPlayer)) {
            return false; }
        else if (pSource == damageSources().playerAttack(lastHurtByPlayer)) {
            switch (random.nextInt(3)) {
                case 0:
                    citaseTalk(level(), "Owch... Stop it!");
                case 1:
                    citaseTalk(level(), "Why would you do this?!");
                default:
                    citaseTalk(level(), "Stop, I had enough of that in my life!");
            }
            return false;
        }
        else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public void tick() {
        TimeAlive++;

        if (!isFamiliar) {
            if (TimeAlive == 20) {
                citaseTalk(level(), "Player, player!");
            }
            if (TimeAlive == 60) {
                citaseTalk(level(), "Hey!!");
            }
            if (TimeAlive == 100) {
                citaseTalk(level(), "Sorry to approach you like that, but...");
            }
            if (TimeAlive == 140) {
                citaseTalk(level(), "Would you... Have some spare food, please?");
            }
        }

        if (this.TimeAlive == 2400) {
            this.remove(RemovalReason.KILLED);
            this.TimeAlive = 0;
            if (isFamiliar) {
                switch (random.nextInt(4)) {
                    case 0:
                        citaseTalk(level(), "See you later!");
                    case 1:
                        citaseTalk(level(), "Ugh, I gotta go. See ya!");
                    case 2:
                        citaseTalk(level(), "Seems like I'm out of time. Goodbye!");
                    default:
                        citaseTalk(level(), "Goodbye!");
                }
            }
            else {
                citaseTalk(level(), "Well, goodbye.");
            }
        }
        super.tick();
    }

    public static void citaseTalk (Level level, String speech) {
        if (level.isClientSide) return;
        if (level.getServer().getPlayerList().getPlayers().isEmpty()) return;
        level.getServer().getPlayerList().broadcastSystemMessage
                (Component.literal(isFamiliarString() + speech), false);
    }

    private static String isFamiliarString () {
        if (isFamiliar) return "<Citase> ";
        else return "<??????> ";
    }
}
