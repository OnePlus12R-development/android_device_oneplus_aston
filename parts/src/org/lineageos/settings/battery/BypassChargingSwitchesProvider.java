/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.battery;

import android.content.Context;

import com.android.settingslib.drawer.CategoryKey;
import com.android.settingslib.drawer.DynamicSummary;
import com.android.settingslib.drawer.SwitchController;
import com.android.settingslib.drawer.SwitchesProvider;

import org.lineageos.settings.R;

import java.util.Collections;
import java.util.List;

public class BypassChargingSwitchesProvider extends SwitchesProvider {
    private static final String KEY_BYPASS_CHARGING = "bypass_charging";

    @Override
    protected List<SwitchController> createSwitchControllers() {
        return Collections.singletonList(new BypassChargingSwitchController(getContext()));
    }

    private static final class BypassChargingSwitchController extends SwitchController
            implements DynamicSummary {
        private final Context mContext;

        BypassChargingSwitchController(Context context) {
            mContext = context;
        }

        @Override
        public String getSwitchKey() {
            return KEY_BYPASS_CHARGING;
        }

        @Override
        protected MetaData getMetaData() {
            final MetaData metaData = new MetaData(CategoryKey.CATEGORY_BATTERY);
            metaData.setTitle(R.string.bypass_charging_title);
            return metaData;
        }

        @Override
        protected boolean isChecked() {
            return BypassChargingUtils.isSupported(mContext)
                    && BypassChargingUtils.isBypassChargingEnabled(mContext);
        }

        @Override
        protected boolean onCheckedChanged(boolean checked) {
            if (!BypassChargingUtils.isSupported(mContext)) {
                return false;
            }

            return BypassChargingUtils.setBypassChargingEnabled(mContext, checked);
        }

        @Override
        protected String getErrorMessage(boolean attemptedChecked) {
            if (!BypassChargingUtils.isSupported(mContext)) {
                return mContext.getString(R.string.bypass_charging_not_supported);
            }

            return null;
        }

        @Override
        public String getDynamicSummary() {
            if (!BypassChargingUtils.isSupported(mContext)) {
                return mContext.getString(R.string.bypass_charging_not_supported);
            }

            return mContext.getString(R.string.bypass_charging_summary);
        }
    }
}
