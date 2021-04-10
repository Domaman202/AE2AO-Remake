package ru.DmN.AE2AO.Mixin;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkControllerChange;
import appeng.api.networking.pathing.ControllerState;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.cache.PathGridCache;
import appeng.me.pathfinding.ControllerValidator;
import appeng.tile.networking.ControllerBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.*;
import ru.DmN.AE2AO.Main;

import java.util.*;

@Mixin(value = PathGridCache.class, remap = false)
public abstract class PathGridCacheMixin {
    @Final @Shadow  private Set<ControllerBlockEntity>  controllers;
    @Final @Shadow  private IGrid                       myGrid;
    @Shadow         private boolean                     recalculateControllerNextTick;
    @Shadow         private ControllerState             controllerState;

    /**
     * @author DomamaN202
     * @reason Adding controller error system control
     */
    @Overwrite private void recalcController() {
        recalculateControllerNextTick = false;
        final ControllerState o = controllerState;

        if (controllers.isEmpty()) {
            controllerState = ControllerState.NO_CONTROLLER;
        } else {
//            ControllerBlockEntity[] controllers_ = controllers.toArray();
            ArrayList<ControllerBlockEntity> controllers_ = new ArrayList<>(controllers);
            ArrayList<ArrayList<ControllerBlockEntity>> controllerMap = new ArrayList<>();

            controllerMap.add(new ArrayList<>());
            controllerMap.get(0).add(controllers_.get(0));

            for (int i = 1; i < controllers_.size(); i++) {
                ControllerBlockEntity controller = controllers_.get(i);
                boolean complete = false;

                for (ArrayList<ControllerBlockEntity> map : controllerMap) {
                    if (complete == true)
                        break;
                    for (ControllerBlockEntity c : map) {
                        if (validate(controller.getPos(), c.getPos())) {
                            complete = true;
                            map.add(controller);
                            break;
                        }
                    }
                }

                if (complete)
                    continue;

                ArrayList<ControllerBlockEntity> nmap = new ArrayList<>();
                nmap.add(controller);
                controllerMap.add(nmap);
            }

            boolean valid = true;

            for (ArrayList<ControllerBlockEntity> map : controllerMap) {
                for (ControllerBlockEntity controller : map) {
                    final IGridNode node = controller.getGridNode(AEPartLocation.INTERNAL);
                    if (node == null) {
                        this.controllerState = ControllerState.CONTROLLER_CONFLICT;
                        return;
                    }

                    final DimensionalCoord dc = node.getGridBlock().getLocation();
                    final ControllerValidator cv = new ControllerValidator(dc.x, dc.y, dc.z);

                    node.beginVisit(cv);

                    if (!cv.isValid())
                        valid = false;
                }
            }

            if (valid)
                this.controllerState = ControllerState.CONTROLLER_ONLINE;
            else
                this.controllerState = ControllerState.CONTROLLER_CONFLICT;
        }

        if (o != this.controllerState) {
            myGrid.postEvent(new MENetworkControllerChange());
        }
    }

    private static boolean validate(BlockPos pos1, BlockPos pos2) {
        return validate_(pos1.add(0, 0, -1), pos2) ||
                validate_(pos1.add(0, 0, 1), pos2) ||
                validate_(pos1.add(0, -1, 0), pos2) ||
                validate_(pos1.add(0, 1, 0), pos2) ||
                validate_(pos1.add(-1, 0, 0), pos2) ||
                validate_(pos1.add(1, 0, 0), pos2);
    }

    private static boolean validate_(BlockPos pos1, BlockPos pos2) {
        return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
    }
}
