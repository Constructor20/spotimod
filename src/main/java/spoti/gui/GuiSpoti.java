package spoti.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiSpoti extends GuiScreen {
    private int guiLeft, guiTop;
    private int xSize = 200;
    private int ySize = 150;
    private int dragOffsetX, dragOffsetY;
    private boolean dragging = false;

    private boolean isEditing = false;

    private SpotiButton edithud;
    private SpotiButton editLocationButton;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, this.height, 0x88000000);
        drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, 0xFF202020);
        drawCenteredString(fontRendererObj, "SpotiMod GUI", guiLeft + xSize / 2, guiTop + 5, 0xFFFFFF);

        if (edithud != null) edithud.drawButton(mouseX, mouseY);
        if (editLocationButton != null) editLocationButton.drawButton(mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        edithud = new SpotiButton(guiLeft + 15, guiTop + 30, 170, 20, "Edit HUD");
        editLocationButton = new SpotiButton(guiLeft + 15, guiTop + 65, 170, 20, "Edit Location");
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (edithud != null && edithud.isMouseOver(mouseX, mouseY)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiEditHUD());
        }


        if (editLocationButton != null && editLocationButton.isMouseOver(mouseX, mouseY)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiEditLocation());
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        dragging = false;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (dragging && clickedMouseButton == 0) {
            guiLeft = mouseX - dragOffsetX;
            guiTop = mouseY - dragOffsetY;
        }
    }

    // Supprimé : plus besoin de actionPerformed car on utilise SpotiButton
}
