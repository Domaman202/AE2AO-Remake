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

//    @Shadow protected abstract void visitorConnection(Object t, IGridVisitor g, Deque<GridNode> nr, Deque<IGridConnection> nc);
//    @Shadow protected abstract void visitorNode(final Object t, final IGridVisitor g, final Deque<GridNode> nr);

//    private boolean f1 = true;
//    private boolean f2 = true;

    /**
     * @author DomamaN202
     * @reason Add system turn off channels
     */
    @Overwrite private int getUsedChannels()
    {
        return Main.lc.DisableChannels ? 1 : usedChannels;
    }

//    @Inject(method = "visitorConnection", at = @At(value = "HEAD", shift = At.Shift.AFTER))
//    private void visitorConnection(Object t, IGridVisitor g, Deque<GridNode> nr, Deque<IGridConnection> nc, CallbackInfo c) {
//        if (f1) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    Thread.sleep(100);
//                    visitorConnection(t, g, nr, nc);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        } else
//            f1 = true;
//    }
//
//    @Inject(method = "visitorNode", at = @At(value = "HEAD", shift = At.Shift.AFTER))
//    private void visitorNode(Object t, IGridVisitor g, Deque<GridNode> n, CallbackInfo c) {
//        if (f2) {
//            CompletableFuture.runAsync(() -> {
//                try {
//                    Thread.sleep(100);
//                    visitorNode(t, g, n);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        } else
//            f2 = true;
//    }
}
