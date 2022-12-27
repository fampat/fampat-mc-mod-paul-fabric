package de.fampat.paul.client.entities.renderer;

import de.fampat.paul.client.entities.models.PaulModel;
import de.fampat.paul.entities.PaulEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3f;

@Environment(value = EnvType.CLIENT)
public class PaulBoneLayerRenderer extends FeatureRenderer<PaulEntity, PaulModel<PaulEntity>> {
    private final HeldItemRenderer heldItemRenderer;

    public PaulBoneLayerRenderer(FeatureRendererContext<PaulEntity, PaulModel<PaulEntity>> context,
            HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
            PaulEntity paulEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress,
            float headYaw, float headPitch) {
        boolean paulIsSleeping = paulEntity.isSleeping();

        // If he has no bone, dont render it
        if (!paulEntity.isCarryBone() || paulIsSleeping)
            return;

        matrixStack.push();

        // Put the bone in relation to the head
        matrixStack.translate(
            this.getContextModel().head.pivotX / 16.0f,
            this.getContextModel().head.pivotY / 16.0f,
            this.getContextModel().head.pivotZ / 16.0f
        );

        // Yaw and pitch the bone when the head does
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch));

        // Offset the bone that it fits the mouth perfectly
        matrixStack.translate(-0.1F, 0.05F, -0.4F);     

        // Rotate the bone that it alignes with the mouth angle
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(110.0f));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-45.0f));

        // Create the bone
        ItemStack itemStack = new ItemStack(Items.BONE);
        
        // Render it onto Pauls model
        this.heldItemRenderer.renderItem(
            paulEntity,
            itemStack,
            ModelTransformation.Mode.GROUND,
            false,
            matrixStack,
            vertexConsumerProvider, light
        );
        
        matrixStack.pop();
    }
}
