package ru.DmN.AE2AO.Mixin;

import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.me.pathfinding.ControllerValidator;
import appeng.tile.networking.ControllerBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

@Mixin(value = ControllerValidator.class, remap = false)
public abstract class ControllerValidatorMixin {
    @Shadow private boolean isValid;
    @Shadow private int found;
    @Shadow private int minX;
    @Shadow private int minY;
    @Shadow private int minZ;
    @Shadow private int maxX;
    @Shadow private int maxY;
    @Shadow private int maxZ;

    /**
     * @author DomamaN202
     * @reason Adding a size customization
     */
    @Overwrite public boolean visitNode(IGridNode n) {
        IGridHost host = n.getMachine();
        if (isValid && host instanceof ControllerBlockEntity) {
            final ControllerBlockEntity c = ((ControllerBlockEntity) host);
            final BlockPos pos = c.getPos();

            this.minX = Math.min(pos.getX(), this.minX);
            this.maxX = Math.max(pos.getX(), this.maxX);
            this.minY = Math.min(pos.getY(), this.minY);
            this.maxY = Math.max(pos.getY(), this.maxY);
            this.minZ = Math.min(pos.getZ(), this.minZ);
            this.maxZ = Math.max(pos.getZ(), this.maxZ);

            if (this.maxX - this.minX < Main.last_config.Max_X && this.maxY - this.minY < Main.last_config.Max_Y && this.maxZ - this.minZ < Main.last_config.Max_Z) {
                this.found++;
                return true;
            }

            isValid = false;
        }
        
        return false;
    }
}
