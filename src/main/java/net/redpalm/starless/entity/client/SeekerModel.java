package net.redpalm.starless.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.SeekerEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class SeekerModel extends GeoModel<SeekerEntity> {
    @Override
    public ResourceLocation getModelResource(SeekerEntity seekerEntity) {
        return new ResourceLocation(Starless.MODID, "geo/seeker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeekerEntity seekerEntity) {
        return new ResourceLocation(Starless.MODID, "textures/entity/seeker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeekerEntity seekerEntity) {
        return new ResourceLocation(Starless.MODID, "animations/seeker.animation.json");
    }

    @Override
    public void setCustomAnimations(SeekerEntity animatable, long instanceId,
                                    AnimationState<SeekerEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
