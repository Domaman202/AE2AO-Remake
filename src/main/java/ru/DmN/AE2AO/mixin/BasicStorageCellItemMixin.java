package ru.DmN.AE2AO.mixin;

import appeng.items.AEBaseItem;
import appeng.items.storage.BasicStorageCell;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import ru.DmN.AE2AO.Main;

@Mixin(BasicStorageCell.class)
public class BasicStorageCellItemMixin extends AEBaseItem {
    @Shadow(remap = false) @Final protected int bytesPerType;

    private BasicStorageCellItemMixin(Properties properties) {
        super(properties);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lappeng/items/AEBaseItem;<init>(Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Properties ctor(Properties properties) {
        return Main.Config.SCFD ? properties : properties.fireResistant();
    }

    /**
     * @author DomamaN202
     * @reason Config
     */
    @Overwrite(remap = false)
    public int getBytes(ItemStack cellItem) {
        return Main.Config.StorageCellLimits ? this.bytesPerType : Integer.MAX_VALUE;
    }
}
