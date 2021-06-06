package ru.DmN.AE2AO;

import appeng.api.networking.IGridNode;
import appeng.api.util.AEPartLocation;
import com.moandjiezana.toml.Toml;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class Main implements ModInitializer {
    //
    // ClassControllerEntity
    public static Class<?> cce = null;
    public static MethodHandle methodGetPos = null;
    public static MethodHandle methodGetGridNode = null;
    public static Field fieldIsValid = null;
    // Config
    public static Config lcc = null;
    public static Config lc = null;
    // Packet ID
    // send config id
    public static final Identifier SCI = new Identifier("ae2ao", "send_config");
    // Init
    @Override
    public void onInitialize() {
        try {
            //
            try {
                cce = Class.forName("appeng.tile.networking.ControllerBlockEntity");
            } catch (ClassNotFoundException e) {
                cce = Class.forName("appeng.tile.networking.ControllerTileEntity");
            }

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            methodGetPos = lookup.findVirtual(cce, "getPos", MethodType.methodType(BlockPos.class));
            methodGetGridNode = lookup.findVirtual(cce, "getGridNode", MethodType.methodType(IGridNode.class, AEPartLocation.class));

            fieldIsValid = cce.getDeclaredField("isValid");
            fieldIsValid.setAccessible(true);

            // Config init
            File conf = new File(FabricLoader.getInstance().getConfigDir() + File.separator + "ae2ao.toml");

            if (conf.createNewFile()) {
                try (FileOutputStream stream = new FileOutputStream(conf)) {
                    stream.write("DisableChannels = false\nControllerLimits = false\nMax_X = 7\nMax_Y = 7\nMax_Z = 7\nSCFD = false\nChatInfo = true".getBytes(StandardCharsets.UTF_8));
                }

                lcc = new Config();
                lc = new Config();
            } else {
                lcc = new Toml().read(conf).to(Config.class);
                lc = lcc.clone();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}