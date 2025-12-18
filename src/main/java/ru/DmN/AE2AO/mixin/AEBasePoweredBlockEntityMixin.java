package ru.DmN.AE2AO.mixin;

import appeng.blockentity.powersink.AEBasePoweredBlockEntity;
import appeng.me.energy.StoredEnergyAmount;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(value = AEBasePoweredBlockEntity.class, remap = false)
public abstract class AEBasePoweredBlockEntityMixin {
    @Shadow public abstract double getAEMaxPower();
    @Shadow public abstract double getInternalMaxPower();
    @Shadow @Final private StoredEnergyAmount stored;

    /**
     * @author DomamaN202
     * @reason EnergyDisable
     */
    @Overwrite
    public final double getAECurrentPower() {
        return Main.Config.DisableEnergy ? this.getAEMaxPower() : this.getInternalCurrentPower();
    }

    /**
     * @author DomamaN202
     * @reason EnergyDisable
     */
    @Overwrite
    public double getInternalCurrentPower() {
        return Main.Config.DisableEnergy ? this.getInternalMaxPower() : this.stored.getAmount();
    }
}