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
    public void onPlayerConnectInject(ClientConnection cc, ServerPlayerEntity p, CallbackInfo ci) {
        PacketByteBuf b = PacketByteBufs.create();
        //
        Config c = Main.lcc;

        b.writeBoolean(c.ControllerLimits);
        b.writeBoolean(c.DisableChannels);
        b.writeBoolean(c.SCFD);

        b.writeInt(c.Max_X);
        b.writeInt(c.Max_Y);
        b.writeInt(c.Max_Z);
        //
        ServerPlayNetworking.send(p, Main.SCI, b);
    }
}
