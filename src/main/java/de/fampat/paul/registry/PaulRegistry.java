package de.fampat.paul.registry;

import de.fampat.paul.Mod;
import de.fampat.paul.entities.PaulEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;

public final class PaulRegistry {
	private PaulRegistry() {
		throw new UnsupportedOperationException("Instantiate a class only containing static definitions makes now sense, bail.");
	}

	/* Entity */
	public static final EntityType<PaulEntity> PAUL_ENTITY_TYPE = Registry.register(
		Registry.ENTITY_TYPE,
		Mod.id("paul"),
		FabricEntityTypeBuilder.<PaulEntity>createMob()
			.entityFactory(PaulEntity::new)
			.defaultAttributes(PaulEntity::createPaulAttributes)
			.dimensions(EntityDimensions.changing(0.75f, 0.4f))
			.build()
	);

	/* Sounds */
	//public static final SoundEvent SNAIL_DEATH_SOUND_EVENT = registerSound("entity.lovely_snails.snail.death");
	//public static final SoundEvent SNAIL_HURT_SOUND_EVENT = registerSound("entity.lovely_snails.snail.hurt");

/* 	private static SoundEvent registerSound(String path) {
		var id = id(path);
		return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
	} */

	public static void init() {
		Mod.LOGGER.info("PAUL: Woof world! iam now registered, woof!");
	}
}
