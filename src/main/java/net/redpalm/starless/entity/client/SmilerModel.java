package net.redpalm.starless.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.SmilerEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SmilerModel extends GeoModel<SmilerEntity> {
    @Override
    public ResourceLocation getModelResource(SmilerEntity smilerEntity) {
        return new ResourceLocation(Starless.MODID, "geo/smiler.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmilerEntity smilerEntity) {
        return new ResourceLocation(Starless.MODID, "textures/entity/smiler.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SmilerEntity smilerEntity) {
        return new ResourceLocation(Starless.MODID, "animations/smiler.animation.json");
    }

    @Override
    public void setCustomAnimations(SmilerEntity animatable, long instanceId, AnimationState<SmilerEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
