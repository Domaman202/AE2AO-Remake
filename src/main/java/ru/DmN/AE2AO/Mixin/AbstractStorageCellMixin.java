package ru.DmN.AE2AO.Mixin;

import appeng.items.AEBaseItem;
import appeng.items.storage.AbstractStorageCell;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import ru.DmN.AE2AO.Main;

@Mixin(AbstractStorageCell.class)
public abstract class AbstractStorageCellMixin extends AEBaseItem {
    public AbstractStorageCellMixin(Settings properties) { super(properties); }

    @Override
    public boolean damage(DamageSource source) {
        return super.damage(source) && Main.config.SCFD;
    }
}
