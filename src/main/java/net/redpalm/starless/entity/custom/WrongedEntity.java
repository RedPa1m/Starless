package net.redpalm.starless.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.redpalm.starless.event.custom.WrongedChatEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import static net.redpalm.starless.Starless.queueServerWork;
import static net.redpalm.starless.event.custom.WrongedRegisterChatEvent.fireAnswer;

public class WrongedEntity extends Mob implements GeoEntity {
    private boolean canGiveItem;
    private int timeAlive = 0;
    public static boolean canChat = false;
    private int chancePhrase = 3;
    private int randomNumberForTexture;
    private boolean isNighttime;
    private boolean isPastFirstDay;
    public static boolean callbackWronged;
    private boolean calculatedRandomNumber;

    public int getTimeAlive () {
        return timeAlive;
    }

    public int getRandomNumberForTexture() {
        return randomNumberForTexture;
    }

    public boolean getIsNighttime () {
        return isNighttime;
    }

    public boolean getPastFirstDay () {
        return isPastFirstDay;
    }

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
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class,
                50f, 1f));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.FOLLOW_RANGE, 10f);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "blinking",
                animationState -> PlayState.CONTINUE).triggerableAnim("blink",
                RawAnimation.begin().then("blink", Animation.LoopType.PLAY_ONCE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean shouldDropExperience() {
        return false;
    }
    // Set his lifetime to 2400 ticks
    @Override
    public void tick() {
        if (!calculatedRandomNumber) {
            randomNumberForTexture = level().random.nextInt(20);
            calculatedRandomNumber = true;
        }
        if (this.timeAlive == 0 && !level().isClientSide) {
            isNighttime = level().getDayTime() > 13000 || level().isNight();
            isPastFirstDay = this.level().getGameTime() > 24000;
        }
        this.timeAlive++;
        if (this.timeAlive == 1) {
            this.canGiveItem = true;
            canChat = true;
            if (!level().isClientSide && !level().getServer().getPlayerList().getPlayers().isEmpty()) {
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> Hello."), false);
            }
        }

        if (!level().isClientSide && this.timeAlive == 40 && !level().getServer().getPlayerList().getPlayers().isEmpty()) {
            if (level().getRandom().nextInt(chancePhrase) == 0) {
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> Can I give you something? " +
                                "I hope it's okay."), false);
            }
            else if (level().getRandom().nextInt(chancePhrase) == 1) {
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> I can give you an item. " +
                                "Not sure if it's as useful as I think it is."), false);
            }
            else {
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> Can you take this item from me, if it's okay? " +
                                        "It gives me painful memories, but I don't want it to go to waste..."),
                                false);
            }
        }

        if (this.timeAlive == 2400) {
            this.remove(RemovalReason.KILLED);
            this.timeAlive = 0;
            if (!level().isClientSide) {
                level().getServer().getPlayerList().broadcastSystemMessage
                        (Component.literal("<Wrong.ed> Goodbye."), false);
            }
            this.canGiveItem = true;
        }

        if (this.isDeadOrDying() || this.isRemoved()) {
            canChat = false;
        }

        if (timeAlive % 100 == 0 && !level().isClientSide) {
            triggerAnim("blinking", "blink");
        }

        if (callbackWronged && !level().isClientSide) {
            WrongedChatEvent.wrongedResponse = "<Wrong.ed> I will make a noise for you.";
            if (canChat == true) fireAnswer = true;
            queueServerWork(40, () -> level().playSound(null, this.blockPosition(),
                        SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 4f, 1f));
            callbackWronged = false;
        }

        super.tick();
    }

    // Make it so he takes no damage unless falls out of the world or /kill applied
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource == damageSources().genericKill() || pSource == damageSources().fellOutOfWorld()) {
            return super.hurt(pSource, pAmount);
        }
        else return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("canGiveItem", this.canGiveItem);
        pCompound.putInt("TimeAlive", this.timeAlive);
        pCompound.putInt("randomNumberForTexture", this.randomNumberForTexture);
        pCompound.putBoolean("isNighttime", this.isNighttime);
        pCompound.putBoolean("isPastFirstDay", this.isPastFirstDay);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("canGiveItem")) {
            this.canGiveItem = pCompound.getBoolean("canGiveItem");
        }
        if (pCompound.contains("TimeAlive")) {
            this.timeAlive = pCompound.getInt("TimeAlive");
        }
        if (pCompound.contains("randomNumberForTexture")) {
            this.randomNumberForTexture = pCompound.getInt("randomNumberForTexture");
        }
        if (pCompound.contains("isNighttime")) {
            this.isNighttime = pCompound.getBoolean("isNighttime");
        }
        if (pCompound.contains("isPastFirstDay")) {
            this.isPastFirstDay = pCompound.getBoolean("isPastFirstDay");
        }
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }
    // emo
}
