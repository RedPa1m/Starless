package net.redpalm.starless.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.redpalm.starless.Starless;
import net.redpalm.starless.entity.custom.CitaseEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class CitaseRenderer extends GeoEntityRenderer<CitaseEntity> {
    protected ItemStack mainHandItem;

    public CitaseRenderer(EntityRendererProvider.Context context) {
        super(context, new CitaseModel());
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Override
            protected @Nullable ItemStack getStackForBone(GeoBone bone, CitaseEntity animatable) {
                ItemStack itemStack;
                if (bone.getName().equals("item")) {
                    itemStack = CitaseRenderer.this.mainHandItem;
                }
                else {
                    itemStack = null;
                }
                return itemStack;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, CitaseEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.scale(0.3f, 0.3f, 0.3f);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    protected int getBlockLightLevel(CitaseEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(CitaseEntity animatable) {
        return new ResourceLocation(Starless.MODID, "textures/entity/citase.png");
    }

    @Override
    public void preRender(PoseStack poseStack, CitaseEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        this.mainHandItem = animatable.getMainHandItem();
    }
}
