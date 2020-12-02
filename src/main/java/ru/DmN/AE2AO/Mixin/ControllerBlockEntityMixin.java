package ru.DmN.AE2AO.Mixin;

import appeng.api.networking.IGridNode;
import appeng.api.networking.pathing.ControllerState;
import appeng.api.networking.pathing.IPathingGrid;
import appeng.api.util.AEPartLocation;
import appeng.api.util.DimensionalCoord;
import appeng.me.GridNode;
import appeng.me.cache.PathGridCache;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.pathfinding.ControllerValidator;
import appeng.tile.grid.AENetworkPowerBlockEntity;
import appeng.tile.networking.ControllerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ru.DmN.AE2AO.Main;

import java.util.Set;

@Mixin(ControllerBlockEntity.class)
public class ControllerBlockEntityMixin {
    @Redirect(method = "updateMeta", at = @At(value = "INVOKE", target = "Lappeng/api/networking/pathing/IPathingGrid;getControllerState()Lappeng/api/networking/pathing/ControllerState;"), remap = false)
    private ControllerState updateMeta(IPathingGrid iPathingGrid) throws Throwable {
        iPathingGrid.onUpdateTick(); iPathingGrid.repath();
        AENetworkProxy proxy = (AENetworkProxy) Main.getProxyMethod.invoke(this);
        ControllerState state = proxy.getPath().getControllerState();

        if (Main.config.ControllerLimits)
            return state;

        if (state != ControllerState.NO_CONTROLLER) {
            IGridNode startingNode = ((Set<ControllerBlockEntity>) Main.controllersField.get(proxy.getPath())).iterator().next().getGridNode(AEPartLocation.INTERNAL);

            if (startingNode == null)
                return ControllerState.CONTROLLER_ONLINE;
            DimensionalCoord dc = startingNode.getGridBlock().getLocation();
            ControllerValidator cv = new ControllerValidator(dc.x, dc.y, dc.z);
            startingNode.beginVisit(cv);
            return cv.isValid() ? ControllerState.CONTROLLER_ONLINE : ControllerState.CONTROLLER_CONFLICT;
        }
        return ControllerState.NO_CONTROLLER;
    }
}
