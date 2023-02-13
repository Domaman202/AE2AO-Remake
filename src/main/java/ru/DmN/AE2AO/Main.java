package ru.DmN.AE2AO;

import appeng.api.networking.IGridNode;
import appeng.api.util.AEPartLocation;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.BlockPos;
import ru.DmN._i.AE2AO.Toml;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class Main implements ModInitializer {
    //
    /** Class Controller Entity */
    public static Class<?> CCE;
    /** Method GetPos */
    public static MethodHandle MGP;
    /** Method GetGridNode */
    public static MethodHandle MGGN;
    /** Field IsValid */
    public static Field FIV;
    /** Config */
    public static Config Config = new Config();
    // Init
    @Override
    public void onInitialize() {
        try {
            //
            try {
                CCE = Class.forName("appeng.tile.networking.ControllerBlockEntity");
            } catch (ClassNotFoundException e) {
                CCE = Class.forName("appeng.tile.networking.ControllerTileEntity");
            }

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            MGP = lookup.findVirtual(CCE, "getPos", MethodType.methodType(BlockPos.class));
            MGGN = lookup.findVirtual(CCE, "getGridNode", MethodType.methodType(IGridNode.class, AEPartLocation.class));

            FIV = CCE.getDeclaredField("isValid");
            FIV.setAccessible(true);

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
