package net.redpalm.starless.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.redpalm.starless.util.StarlessSavedData;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;

import static net.redpalm.starless.event.custom.CitaseEventsAndReputation.isFamiliar;

public class CitaseEntity extends Mob implements GeoEntity {
    private int timeAlive = 0;
    private int specialTimer = 0;
    private boolean reachedPlayer = false;
    private boolean deathMessage = true;
    private boolean canAcceptFood = true;
    private boolean isTimerRunning = true;
    static Random random = new Random();

    public boolean getCanAcceptFood () {
        return this.canAcceptFood;
    }

    public void setCanAcceptFood (boolean set) {
        this.canAcceptFood = set;
    }

    public CitaseEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "blinking",
                0, animationState -> {
            return animationState.setAndContinue(RawAnimation.begin().then("blink",
                    Animation.LoopType.LOOP));
        }));
        controllerRegistrar.add(new AnimationController<>(this, "hold_controller",
                animationState -> PlayState.CONTINUE).triggerableAnim("hold",
                RawAnimation.begin().then("hold", Animation.LoopType.HOLD_ON_LAST_FRAME)));
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
                .add(Attributes.FOLLOW_RANGE, 100f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof Player) {
            switch (random.nextInt(3)) {
                case 0:
                    citaseTalk(level(), "Owch... Stop it!");
                    break;
                case 1:
                    citaseTalk(level(), "Why would you do this?!");
                    break;
                case 2:
                    citaseTalk(level(), "Stop, I had enough of that in my life!");
                    break;
            }
        }
        if (pSource != damageSources().genericKill() || pSource != damageSources().fellOutOfWorld()) {
            return false;
        }
        else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public void tick() {
        timeAlive++;
        isTimerRunning = true;

        if (!isFamiliar) {
            if (timeAlive == 20) {
                citaseTalk(level(), "Player, player!");
            }
            if (timeAlive == 60) {
                citaseTalk(level(), "Hey!!");
            }
            if (timeAlive == 100) {
                citaseTalk(level(), "Sorry to approach you like that, but...");
            }
            if (timeAlive == 140) {
                citaseTalk(level(), "Would you... Have some spare food, please?");
            }
        }

        if (!this.canAcceptFood && !isFamiliar) {
            isTimerRunning = false;
            specialTimer++;
            if (specialTimer == 100) {
                citaseTalk(level(), "It's so nice to finally get an actual food.");
            }
            if (specialTimer == 160) {
                citaseTalk(level(), "By the way...");
            }
            if (specialTimer == 200) {
                if (level().isClientSide) return;
                if (level().getServer().getPlayerList().getPlayers().isEmpty()) return;
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Citase> My name is Citase."), false);
            }
            if (specialTimer == 260) {
                if (level().isClientSide) return;
                if (level().getServer().getPlayerList().getPlayers().isEmpty()) return;
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Citase> See you later!"), false);
                isFamiliar = true;
                StarlessSavedData.save(level().getServer());
                this.remove(RemovalReason.KILLED);
            }
        }

        if (isFamiliar) {
            if (timeAlive == 20) {
                citaseTalk(level(), "Hello!");
            }
            if (timeAlive == 80) {
                citaseTalk(level(), "Do you, perchance, have a spare meal? I would be very thankful!");
            }
        }

        if (isFamiliar && !canAcceptFood) {
            isTimerRunning = false;
            specialTimer++;
            if (specialTimer == 100) {
                citaseTalk(level(), "Well, I gotta go now.");
            }
            if (specialTimer == 160) {
                citaseTalk(level(), "See you later, and thank you once more!");
                this.remove(RemovalReason.KILLED);
            }
        }

        if (this.timeAlive == 2400 && isTimerRunning) {
            this.remove(RemovalReason.KILLED);
            this.timeAlive = 0;
            if (isFamiliar) {
                switch (random.nextInt(4)) {
                    case 0:
                        citaseTalk(level(), "See you later!");
                        break;
                    case 1:
                        citaseTalk(level(), "Ugh, I gotta go. See ya!");
                        break;
                    case 2:
                        citaseTalk(level(), "Seems like I'm out of time. Goodbye!");
                        break;
                    case 3:
                        citaseTalk(level(), "Goodbye!");
                        break;
                }
            }
            else {
                citaseTalk(level(), "Well, goodbye.");
            }
        }
        if (!canAcceptFood && !level().isClientSide) {
            triggerAnim("hold_controller", "hold");
        }
        if (level().getNearestPlayer(this, 50D) != null && canAcceptFood) {
            getLookControl().setLookAt(level().getNearestPlayer(this, 50D));
        }
        if (!reachedPlayer && !(level().getNearestPlayer(this, 200D) == null)) {
            Player player = level().getNearestPlayer(this, 200D);
            this.getNavigation().moveTo(player, 1.0f);
            if (!(level().getNearestPlayer(this, 5D) == null)) {
                reachedPlayer = true;
            }
        }
        if (this.isDeadOrDying() && deathMessage == true) {
            citaseTalk(level(), "Huh?! This is NOT nice!!");
            deathMessage = false;
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

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("canAcceptFood", this.canAcceptFood);
        pCompound.putBoolean("isTimerRunning", this.isTimerRunning);
        pCompound.putBoolean("reachedPlayer", this.reachedPlayer);
        pCompound.putInt("TimeAlive", this.timeAlive);
        pCompound.putInt("specialTimer", this.specialTimer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("canAcceptFood")) {
            this.canAcceptFood = pCompound.getBoolean("canAcceptFood");
        }
        if (pCompound.contains("isTimerRunning")) {
            this.isTimerRunning = pCompound.getBoolean("isTimerRunning");
        }
        if (pCompound.contains("TimeAlive")) {
            this.timeAlive = pCompound.getInt("TimeAlive");
        }
        if (pCompound.contains("specialTimer")) {
            this.specialTimer = pCompound.getInt("specialTimer");
        }
        if (pCompound.contains("reachedPlayer")) {
            this.reachedPlayer = pCompound.getBoolean("reachedPlayer");
        }
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }
}
