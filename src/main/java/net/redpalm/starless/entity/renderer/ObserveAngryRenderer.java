package net.redpalm.starless.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.model.ObserveAngryModel;
import net.redpalm.starless.entity.custom.ObserveAngryEntity;
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
                new ResourceLocation(Starless.MODID, "textures/entity/observe_angry.png") :
                new ResourceLocation(Starless.MODID, "textures/entity/observe_angry_fluffy.png");
    }
}
