package spoti.gui;

import net.minecraft.client.Minecraft;

public class SpotiButton {
    private int x, y;
    private int baseWidth, baseHeight;
    private float currentWidth, currentHeight;
    private float targetWidth, targetHeight;

    private String label;
    private boolean hovered;

    private int currentColor;
    private final int normalColor = 0xFF1E9E4F;
    private final int hoverColor = 0xFF1ED55F;

    public SpotiButton(int x, int y, int width, int height, String label) {
        this.x = x;
        this.y = y;
        this.baseWidth = width;
        this.baseHeight = height;
        this.label = label;

        this.currentColor = normalColor;
        this.currentWidth = width;
        this.currentHeight = height;
        this.targetWidth = width;
        this.targetHeight = height;
    }

    public void drawButton(int mouseX, int mouseY) {
        hovered = isMouseOver(mouseX, mouseY);

        // Couleur : transition fluide vers hover ou normal
        int targetColor = hovered ? hoverColor : normalColor;
        currentColor = lerpColor(currentColor, targetColor, 0.2f);

        // Taille : léger agrandissement au survol
        targetWidth = hovered ? baseWidth + 10 : baseWidth;
        targetHeight = hovered ? baseHeight + 4 : baseHeight;

        float speed = 0.3f;
        currentWidth += (targetWidth - currentWidth) * speed;
        currentHeight += (targetHeight - currentHeight) * speed;

        if (Math.abs(currentWidth - targetWidth) < 0.2f) currentWidth = targetWidth;
        if (Math.abs(currentHeight - targetHeight) < 0.2f) currentHeight = targetHeight;

        // Calcul des coordonnées réelles avec centrage autour de x, y
        float drawX = x + (baseWidth - currentWidth) / 2f;
        float drawY = y + (baseHeight - currentHeight) / 2f;

        // Fond coloré
        GuiSpoti.drawRect((int) drawX, (int) drawY, (int)(drawX + currentWidth), (int)(drawY + currentHeight), currentColor);

        // Texte centré avec ombre
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                label,
                (int)(drawX + currentWidth / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(label) / 2),
                (int)(drawY + (currentHeight - 8) / 2),
                0xFFFFFF
        );
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        float drawX = x + (baseWidth - currentWidth) / 2f;
        float drawY = y + (baseHeight - currentHeight) / 2f;

        return mouseX >= drawX && mouseX <= drawX + currentWidth &&
                mouseY >= drawY && mouseY <= drawY + currentHeight;
    }

    private int lerpColor(int from, int to, float amount) {
        int a1 = (from >> 24) & 0xFF, r1 = (from >> 16) & 0xFF, g1 = (from >> 8) & 0xFF, b1 = from & 0xFF;
        int a2 = (to >> 24) & 0xFF, r2 = (to >> 16) & 0xFF, g2 = (to >> 8) & 0xFF, b2 = to & 0xFF;

        int a = (int)(a1 + (a2 - a1) * amount);
        int r = (int)(r1 + (r2 - r1) * amount);
        int g = (int)(g1 + (g2 - g1) * amount);
        int b = (int)(b1 + (b2 - b1) * amount);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public void updatePosition(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public String getLabel() {
        return label;
    }

}
