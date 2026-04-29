package net.redpalm.starless.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFood {

    public static final FoodProperties CORRUPTED_BREAD = new FoodProperties.Builder().alwaysEdible().nutrition(10)
            .saturationModifier(0.5f).effect(() -> new MobEffectInstance(MobEffects.GLOWING, 400), 1)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400), 1)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 400), 1)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 400), 1)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 60), 1).build();
}
