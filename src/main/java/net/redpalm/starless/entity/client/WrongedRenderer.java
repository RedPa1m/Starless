package net.redpalm.starless.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.WrongedEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class WrongedRenderer extends GeoEntityRenderer<WrongedEntity> {
    public WrongedRenderer(EntityRendererProvider.Context context) {
        super(context, new WrongedModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(WrongedEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/wronged.png");
    }
}
