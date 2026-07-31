package net.redpalm.starless.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.redpalm.starless.entity.custom.SmilerEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.model.GeoModel;

public class SmilerModel extends GeoModel<SmilerEntity> {
    @Override
    public ResourceLocation getModelResource(SmilerEntity smilerEntity) {
        return ResourceLocation.parse("starless:geo/smiler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmilerEntity smilerEntity) {
        return ResourceLocation.parse("starless:textures/entity/smiler.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SmilerEntity smilerEntity) {
        return ResourceLocation.parse("starless:animations/smiler.animation.json");
    }

    @Override
    public void setCustomAnimations(SmilerEntity animatable, long instanceId, AnimationState<SmilerEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
