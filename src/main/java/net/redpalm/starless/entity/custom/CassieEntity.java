package net.redpalm.starless.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.redpalm.starless.util.StarlessSavedData;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class CassieEntity extends PathfinderMob implements GeoEntity {
    private int timeAlive = 0;
    private int tickCountCassie = 0;
    private int tickCountChoosePlayer = 0;
    private int tickCountCheckHealth = 0;
    private int tickCountGoalStateCheck = 0;
    private int moodValue = -1;
    private int maxTimeAlive = 20*120;
    private int moodVariant;
    private boolean startedFollowingPlayer = false;
    private boolean setTimeAlive = false;
    private boolean goalStateSet = false;
    private boolean cassieFirstSpawnSequenceOn = false;
    private boolean canStealFood = false;
    private boolean hasStolenFood = false;
    private boolean chosePlayfulMoodVariant = false;
    private boolean saidMessageOnDying = false;
    private boolean stopFollowingPlayer = false;
    private boolean canGiveEffectOrGift = false;
    public static boolean cassieFirstSpawn = true;
    private Player playerToChoose;

    public CassieEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", state -> {
            if (state.isMoving())
                return state.setAndContinue(CassieEntity.this.isSprinting() ?
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
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 700f)
                .add(Attributes.FOLLOW_RANGE, 100f)
                .add(Attributes.MOVEMENT_SPEED, 0.5f);
    }

    @Override
    public float getStepHeight() {
        return 12f;
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource != damageSources().genericKill()) {
            return false;
        }
        else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    public void kill() {
        super.kill();
        this.setInvisible(true);
        if (!saidMessageOnDying && !level().isClientSide) {
            cassieSpeech(this.level(), "Boring.");
            saidMessageOnDying = true;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (timeAlive == 0) {
            moodValue = level().random.nextInt(4);
        }
        timeAlive++;

        if (timeAlive == maxTimeAlive) {
            this.discard();
        }

        if (!level().isClientSide) {
            if (!cassieFirstSpawn && !cassieFirstSpawnSequenceOn) cassieRandomMood(this);
            else cassieFirstSpawnSequence(this);
            checkHealth();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("TimeAlive", this.timeAlive);
        pCompound.putInt("moodValue", this.moodValue);
        pCompound.putBoolean("startFollowingPlayer", this.startedFollowingPlayer);
        pCompound.putBoolean("goalStateSet", this.goalStateSet);
        pCompound.putBoolean("stopFollowingPlayer", this.stopFollowingPlayer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("TimeAlive")) {
            this.timeAlive = pCompound.getInt("TimeAlive");
            this.moodValue = pCompound.getInt("moodValue");
            this.startedFollowingPlayer = pCompound.getBoolean("startFollowingPlayer");
            this.goalStateSet = pCompound.getBoolean("goalStateSet");
            this.stopFollowingPlayer = pCompound.getBoolean("stopFollowingPlayer");
        }
    }

    public void cassieFirstSpawnSequence (CassieEntity entity) {
        choosePlayer(entity);
        cassieFirstSpawnSequenceOn = true;
        if (!setTimeAlive) {
            maxTimeAlive = 20*35;
            setTimeAlive = true;
        }
        if (playerToChoose != null) {
            entity.lookControl.setLookAt(playerToChoose);
            if (timeAlive == 40) cassieSpeech(entity.level(), "Hello.");
            if (timeAlive == 80) cassieSpeech(entity.level(), "Don't worry. I won't hurt you.");
            if (timeAlive == 120) cassieSpeech(entity.level(), "I am just a little bit bored. And you and your" +
                    " world look quite interesting.");
            if (timeAlive == 140) {
                cassieFirstSpawn = false;
                StarlessSavedData.save(entity.getServer());
            }
            if (timeAlive == 200) {
                cassieFirstSpawn = false;
                StarlessSavedData.save(entity.getServer());
            }
        }
    }

    private void moveToPlayer (Player player, double speedMod) {
        tickCountCassie++;
        if (tickCountCassie == 10) {
            this.getNavigation().moveTo(player, speedMod);
            tickCountCassie = 0;
        }
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

    public void cassieRandomMood (CassieEntity entity) {
        if (moodValue == MoodType.BORED.val) cassieBoredMood(entity);
        else if (moodValue == MoodType.CALM.val) cassieCalmMood(entity);
        else if (moodValue == MoodType.PLAYFUL.val) cassiePlayfulMood(entity);
        else if (moodValue == MoodType.CURIOUS.val) cassieCuriousMood(entity);
    }

    public void cassieBoredMood (CassieEntity entity) {
        choosePlayer(entity);
        checkGoal(entity);
        if (!setTimeAlive) {
            maxTimeAlive = 20*60;
            setTimeAlive = true;
        }
        if (playerToChoose != null) {
            if (timeAlive == 20) {
                this.goalSelector.addGoal(3, new LookAtPlayerGoal (entity, Player.class, 100f, 0.15f));
                goalStateSet = true;
            }
            if (timeAlive == 100) this.goalSelector.addGoal(2, new RandomLookAroundGoal(entity));
            if (timeAlive == 160) this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(entity, 0.5f));
        }
    }

    public void cassieCalmMood (CassieEntity entity) {
        choosePlayer(entity);
        if (!setTimeAlive) {
            maxTimeAlive = 20*60;
            setTimeAlive = true;
        }
        if (playerToChoose != null) {
            for (int i = 0; i < 140; i++) {
                entity.lookControl.setLookAt(playerToChoose);
                //if (i == 130)
            }
        }
    }

    public void cassiePlayfulMood (CassieEntity entity) {
        choosePlayer(entity);
        if (playerToChoose != null) {
            cassieFollowPlayer(entity, 0.85f, true, true);
            if (!chosePlayfulMoodVariant) {
                moodVariant = random.nextInt(2);
                chosePlayfulMoodVariant = true;
            }
            if (moodVariant == 0) {
                cassiePlayfulFirstVariant(entity);
                chosePlayfulMoodVariant = true;
            }
            else {
                cassiePlayfulSecondVariant(entity);
                chosePlayfulMoodVariant = true;
            }
        }
    }

    public void cassieCuriousMood (CassieEntity entity) {
        choosePlayer(entity);
        if (playerToChoose != null) {
            cassieFollowPlayer(entity, 0.55f, false, true);
        }
    }

    public void cassieFollowPlayer (CassieEntity entity, float speedMod, boolean isFollowingUnconditionally,
                                    boolean isWatchingAfterStopping) {
        if (isWatchingAfterStopping && stopFollowingPlayer) entity.lookControl.setLookAt(playerToChoose);
        if (!stopFollowingPlayer) {
            if (!isFollowingUnconditionally) {
                entity.lookControl.setLookAt(playerToChoose);
                if (!startedFollowingPlayer) {
                    if (entity.isWithinMeleeAttackRange(playerToChoose)) startedFollowingPlayer = true;
                    moveToPlayer(playerToChoose, speedMod);
                } else if (entity.distanceTo(playerToChoose) > 30f) {
                    startedFollowingPlayer = false;
                }
            }
            else {
                entity.lookControl.setLookAt(playerToChoose);
                if (!startedFollowingPlayer && !entity.isWithinMeleeAttackRange(playerToChoose)) {
                    if (entity.isWithinMeleeAttackRange(playerToChoose)) startedFollowingPlayer = true;
                    moveToPlayer(playerToChoose, speedMod);
                }
                else if (!entity.isWithinMeleeAttackRange(playerToChoose)) {
                    startedFollowingPlayer = false;
                }
            }
        }
    }

    public enum MoodType {
        BORED(0),
        CALM(1),
        PLAYFUL(2),
        CURIOUS(3);

        private final int val;

        MoodType(int i) {
            val = i;
        }
    }
    public void choosePlayer (CassieEntity entity) {
        if (playerToChoose == null) {
            tickCountChoosePlayer++;
            if (tickCountChoosePlayer == 40) {
                playerToChoose = entity.level().getNearestPlayer(entity, 200D);
                tickCountChoosePlayer = 0;
            }
        }
    }

    public void checkHealth () {
        tickCountCheckHealth++;
        if (tickCountCheckHealth == 60) {
            if (this.getHealth() < this.getMaxHealth() && this.getLastDamageSource() !=
                    this.damageSources().genericKill()) this.setHealth(this.getMaxHealth());
            tickCountCheckHealth = 0;
        }
    }

    public void checkGoal (CassieEntity entity) {
        tickCountGoalStateCheck++;
        if (tickCountGoalStateCheck == 80) {
            if (this.goalSelector.getRunningGoals().noneMatch
                    (goal -> goal.getGoal().getClass() == LookAtPlayerGoal.class)) {
                this.goalSelector.addGoal(3, new LookAtPlayerGoal
                        (entity, Player.class, 100f, 0.15f));
            }

            if (this.goalSelector.getRunningGoals().noneMatch
                    (goal -> goal.getGoal().getClass() == RandomLookAroundGoal.class)) {
                this.goalSelector.addGoal(2, new RandomLookAroundGoal(entity));
            }

            if (this.goalSelector.getRunningGoals().noneMatch
                    (goal -> goal.getGoal().getClass() == WaterAvoidingRandomStrollGoal.class)) {
                this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(entity, 0.5f));
            }
            tickCountGoalStateCheck = 0;
        }
    }

    public void cassieSpeech(Level level, String answer) {
        level.getServer().getPlayerList().broadcastSystemMessage(Component.literal
                ("§4<" + "§kCassiopeia" + "§4> " + answer), false);
    }

    public void cassiePlayfulFirstVariant (CassieEntity entity) {
        if (timeAlive == 220) {
            canStealFood = true;
        }
        if (canStealFood && playerToChoose != null && !hasStolenFood && hasEdibleFood(playerToChoose)) {
            if (entity.isWithinMeleeAttackRange(playerToChoose)) {
                ItemStack food = playerToChoose.getInventory().items.stream().filter(stack -> !stack.isEmpty() && (stack.getItem()
                        == Items.COOKED_BEEF || stack.getItem() == Items.COOKED_CHICKEN || stack.getItem() ==
                        Items.COOKED_COD || stack.getItem() == Items.BREAD || stack.getItem() ==
                        Items.COOKED_MUTTON || stack.getItem() == Items.COOKED_RABBIT || stack.getItem() ==
                        Items.COOKED_PORKCHOP || stack.getItem() == Items.COOKED_SALMON || stack.getItem() ==
                        Items.BAKED_POTATO || stack.getItem() == Items.RABBIT_STEW || stack.getItem() ==
                        Items.PUMPKIN_PIE || stack.getItem() == Items.BEETROOT_SOUP || stack.getItem() ==
                        Items.COOKIE || stack.getItem() == Items.GOLDEN_CARROT || stack.getItem() == Items.CAKE)).findFirst().get();
                int i = playerToChoose.getInventory().items.indexOf(food);
                playerToChoose.getInventory().removeItem(i, 1);
                hasStolenFood = true;
                stopFollowingPlayer = true;
            }
        }
    }

    public void cassiePlayfulSecondVariant (CassieEntity entity) {
        if (timeAlive == 220) canGiveEffectOrGift = true;
        if (canGiveEffectOrGift) {
            if (random.nextInt(2) == 0) cassieGiveRandomEffect(entity);
            else cassieGiveRandomEffect(entity);
        }
    }

    public void cassieGiveRandomEffect (CassieEntity entity) {
        if (entity.isWithinMeleeAttackRange(playerToChoose)) {
            int effectToChoose = random.nextInt(5);
            switch (effectToChoose) {
                case 0: playerGetRandomEffect(playerToChoose, MobEffects.GLOWING);
                    break;
                case 1: playerGetRandomEffect(playerToChoose, MobEffects.DAMAGE_RESISTANCE);
                    break;
                case 2: playerGetRandomEffect(playerToChoose, MobEffects.DIG_SPEED);
                    break;
                case 3: playerGetRandomEffect(playerToChoose, MobEffects.MOVEMENT_SPEED);
                    break;
                case 4: playerGetRandomEffect(playerToChoose, MobEffects.SLOW_FALLING);
                    break;
            }
            canGiveEffectOrGift = false;
            stopFollowingPlayer = true;
        }
    }

    public void playerGetRandomEffect (Player player, MobEffect effect) {
        player.addEffect(new MobEffectInstance(effect, 12000));
    }

    public boolean hasEdibleFood(Player player) {
        return player.getInventory().items.stream()
                .anyMatch(stack -> !stack.isEmpty() && (stack.getItem() == Items.COOKED_BEEF ||
                        stack.getItem() == Items.COOKED_CHICKEN || stack.getItem() == Items.COOKED_COD ||
                        stack.getItem() == Items.COOKED_MUTTON || stack.getItem() == Items.BREAD ||
                        stack.getItem() == Items.COOKED_RABBIT || stack.getItem() == Items.COOKED_PORKCHOP ||
                        stack.getItem() == Items.COOKED_SALMON || stack.getItem() == Items.BAKED_POTATO ||
                        stack.getItem() == Items.RABBIT_STEW || stack.getItem() == Items.PUMPKIN_PIE ||
                        stack.getItem() == Items.BEETROOT_SOUP || stack.getItem() == Items.COOKIE ||
                        stack.getItem() == Items.GOLDEN_CARROT || stack.getItem() == Items.CAKE));
    }

}
