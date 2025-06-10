package spoti.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiSpotifyHUD extends GuiScreen {

    public GuiSpotifyHUD() {
        Minecraft mc = Minecraft.getMinecraft();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = mc.displayWidth;
        this.height = mc.displayHeight;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Fond noir transparent
        drawRect(10, 10, 210, 60, 0x90000000);
        drawCenteredString(fontRendererObj, "Spotify HUD activé", 110, 30, 0xFFFFFF);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
