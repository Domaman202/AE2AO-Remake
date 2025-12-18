package ru.DmN.AE2AO.mixin;

import appeng.api.networking.IGridNode;
import appeng.api.networking.pathing.ControllerState;
import appeng.blockentity.networking.ControllerBlockEntity;
import appeng.me.pathfinding.ControllerValidator;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import ru.DmN.AE2AO.Main;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Collection;

@Mixin(value = ControllerValidator.class, remap = false)
public class ControllerValidatorMixin {
    @Shadow private static boolean hasControllerCross(Collection<ControllerBlockEntity> controllers) {return false;}
    @Shadow private boolean valid;
    @Shadow private int found;
    @Shadow private int minX;
    @Shadow private int minY;
    @Shadow private int minZ;
    @Shadow private int maxX;
    @Shadow private int maxY;
    @Shadow private int maxZ;

    @Unique private final static MethodHandle CV_CTOR;

    /**
     * @author DomamaN202
     * @reason Adding a size customization
     */
    @Overwrite
    public boolean visitNode(IGridNode n) {
        Object h = n.getOwner();
        if (valid && h instanceof ControllerBlockEntity) {
            BlockPos pos = ((ControllerBlockEntity) h).getBlockPos();

            minX = Math.min(pos.getX(), minX);
            maxX = Math.max(pos.getX(), maxX);
            minY = Math.min(pos.getY(), minY);
            maxY = Math.max(pos.getY(), maxY);
            minZ = Math.min(pos.getZ(), minZ);
            maxZ = Math.max(pos.getZ(), maxZ);

            if (maxX - minX < Main.Config.Max_X && maxY - minY < Main.Config.Max_Y && maxZ - minZ < Main.Config.Max_Z) {
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
        ControllerValidator cv = (ControllerValidator) CV_CTOR.invoke(startingController.getBlockPos());
        startingNode.beginVisit(cv);
        if (!cv.isValid())
            return ControllerState.CONTROLLER_CONFLICT;
        if (cv.getFound() != controllers.size() && Main.Config.ControllerLimits)
            return ControllerState.CONTROLLER_CONFLICT;
        if (hasControllerCross(controllers))
            return ControllerState.CONTROLLER_CONFLICT;
        return ControllerState.CONTROLLER_ONLINE;
    }

    static {
        try {
            Constructor<?> ctor = ControllerValidator.class.getDeclaredConstructor(BlockPos.class);
            ctor.setAccessible(true);
            CV_CTOR = MethodHandles.publicLookup().unreflectConstructor(ctor);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}