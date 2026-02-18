package net.redpalm.starless.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.FireServantEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class FireServantRenderer extends GeoEntityRenderer<FireServantEntity> {
    public FireServantRenderer(EntityRendererProvider.Context context) {
        super(context, new FireServantModel());
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(FireServantEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/fire_servant.png");
    }

    @Override
    protected int getBlockLightLevel(FireServantEntity pEntity, BlockPos pPos) {
        return 15;
    }
}
