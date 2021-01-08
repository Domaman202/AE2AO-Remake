package ru.DmN.AE2AO;

import appeng.me.cache.PathGridCache;
import appeng.tile.grid.AENetworkPowerBlockEntity;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.FieldAccessor;
import com.github.mouse0w0.fastreflection.MethodAccessor;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
    // Config
    public static Config config = null;
    // Reflection
    public static FieldAccessor controllersField = null;
    // Init
    @Override
    public void onInitialize() {
        // Config init
        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        config = AutoConfig.getConfigHolder(Config.class).getConfig();
        // Reflection init
        try {
            controllersField = FastReflection.create(PathGridCache.class.getDeclaredField("controllers"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
