package ru.DmN.AE2AO;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Main.SCI, (m, h, p, ps) -> {
            Config config = Main.lc;

            config.ControllerLimits = p.readBoolean();
            config.DisableChannels = p.readBoolean();
            config.SCFD = p.readBoolean();
            config.ChatInfo = p.readBoolean();

            config.Max_X = p.readInt();
            config.Max_Y = p.readInt();
            config.Max_Z = p.readInt();
        });
    }
}
