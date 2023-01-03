package de.fampat.paul.registry;

import de.fampat.paul.EntryMain;
import de.fampat.paul.entities.PaulEntity;
import de.fampat.paul.networking.PaulSitServerListener;
import de.fampat.paul.networking.PaulSpawnServerListener;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public final class ModRegistry {
	private ModRegistry() {
		throw new UnsupportedOperationException("Instantiate a class only containing static definitions makes now sense, bail.");
	}

    // Register Paul entity
    public static final EntityType<PaulEntity> PAUL_ENTITY_TYPE = Registry.register(
		Registries.ENTITY_TYPE,
		EntryMain.id("paul"),
		FabricEntityTypeBuilder.<PaulEntity>createMob()
			.entityFactory(PaulEntity::new)
			.defaultAttributes(PaulEntity::createPaulAttributes)
			.dimensions(EntityDimensions.changing(1.25f, 1.4f))
			.build()
	);

	// Register Paul sounds
	public static final SoundEvent PAUL_AMBIENT = registerSound("paul.ambient");
	public static final SoundEvent PAUL_BARK_0 = registerSound("paul.bark.0");
	public static final SoundEvent PAUL_BARK_1 = registerSound("paul.bark.1");
	public static final SoundEvent PAUL_BARK_2 = registerSound("paul.bark.2");
	public static final SoundEvent PAUL_WALK_0 = registerSound("paul.walk.0");

	private static SoundEvent registerSound(String path) {
		var modId = EntryMain.id(path);
		return Registry.register(
            Registries.SOUND_EVENT,
            modId,
            SoundEvent.of(modId)
        );
	}

	public static void init() {
        // Initialize the listener that spawns or unspawns Paul on the server
        PaulSpawnServerListener.register();

        // Initialize the listener that sits or unsits Paul on the server
        PaulSitServerListener.register();
	}
}
