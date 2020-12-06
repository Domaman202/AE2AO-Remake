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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.DmN.AE2AO.Main;

import java.util.Set;

@Mixin(PathGridCache.class)
public abstract class PathGridCacheMixin {
    @Final @Shadow private Set<ControllerBlockEntity> controllers;
    @Final @Shadow private IGrid myGrid;
    @Shadow private boolean recalculateControllerNextTick = true;
    @Shadow private ControllerState controllerState = ControllerState.NO_CONTROLLER;

    /**
     * @author DomamaN202
     */
    @Overwrite(remap = false)
    private void recalcController() {
        this.recalculateControllerNextTick = false;
        final ControllerState old = this.controllerState;

        if (this.controllers.isEmpty()) {
            this.controllerState = ControllerState.NO_CONTROLLER;
        } else {
            final IGridNode startingNode = this.controllers.iterator().next().getGridNode(AEPartLocation.INTERNAL);
            if (startingNode == null) {
                this.controllerState = ControllerState.CONTROLLER_CONFLICT;
                return;
            }

            final DimensionalCoord dc = startingNode.getGridBlock().getLocation();
            final ControllerValidator cv = new ControllerValidator(dc.x, dc.y, dc.z);

            startingNode.beginVisit(cv);

            if (cv.isValid()) {
                if (Main.config.ControllerLimits) {
                    if (cv.getFound() == this.controllers.size())
                        this.controllerState = ControllerState.CONTROLLER_ONLINE;
                    else
                        this.controllerState = ControllerState.CONTROLLER_CONFLICT;
                } else
                    this.controllerState = ControllerState.CONTROLLER_ONLINE;
            } else
                this.controllerState = ControllerState.CONTROLLER_CONFLICT;
        }

        if (old != this.controllerState) {
            this.myGrid.postEvent(new MENetworkControllerChange());
        }
    }
}
