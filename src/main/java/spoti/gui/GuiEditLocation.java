package spoti.gui;

import net.minecraft.client.gui.GuiScreen;
import spoti.SpotiMod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiEditLocation extends GuiScreen {

    private final List<SpotiButton> buttons = new ArrayList<SpotiButton>();
    private final int BUTTON_WIDTH = 120;
    private final int BUTTON_HEIGHT = 24;

    // Fenêtre Musique
    private int musicX = 50, musicY = 50;
    private int musicWidth = 200, musicHeight = 100;
    private boolean dragging = false;
    private boolean resizing = false;
    private int dragOffsetX, dragOffsetY;
    private final int RESIZE_MARGIN = 8;

    @Override
    public void onGuiClosed() {
        SpotiMod.saveWindowLocation(musicX, musicY, musicWidth, musicHeight);
    }

    @Override
    public void initGui() {
        buttons.clear();

        int centerX = width / 2;
        int centerY = height / 2;

        buttons.add(new SpotiButton(centerX - BUTTON_WIDTH / 2, centerY - 30, BUTTON_WIDTH, BUTTON_HEIGHT, "Reset Locations"));
        buttons.add(new SpotiButton(centerX - BUTTON_WIDTH / 2, centerY + 10, BUTTON_WIDTH, BUTTON_HEIGHT, "Exit Edit Location"));

        // Chargement de la position sauvegardée
        if (SpotiMod.savedMusicX != -1 && SpotiMod.savedMusicY != -1) {
            musicX = SpotiMod.savedMusicX;
            musicY = SpotiMod.savedMusicY;
            musicWidth = SpotiMod.savedMusicWidth;
            musicHeight = SpotiMod.savedMusicHeight;
        } else {
            // Position par défaut (comme dans Reset)
            musicX = width - 220;
            musicY = 20;
            musicWidth = 200;
            musicHeight = 100;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Aucun fond de GUI

        // Fenêtre musique
        drawRect(musicX, musicY, musicX + musicWidth, musicY + musicHeight, 0x90000000);
        drawCenteredString(fontRendererObj, "Fenêtre Musique", musicX + musicWidth / 2, musicY + 5, 0xFFFFFF);

        // Zone de redimensionnement visible
        drawRect(musicX + musicWidth - RESIZE_MARGIN, musicY + musicHeight - RESIZE_MARGIN,
                musicX + musicWidth, musicY + musicHeight, 0x50FFFFFF);

        // Boutons
        for (SpotiButton button : buttons) {
            button.drawButton(mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // Zone de redimensionnement
        if (mouseX >= musicX + musicWidth - RESIZE_MARGIN && mouseX <= musicX + musicWidth &&
                mouseY >= musicY + musicHeight - RESIZE_MARGIN && mouseY <= musicY + musicHeight) {
            resizing = true;
            return;
        }

        // Zone de déplacement
        if (mouseX >= musicX && mouseX <= musicX + musicWidth &&
                mouseY >= musicY && mouseY <= musicY + musicHeight) {
            dragging = true;
            dragOffsetX = mouseX - musicX;
            dragOffsetY = mouseY - musicY;
            return;
        }

        for (SpotiButton button : buttons) {
            if (button.isMouseOver(mouseX, mouseY)) {
                handleButtonClick(button);
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (dragging) {
            musicX = mouseX - dragOffsetX;
            musicY = mouseY - dragOffsetY;
            SpotiMod.saveWindowLocation(musicX, musicY, musicWidth, musicHeight);
        }

        if (resizing) {
            musicWidth = Math.max(100, mouseX - musicX);
            musicHeight = Math.max(60, mouseY - musicY);
            SpotiMod.saveWindowLocation(musicX, musicY, musicWidth, musicHeight);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
        resizing = false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void handleButtonClick(SpotiButton button) {
        if ("Reset Locations".equals(button.getLabel())) {
            // Réinitialise la position et la taille de la fenêtre musique
            musicX = width - 220;
            musicY = 20;
            musicWidth = 200;
            musicHeight = 100;
            SpotiMod.resetWindowLocation(); // tu peux ajouter ça pour d’autres éléments
        } else if ("Exit Edit Location".equals(button.getLabel())) {
            mc.displayGuiScreen(new GuiSpoti());
        }
    }
}
