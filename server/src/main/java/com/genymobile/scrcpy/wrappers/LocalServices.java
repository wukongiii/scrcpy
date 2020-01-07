package com.genymobile.scrcpy.wrappers;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Method;

public class LocalServices {

    private static final Method getServiceMethod;


    static  {
        try {
            getServiceMethod = Class.forName("com.android.server.LocalServices").getDeclaredMethod("getService", String.class);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public static Object getService(String serviceName) {
        try {
            Object service = getServiceMethod.invoke(null, Class.forName(serviceName));
            return service;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
