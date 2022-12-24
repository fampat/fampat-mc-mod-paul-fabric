package de.fampat.paul.client;

import de.fampat.paul.Mod;
import de.fampat.paul.client.entities.models.PaulModel;
import de.fampat.paul.client.entities.renderer.PaulRenderer;
import de.fampat.paul.registry.PaulRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class PaulClient implements ClientModInitializer {
	public static final EntityModelLayer PAUL_MODEL_LAYER = new EntityModelLayer(Mod.id("paul"), "main");

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(PaulRegistry.PAUL_ENTITY_TYPE, PaulRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(PAUL_MODEL_LAYER, () -> PaulModel.getTexturedModelData());
	}
}
