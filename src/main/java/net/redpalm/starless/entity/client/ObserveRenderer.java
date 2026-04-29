package net.redpalm.starless.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.entity.custom.ObserveEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ObserveRenderer extends GeoEntityRenderer<ObserveEntity> {
    public ObserveRenderer(EntityRendererProvider.Context context) {
        super(context, new ObserveModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ObserveEntity animatable) {
        return ResourceLocation.parse("starless:textures/entity/observe.png");
    }

}
