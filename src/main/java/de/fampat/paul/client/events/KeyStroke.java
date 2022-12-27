package de.fampat.paul.client.events;

import org.lwjgl.glfw.GLFW;

import de.fampat.paul.client.networking.PaulSpawnServerCaller;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class KeyStroke {
    private static KeyBinding keyBindingPaulSpawn;

    public static void registerPaulComeHere() {
        // Setup the key
        keyBindingPaulSpawn = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "paul_come_here.paul.fampat.de",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "paul.paul.fampat.de"
        ));

        // Handle key-press event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBindingPaulSpawn.wasPressed()) {
                client.player.sendMessage(
                    Text.translatable("paul_come_here.text.paul.fampat.de"),
                    false
                );

                // Spawn Paul
                PaulSpawnServerCaller.callServerWith();
            }
        });
    }
}
