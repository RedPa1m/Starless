package net.redpalm.starless.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.SeekerEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SeekerRenderer extends GeoEntityRenderer<SeekerEntity> {
    public SeekerRenderer(EntityRendererProvider.Context context) {
        super(context, new SeekerModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SeekerEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/seeker.png");
    }
}
