package net.redpalm.starless.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.entity.custom.SmilerEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SmilerRenderer extends GeoEntityRenderer<SmilerEntity> {
    public SmilerRenderer(EntityRendererProvider.Context context) {
        super(context, new SmilerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SmilerEntity animatable) {
        return ResourceLocation.parse("starless:textures/entity/smiler.png");
    }
}
