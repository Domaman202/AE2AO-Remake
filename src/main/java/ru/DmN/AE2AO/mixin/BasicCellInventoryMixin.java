package ru.DmN.AE2AO.mixin;

import appeng.me.cells.BasicCellInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(value = BasicCellInventory.class, remap = false)
public class BasicCellInventoryMixin {
    @Shadow private int maxItemTypes;

    /**
     * @author DomamaN202
     * @reason Config
     */
    @Overwrite
    public long getTotalItemTypes() {
        return Main.Config.StorageCellLimits ? this.maxItemTypes : Integer.MAX_VALUE;
    }
}
