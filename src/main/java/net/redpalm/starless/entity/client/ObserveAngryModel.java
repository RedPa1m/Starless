package net.redpalm.starless.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.ObserveAngryEntity;
import software.bernie.geckolib.model.GeoModel;

public class ObserveAngryModel extends GeoModel<ObserveAngryEntity>  {
    @Override
    public ResourceLocation getModelResource(ObserveAngryEntity observeAngryEntity) {
        return new ResourceLocation(Starless.MODID, "geo/observe_angry.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ObserveAngryEntity observeAngryEntity) {
        return new ResourceLocation(Starless.MODID, "textures/entity/observe_angry.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ObserveAngryEntity observeAngryEntity) {
        return new ResourceLocation(Starless.MODID, "animations/observe_angry.animations.json");
    }
}
