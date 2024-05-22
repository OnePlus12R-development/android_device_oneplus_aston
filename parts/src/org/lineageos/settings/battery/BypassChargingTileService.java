/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.battery;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import org.lineageos.settings.R;

public class BypassChargingTileService extends TileService {

    @Override
    public void onStartListening() {
        super.onStartListening();
        BypassChargingUtils.enforceMutualExclusion(this);
        refreshTileState();
    }

    @Override
    public void onClick() {
        super.onClick();

        if (!BypassChargingUtils.isSupported(this)) {
            refreshTileState();
            return;
        }

        final boolean isEnabled = BypassChargingUtils.isBypassChargingEnabled(this);
        BypassChargingUtils.setBypassChargingEnabled(this, !isEnabled);
        refreshTileState();
    }

    private void refreshTileState() {
        final Tile tile = getQsTile();
        if (tile == null) {
            return;
        }

        final boolean isSupported = BypassChargingUtils.isSupported(this);
        final boolean isEnabled = isSupported && BypassChargingUtils.isBypassChargingEnabled(this);

        tile.setLabel(getString(R.string.bypass_charging_tile_title));
        if (!isSupported) {
            tile.setState(Tile.STATE_UNAVAILABLE);
        } else {
            tile.setState(isEnabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }
}
