package ru.DmN.AE2AO;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import ru.DmN._i.AE2AO.Toml;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class Main implements ModInitializer {
    /** Default Config */
    public static Config DC = new Config();
    /** Last Config */
    public static Config LC = DC.clone();
    /** Prev Config */
    public static Config PC = LC.clone();
    // Packet ID
    /** Send Config Id */
    public static final Identifier SCI = new Identifier("ae2ao", "send_config");
    // Init
    @Override
    public void onInitialize() {
        try {
            File conf = new File(FabricLoader.getInstance().getConfigDir() + File.separator + "ae2ao.toml");

            if (conf.createNewFile()) {
                try (FileOutputStream stream = new FileOutputStream(conf)) {
                    stream.write("DisableChannels = false\nControllerLimits = false\nMax_X = 7\nMax_Y = 7\nMax_Z = 7\nSCFD = false\nStorageCellLimits = true\nChatInfo = true".getBytes(StandardCharsets.UTF_8));
                }
            } else {
                DC = new Toml().read(conf).to(Config.class);
                LC = DC.clone();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}