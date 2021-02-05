package ru.DmN.AE2AO.Mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.DmN.AE2AO.Config;
import ru.DmN.AE2AO.Main;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    public void onPlayerConnectInject(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PacketByteBuf buf = PacketByteBufs.create();
        //
        Config config = Main.local_config;

        buf.writeBoolean(config.ControllerLimits);
        buf.writeBoolean(config.DisableChannels);
        buf.writeBoolean(config.SCFD);

        buf.writeInt(config.Max_X);
        buf.writeInt(config.Max_Y);
        buf.writeInt(config.Max_Z);
        //
        ServerPlayNetworking.send(player, Main.SCI, buf);
    }
}
