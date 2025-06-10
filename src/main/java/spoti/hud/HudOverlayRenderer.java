package spoti.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import spoti.SpotiMod;

public class HudOverlayRenderer {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (!SpotiMod.isHudVisible()) return;

        FontRenderer fontRenderer = mc.fontRendererObj;
        String text = "🎵 SpotiMod HUD actif";  // Ce texte sera remplacé plus tard par le titre, artiste, etc.

        int x = 10; // position X
        int y = 10; // position Y

        fontRenderer.drawStringWithShadow(text, x, y, 0xFFFFFF); // blanc
    }
}
