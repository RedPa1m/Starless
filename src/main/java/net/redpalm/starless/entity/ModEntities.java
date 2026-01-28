package net.redpalm.starless.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.ObserveAngryEntity;
import net.redpalm.starless.entity.custom.ObserveEntity;
import net.redpalm.starless.entity.custom.WrongedEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Starless.MODID);

    public static final RegistryObject<EntityType<ObserveEntity>> OBSERVE =
            ENTITY_TYPES.register("observe", () -> EntityType.Builder.of(ObserveEntity::new, MobCategory.CREATURE)
                    .sized(1f, 1.5f).build("observe"));

    public static final RegistryObject<EntityType<WrongedEntity>> WRONGED =
            ENTITY_TYPES.register("wronged", () -> EntityType.Builder.of(WrongedEntity::new, MobCategory.CREATURE)
                    .sized(1f, 1.5f).build("wronged"));

    public static final RegistryObject<EntityType<ObserveAngryEntity>> OBSERVE_ANGRY =
            ENTITY_TYPES.register("observe_angry", () -> EntityType.Builder.of(ObserveAngryEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 1.5f).build("observe_angry"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
