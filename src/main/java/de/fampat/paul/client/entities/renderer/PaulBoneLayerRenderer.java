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
            PaulEntity paulEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        boolean paulIsSleeping = paulEntity.isSleeping();

        // If he has no bone, dont render it
        if (!paulEntity.isCarryBone() || paulIsSleeping)
            return;

        matrixStack.push();

        matrixStack.translate(this.getContextModel().head.pivotX / 16.0f,
                this.getContextModel().head.pivotY / 16.0f,
                this.getContextModel().head.pivotZ / 16.0f);

        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch));

        matrixStack.translate(0.06f, 0.27f, -0.5);
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));

        ItemStack itemStack = new ItemStack(Items.BONE);
        this.heldItemRenderer.renderItem(paulEntity, itemStack, ModelTransformation.Mode.GROUND, false, matrixStack,
                vertexConsumerProvider, light);
        matrixStack.pop();
    }
}
