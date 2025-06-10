package spoti.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import spoti.SpotiMod;

public class HudMusique {

    public static void render(float partialTicks) {
        if (!SpotiMod.isHudVisible()) return;

        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer font = mc.fontRendererObj;
        ScaledResolution res = new ScaledResolution(mc);

        int x = SpotiMod.savedMusicX;
        int y = SpotiMod.savedMusicY;
        int width = SpotiMod.savedMusicWidth;
        int height = SpotiMod.savedMusicHeight;

        // Fallback position par défaut si jamais
        if (x == -1 || y == -1) {
            x = res.getScaledWidth() - 220;
            y = 20;
            width = 200;
            height = 100;
        }

        // Dessin de la fenêtre
        drawRect(x, y, x + width, y + height, 0x90000000);
        font.drawString("♫ Fenêtre Musique", x + 8, y + 8, 0xFFFFFF);
    }


    // Méthode utilitaire pour dessiner un rectangle (Minecraft 1.8.9)
    private static void drawRect(int left, int top, int right, int bottom, int color) {
        net.minecraft.client.gui.Gui.drawRect(left, top, right, bottom, color);
    }
}
