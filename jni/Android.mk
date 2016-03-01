LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES:=  elf.c inject.c ptrace.c
LOCAL_MODULE := inj
LOCAL_MODULE_TAGS := optional
LOCAL_CFLAGS := -DANDROID -DTHUMB
#LOCAL_C_INCLUDES := 
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_SRC_FILES:=  elf.c inj_dalvik.c ptrace.c
LOCAL_MODULE := inj_dalvik
LOCAL_MODULE_TAGS := optional
LOCAL_CFLAGS := -DANDROID -DTHUMB
#LOCAL_C_INCLUDES := 
include $(BUILD_EXECUTABLE)


include $(CLEAR_VARS)
LOCAL_SRC_FILES:=  testapp.c
LOCAL_MODULE := testapp
LOCAL_MODULE_TAGS := optional
LOCAL_SHARED_LIBRARIES += libdl
#LOCAL_C_INCLUDES := 
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)
LOCAL_SRC_FILES:=  libmynet.c
LOCAL_MODULE := libmynet
LOCAL_MODULE_TAGS := optional
#LOCAL_C_INCLUDES := 
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_SRC_FILES :=  libinject.cpp
LOCAL_MODULE := libinject
LOCAL_MODULE_TAGS := optional

LOCAL_C_INCLUDES := $(LOCAL_PATH)/include 

LOCAL_SHARED_LIBRARIES := 

LOCAL_LDFLAGS += $(LOCAL_PATH)/so/libandroid_runtime.so \
$(LOCAL_PATH)/so/libnativehelper.so

LOCAL_LDLIBS := \
	-llog
$(warning $(LOCAL_SHARED_LIBRARIES))
include $(BUILD_SHARED_LIBRARY)


