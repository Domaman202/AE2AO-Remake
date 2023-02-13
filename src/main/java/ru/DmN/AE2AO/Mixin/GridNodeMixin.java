package ru.DmN.AE2AO.Mixin;

import appeng.me.GridNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(value = GridNode.class, remap = false)
public class GridNodeMixin {
    @Shadow private int usedChannels;

    /**
     * @author DomamaN202
     * @reason Add system turn off channels
     */
    @Overwrite private int getUsedChannels() {
        return Main.Config.DisableChannels ? 1 : usedChannels;
    }
}
