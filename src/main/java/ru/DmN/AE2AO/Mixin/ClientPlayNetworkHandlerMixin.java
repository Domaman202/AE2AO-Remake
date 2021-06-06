package ru.DmN.AE2AO.Mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.DmN.AE2AO.Config;
import ru.DmN.AE2AO.Main;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onGameJoin", at = @At("RETURN"))
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (Main.LC.CI) {
            Config config = Main.LC;
            MinecraftClient.getInstance().player.sendMessage(new LiteralText(
                    "AE2AO config loaded!\nControllerLimits = " + config.CL +
                            "\nDisableChannels = " + config.DC +
                            "\nSCFD = " + config.SCFD +
                            "\nMax_X = " + config.MX +
                            "\nMax_Y = " + config.MY +
                            "\nMax_Z = " + config.MZ
            ), false);
        }
    }
}
