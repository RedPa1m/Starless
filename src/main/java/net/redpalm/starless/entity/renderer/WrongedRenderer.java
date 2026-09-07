package net.redpalm.starless.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.model.WrongedModel;
import net.redpalm.starless.entity.custom.WrongedEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Random;

public class WrongedRenderer extends GeoEntityRenderer<WrongedEntity> {
    private int randomNumber;
    private boolean isNighttime;

    public WrongedRenderer(EntityRendererProvider.Context context) {
        super(context, new WrongedModel());
    }

    @Override
    protected int getBlockLightLevel(WrongedEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(WrongedEntity animatable) {
        return pickWrongedTexture(animatable);
    }

    public ResourceLocation pickWrongedTexture (WrongedEntity entity) {
        randomNumber = entity.getRandomNumberForTexture();
        isNighttime = entity.getIsNighttime();
        if (entity.level().getGameTime() > 24000 || entity.getPastFirstDay()) {
            for (int i = 1; i < 4; i++) {
                if (randomNumber == i) {
                    if (isNighttime || entity.level().isNight() || entity.level().getDayTime() > 13000)
                        return new ResourceLocation(Starless.MODID, "textures/entity/wronged_fedora_night.png");
                    else return new ResourceLocation(Starless.MODID, "textures/entity/wronged_fedora.png");
                } else if (i == 3) {
                    if (randomNumber == 4 || randomNumber == 5)
                        if (isNighttime || entity.level().isNight() || entity.level().getDayTime() > 13000)
                            return new ResourceLocation(Starless.MODID, "textures/entity/wronged_emo_night.png");
                        else return new ResourceLocation(Starless.MODID, "textures/entity/wronged_emo.png");
                }
            }
        }
        return pickDefaultTexture(entity);
    }

    public ResourceLocation pickDefaultTexture (WrongedEntity entity) {
        if (isNighttime || entity.level().isNight() || entity.level().getDayTime() > 13000) {
            return new ResourceLocation(Starless.MODID, "textures/entity/wronged_night.png");
        }
        else
            return new ResourceLocation(Starless.MODID, "textures/entity/wronged.png");
    }

}
