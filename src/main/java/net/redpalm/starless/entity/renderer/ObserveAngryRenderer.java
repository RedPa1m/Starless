package net.redpalm.starless.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.entity.custom.ObserveAngryEntity;
import net.redpalm.starless.entity.model.ObserveAngryModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ObserveAngryRenderer extends GeoEntityRenderer<ObserveAngryEntity> {

    public ObserveAngryRenderer(EntityRendererProvider.Context context) {
        super(context, new ObserveAngryModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ObserveAngryEntity animatable) {
        return pickObserveTexture(animatable);
    }

    public ResourceLocation pickObserveTexture (ObserveAngryEntity entity) {
        return entity.getRandomNumberForTexture() == 0 ?
                ResourceLocation.parse("starless:textures/entity/observe_angry.png") :
                ResourceLocation.parse("starless:textures/entity/observe_angry_fluffy.png");
    }
}
