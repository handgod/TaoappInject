/*
 * libmynet.c
 *
 *  Created on: 2013-1-17
 *      Author: d
 */
#include <jni.h>
#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <android/log.h>
#include <android_runtime/AndroidRuntime.h>
#define LOG_TAG "inject"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#include "libinject.h"

using namespace android;

static int invoke_dex_method(const char* dexPath, const char* dexOptDir, const char* className, const char* methodName, int argc, char *argv[]) {
    LOGE("Invoke dex E");
    LOGE("className:%s.methodName:%s", className,methodName);
    JNIEnv* env = android::AndroidRuntime::getJNIEnv();
    LOGE("JNIEnv pointor %p.",env);    
	jclass stringClass, classLoaderClass, dexClassLoaderClass, targetClass;
    jmethodID getSystemClassLoaderMethod, dexClassLoaderContructor, loadClassMethod, targetMethod;
    jobject systemClassLoaderObject, dexClassLoaderObject;
    jstring dexPathString, dexOptDirString, classNameString, tmpString;    
    jobjectArray stringArray;

    /* Get SystemClasLoader */
    stringClass = env->FindClass("java/lang/String");
    classLoaderClass = env->FindClass("java/lang/ClassLoader");
    dexClassLoaderClass = env->FindClass("dalvik/system/DexClassLoader");
    getSystemClassLoaderMethod = env->GetStaticMethodID(classLoaderClass, "getSystemClassLoader", "()Ljava/lang/ClassLoader;");
    systemClassLoaderObject = env->CallStaticObjectMethod(classLoaderClass, getSystemClassLoaderMethod);
LOGE("systemClassLoaderObject %p.",systemClassLoaderObject);
    /* Create DexClassLoader */
    dexClassLoaderContructor = env->GetMethodID(dexClassLoaderClass, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/ClassLoader;)V");
    dexPathString = env->NewStringUTF(dexPath);
    dexOptDirString = env->NewStringUTF(dexOptDir);
    dexClassLoaderObject = env->NewObject(dexClassLoaderClass, dexClassLoaderContructor, dexPathString, dexOptDirString, NULL, systemClassLoaderObject);
LOGE("dexClassLoaderObject %p.",dexClassLoaderObject);
    /* Use DexClassLoader to load target class */
    loadClassMethod = env->GetMethodID(dexClassLoaderClass, "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    classNameString = env->NewStringUTF(className);
    targetClass = (jclass)env->CallObjectMethod(dexClassLoaderObject, loadClassMethod, classNameString);
    if (!targetClass) {
        LOGE("Failed to load target class %s", className);
        return -1;
    }

    /* Invoke target method */
    targetMethod = env->GetStaticMethodID(targetClass, methodName, "([Ljava/lang/String;)V");
    if (!targetMethod) {
        LOGE("Failed to load target method %s", methodName);
        return -1;
    }
    stringArray = env->NewObjectArray(argc, stringClass, NULL);
    for (int i = 0; i < argc; i++) {
        tmpString = env->NewStringUTF(argv[i]);
        env->SetObjectArrayElement(stringArray, i, tmpString);
    }
    env->CallStaticVoidMethod(targetClass, targetMethod, stringArray);
    LOGE("Invoke dex X");
    return 0;
}
__attribute__((constructor)) void inject_internal(){
	printf("inject_internal\n");
char *argv[] = {
        "linker"
        "libdvm.so",
        "libnativehelper.so",
        "libandroid_runtime.so",
        "libmath.so",
        "testapp",
        "libc.so",
        NULL
};
	invoke_dex_method("/data/local/inj/inject.apk","/data/dalvik-cache","com.taobao.inject.Injector","main",7,argv);
}
void  inject() {
	printf("inject\n");
void inject_internal();
}
