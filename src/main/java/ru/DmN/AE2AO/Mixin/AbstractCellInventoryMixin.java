package ru.DmN.AE2AO.Mixin;

import appeng.me.storage.AbstractCellInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(value = AbstractCellInventory.class, remap = false)
public class AbstractCellInventoryMixin {
    @Shadow private int maxItemTypes;

    /**
     * @author DomamaN202
     * @reason Config
     */
    @Overwrite
    public long getTotalItemTypes() {
        return Main.LC.StorageCellLimits ? this.maxItemTypes : Integer.MAX_VALUE;
    }
}
