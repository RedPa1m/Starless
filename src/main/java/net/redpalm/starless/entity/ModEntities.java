package net.redpalm.starless.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.*;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Starless.MODID);

    public static final Supplier<EntityType<ObserveEntity>> OBSERVE =
            ENTITY_TYPES.register("observe", () -> EntityType.Builder.of(ObserveEntity::new, MobCategory.CREATURE)
                    .sized(1f, 1.5f).build("observe"));

    public static final Supplier<EntityType<WrongedEntity>> WRONGED =
            ENTITY_TYPES.register("wronged", () -> EntityType.Builder.of(WrongedEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 1.5f).build("wronged"));

    public static final Supplier<EntityType<ObserveAngryEntity>> OBSERVE_ANGRY =
            ENTITY_TYPES.register("observe_angry", () -> EntityType.Builder.of(ObserveAngryEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.5f).build("observe_angry"));

    public static final Supplier<EntityType<FireServantEntity>> FIRE_SERVANT =
            ENTITY_TYPES.register("fire_servant", () -> EntityType.Builder.of(FireServantEntity::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.4f).build("fire_servant"));

    public static final Supplier<EntityType<CitaseEntity>> CITASE =
            ENTITY_TYPES.register("citase", () -> EntityType.Builder.of(CitaseEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 1.8f).build("citase"));

    public static final Supplier<EntityType<SeekerEntity>> SEEKER =
            ENTITY_TYPES.register("seeker", () -> EntityType.Builder.of(SeekerEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 2.75f).build("seeker"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
