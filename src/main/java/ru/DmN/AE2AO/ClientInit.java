package ru.DmN.AE2AO;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientInit implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Main.SCI, (m, h, p, ps) -> {
            Config c = Main.LC;
            Main.PC = c.clone();

            c.CL = p.readBoolean();
            c.DC = p.readBoolean();
            c.SCFD = p.readBoolean();
            c.CI = p.readBoolean();

            c.MX = p.readInt();
            c.MY = p.readInt();
            c.MZ = p.readInt();
        });
    }
}
