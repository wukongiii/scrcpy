package com.genymobile.scrcpy.wrappers;

import com.genymobile.scrcpy.DisplayInfo;
import com.genymobile.scrcpy.Size;

import android.os.IInterface;

public final class DisplayManager {
    private final IInterface manager;

    public DisplayManager(IInterface manager) {
        this.manager = manager;
    }

    public DisplayInfo getDisplayInfo(int displayId) {
        try {
            Object displayInfo = manager.getClass().getMethod("getDisplayInfo", int.class).invoke(manager, displayId);
            Class<?> cls = displayInfo.getClass();
            // width and height already take the rotation into account
            int width = cls.getDeclaredField("logicalWidth").getInt(displayInfo);
            int height = cls.getDeclaredField("logicalHeight").getInt(displayInfo);
            int rotation = cls.getDeclaredField("rotation").getInt(displayInfo);
            //int type, String name, int ownerUid, String ownerPackageName
            int type = cls.getDeclaredField("type").getInt(displayInfo);
            String name = cls.getDeclaredField("name").get(displayInfo).toString();
            int ownerUid = cls.getDeclaredField("ownerUid").getInt(displayInfo);

            Object ownerPackageName = cls.getDeclaredField("ownerPackageName").get(displayInfo);
            return new DisplayInfo(new Size(width, height), rotation, type, name,ownerUid, ownerPackageName != null ? ownerPackageName.toString() : null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public int[] getDisplayIds() {
        try {
            return (int[]) manager.getClass().getMethod("getDisplayIds").invoke(manager);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
