#
# Copyright (C) 2021-2025 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

USE_PREBUILT_KERNEL ?= true

# Partitions
BOARD_SUPER_PARTITION_SIZE := 16642998272

# Include the common OEM chipset BoardConfig.
include device/oneplus/sm8550-common/BoardConfigCommon.mk

DEVICE_PATH := device/oneplus/aston

# Assert
TARGET_OTA_ASSERT_DEVICE := OP5D35L1

# Display
TARGET_SCREEN_DENSITY := 420

# Kernel
ifeq ($(USE_PREBUILT_KERNEL), true)
include device/oneplus/aston-kernel/BoardConfig.mk
else
TARGET_KERNEL_ADDITIONAL_FLAGS += CONFIG_ASTON_DTB=y
endif

# Properties
TARGET_ODM_PROP += $(DEVICE_PATH)/odm.prop
TARGET_SYSTEM_EXT_PROP += $(DEVICE_PATH)/system_ext.prop
TARGET_VENDOR_PROP += $(DEVICE_PATH)/vendor.prop

# Recovery
TARGET_RECOVERY_UI_MARGIN_HEIGHT := 103

# Sepolicy
BOARD_VENDOR_SEPOLICY_DIRS += $(DEVICE_PATH)/sepolicy/vendor

# Include the proprietary files BoardConfig.
include vendor/oneplus/aston/BoardConfigVendor.mk
