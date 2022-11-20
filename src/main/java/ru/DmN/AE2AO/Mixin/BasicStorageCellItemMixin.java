package ru.DmN.AE2AO.Mixin;

import appeng.items.AEBaseItem;
import appeng.items.storage.BasicStorageCell;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(BasicStorageCell.class)
public class BasicStorageCellItemMixin extends AEBaseItem {
    public BasicStorageCellItemMixin(Settings properties) {
        super(properties);
    }

    @Shadow
    @Final
    protected int totalBytes;

    /**
     * @author DomamaN202
     * @reason Config
     */
    @Overwrite(remap = false)
    public int getBytes(ItemStack cellItem) {
        return Main.LC.StorageCellLimits ? this.totalBytes : Integer.MAX_VALUE;
    }

    @Override
    public boolean damage(DamageSource source) {
        return !Main.LC.SCFD && super.damage(source);
    }
}
