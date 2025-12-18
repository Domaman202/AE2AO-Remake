package ru.DmN.AE2AO.mixin;

import appeng.items.tools.powered.AbstractPortableCell;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import ru.DmN.AE2AO.Main;

@Mixin(AbstractPortableCell.class)
public class AbstractPortableCellMixin {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lappeng/items/tools/powered/powersink/AEBasePoweredItem;<init>(Ljava/util/function/DoubleSupplier;Lnet/minecraft/world/item/Item$Properties;)V"))
    private static Item.Properties ctor(Item.Properties properties) {
        return Main.Config.SCFD ? properties : properties.fireResistant();
    }
}
