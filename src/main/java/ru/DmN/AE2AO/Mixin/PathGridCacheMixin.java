package ru.DmN.AE2AO.Mixin;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkControllerChange;
import appeng.api.networking.pathing.ControllerState;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.cache.PathGridCache;
import appeng.me.pathfinding.ControllerValidator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

import java.util.Set;

@Mixin(value = PathGridCache.class, remap = false)
public abstract class PathGridCacheMixin {
    @Final @Shadow  private Set<?>                      controllers;
    @Final @Shadow  private IGrid                       myGrid;
    @Shadow         private boolean                     recalculateControllerNextTick;
    @Shadow         private ControllerState             controllerState;

    /**
     * @author DomamaN202
     * @reason Adding controller error system control
     */
    @Overwrite private void recalcController() {
        try {
            recalculateControllerNextTick = false;
            final ControllerState o = controllerState;

            if (controllers.isEmpty()) {
                controllerState = ControllerState.NO_CONTROLLER;
            } else if (Main.lc.ControllerLimits) {
                IGridNode startingNode = (IGridNode) Main.method2.invokeWithArguments(Main.class1.cast(this.controllers.iterator().next()), AEPartLocation.INTERNAL);
                if (startingNode == null) {
                    this.controllerState = ControllerState.CONTROLLER_CONFLICT;
                    return;
                }

                DimensionalCoord dc = startingNode.getGridBlock().getLocation();
                ControllerValidator cv = new ControllerValidator(dc.x, dc.y, dc.z);
                startingNode.beginVisit(cv);
                if (cv.isValid() && cv.getFound() == this.controllers.size()) {
                    this.controllerState = ControllerState.CONTROLLER_ONLINE;
                } else {
                    this.controllerState = ControllerState.CONTROLLER_CONFLICT;
                }
            } else {
                for (Object controller : controllers) {
                    final IGridNode node = (IGridNode) Main.method2.invokeWithArguments(Main.class1.cast(controller), AEPartLocation.INTERNAL);
                    if (node == null) {
                        this.controllerState = ControllerState.CONTROLLER_CONFLICT;
                        return;
                    }

                    final DimensionalCoord dc = node.getGridBlock().getLocation();
                    final ControllerValidator cv = new ControllerValidator(dc.x, dc.y, dc.z);

                    node.beginVisit(cv);

                    if (!cv.isValid()) {
                        this.controllerState = ControllerState.CONTROLLER_CONFLICT;
                        return;
                    }
                }

                this.controllerState = ControllerState.CONTROLLER_ONLINE;
            }

            if (o != this.controllerState) {
                myGrid.postEvent(new MENetworkControllerChange());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
