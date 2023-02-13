package ru.DmN.AE2AO.Mixin;

import appeng.blockentity.powersink.AEBasePoweredBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(value = AEBasePoweredBlockEntity.class, remap = false)
public abstract class AEBasePoweredBlockEntityMixin {
    @Shadow public abstract double getAEMaxPower();

    @Shadow public abstract double getInternalMaxPower();

    @Shadow private double internalCurrentPower;

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
        return Main.Config.DisableEnergy ? this.getInternalMaxPower() : this.internalCurrentPower;
    }
}