package ru.DmN.AE2AO.Mixin;

import appeng.me.GridNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(GridNode.class)
public abstract class GridNodeMixin {
    @Shadow
    private int usedChannels;

    /**
     * @author DomamaN202
     */
    @Overwrite(remap = false)
    private int getUsedChannels()
    {
        return Main.config.DisableChannels ? 1 : usedChannels;
    }
}
