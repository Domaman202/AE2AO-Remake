package ru.DmN.AE2AO.Mixin;

import appeng.me.GridNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(value = GridNode.class, remap = false)
public abstract class GridNodeMixin {
    @Shadow private int usedChannels;

    /**
     * @author DomamaN202
     */
    @Overwrite private int getUsedChannels()
    {
        return Main.config.DisableChannels ? 1 : usedChannels;
    }
}
