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

@Mixin(ControllerValidator.class)
public abstract class ControllerValidatorMixin {
    @Shadow private boolean isValid = true;
    @Shadow private int found = 0;
    @Shadow private int minX;
    @Shadow private int minY;
    @Shadow private int minZ;
    @Shadow private int maxX;
    @Shadow private int maxY;
    @Shadow private int maxZ;

    /**
     * @author DomamaN202
     */
    @Overwrite(remap = false)
    public boolean visitNode(IGridNode n) {
        IGridHost host = n.getMachine();
        if (this.isValid() && host instanceof ControllerBlockEntity) {
            ControllerBlockEntity c = (ControllerBlockEntity)host;
            BlockPos pos = c.getPos();
            this.minX = Math.min(pos.getX(), this.minX);
            this.maxX = Math.max(pos.getX(), this.maxX);
            this.minY = Math.min(pos.getY(), this.minY);
            this.maxY = Math.max(pos.getY(), this.maxY);
            this.minZ = Math.min(pos.getZ(), this.minZ);
            this.maxZ = Math.max(pos.getZ(), this.maxZ);
            if (this.maxX - this.minX < Main.config.Max_X && this.maxY - this.minY < Main.config.Max_Y && this.maxZ - this.minZ < Main.config.Max_Z) {
                this.setFound(this.getFound() + 1);
                return true;
            } else {
                this.setValid(false);
                return this.isValid();
            }
        } else {
            return false;
        }
    }

    @Shadow public boolean isValid() {
        return this.isValid;
    }
    @Shadow private void setValid(boolean isValid) { this.isValid = isValid; }
    @Shadow public int getFound() {
        return this.found;
    }
    @Shadow private void setFound(int found) {
        this.found = found;
    }
}
