package ru.DmN.AE2AO.Mixin;

import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.me.pathfinding.ControllerValidator;
import appeng.tile.networking.ControllerBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ControllerValidator.class)
public class ControllerValidatorMixin {
    private boolean isValid = true;
    private int found = 0;
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    /**
     * @author DomamaN202
     */
    @Overwrite(remap = false)
    public boolean visitNode(IGridNode n) {
        IGridHost host = n.getMachine();
        if (this.isValid && host instanceof ControllerBlockEntity) {
            ControllerBlockEntity c = (ControllerBlockEntity)host;
            BlockPos pos = c.getPos();
            this.minX = Math.min(pos.getX(), this.minX);
            this.maxX = Math.max(pos.getX(), this.maxX);
            this.minY = Math.min(pos.getY(), this.minY);
            this.maxY = Math.max(pos.getY(), this.maxY);
            this.minZ = Math.min(pos.getZ(), this.minZ);
            this.maxZ = Math.max(pos.getZ(), this.maxZ);
            if (this.maxX - this.minX < 7 && this.maxY - this.minY < 7 && this.maxZ - this.minZ < 7) {
                this.found = this.found + 1;
                return true;
            } else {
                this.isValid = false;
                return false;
            }
        } else {
            return false;
        }
    }
}
