package ru.DmN.AE2AO;

import appeng.api.networking.IGridNode;
import appeng.api.util.AEPartLocation;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import ru.DmN.config.Utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

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
    // Config
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
            if (!Utils.checkCreateConfig("ae2ao.toml", "DisableChannels = false\nControllerLimits = false\nMax_X = 7\nMax_Y = 7\nMax_Z = 7\nSCFD = false\nChatInfo = true")) {
                DC = Utils.loadConfig("ae2ao.toml", Config.class);
                LC = DC.clone();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}