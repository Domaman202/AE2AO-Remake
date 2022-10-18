package ru.DmN.AE2AO.Mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
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
        if (Main.LC.ChatInfo && Main.LC != Main.PC) {
            Config config = Main.LC;
            MinecraftClient.getInstance().player.sendMessage(Text.of(
                    "AE2AO config loaded!\nControllerLimits = " + config.ControllerLimits +
                            "\nDisableChannels = " + config.DisableChannels +
                            "\nSCFD = " + config.SCFD +
                            "\nStorageCellLimits = " + config.StorageCellLimits +
                            "\nMax_X = " + config.Max_X +
                            "\nMax_Y = " + config.Max_Y +
                            "\nMax_Z = " + config.Max_Z
            ), false);
        }
    }
}
