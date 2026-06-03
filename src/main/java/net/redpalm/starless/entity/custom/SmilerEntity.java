package net.redpalm.starless.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.redpalm.starless.entity.base.BaseAggressiveMonster;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class SmilerEntity extends BaseAggressiveMonster implements GeoEntity {
    private boolean moveSlowly = true;
    private boolean timeAliveRun = true;
    private boolean stalkModeOn = true;
    protected Path path;
    private int tickCountSmiler = 0;
    private int angerChaseTime = 20 * 30;
    private int angerChaseRunTimer = 0;
    private int specialTimer = 0;
    private final float CHASE_PLAYER_SPEED_MOD = 0.45f;
    private Vec3 vecZero = new Vec3(0, 0, 0);

    private boolean stalkMode () {
        return stalkModeOn;
    }

    private boolean startSprinting () {
        return (this.getDeltaMovement() != vecZero) && !moveSlowly;
    }

    @Override
    public boolean timeAliveShouldRun() {
        return timeAliveRun;
    }

    @Override
    public boolean canMoveToPlayerClassPath() {
        return moveSlowly;
    }

    @Override
    public float moveToPlayerSpeedModifier() {
        return 0.25f;
    }

    public SmilerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", state -> {
            if (state.isMoving())
                return state.setAndContinue(SmilerEntity.this.startSprinting() ?
                        RawAnimation.begin().then("run", Animation.LoopType.LOOP)
                        : RawAnimation.begin().then("walk", Animation.LoopType.LOOP));

            return state.setAndContinue(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100D)
                .add(Attributes.FOLLOW_RANGE, 100D)
                .add(Attributes.ATTACK_DAMAGE, 12f)
                .add(Attributes.MOVEMENT_SPEED, 1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5f)
                .add(Attributes.JUMP_STRENGTH, 0.5f);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource != damageSources().fall()) {
            return super.hurt(pSource, pAmount);
        }
        else return false;
    }

    @Override
    public void injectBehaviorAfterPlayerChase(Player player, Level level) {
        if (stalkMode() && (this.distanceTo(player) < 15 || isLookingAtMe(player))) {
            moveSlowly = false;
            timeAliveRun = false;
            moveToPlayer(player, CHASE_PLAYER_SPEED_MOD);
            angerChaseRunTimer++;
            if (angerChaseRunTimer == angerChaseTime) this.discard();
        }
        if (this.getHealth() < this.getMaxHealth() / 3) {
            stalkModeOn = false;
            smilerAvoidPlayer(player);
            specialTimer++;
            if (specialTimer == 100) this.discard();
        }
    }

    public void smilerAvoidPlayer (Player player) {
        Vec3 $$0 = DefaultRandomPos.getPosAway(this, 20, 7, player.position());
        if ($$0 != null) {
            this.path = this.getNavigation().createPath($$0.x, $$0.y, $$0.z, 0);
            this.getNavigation().moveTo(this.path, CHASE_PLAYER_SPEED_MOD);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("timeAlive")) {
            this.timeAlive = compound.getInt("timeAlive");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("timeAlive", timeAlive);
    }

    private void moveToPlayer (Player player, double speedMod) {
        tickCountSmiler++;
        if (tickCountSmiler == 10) {
            this.getNavigation().moveTo(player, speedMod);
            tickCountSmiler = 0;
        }
    }
}
