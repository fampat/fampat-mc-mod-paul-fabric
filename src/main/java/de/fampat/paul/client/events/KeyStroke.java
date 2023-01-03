package de.fampat.paul.client.events;

import org.lwjgl.glfw.GLFW;

import de.fampat.paul.client.networking.PaulSitServerCaller;
import de.fampat.paul.client.networking.PaulSpawnServerCaller;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyStroke {
    private static KeyBinding keyBindingPaulSpawn, keyBindingPaulSit;

    // Spawn button press register
    public static void registerPaulSpawnToggle() {
        // Setup the key
        keyBindingPaulSpawn = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "paul_come_here.text.paul.fampat.de",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "paul.paul.fampat.de"
        ));

        // Handle key-press event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBindingPaulSpawn.wasPressed()) {
                // Spawn or despawn Paul
                PaulSpawnServerCaller.callServerWith();
            }
        });
    }

    // Sit button press register
    public static void registerPaulSitToggle() {
        // Setup the key
        keyBindingPaulSit = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "paul_sit.text.paul.fampat.de",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "paul.paul.fampat.de"
        ));

        // Handle key-press event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBindingPaulSit.wasPressed()) {
                // Sit or unsit Paul
                PaulSitServerCaller.callServerWith();
            }
        });
    }
}
