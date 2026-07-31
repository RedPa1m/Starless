package net.redpalm.starless.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.model.SmilerModel;
import net.redpalm.starless.entity.custom.SmilerEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SmilerRenderer extends GeoEntityRenderer<SmilerEntity> {
    public SmilerRenderer(EntityRendererProvider.Context context) {
        super(context, new SmilerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SmilerEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/smiler.png");
    }
}
