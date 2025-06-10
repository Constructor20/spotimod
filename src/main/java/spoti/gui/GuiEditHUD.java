package spoti.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import spoti.config.SpotiConfig;


public class GuiEditHUD extends GuiScreen {

    private final Minecraft mc = Minecraft.getMinecraft();

    // Options avec état actif/inactif (LinkedHashMap pour garder l'ordre)
    private Map<String, Boolean> options = new LinkedHashMap<String, Boolean>() {{
        put("Play", true);
        put("Back", true);
        put("Skip", true);
        put("Loop", true);
        put("Random", true);
        put("SongIncoming", true);
        put("VolumeBar", true);
        put("ProgressBar", true);
        put("Cover", true);
    }};


    private Map<String, GuiButton> optionButtons = new LinkedHashMap<String, GuiButton>();

    private java.util.List<HUDComponent> hudComponents = new java.util.ArrayList<HUDComponent>();

    private int musicWindowX = 100;
    private int musicWindowY = 100;
    private int musicWindowWidth = 150;
    private int musicWindowHeight = 100;

    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    @Override
    public void initGui() {
        super.initGui();


        int buttonWidth = 100;
        int buttonHeight = 20;
        int xStart = this.width / 2 - buttonWidth / 2;
        int yStart = this.height / 4;

        int i = 0;
        optionButtons.clear();
        for (String key : options.keySet()) {
            boolean value = SpotiConfig.getOption(key); // ⬅️ récupère la valeur
            options.put(key, value);

            GuiButton btn = new GuiButton(i, xStart, yStart + i * (buttonHeight + 5), buttonWidth, buttonHeight, getButtonLabel(key));
            this.buttonList.add(btn);
            optionButtons.put(key, btn);
            i++;
        }
        updateHudComponents();
    }

    private void updateHudComponents() {
        hudComponents.clear();

        int currentY = musicWindowY + 20;
        for (Map.Entry<String, Boolean> entry : options.entrySet()) {
            if (entry.getValue()) {
                HUDComponent comp = new HUDComponent(entry.getKey(), musicWindowX + 10, currentY);
                hudComponents.add(comp);
                currentY += comp.height + 2;
            }
        }
    }




    // Texte affiché sur le bouton selon l’état
    private String getButtonLabel(String key) {
        boolean active = options.get(key);
        return key + ": " + (active ? "ON" : "OFF");
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        for (Map.Entry<String, GuiButton> entry : optionButtons.entrySet()) {
            if (entry.getValue().id == button.id) {
                String key = entry.getKey();
                boolean newValue = !options.get(key);
                options.put(key, newValue);  // Toggle l'option
                button.displayString = getButtonLabel(key);

                // 🔁 Sauvegarde dans le fichier de config
                SpotiConfig.updateOption(key, newValue);
                SpotiConfig.saveConfig();
                break;
            }
        }
        updateHudComponents(); // 🔁 Met à jour les blocs visibles
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        int backgroundPadding = 10;
        int totalHeight = optionButtons.size() * (20 + 5) - 5;
        int backgroundX = this.width / 2 - 100 / 2 - backgroundPadding;
        int backgroundY = this.height / 4 - backgroundPadding;
        int backgroundWidth = 100 + backgroundPadding * 2;
        int backgroundHeight = totalHeight + backgroundPadding * 2;

        drawRect(backgroundX, backgroundY, backgroundX + backgroundWidth, backgroundY + backgroundHeight, 0xFF333333); // Gris foncé

        drawCenteredString(this.fontRendererObj, "Éditeur HUD simple", this.width / 2, 20, 0xFFFFFF);

        for (HUDComponent comp : hudComponents) {
            drawRect(comp.x, comp.y, comp.x + comp.width, comp.y + comp.height, 0x9000FF00);
            drawCenteredString(this.fontRendererObj, comp.name, comp.x + comp.width / 2, comp.y + 2, 0xFFFFFF);
        }


        // Redessiner chaque bouton avec couleur personnalisée
        for (Map.Entry<String, GuiButton> entry : optionButtons.entrySet()) {
            GuiButton button = entry.getValue();
            String key = entry.getKey();
            boolean active = options.get(key);

            // Couleur : vert si actif, rouge si inactif
            int baseColor = active ? 0xFF55FF55 : 0xFFFF5555;
            int hoverColor = active ? 0xFF22CC22 : 0xFFCC2222;

            // Si la souris est au-dessus
            boolean hovering = mouseX >= button.xPosition && mouseX <= button.xPosition + button.width &&
                    mouseY >= button.yPosition && mouseY <= button.yPosition + button.height;

            int fillColor = hovering ? hoverColor : baseColor;

            // Dessin du fond du bouton
            drawRect(button.xPosition, button.yPosition, button.xPosition + button.width, button.yPosition + button.height, fillColor);

            // Texte centré
            drawCenteredString(this.fontRendererObj, button.displayString,
                    button.xPosition + button.width / 2,
                    button.yPosition + (button.height - 8) / 2,
                    0xFFFFFF);
        }

        // Fenêtre Musique + options actives
        int musicWindowX = (this.width / 4) - 80;
        int musicWindowY = (this.height / 3) + 30;
        drawRect(musicWindowX, musicWindowY, musicWindowX + musicWindowWidth, musicWindowY + musicWindowHeight, 0x90000000);
        drawString(this.fontRendererObj, "Fenêtre Musique", musicWindowX + 5, musicWindowY + 5, 0xFFFFFF);

        int yOffset = 20;
        for (Map.Entry<String, Boolean> entry : options.entrySet()) {
            if (entry.getValue()) {
                drawString(this.fontRendererObj, entry.getKey(), musicWindowX + 10, musicWindowY + yOffset, 0x00FF00);
                yOffset += 12;
            }
        }

        // Affiche les blocs
        for (HUDComponent comp : hudComponents) {
            drawRect(comp.x, comp.y, comp.x + comp.width, comp.y + comp.height, 0xFF444444);
            drawCenteredString(this.fontRendererObj, comp.name, comp.x + comp.width / 2, comp.y + 6, 0xFFFFFF);
        }

        // Lignes d’alignement
        for (HUDComponent comp1 : hudComponents) {
            for (HUDComponent comp2 : hudComponents) {
                if (comp1 == comp2) continue;

                if (Math.abs(comp1.x - comp2.x) <= 4) {
                    drawVerticalLine(comp1.x, 0, this.height, 0x88FF0000); // ligne rouge verticale
                }
                if (Math.abs(comp1.y - comp2.y) <= 4) {
                    drawHorizontalLine(0, this.width, comp1.y, 0x8800FF00); // ligne verte horizontale
                }
            }
        }

    }


    // Ne pas oublier d'autoriser la fermeture par la touche ESC
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
    

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0 && isMouseOverMusicWindow(mouseX, mouseY)) {
            dragging = true;
            dragOffsetX = mouseX - musicWindowX;
            dragOffsetY = mouseY - musicWindowY;
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (HUDComponent comp : hudComponents) {
            comp.dragging = false;
        }
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for (HUDComponent comp : hudComponents) {
            if (comp.isMouseOver(mouseX, mouseY)) {
                comp.dragging = true;
                comp.offsetX = mouseX - comp.x;
                comp.offsetY = mouseY - comp.y;
                return;
            }
        }

        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (HUDComponent comp : hudComponents) {
            if (comp.dragging) {
                comp.x = mouseX - comp.offsetX;
                comp.y = mouseY - comp.offsetY;

                for (HUDComponent other : hudComponents) {
                    if (comp == other) continue;

                    if (Math.abs(comp.x - other.x) <= 4) comp.x = other.x;
                    if (Math.abs(comp.y - other.y) <= 4) comp.y = other.y;
                }
            }
        }
        if (dragging) {
            // Zone limite : ici on garde dans l'écran
            int minX = 0;
            int minY = 0;
            int maxX = this.width - musicWindowWidth;
            int maxY = this.height - musicWindowHeight;

            musicWindowX = Math.max(minX, Math.min(mouseX - dragOffsetX, maxX));
            musicWindowY = Math.max(minY, Math.min(mouseY - dragOffsetY, maxY));
        }
    }

    private boolean isMouseOverMusicWindow(int x, int y) {
        return x >= musicWindowX && x <= musicWindowX + musicWindowWidth &&
                y >= musicWindowY && y <= musicWindowY + musicWindowHeight;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public class HUDComponent {
        public String name;
        public int x, y;
        public int width = 80;
        public int height = 12;

        public boolean dragging = false;  // <-- ajouté
        public int offsetX = 0;            // utile pour le drag
        public int offsetY = 0;            // utile pour le drag

        public HUDComponent(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        public HUDComponent(String name, int x, int y, int width, int height) {
            this(name, x, y);
            this.width = width;
            this.height = height;
        }

        // Tu peux aussi ajouter une méthode helper pour vérifier si la souris est dessus
        public boolean isMouseOver(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }
    }



}
