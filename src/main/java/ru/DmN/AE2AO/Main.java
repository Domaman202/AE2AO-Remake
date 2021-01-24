package ru.DmN.AE2AO;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    // Config
    public static Config config = null;

    // Init
    @Override
    public void onInitialize() {
        // Config init
        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(Config.class).getConfig();
    }
}
