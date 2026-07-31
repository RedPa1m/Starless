package net.redpalm.starless.entity.base;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

// Base aggressive monster that follows and tries to kill a player
// See comments for assistance

public class BaseAggressiveMonster extends Monster{
    public int timeAlive = 0;
    private int angerChaseTime = 20 * 30;
    private int angerChaseRunTimer = 0;
    private int tickCountBaseMonster = 0;
    public boolean isAngry = false;
    public boolean canMoveToPlayerClassPath = true;
    public boolean chaseStart = false;
    public boolean timeAliveRun = true;
    public boolean transitionFromStalkToChase = false;

    // Max lifespan
    public int maxTimeAlive () {
        return 2400;
    }

    // If entity should immediately run to the closest player or stalk until player is close.
    public boolean isStalkingType () {
        return false;
    }

    // Distance that's needed for stalking entity to start moving towards player
    public double distanceToPlayerToStalk () {
        return 25D;
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
        return canMoveToPlayerClassPath;
    }

    // Change to false if you don't want this entity to have lifespan
    public boolean timeAliveShouldRun () {
        return timeAliveRun;
    }

    // Should entity disappear after killing player
    public boolean discardAfterKillingPlayer () {
        return true;
    }

    //
    // CHASE SEQUENCE VARS
    //

    // Chase Sequence is turned off by default. Override this and return true to make it fire.
    public boolean initiateChaseSequence () {
        return false;
    }

    // Distance to Player to initiate chase sequence
    public int distanceToPlayerForChase() {
        return 15;
    }

    // Speed Modifier when entity chases player
    public float chasePlayerSpeedModifier() {
        return 0.45f;
    }

    // Chase timer - for how long entity should chase player when angered
    public int angerChaseTime () {
        return angerChaseTime;
    }

    // Checks if Chase Sequence has started
    public boolean isChaseSequenceActive () {
        return chaseStart;
    }

    public BaseAggressiveMonster(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    // Vanilla goals
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 100));
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
            if (!isStalkingType()) {
                entityMoveToPlayer(player, level());
            }
            else {
                if (this.distanceTo(player) < distanceToPlayerToStalk()) transitionFromStalkToChase = true;
                if (transitionFromStalkToChase) {
                    entityMoveToPlayer(player, level());
                }
            }
        }

        else isAngry = false;

        if (player != null) {
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
        super.tick();
    }

    // Override Inject Behavior methods in your entity class to inject behavior between smth happening.
    //  Feel free to create your own empty inject behavior methods.

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

    public void initiateChaseSequence (Player player, Level level) {
        if (this.distanceTo(player) < distanceToPlayerForChase()) {
            canMoveToPlayerClassPath = false;
            chaseStart = true;
        }
        if (chaseStart) {
            timeAliveRun = false;
            moveToPlayer(player, chasePlayerSpeedModifier());
            angerChaseRunTimer++;
            injectBehaviorInsideChaseSequence(player, level);
            if (angerChaseRunTimer == angerChaseTime()) this.discard();
        }
        else timeAliveRun = true;
    }

    public void injectBehaviorInsideChaseSequence (Player player, Level level) {
    }

    private void moveToPlayer (Player player, double speedMod) {
        tickCountBaseMonster++;
        if (tickCountBaseMonster == 10) {
            this.getNavigation().moveTo(player, speedMod);
            tickCountBaseMonster = 0;
        }
    }

    private void entityMoveToPlayer (Player player, Level level) {
        injectBehaviorBeforePlayerChase(player, level);
        isAngry = true;
        // Makes entity move to player. Cancel this with canMoveToPlayerClassPath
        if (canMoveToPlayerClassPath()) moveToPlayer(player, moveToPlayerSpeedModifier());
        injectBehaviorAfterPlayerChase(player, level);

        // Chase Sequence initiated here
        if (initiateChaseSequence()) initiateChaseSequence(player, level);
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

}
