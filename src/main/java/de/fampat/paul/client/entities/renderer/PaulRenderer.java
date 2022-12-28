package de.fampat.paul.client.entities.renderer;

import de.fampat.paul.EntryMain;
import de.fampat.paul.client.EntryClient;
import de.fampat.paul.client.entities.models.PaulModel;
import de.fampat.paul.entities.PaulEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PaulRenderer extends MobEntityRenderer<PaulEntity, PaulModel<PaulEntity>> {
    private static final Identifier TEXTURE = EntryMain.id("textures/entities/paul.png");

    public PaulRenderer(EntityRendererFactory.Context context) {
        super(context, new PaulModel<>(context.getPart(EntryClient.PAUL_MODEL_LAYER)), 0.5f);

        // Apply the bone-in-mouth renderer
        this.addFeature(new PaulBoneLayerRenderer(this, context.getHeldItemRenderer()));
    }

    @Override
    public void render(PaulEntity paulEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (paulEntity.isFurWet()) {
            float h = paulEntity.getFurWetBrightnessMultiplier(g);
            ((PaulModel<PaulEntity>)this.model).setColorMultiplier(h, h, h);
        }

        super.render(paulEntity, f, g, matrixStack, vertexConsumerProvider, i);
        
        if (paulEntity.isFurWet()) {
            ((PaulModel<PaulEntity>)this.model).setColorMultiplier(1.0f, 1.0f, 1.0f);
        }
    }

    @Override
    public Identifier getTexture(PaulEntity paulEntity) {        
        return TEXTURE;
    }
}

