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
import org.spongepowered.asm.mixin.*;
import ru.DmN.AE2AO.Main;

import java.util.Set;

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
            final IGridNode sn = controllers.iterator().next().getGridNode(AEPartLocation.INTERNAL);
            if (sn == null) {
                controllerState = ControllerState.CONTROLLER_CONFLICT;
                return;
            }

            DimensionalCoord c = sn.getGridBlock().getLocation();
            ControllerValidator v = new ControllerValidator(c.x, c.y, c.z);

            sn.beginVisit(v);

            if (v.isValid() && (v.getFound() == controllers.size() || !Main.lc.ControllerLimits))
                controllerState = ControllerState.CONTROLLER_ONLINE;
            else
                controllerState = ControllerState.CONTROLLER_CONFLICT;

        }

        if (o != this.controllerState) {
            myGrid.postEvent(new MENetworkControllerChange());
        }
    }
}
