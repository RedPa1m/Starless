package net.redpalm.starless.entity.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.redpalm.starless.event.custom.WrongedChatEvent;
import net.redpalm.starless.event.custom.WrongedRegisterChatEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class WrongedEntity extends Mob implements GeoEntity {
    public static boolean canGiveItem;
    private int TimeAlive = 0;

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
        TimeAlive++;
        if (TimeAlive == 1) {
            canGiveItem = true;
            MinecraftForge.EVENT_BUS.register(WrongedRegisterChatEvent.class);
        }
        if (TimeAlive == 2400) {
            this.remove(RemovalReason.KILLED);
            TimeAlive = 0;
            if (!level().isClientSide) {
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> Goodbye."), false);
            }
            canGiveItem = true;
        }
        if (level().getNearestPlayer(this, 50D) != null) {
            getLookControl().setLookAt(level().getNearestPlayer(this, 50D));
        }
        if (this.isDeadOrDying()) {
            MinecraftForge.EVENT_BUS.unregister(WrongedRegisterChatEvent.class);
            MinecraftForge.EVENT_BUS.unregister(WrongedChatEvent.class);
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
    // emo
}
