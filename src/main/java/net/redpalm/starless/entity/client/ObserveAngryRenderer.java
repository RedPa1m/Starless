package net.redpalm.starless.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.entity.custom.ObserveAngryEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ObserveAngryRenderer extends GeoEntityRenderer<ObserveAngryEntity> {

    public ObserveAngryRenderer(EntityRendererProvider.Context context) {
        super(context, new ObserveAngryModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ObserveAngryEntity animatable) {
        return ResourceLocation.parse("starless:textures/entity/observe_angry.png");
    }
}
