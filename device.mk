#
# SPDX-FileCopyrightText: The LineageOS Project
# SPDX-License-Identifier: Apache-2.0
#

# AAPT
PRODUCT_AAPT_CONFIG := normal
PRODUCT_AAPT_PREF_CONFIG := xxhdpi

# Audio
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/audio/audio_policy_volumes.xml:$(TARGET_COPY_OUT_VENDOR)/etc/audio_policy_volumes.xml \
    $(LOCAL_PATH)/configs/audio/default_volume_tables.xml:$(TARGET_COPY_OUT_VENDOR)/etc/default_volume_tables.xml

# Boot animation
TARGET_SCREEN_HEIGHT := 2780
TARGET_SCREEN_WIDTH := 1264

# Display
PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/configs/display/displayconfig.xml:$(TARGET_COPY_OUT_VENDOR)/etc/displayconfig/display_id_4630946607878435459.xml

PRODUCT_SYSTEM_PROPERTIES += \
    sys.brightness.disable_gamma_conversion=true

$(call soong_config_set,qtidisplay,pxlw_vendor_namespace,vendor/oneplus/aston)
$(call soong_config_set_bool,qtidisplay,pxlw_hw_iris7,true)

# IR
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.consumerir.xml:$(TARGET_COPY_OUT_ODM)/etc/permissions/android.hardware.consumerir.xml

PRODUCT_PACKAGES += \
    android.hardware.ir-service.oplus

# Fingerprint
$(call soong_config_set,surfaceflinger,udfps_lib,//hardware/oplus:libudfps_extension.oplus)
$(call soong_config_set_bool,qtidisplay,oplus_udfps,true)

# LiveDisplay
$(call soong_config_set_bool,OPLUS_LINEAGE_LIVEDISPLAY_HAL,ENABLE_AF,true)
$(call soong_config_set_bool,OPLUS_LINEAGE_LIVEDISPLAY_HAL,ENABLE_SE,false)

# Overlays
DEVICE_PACKAGE_OVERLAYS += \
    $(LOCAL_PATH)/overlay-lineage

PRODUCT_PACKAGES += \
    FrameworksResEuicc_NA \
    KeyHandlerResTarget \
    OPlusFrameworksResTarget \
    OPlusSettingsProviderResTarget \
    OPlusSettingsResTarget \
    OPlusSystemUIResTarget

# Power
$(call soong_config_set,qtipower,mode_ext_lib,power-ext-oplus)

# Regional properties
REGIONAL_PROP_FILES := $(wildcard $(LOCAL_PATH)/properties/*/*.prop)

PRODUCT_COPY_FILES += $(foreach f,$(REGIONAL_PROP_FILES), \
    $(f):$(TARGET_COPY_OUT_ODM)/etc/$(patsubst $(LOCAL_PATH)/properties/%,%,$(f)) \
    $(f):$(TARGET_COPY_OUT_RECOVERY)/root/vendor/odm/etc/$(patsubst $(LOCAL_PATH)/properties/%,%,$(f)))

# Sensors
PRODUCT_PACKAGES += \
    sensors.oplus

# Soong namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)

# Telephony
PRODUCT_PACKAGES += \
    OplusEsimSwitcher \
    OplusEuicc

PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.telephony.euicc.xml:$(TARGET_COPY_OUT_PRODUCT)/etc/permissions/android.hardware.telephony.euicc.xml

# Touch
$(call soong_config_set_bool,OPLUS_LINEAGE_TOUCH_HAL,USE_OPLUSTOUCH,true)

# Vibrator
PRODUCT_PACKAGES += \
    vendor.qti.hardware.vibrator.service.oplus

$(call soong_config_set_bool,OPLUS_LINEAGE_VIBRATOR_HAL,USE_EFFECT_STREAM,true)

# Inherit from the common OEM chipset makefile.
$(call inherit-product, device/oneplus/sm8550-common/common.mk)

# Inherit from the proprietary files makefile.
$(call inherit-product, vendor/oneplus/aston/aston-vendor.mk)
