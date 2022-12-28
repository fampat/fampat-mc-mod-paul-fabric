package de.fampat.paul.client;

import de.fampat.paul.EntryMain;
import de.fampat.paul.client.entities.models.PaulModel;
import de.fampat.paul.client.entities.renderer.PaulRenderer;
import de.fampat.paul.client.events.KeyStroke;
import de.fampat.paul.client.networking.PaulBoneClientListener;
import de.fampat.paul.registry.ModRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

import net.minecraft.client.render.entity.model.EntityModelLayer;

@Environment(EnvType.CLIENT)
public class EntryClient implements ClientModInitializer {
	public static final EntityModelLayer PAUL_MODEL_LAYER = new EntityModelLayer(EntryMain.id("paul"), "main");

	@Override
	public void onInitializeClient() {
        // Register Paul entity and model
		EntityRendererRegistry.register(ModRegistry.PAUL_ENTITY_TYPE, PaulRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(PAUL_MODEL_LAYER, () -> PaulModel.getTexturedModelData());

        // Initialize the listener that renders Pauls bone on all clients
        PaulBoneClientListener.register();

        // Initialize the key-event listeners
        KeyStroke.registerPaulSpawnToggle();
        KeyStroke.registerPaulSitToggle();
	}
}
