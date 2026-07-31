package net.redpalm.starless.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.model.ObserveModel;
import net.redpalm.starless.entity.custom.ObserveEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ObserveRenderer extends GeoEntityRenderer<ObserveEntity> {
    public ObserveRenderer(EntityRendererProvider.Context context) {
        super(context, new ObserveModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ObserveEntity animatable) {
        return pickObserveTexture(animatable);
    }

    public ResourceLocation pickObserveTexture (ObserveEntity entity) {
        return entity.getRandomNumberForTexture() == 0 ?
                new ResourceLocation(Starless.MODID, "textures/entity/observe.png") :
                new ResourceLocation(Starless.MODID, "textures/entity/observe_fluffy.png");
    }
}
