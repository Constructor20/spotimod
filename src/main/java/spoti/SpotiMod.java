package spoti;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spoti.config.SpotiConfig;
import spoti.gui.GuiSpotifyHUD;
import spoti.hud.HudMusique;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;





@Mod(modid = SpotiMod.MODID, version = SpotiMod.VERSION, name = SpotiMod.NAME)
public class SpotiMod {

    public static final String MODID = "spotimod";
    public static final String NAME = "SpotiMod";
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    // Visibilité du HUD Spotify
    private static boolean hudVisible = true;

    private static int defaultGuiLeft = 100;
    private static int defaultGuiTop = 100;

    private static int guiLeft = defaultGuiLeft;
    private static int guiTop = defaultGuiTop;

    public static int savedMusicX = -1, savedMusicY = -1, savedMusicWidth = 200, savedMusicHeight = 100;

    public static GuiSpotifyHUD hud;

    public static void loadWindowLocation() {
        savedMusicX = SpotiConfig.loadInt("musicX", 50);
        savedMusicY = SpotiConfig.loadInt("musicY", 50);
        savedMusicWidth = SpotiConfig.loadInt("musicWidth", 200);
        savedMusicHeight = SpotiConfig.loadInt("musicHeight", 100);
    }

    public static void saveWindowLocation(int x, int y, int width, int height) {
        savedMusicX = x;
        savedMusicY = y;
        savedMusicWidth = width;
        savedMusicHeight = height;

        // Sauvegarde persistante dans les préférences
        SpotiConfig.saveInt("musicX", x);
        SpotiConfig.saveInt("musicY", y);
        SpotiConfig.saveInt("musicWidth", width);
        SpotiConfig.saveInt("musicHeight", height);
    }

    public static void resetWindowLocation() {
        saveWindowLocation(50, 50, 200, 100);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("[SpotiMod] Initialisation du mod.");
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
        MinecraftForge.EVENT_BUS.register(new spoti.hud.HudOverlayRenderer());
        SpotiConfig.loadConfig();

        loadWindowLocation();
        LOGGER.info("[SpotiMod] Position de la fenêtre chargée : x=" + savedMusicX + ", y=" + savedMusicY);

        // Tu pourras aussi enregistrer ici d'autres handlers, comme le rendu du HUD
    }


    // Méthode utilitaire pour ouvrir le GUI principal
    public static void openGui() {
        Minecraft.getMinecraft().displayGuiScreen(new spoti.gui.GuiSpoti());
    }


    // Méthodes pour contrôler la visibilité du HUDee
    public static void toggleHud() {
        hudVisible = !hudVisible;
        LOGGER.info("[SpotiMod] HUD Spotify " + (hudVisible ? "activé" : "désactivé"));
    }

    public static boolean isHudVisible() {
        return hudVisible;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            HudMusique.render(event.partialTicks);
        }
    }


}