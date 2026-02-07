package net.redpalm.starless.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.ObserveAngryEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

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

    @Override
    public void setCustomAnimations(ObserveAngryEntity animatable, long instanceId,
                                    AnimationState<ObserveAngryEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
