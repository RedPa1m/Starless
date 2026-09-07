package net.redpalm.starless.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.CassieEntity;
import net.redpalm.starless.entity.model.CassieModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CassieRenderer extends GeoEntityRenderer<CassieEntity> {
    public CassieRenderer(EntityRendererProvider.Context context) {
        super(context, new CassieModel());
    }

    @Override
    public ResourceLocation getTextureLocation(CassieEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/citase.png");
    }
}
