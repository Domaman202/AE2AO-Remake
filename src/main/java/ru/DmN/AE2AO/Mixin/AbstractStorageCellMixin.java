package ru.DmN.AE2AO.Mixin;

import appeng.items.AEBaseItem;
import appeng.items.storage.AbstractStorageCell;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(AbstractStorageCell.class)
public class AbstractStorageCellMixin extends AEBaseItem {
    @Shadow @Final protected int totalBytes;

    private AbstractStorageCellMixin(Settings p) { super(p); }

    @Override
    public boolean damage(DamageSource s) {
        return !Main.LC.SCFD && super.damage(s);
    }

    /**
     * @author DomamaN202
     * @reason Config
     */
    @Overwrite
    public int getBytes(ItemStack cellItem) {
        return Main.LC.StorageCellLimits ? this.totalBytes : Integer.MAX_VALUE;
    }
}
