package net.redpalm.starless.entity.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class FireServantEntity extends Allay implements GeoEntity {
    public FireServantEntity(EntityType<? extends Allay> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller",
                0, this::predicate));
    }

    private PlayState predicate(AnimationState<FireServantEntity> fireServantEntityAnimationState) {
        fireServantEntityAnimationState.getController().setAnimation(RawAnimation.begin().then(
                "idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource != damageSources().genericKill() && pSource != damageSources().fellOutOfWorld()) {
            return false; }
        else {
            return super.hurt(pSource, pAmount);
        }
    }
}