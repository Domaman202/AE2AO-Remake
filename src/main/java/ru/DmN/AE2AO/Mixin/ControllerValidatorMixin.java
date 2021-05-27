package ru.DmN.AE2AO.Mixin;

import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.me.pathfinding.ControllerValidator;
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
        try {
            IGridHost h = n.getMachine();

            if (isValid && Main.class1.isInstance(h)) {
                BlockPos pos = (BlockPos) Main.method1.invokeWithArguments(Main.class1.cast(h));

                minX = Math.min(pos.getX(), minX);
                maxX = Math.max(pos.getX(), maxX);
                minY = Math.min(pos.getY(), minY);
                maxY = Math.max(pos.getY(), maxY);
                minZ = Math.min(pos.getZ(), minZ);
                maxZ = Math.max(pos.getZ(), maxZ);

                if (maxX - minX < Main.lc.Max_X && maxY - minY < Main.lc.Max_Y && maxZ - minZ < Main.lc.Max_Z) {
                    this.found++;
                    return true;
                }

                isValid = false;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return false;
    }
}
