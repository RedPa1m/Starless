package net.redpalm.starless.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.CitaseEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CitaseModel extends GeoModel<CitaseEntity> {
    @Override
    public ResourceLocation getModelResource(CitaseEntity citaseEntity) {
        return new ResourceLocation(Starless.MODID, "geo/citase.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CitaseEntity citaseEntity) {
        return new ResourceLocation(Starless.MODID, "textures/entity/citase.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CitaseEntity citaseEntity) {
        return new ResourceLocation(Starless.MODID, "animations/citase.animation.json");
    }

    @Override
    public void setCustomAnimations(CitaseEntity animatable, long instanceId, AnimationState<CitaseEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
