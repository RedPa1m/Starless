package net.redpalm.starless.entity.custom;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class SeekerEntity extends Mob implements GeoEntity {
    public static final double TEMPT_SPEED_MOD = 0.6;
    public static final double WALK_SPEED_MOD = 0.8;
    public static final double SPRINT_SPEED_MOD = 1.33;
    private boolean gotFood = false;

    private int timeAlive = 0;

    public SeekerEntity(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 120D)
                .add(Attributes.FOLLOW_RANGE, 100f)
                .add(Attributes.MOVEMENT_SPEED, 0.3f);
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource != damageSources().genericKill() || pSource != damageSources().fellOutOfWorld() ||
                !(pSource.getEntity() instanceof Player)) {
            return false;
        }
        else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public boolean isSprinting() {
        return this.getSpeed() == (float) (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() * SPRINT_SPEED_MOD);
    }

    @Override
    public void tick() {
        timeAlive++;
        if (timeAlive == 2400) {
            this.discard();
            timeAlive = 0;
        }
        if (!level().isClientSide) {
            if (!gotFood) {
                seekerFollowPlayer(level());
            }
            else this.discard();
        }

        super.tick();
    }

    private void seekerFollowPlayer (Level level) {
        Player player = level.getNearestPlayer(this, 100D);
        if (player != null && hasEdibleFood(player) && !player.isCreative()) {
            if (this.distanceTo(player) > 30 || this.distanceTo(player) < 10) {
                this.getNavigation().moveTo(player, SPRINT_SPEED_MOD);
            }
            else {
                this.getNavigation().moveTo(player, WALK_SPEED_MOD);
            }
            if (this.isWithinMeleeAttackRange(player) && hasEdibleFood(player)) {
                ItemStack food = player.getInventory().items.stream().filter(stack -> !stack.isEmpty() && stack.getItem()
                        .isEdible()).findFirst().get();
                int i = player.getInventory().items.indexOf(food);
                player.getInventory().removeItem(i, 1);
                gotFood = true;
            }
        }
    }

    public boolean hasEdibleFood(Player player) {
        return player.getInventory().items.stream()
                .anyMatch(stack -> !stack.isEmpty() && stack.getItem().isEdible());
    }

}
