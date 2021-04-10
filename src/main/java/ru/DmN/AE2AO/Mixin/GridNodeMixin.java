package ru.DmN.AE2AO.Mixin;

import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridVisitor;
import appeng.me.GridNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.DmN.AE2AO.Main;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;

@Mixin(value = GridNode.class, remap = false)
public abstract class GridNodeMixin {
    @Shadow private int usedChannels;

    /**
     * @author DomamaN202
     * @reason Add system turn off channels
     */
    @Overwrite private int getUsedChannels()
    {
        return Main.lc.DisableChannels ? 1 : usedChannels;
    }
}
