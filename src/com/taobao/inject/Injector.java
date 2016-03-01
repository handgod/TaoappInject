package com.taobao.inject;

import java.lang.reflect.Method;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

public class Injector {
	// private static Handler handler = new Handler(Looper.getMainLooper());

	public static void main(String[] args) {
		try {
			Object obj = static_invoke("android.app.ActivityThread",
					"currentApplication", null, null);
			Application app = (Application) obj;
			Log.e("inject", "Injector.main app:" + app);
//			IBinder objIPackageManagerBinder = (IBinder) static_invoke(
//					"android.os.ServiceManager", "getService",
//					new Class<?>[] { String.class }, new Object[] { "package" });
//			Log.e("inject", "Injector.main objIPackageManagerBinder:"
//					+ objIPackageManagerBinder);
//			Object objIPackageManager = static_invoke(
//					"android.content.pm.IPackageManager.Stub", "asInterface",
//					new Class<?>[] { IBinder.class },
//					new Object[] { objIPackageManagerBinder });
//			Log.e("inject", "Injector.main objIPackageManager:"
//					+ objIPackageManager);
			PackageManager pm = app.getPackageManager();
			pm.setApplicationEnabledSetting("com.taobao.appcenter",
					PackageManager.COMPONENT_ENABLED_STATE_DISABLED, 0);
			// Object objIPackageManager = static_invoke(
			// "android.os.ServiceManager", "getService",
			// new Class<?>[] { String.class }, new Object[] { "package" });
			// Log.e("inject", "Injector.main objIPackage:" +
			// objIPackageManager);
			// invoke("com.android.server.pm.PackageManagerService",
			// "setApplicationEnabledSetting", objIPackageManager,
			// new Class<?>[] { String.class, int.class, int.class,
			// int.class }, new Object[] {
			// "com.taobao.appcenter",
			// PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0,
			// 1000 });
			// Log.e("inject",
			// "Injector.main:" + Process.myPid() + "/" + Process.myTid()
			// + "/" + Looper.getMainLooper().getThread().getId()
			// + "/"
			// + Looper.getMainLooper().getThread().getName()
			// + "/" + Thread.currentThread().getId() + "/"
			// + Thread.currentThread().getName());
		} catch (Exception e) {
			Log.e("inject", "exception:" + e.getMessage());
		} catch (Throwable e) {
			Log.e("inject", "throwable:" + e.getMessage());
		}
		// handler.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// Log.e("inject", "Injector.runnable:" + Process.myPid() + "/"
		// + Process.myTid() + "/"
		// + Looper.getMainLooper().getThread().getId() + "/"
		// + Looper.getMainLooper().getThread().getName() + "/"
		// + Thread.currentThread().getId() + "/"
		// + Thread.currentThread().getName());
		// }
		// });
	}

	static public Object static_invoke(String className, String methodName,
			Class<?>[] parameterTypes, Object[] args) {
		return invoke(className, methodName, null, parameterTypes, args);
	}

	static public Object invoke(String className, String methodName,
			Object instance, Class<?>[] parameterTypes, Object[] args) {

		try {
			Class<?> clazz = Class.forName(className);
			// Method[] methods = clazz.getMethods();
			// if (methods != null) {
			// for (Method m : methods) {
			// Class<?>[] clses = m.getParameterTypes();
			// StringBuilder sb = new StringBuilder();
			// for (Class<?> cls : clses) {
			// sb.append(cls.getName());
			// }
			// Log.e("inject",
			// "invoke method " + m.getName() + ":"
			// + sb.toString());
			// }
			// }
			Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method.invoke(instance, args);
		} catch (Throwable e) {
			Log.e("inject", "invoke throwable:" + e.getMessage() + "/" + e);
			// StackTraceElement[] elements = e.getStackTrace();
			// if (elements != null) {
			// StringBuilder sb = new StringBuilder();
			// for (StackTraceElement element : elements) {
			// sb.append(element.getClassName() + "."
			// + element.getMethodName() + "("
			// + element.getFileName() + ")\n");
			// }
			// Log.e("inject", "invoke throwable elements:" + sb.toString());
			// }
		}
		return null;
	}
}
