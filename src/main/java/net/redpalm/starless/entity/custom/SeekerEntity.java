package net.redpalm.starless.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class SeekerEntity extends PathfinderMob implements GeoEntity {
    public static final double TEMPT_SPEED_MOD = 0.6;
    public static final double WALK_SPEED_MOD = 0.8;
    public static final double SPRINT_SPEED_MOD = 1.33;
    private boolean gotFood = false;
    private boolean lookTrigger = false;
    protected Path path;
    private int specialTimer = 0;
    private int timeAlive = 0;
    private int lookTimer = 0;
    private int runAwayByHurtTimer = 0;

    public SeekerEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
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
        if (pSource != damageSources().genericKill() || pSource != damageSources().fellOutOfWorld()) {
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
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    public void tick() {
        timeAlive++;
        if (timeAlive == 2400) {
            this.discard();
            timeAlive = 0;
        }
        if (!level().isClientSide) {
            Player player = level().getNearestPlayer(this, 100D);
            if (!gotFood) {
                if (player != null) {
                    if (!player.isCrouching() || !player.isSteppingCarefully()) {
                        if (!player.isCreative()) {
                            if (!isLookingAtMe(player) && !lookTrigger) {
                                seekerFollowPlayer(player);
                            } else {
                                seekerAvoidPlayer(player);
                                lookTrigger = true;
                                lookTimer++;
                                if (lookTimer == 100) {
                                    lookTrigger = false;
                                    lookTimer = 0;
                                }
                            }
                        }
                    } else if (player.isCrouching() || player.isSteppingCarefully()) {
                        seekerTempted(player);
                    }
                }
            }
            else if (player != null) {
                seekerAvoidPlayer(player);
                specialTimer++;
                if (specialTimer == 100) this.discard();
            }
            else this.discard();
        }
        super.tick();
    }

    private void seekerFollowPlayer (Player player) {
        if (player != null && hasEdibleFood(player) && !player.isCreative()) {
            if (this.distanceTo(player) > 30 || this.distanceTo(player) < 10) {
                this.getNavigation().moveTo(player, SPRINT_SPEED_MOD);
            }
            else {
                this.getNavigation().moveTo(player, WALK_SPEED_MOD);
            }
            seekerTakeFood(player);
        }
        else if (player != null && !hasEdibleFood(player) && this.distanceTo(player) < 10) {
            seekerAvoidPlayer(player);
        }
    }

    private void seekerTempted (Player player) {
        if (player != null && hasEdibleFood(player)) {
            this.getNavigation().moveTo(player, TEMPT_SPEED_MOD);
            if (player.getMainHandItem().isEdible() && this.isWithinMeleeAttackRange(player)) {
                player.getMainHandItem().shrink(1);
                gotFood = true;
            }
            else seekerTakeFood(player);
        }
    }

    private void seekerTakeFood (Player player) {
        if (this.isWithinMeleeAttackRange(player) && hasEdibleFood(player)) {
            ItemStack food = player.getInventory().items.stream().filter(stack -> !stack.isEmpty() && stack.getItem()
                    .isEdible()).findFirst().get();
            int i = player.getInventory().items.indexOf(food);
            player.getInventory().removeItem(i, 1);
            gotFood = true;
        }
    }

    public boolean hasEdibleFood(Player player) {
        return player.getInventory().items.stream()
                .anyMatch(stack -> !stack.isEmpty() && stack.getItem().isEdible());
    }

    public boolean isLookingAtMe (Player pPlayer) {
        Vec3 vec3 = pPlayer.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() - pPlayer.getX(), this.getEyeY() - pPlayer.getEyeY(), this.getZ() - pPlayer.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > (double)1.0F - 0.025 / d0 ? pPlayer.hasLineOfSight(this) : false;
    }

    public void seekerAvoidPlayer (Player player) {
        Vec3 $$0 = DefaultRandomPos.getPosAway(this, 20, 7, player.position());
        if ($$0 != null) {
            this.path = this.getNavigation().createPath($$0.x, $$0.y, $$0.z, 0);
            this.getNavigation().moveTo(this.path, SPRINT_SPEED_MOD);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("TimeAlive", this.timeAlive);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("TimeAlive")) {
            this.timeAlive = pCompound.getInt("TimeAlive");
        }
    }

}
