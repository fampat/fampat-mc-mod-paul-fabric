package de.fampat.paul.registry;

import de.fampat.paul.Mod;
import de.fampat.paul.entities.PaulEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.sound.SoundEvent;

public final class PaulRegistry {
	private PaulRegistry() {
		throw new UnsupportedOperationException("Instantiate a class only containing static definitions makes now sense, bail.");
	}

	/* Entities */
	public static final EntityType<PaulEntity> PAUL_ENTITY_TYPE = Registry.register(
		Registry.ENTITY_TYPE,
		Mod.id("paul"),
		FabricEntityTypeBuilder.<PaulEntity>createMob()
			.entityFactory(PaulEntity::new)
			.defaultAttributes(PaulEntity::createPaulAttributes)
			.dimensions(EntityDimensions.changing(1.5f, 1.4f))
			.build()
	);

	/* Sounds */
	public static final SoundEvent PAUL_AMBIENT = registerSound("paul.ambient");
	public static final SoundEvent PAUL_BARK_0 = registerSound("paul.bark.0");
	public static final SoundEvent PAUL_BARK_1 = registerSound("paul.bark.1");
	public static final SoundEvent PAUL_BARK_2 = registerSound("paul.bark.2");
	public static final SoundEvent PAUL_WALK_0 = registerSound("paul.walk.0");

    // Sound registerer
	private static SoundEvent registerSound(String path) {
		var modId = Mod.id(path);
		return Registry.register(Registry.SOUND_EVENT, modId, new SoundEvent(modId));
	}

	public static void init() {
		Mod.LOGGER.info("PAUL: Woof world! iam now registered, woof!");
	}
}