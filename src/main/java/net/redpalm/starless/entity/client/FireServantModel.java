package net.redpalm.starless.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.FireServantEntity;
import software.bernie.geckolib.model.GeoModel;

public class FireServantModel extends GeoModel<FireServantEntity> {
    @Override
    public ResourceLocation getModelResource(FireServantEntity fireServantEntity) {
        return new ResourceLocation(Starless.MODID, "geo/fire_servant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FireServantEntity fireServantEntity) {
        return new ResourceLocation(Starless.MODID, "textures/entity/fire_servant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FireServantEntity fireServantEntity) {
        return new ResourceLocation(Starless.MODID, "animations/fire_servant.animations.json");
    }
}
