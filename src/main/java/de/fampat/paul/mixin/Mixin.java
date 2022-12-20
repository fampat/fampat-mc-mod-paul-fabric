package de.fampat.paul.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.fampat.paul.Mod;

@org.spongepowered.asm.mixin.Mixin(TitleScreen.class)
public class Mixin {
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		Mod.LOGGER.info("MOEP: This line is printed by an example mod mixin!");
	}
}
