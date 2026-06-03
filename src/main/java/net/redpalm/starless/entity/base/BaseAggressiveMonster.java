package net.redpalm.starless.entity.base;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BaseAggressiveMonster extends Monster {
    private int tickCountBaseMonster = 0;
    public int timeAlive = 0;
    public static boolean isAngry = false;

    // Max lifespan
    public int maxTimeAlive () {
        return 2400;
    }

    // Distance to find the closest player to this entity
    public double distanceToFindClosestPlayer () {
        return 100D;
    }

    // Speed modifier with which entity will run to player
    public float moveToPlayerSpeedModifier () {
        return 0.45f;
    }

    // Can attack player
    public boolean canAttack () {
        return true;
    }

    // Can move to player with a specific navigation path inside this class tick method
    public boolean canMoveToPlayerClassPath() {
        return true;
    }

    // Change to false if you don't want this entity to have lifespan
    public boolean timeAliveShouldRun () {
        return true;
    }

    // Should entity disappear after killing player
    public boolean discardAfterKillingPlayer () {
        return true;
    }

    public BaseAggressiveMonster(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    // Vanilla goals
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 100, 1f));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.45f));
    }

    @Override
    public void tick() {

        if (timeAliveShouldRun()) {
            timeAlive++;
            if (timeAlive == maxTimeAlive()) {
                this.remove(RemovalReason.KILLED);
            }
        }

        Player player = level().getNearestPlayer(this, distanceToFindClosestPlayer());
        injectBehaviorAfterChoosingPlayer(player, level());

        if (player != null && !player.isCreative() && this.hasLineOfSight(player)) {

            injectBehaviorBeforePlayerChase(player, level());
            isAngry = true;
            // Makes entity move to player. Cancel this with canMoveToPlayerClassPath
            if (canMoveToPlayerClassPath()) moveToPlayer(player, moveToPlayerSpeedModifier());
            injectBehaviorAfterPlayerChase(player, level());

            if (this.canAttack(player) && this.isWithinMeleeAttackRange(player) && !player.isCreative()
                    && canAttack()) {

                injectBehaviorBeforePlayerHurt(player, level());
                // Makes entity attack player, cancel with canAttack
                this.doHurtTarget(player);
                injectBehaviorAfterPlayerHurt(player, level());

                // Makes entity be discarded after killing player, cancel with discardAfterKillingPlayer
                if (player.isDeadOrDying() && discardAfterKillingPlayer()) {
                    this.discard();
                }
            }
        }

        else isAngry = false;
        super.tick();
    }

    public void injectBehaviorAfterChoosingPlayer (Player player, Level level) {
    }

    public void injectBehaviorBeforePlayerChase (Player player, Level level) {
    }

    public void injectBehaviorAfterPlayerChase (Player player, Level level) {
    }

    public void injectBehaviorBeforePlayerHurt (Player player, Level level) {
    }

    public void injectBehaviorAfterPlayerHurt (Player player, Level level) {
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    public boolean isLookingAtMe (Player pPlayer) {
        Vec3 vec3 = pPlayer.getViewVector(1.0F).normalize();
        Vec3 vec31 = new Vec3(this.getX() - pPlayer.getX(), this.getEyeY() - pPlayer.getEyeY(),
                this.getZ() - pPlayer.getZ());
        double d0 = vec31.length();
        vec31 = vec31.normalize();
        double d1 = vec3.dot(vec31);
        return d1 > (double)1.0F - 0.025 / d0 ? pPlayer.hasLineOfSight(this) : false;
    }

    private void moveToPlayer (Player player, double speedMod) {
        tickCountBaseMonster++;
        if (tickCountBaseMonster == 10) {
            this.getNavigation().moveTo(player, speedMod);
            tickCountBaseMonster = 0;
        }
    }

}
