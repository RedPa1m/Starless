package net.redpalm.starless.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;

import java.util.Random;

public class ObserveAngryEntity extends Monster implements GeoEntity {
    private int timeAlive = 0;
    Random random = new Random();
    private boolean runTask = true; // thing for if he can start chase (so it doesn't duplicate)
    private boolean deathMessage = true;
    private boolean goalState = false;

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ObserveAngryEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller",
                0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "walking",
                0, animationState -> {
            return animationState.isMoving() ? animationState.setAndContinue(RawAnimation.begin().then
                    ("observe_run", Animation.LoopType.LOOP)) : PlayState.STOP;
        }));
    }

    private PlayState predicate(AnimationState<ObserveAngryEntity> observeAngryEntityAnimationState) {
        observeAngryEntityAnimationState.getController().setAnimation(RawAnimation.begin().then(
                "observe_idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class,
                50f, 1f));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>
                (this, Player.class, 0, false,
                        false, null));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300D)
                .add(Attributes.FOLLOW_RANGE, 200D)
                .add(Attributes.ATTACK_DAMAGE, 12f)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5f)
                .add(Attributes.JUMP_STRENGTH, 0.4f);
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void tick() {
        timeAlive++;

        if (timeAlive == 1) {
            observeTalk(level(), "<?> I see you.");
        }

        if ((timeAlive == 200 || level().getNearestPlayer(this, 10D) != null) && runTask == true) {
            if (random.nextInt(3) == 0) {
                observeTalk(level(), "<?> Try.");
            } else if (random.nextInt(3) == 1) {
                observeTalk(level(), "<?> Again.");
            } else {
                observeTalk(level(), "<?> Begin.");
            }
            runTask = false;
            goalState = true;
            this.goalSelector.addGoal(1, new MeleeAttackGoal
                    (this, 1D, true));
        }

        // check if attack goal is running so he attacks after world reload
        if (!level().isClientSide && goalState == true && this.goalSelector.getAvailableGoals().stream().noneMatch
                (goal -> goal.getGoal().getClass() == MeleeAttackGoal.class)) {
            this.goalSelector.addGoal(1, new MeleeAttackGoal
                    (this, 1D, true));
        }

        if (timeAlive == 1600) {
            this.remove(RemovalReason.KILLED);
            timeAlive = 0;
            observeTalk(level(), "<?> ...");
        }

        if (this.isDeadOrDying() && deathMessage == true) {
            observeTalk(level(), "<?> Won't help.");
            deathMessage = false;
        }
        super.tick();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource == damageSources().hotFloor() || pSource == damageSources().lava() ||
                pSource == damageSources().onFire() || pSource == damageSources().wither()
        || pSource == damageSources().fall()) {
            return false; }
        else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("TimeAlive", this.timeAlive);
        pCompound.putBoolean("runTask", this.runTask);
        pCompound.putBoolean("goalState", this.goalState);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("TimeAlive")) {
            this.timeAlive = pCompound.getInt("TimeAlive");
        }
        if (pCompound.contains("runTask")) {
            this.runTask = pCompound.getBoolean("runTask");
        }
        if (pCompound.contains("goalState")) {
            this.goalState = pCompound.getBoolean("goalState");
        }
    }

    public void observeTalk(Level level, String speech) {
        if (!level.isClientSide) {
            if (!level.getServer().getPlayerList().getPlayers().isEmpty()) {
                level.getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal(speech), false);
            }
        }
    }
}
