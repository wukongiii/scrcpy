package com.genymobile.scrcpy.wrappers;

import com.genymobile.scrcpy.Ln;

import android.os.IInterface;
import android.util.SparseArray;
import android.view.InputEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.genymobile.scrcpy.wrappers.LocalServices.getService;

public final class InputManager {

    public static final int INJECT_INPUT_EVENT_MODE_ASYNC = 0;
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_RESULT = 1;
    public static final int INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH = 2;

    private final IInterface manager;
    private Method injectInputEventMethod;

    public InputManager(IInterface manager) {
        this.manager = manager;
    }

    private Method getInjectInputEventMethod() throws NoSuchMethodException {
        if (injectInputEventMethod == null) {
            injectInputEventMethod = manager.getClass().getMethod("injectInputEvent", InputEvent.class, int.class);
        }
        return injectInputEventMethod;
    }


    public boolean dispatchEvent(InputEvent inputEvent, int displayId) {
        if (getInputForwarder(displayId) == null) {
            return injectInputEvent(inputEvent);
        } else {
            return forwardInputEvent(inputEvent, displayId);
        }
    }

    public boolean injectInputEvent(InputEvent inputEvent) {
        try {
            Method method = getInjectInputEventMethod();
            return (boolean) method.invoke(manager, inputEvent, InputManager.INJECT_INPUT_EVENT_MODE_ASYNC);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            Ln.e("Could not invoke method", e);
            return false;
        }

    }

    public boolean forwardInputEvent(InputEvent inputEvent, int displayId) {
        Object inputForwarder = getInputForwarder(displayId);
        if(inputForwarder != null && forwardEventMethod != null){
            try {
                return (boolean) forwardEventMethod.invoke(inputForwarder,inputEvent);
            } catch (Exception e) {
                Ln.e(e.toString());
            }
        }
        return false;

    }

    private SparseArray<Object> forwarders = new SparseArray<>();
    private Object createInputForwarderFailedFlag = new Object();

    private Object getInputForwarder(int displayId) {
        Object forwarder = forwarders.get(displayId);

        if (forwarder == null) {
            forwarder = createInputForwarder(displayId);
            if (forwarder == null) {
                forwarders.put(displayId, createInputForwarderFailedFlag);
                forwarder = createInputForwarderFailedFlag;
            } else {
                forwarders.put(displayId, forwarder);
            }
        }

        if (forwarder == createInputForwarderFailedFlag) {
            return null;
        }
        return forwarder;
    }

    Method forwardEventMethod = null;
    private Object createInputForwarder(int displayId) {
        Object inputForwarder = null;
        try {
            Method createInputForwarderMethod = manager.getClass().getMethod("createInputForwarder", int.class);
            inputForwarder = createInputForwarderMethod.invoke(manager, displayId);

            if (forwardEventMethod == null) {
                forwardEventMethod = inputForwarder.getClass().getMethod("forwardEvent", InputEvent.class);
            }
        } catch (Exception e) {
            Ln.e("Could not create input forwarder", e);
            return null;
        }

        return inputForwarder;
    }

//    private Object createInputForwarder(int displayId) {
//        try {
//            Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("com.android.server.input.InputForwarder");
////            Class<?> clazz = Class.forName("com.android.server.input.InputForwarder");
//            Constructor<?> ctor = clazz.getConstructor(int.class);
//            Object inputForwarder = ctor.newInstance(displayId);
//
//            return inputForwarder;
//
//        }catch (Exception e) {
//            Ln.e(e.toString());
//        }
////        Object InputManagerInternal = LocalServices.getService("android.hardware.input.InputManagerInternal");
//        return null;
//    }
}
