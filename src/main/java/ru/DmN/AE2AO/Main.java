package ru.DmN.AE2AO;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import ru.DmN._i.AE2AO.Toml;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class Main implements ModInitializer {
    /** Default Config */
    public static Config Config = new Config();
    // Init
    @Override
    public void onInitialize() {
        try {
            // Config init
            File conf = new File(FabricLoader.getInstance().getConfigDir() + File.separator + "ae2ao.toml");

            if (conf.createNewFile()) {
                try (FileOutputStream stream = new FileOutputStream(conf)) {
                    stream.write("DisableChannels = false\nControllerLimits = false\nMax_X = 7\nMax_Y = 7\nMax_Z = 7\nSCFD = false\nStorageCellLimits = true\nDisableEnergy = false".getBytes(StandardCharsets.UTF_8));
                }
            } else {
                Config = new Toml().read(conf).to(Config.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}