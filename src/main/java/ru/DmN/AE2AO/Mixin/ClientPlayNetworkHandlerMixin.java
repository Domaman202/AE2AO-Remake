package ru.DmN.AE2AO.Mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.DmN.AE2AO.Config;
import ru.DmN.AE2AO.Main;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow private MinecraftClient client;

    @Inject(method = "onGameJoin", at = @At("RETURN"))
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        Config config = Main.lc;
        client.player.sendMessage(Text.of(
                "AE2AO config loaded!\nControllerLimits = " + config.ControllerLimits +
                        "\nDisableChannels = " + config.DisableChannels +
                        "\nSCFD = " + config.SCFD +
                        "\nMax_X = " + config.Max_X +
                        "\nMax_Y = " + config.Max_Y +
                        "\nMax_Z = " + config.Max_Z
        ), false);
    }
}
