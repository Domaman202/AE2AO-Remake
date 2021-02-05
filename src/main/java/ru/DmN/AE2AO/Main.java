package ru.DmN.AE2AO;

import appeng.me.cache.PathGridCache;
import com.github.mouse0w0.fastreflection.FastReflection;
import com.github.mouse0w0.fastreflection.FieldAccessor;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Main implements ModInitializer {
    // Config
    public static Config local_config = null;
    public static Config last_config = null;
    // Packet ID
    // send config id
    public static final Identifier SCI = new Identifier("ae2ao", "send_config");
    // Reflection
    public static FieldAccessor recalculateControllerNextTick = null;

    // Init
    @Override
    public void onInitialize() {
        // Config init
        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        local_config = AutoConfig.getConfigHolder(Config.class).getConfig();
        last_config = local_config.clone();
        // Reflection init
        try {
            recalculateControllerNextTick = FastReflection.create(PathGridCache.class.getDeclaredField("recalculateControllerNextTick"));
        } catch (Throwable t) {
            System.out.println(t);
        }
    }
}
