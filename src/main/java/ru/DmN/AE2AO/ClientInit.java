package ru.DmN.AE2AO;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Main.SCI, (m, h, p, ps) -> {
            Config c = Main.LC;
            Main.PC = c.clone();

            c.ControllerLimits = p.readBoolean();
            c.DisableChannels = p.readBoolean();
            c.SCFD = p.readBoolean();
            c.ChatInfo = p.readBoolean();

            c.Max_X = p.readInt();
            c.Max_Y = p.readInt();
            c.Max_Z = p.readInt();
        });
    }
}
