package net.redpalm.starless.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.WrongedEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WrongedModel extends GeoModel<WrongedEntity> {
    @Override
    public ResourceLocation getModelResource(WrongedEntity wrongedEntity) {
        return new ResourceLocation(Starless.MODID, "geo/wronged.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WrongedEntity wrongedEntity) {
        return new ResourceLocation(Starless.MODID, "textures/entity/wronged.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WrongedEntity wrongedEntity) {
        return new ResourceLocation(Starless.MODID, "animations/wronged.animation.json");
    }

    @Override
    public void setCustomAnimations(WrongedEntity animatable, long instanceId,
                                    AnimationState<WrongedEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
