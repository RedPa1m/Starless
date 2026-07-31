package net.redpalm.starless.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.ObserveEntity;
import software.bernie.geckolib.model.GeoModel;

public class ObserveModel extends GeoModel<ObserveEntity> {
    @Override
    public ResourceLocation getModelResource(ObserveEntity observeEntity) {
        return new ResourceLocation(Starless.MODID, "geo/observe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ObserveEntity observeEntity) {
        return new ResourceLocation(Starless.MODID, "textures/entity/observe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ObserveEntity observeEntity) {
        return new ResourceLocation(Starless.MODID, "animations/observe.animation.json");
    }

}
