package net.redpalm.starless.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.WrongedEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WrongedRenderer extends GeoEntityRenderer<WrongedEntity> {
    public WrongedRenderer(EntityRendererProvider.Context context) {
        super(context, new WrongedModel());
    }

    @Override
    protected int getBlockLightLevel(WrongedEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(WrongedEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/wronged.png");
    }
}
