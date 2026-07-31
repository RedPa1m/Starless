package net.redpalm.starless.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.redpalm.starless.entity.custom.WrongedEntity;
import net.redpalm.starless.entity.model.WrongedModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

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
        if (entity.level().getGameTime() > 24000) {
            for (int i = 0; i < 3; i++) {
                if (randomNumber == i) {
                    if (isNighttime)
                        return ResourceLocation.parse("starless:textures/entity/wronged_fedora_night.png");
                    else return ResourceLocation.parse("starless:textures/entity/wronged_fedora.png");
                } else if (i == 2) {
                    if (randomNumber == 3 || randomNumber == 4)
                        if (isNighttime)
                            return ResourceLocation.parse("starless:textures/entity/wronged_emo_night.png");
                        else return ResourceLocation.parse("starless:textures/entity/wronged_emo.png");
                }
            }
        }
        return pickDefaultTexture(entity);
    }

    public ResourceLocation pickDefaultTexture (WrongedEntity entity) {
        if (isNighttime) {
            return ResourceLocation.parse("starless:textures/entity/wronged_night.png");
        }
        else
            return ResourceLocation.parse("starless:textures/entity/wronged.png");
    }

}
