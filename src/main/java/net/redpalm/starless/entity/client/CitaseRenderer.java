package net.redpalm.starless.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.CitaseEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CitaseRenderer extends GeoEntityRenderer<CitaseEntity> {
    public CitaseRenderer(EntityRendererProvider.Context context) {
        super(context, new CitaseModel());
    }

    @Override
    protected int getBlockLightLevel(CitaseEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(CitaseEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/citase.png");
    }
}
