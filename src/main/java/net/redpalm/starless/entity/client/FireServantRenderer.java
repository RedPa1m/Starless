package net.redpalm.starless.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.FireServantEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class FireServantRenderer extends GeoEntityRenderer<FireServantEntity> {
    protected ItemStack mainHandItem;

    public FireServantRenderer(EntityRendererProvider.Context context) {
        super(context, new FireServantModel());
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, FireServantEntity animatable) {
                ItemStack itemStack;
                if (bone.getName().equals("hand")) {
                    itemStack = FireServantRenderer.this.mainHandItem;
                }
                else {
                    itemStack = null;
                }
                return itemStack;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, FireServantEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.scale(0.3f, 0.3f, 0.3f);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(FireServantEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/fire_servant.png");
    }

    @Override
    protected int getBlockLightLevel(FireServantEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public void preRender(PoseStack poseStack, FireServantEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        this.mainHandItem = animatable.getMainHandItem();
    }
}
