package ru.DmN.AE2AO;

import appeng.tile.networking.ControllerBlockEntity;
import com.mojang.brigadier.Command;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class Main implements ModInitializer {
    // Config
    public static Config lcc = null;
    public static Config lc = null;
    // Packet ID
    // send config id
    public static final Identifier SCI = new Identifier("ae2ao", "send_config");

    // Init
    @Override
    public void onInitialize() {
        // Config init
        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
        lcc = AutoConfig.getConfigHolder(Config.class).getConfig();
        lc = lcc.clone();

        // Commands init
        CommandRegistrationCallback.EVENT.register((d, x) -> {
            d.register(CommandManager.literal("ae2ao_recalc").executes(c -> {
                World w = c.getSource().getWorld();

                List<BlockEntity> e1 = w.blockEntities;
                List<BlockEntity> e2 = w.tickingBlockEntities;

                for (int i = 0; i < e1.size(); i++) {
                    if (e1.get(i) instanceof ControllerBlockEntity) {
                        e1.set(i, ((BlockEntityProvider) w.getBlockState(e1.remove(i).getPos()).getBlock()).createBlockEntity(w));
                    }
                }

                for (int i = 0; i < e2.size(); i++) {
                    if (e2.get(i) instanceof ControllerBlockEntity) {
                        e2.set(i, ((BlockEntityProvider) w.getBlockState(e2.remove(i).getPos()).getBlock()).createBlockEntity(w));
                    }
                }

                return Command.SINGLE_SUCCESS;
            }));
        });
    }
}