package ru.DmN.AE2AO;

import appeng.api.networking.IGridNode;
import appeng.api.util.AEPartLocation;
import com.moandjiezana.toml.Toml;
import com.mojang.brigadier.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

        // Commands init
        CommandRegistrationCallback.EVENT.register((d, x) -> d.register(CommandManager.literal("ae2ao_recalc").executes(c -> {
            World w = c.getSource().getWorld();

            List<BlockEntity> e1 = w.blockEntities;
            List<BlockEntity> e2 = w.tickingBlockEntities;

            for (int i = 0; i < e1.size(); i++) {
                if (cce.isInstance(e1.get(i))) {
                    e1.set(i, ((BlockEntityProvider) w.getBlockState(e1.remove(i).getPos()).getBlock()).createBlockEntity(w));
                }
            }

            for (int i = 0; i < e2.size(); i++) {
                if (cce.isInstance(e2.get(i))) {
                    e2.set(i, ((BlockEntityProvider) w.getBlockState(e2.remove(i).getPos()).getBlock()).createBlockEntity(w));
                }
            }

            return Command.SINGLE_SUCCESS;
        })));
    }
}