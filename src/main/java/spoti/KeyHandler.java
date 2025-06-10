package spoti;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import spoti.gui.GuiSpoti;

public class KeyHandler {

    private static final int KEY_GUI = Keyboard.KEY_F9;
    private static final int KEY_TOGGLE_HUD = Keyboard.KEY_F10;

    private final KeyBinding openGuiKey;
    private final KeyBinding toggleHudKey;

    public KeyHandler() {
        openGuiKey = new KeyBinding("key.spotimod.gui", KEY_GUI, "key.categories.spotimod");
        toggleHudKey = new KeyBinding("key.spotimod.togglehud", KEY_TOGGLE_HUD, "key.categories.spotimod");

        ClientRegistry.registerKeyBinding(openGuiKey);
        ClientRegistry.registerKeyBinding(toggleHudKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        try {
            if (openGuiKey.isPressed()) {
                System.out.println("[SpotiMod] F9 pressé : ouverture de l'interface principale.");
                mc.displayGuiScreen(new GuiSpoti());
            }


            if (toggleHudKey.isPressed()) {
                System.out.println("[SpotiMod] F10 pressé : toggler le HUD Spotify.");
                SpotiMod.toggleHud(); // Active ou désactive le HUD
            }

        } catch (Exception e) {
            System.err.println("[SpotiMod] Erreur lors de la gestion des touches :");
            e.printStackTrace();
        }
    }
}
