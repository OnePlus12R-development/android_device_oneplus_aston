/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.battery;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.android.internal.lineage.health.HealthInterface;

import org.lineageos.settings.utils.FileUtils;

public final class BypassChargingUtils {
    private static final String TAG = "BypassChargingUtils";

    private static final String KEY_BYPASS_CHARGING_ENABLED = "bypass_charging_enabled";

    private static final String BYPASS_CHARGING_NODE =
            "/sys/class/oplus_chg/battery/mmi_charging_enable";

    private BypassChargingUtils() {
    }

    public static boolean isSupported(Context context) {
        return HealthInterface.isChargingControlSupported(context);
    }

    public static boolean isChargeLimitEnabled(Context context) {
        if (!isSupported(context)) {
            return false;
        }

        final HealthInterface healthInterface = HealthInterface.getInstance(context);
        return healthInterface.getEnabled() && healthInterface.getMode() == HealthInterface.MODE_LIMIT;
    }

    public static boolean isChargingControlEnabled(Context context) {
        if (!isSupported(context)) {
            return false;
        }

        final HealthInterface healthInterface = HealthInterface.getInstance(context);
        return healthInterface.getEnabled();
    }

    public static boolean isBypassChargingAvailable(Context context) {
        return isSupported(context);
    }

    public static boolean isBypassChargingEnabled(Context context) {
        final String nodeValue = FileUtils.readOneLine(BYPASS_CHARGING_NODE);
        if (nodeValue != null) {
            final String trimmed = nodeValue.trim();
            if ("0".equals(trimmed) || "1".equals(trimmed)) {
                return "0".equals(trimmed);
            }
        }
        return getSharedPreferences(context).getBoolean(KEY_BYPASS_CHARGING_ENABLED, false);
    }

    public static boolean setBypassChargingEnabled(Context context, boolean enabled) {
        if (!isSupported(context)) {
            return false;
        }

        if (enabled && !disableChargingControl(context)) {
            return false;
        }

        if (!writeBypassNode(enabled)) {
            Log.w(TAG, "Failed to write bypass charging node: " + BYPASS_CHARGING_NODE);
            return false;
        }

        getSharedPreferences(context).edit().putBoolean(KEY_BYPASS_CHARGING_ENABLED, enabled).apply();
        return true;
    }

    public static void restoreBypassCharging(Context context) {
        if (!isSupported(context)) {
            return;
        }

        final boolean chargingControlEnabled = isChargingControlEnabled(context);
        if (chargingControlEnabled) {
            setStoredBypassEnabled(context, false);
            writeBypassNode(false);
            return;
        }

        final boolean enabled = isBypassChargingEnabled(context);
        if (enabled) {
            disableChargingControl(context);
        }
        writeBypassNode(enabled);
    }

    public static void enforceMutualExclusion(Context context) {
        if (!isSupported(context)) {
            return;
        }

        final boolean bypassEnabled = isBypassChargingEnabled(context);
        final boolean chargingControlEnabled = isChargingControlEnabled(context);

        if (bypassEnabled && chargingControlEnabled) {
            setStoredBypassEnabled(context, false);
            writeBypassNode(false);
            return;
        }

        if (bypassEnabled) {
            disableChargingControl(context);
        }
    }

    public static void enforceChargingLimitExclusion(Context context) {
        enforceMutualExclusion(context);
    }

    private static boolean writeBypassNode(boolean enabled) {
        return FileUtils.writeLine(BYPASS_CHARGING_NODE, enabled ? "0" : "1");
    }

    private static boolean disableChargingControl(Context context) {
        final HealthInterface healthInterface = HealthInterface.getInstance(context);
        if (!healthInterface.isChargingControlSupported()) {
            return false;
        }

        if (!healthInterface.getEnabled()) {
            return true;
        }

        if (healthInterface.setEnabled(false)) {
            return true;
        }
        if (healthInterface.reset()) {
            return !healthInterface.getEnabled() || healthInterface.setEnabled(false);
        }
        return false;
    }

    private static void setStoredBypassEnabled(Context context, boolean enabled) {
        getSharedPreferences(context).edit().putBoolean(KEY_BYPASS_CHARGING_ENABLED, enabled).apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
