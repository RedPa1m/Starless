package net.redpalm.starless.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.entity.custom.ObserveEntity;
import software.bernie.geckolib.model.GeoModel;

public class ObserveModel extends GeoModel<ObserveEntity> {
    @Override
    public ResourceLocation getModelResource(ObserveEntity observeEntity) {
        return ResourceLocation.parse("starless:geo/observe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ObserveEntity observeEntity) {
        return ResourceLocation.parse("starless:textures/entity/observe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ObserveEntity observeEntity) {
        return ResourceLocation.parse("starless:animations/observe.idle.json");
    }

}
