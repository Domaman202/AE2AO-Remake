package ru.DmN.AE2AO;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Main.SCI, (m, h, p, ps) -> {
            Config config = Main.last_config;

            config.ControllerLimits = p.readBoolean();
            config.DisableChannels = p.readBoolean();
            config.SCFD = p.readBoolean();

            config.Max_X = p.readInt();
            config.Max_Y = p.readInt();
            config.Max_Z = p.readInt();

            CompletableFuture.runAsync(() -> {
                while (m.player == null) try {
                    Thread.sleep(1000);
                } catch (Throwable ignored) {
                    System.out.println("Fuck, Thread don't sleep!");
                }

                m.player.sendMessage(Text.of(
                        "AE2AO config loaded!\nControllerLimits = " + config.ControllerLimits +
                                "\nDisableChannels = " + config.DisableChannels +
                                "\nSCFD = " + config.SCFD +
                                "\nMax_X = " + config.Max_X +
                                "\nMax_Y = " + config.Max_Y +
                                "\nMax_Z = " + config.Max_Z
                ), false);
            });
        });
    }
}
