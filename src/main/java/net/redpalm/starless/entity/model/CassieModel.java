package net.redpalm.starless.entity.model;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.CassieEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CassieModel extends GeoModel<CassieEntity> {
    @Override
    public ResourceLocation getModelResource(CassieEntity cassieEntity) {
        return new ResourceLocation(Starless.MODID, "geo/citase.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CassieEntity cassieEntity) {
        return new ResourceLocation(Starless.MODID, "textures/entity/citase.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CassieEntity cassieEntity) {
        return new ResourceLocation(Starless.MODID, "animations/citase.animation.json");
    }

    @Override
    public void setCustomAnimations(CassieEntity animatable, long instanceId, AnimationState<CassieEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
