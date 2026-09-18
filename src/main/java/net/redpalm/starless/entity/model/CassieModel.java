package net.redpalm.starless.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.redpalm.starless.entity.custom.CassieEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CassieModel extends GeoModel<CassieEntity> {
    @Override
    public ResourceLocation getModelResource(CassieEntity animatable) {
        return ResourceLocation.parse("starless:geo/cassie.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CassieEntity animatable) {
        return ResourceLocation.parse("starless:textures/entity/cassie.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CassieEntity animatable) {
        return ResourceLocation.parse("starless:animations/cassie.animation.json");
    }

    @Override
    public void setCustomAnimations(CassieEntity animatable, long instanceId, AnimationState<CassieEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
