package net.redpalm.starless.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class WrongedEntity extends Mob implements GeoEntity {
    private boolean canGiveItem;
    private int TimeAlive = 0;
    public static boolean canChat = false;

    public boolean getCanGiveItem() {
        return this.canGiveItem;
    }

    public void setCanGiveItem(boolean set) {
        this.canGiveItem = set;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public WrongedEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.FOLLOW_RANGE, 10f);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<WrongedEntity> wrongedEntityAnimationState) {
        wrongedEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("wronged.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }
    // Set his lifetime to 2400 ticks and make him look at player
    @Override
    public void tick() {
        this.TimeAlive++;
        if (this.TimeAlive == 1) {
            this.canGiveItem = true;
            canChat = true;
        }
        if (this.TimeAlive == 2400) {
            this.remove(RemovalReason.KILLED);
            this.TimeAlive = 0;
            if (!level().isClientSide) {
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> Goodbye."), false);
            }
            this.canGiveItem = true;
        }
        if (level().getNearestPlayer(this, 50D) != null) {
            getLookControl().setLookAt(level().getNearestPlayer(this, 50D));
        }

        if (this.isDeadOrDying() || this.isRemoved()) {
            canChat = false;
        }

        super.tick();
    }

    // Make it so he takes no damage unless falls out of the world or /kill applied
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource != damageSources().genericKill() && pSource != damageSources().fellOutOfWorld()) {
            return false; }
        else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("canGiveItem", this.canGiveItem);
        pCompound.putInt("TimeAlive", this.TimeAlive);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("canGiveItem")) {
            this.canGiveItem = pCompound.getBoolean("canGiveItem");
        }
        if (pCompound.contains("TimeAlive")) {
            this.TimeAlive = pCompound.getInt("TimeAlive");
        }
    }
    // emo
}
