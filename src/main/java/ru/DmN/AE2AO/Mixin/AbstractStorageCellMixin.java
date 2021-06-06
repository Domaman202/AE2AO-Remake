package ru.DmN.AE2AO.Mixin;

import appeng.items.AEBaseItem;
import appeng.items.storage.AbstractStorageCell;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import ru.DmN.AE2AO.Main;

@Mixin(AbstractStorageCell.class)
public class AbstractStorageCellMixin extends AEBaseItem {
    private AbstractStorageCellMixin(Settings p) { super(p); }

    @Override
    public boolean damage(DamageSource s) {
        return !Main.LC.SCFD && super.damage(s);
    }
}
