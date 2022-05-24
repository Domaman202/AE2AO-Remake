package ru.DmN.AE2AO.Mixin;

import appeng.api.networking.IGridNode;
import appeng.api.networking.pathing.ControllerState;
import appeng.blockentity.networking.ControllerBlockEntity;
import appeng.me.pathfinding.ControllerValidator;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

import java.lang.reflect.Constructor;
import java.util.Collection;

@Mixin(value = ControllerValidator.class, remap = false)
public class ControllerValidatorMixin {
    @Shadow
    private static boolean hasControllerCross(Collection<ControllerBlockEntity> controllers) {
        return false;
    }

    @Shadow
    private boolean valid;
    @Shadow
    private int found;
    @Shadow
    private int minX;
    @Shadow
    private int minY;
    @Shadow
    private int minZ;
    @Shadow
    private int maxX;
    @Shadow
    private int maxY;
    @Shadow
    private int maxZ;

    /**
     * @author DomamaN202
     * @reason Adding a size customization
     */
    @Overwrite
    public boolean visitNode(IGridNode n) {
        Object h = n.getOwner();
        if (valid && h instanceof ControllerBlockEntity) {
            BlockPos pos = ((ControllerBlockEntity) h).getPos();

            minX = Math.min(pos.getX(), minX);
            maxX = Math.max(pos.getX(), maxX);
            minY = Math.min(pos.getY(), minY);
            maxY = Math.max(pos.getY(), maxY);
            minZ = Math.min(pos.getZ(), minZ);
            maxZ = Math.max(pos.getZ(), maxZ);

            if (maxX - minX < Main.LC.Max_X && maxY - minY < Main.LC.Max_Y && maxZ - minZ < Main.LC.Max_Z) {
                this.found++;
                return true;
            }

            valid = false;
        }

        return false;
    }

    /**
     * @author DomamaN202
     * @reason Config
     */
    @Overwrite
    public static ControllerState calculateState(Collection<ControllerBlockEntity> controllers) throws Throwable {
        if (controllers.isEmpty()) {
            return ControllerState.NO_CONTROLLER;
        }
        ControllerBlockEntity startingController = controllers.iterator().next();
        IGridNode startingNode = startingController.getGridNode();
        if (startingNode == null) {
            return ControllerState.CONTROLLER_CONFLICT;
        }
        Constructor<?> constructor = ControllerValidator.class.getDeclaredConstructor(BlockPos.class);
        constructor.setAccessible(true);
        ControllerValidator cv = (ControllerValidator) constructor.newInstance(startingController.getPos());
        startingNode.beginVisit(cv);
        if (!cv.isValid())
            return ControllerState.CONTROLLER_CONFLICT;
        if (cv.getFound() != controllers.size() && Main.LC.ControllerLimits)
            return ControllerState.CONTROLLER_CONFLICT;
        if (hasControllerCross(controllers))
            return ControllerState.CONTROLLER_CONFLICT;
        return ControllerState.CONTROLLER_ONLINE;
    }
}